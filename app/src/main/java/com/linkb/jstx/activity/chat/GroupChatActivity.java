
package com.linkb.jstx.activity.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.androidadvance.topsnackbar.TSnackbar;
import com.linkb.jstx.activity.contact.GroupDetailActivity;
import com.linkb.jstx.activity.contact.GroupMemberAtSelectActivity;
import com.linkb.jstx.activity.wallet.RedPacketActivityV2;
import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.GroupMember;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.QueryGroupInfoResult;
import com.linkb.jstx.profession.MessageHelp;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.MessageUtil;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.message.extra.MessageExtraAt;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.R;
import com.linkb.video.GroupVideoMemberSelectActivity;
import com.linkb.video.RoomActivity;

import java.util.List;
import java.util.Objects;


public class GroupChatActivity extends FriendChatActivity implements ViewTreeObserver.OnGlobalLayoutListener {
    private static final int FACTOR = 9999;
    private boolean isDeleted = false;
    private long groupId;
    private String groupName;
    private MessageExtraAt extraAt = new MessageExtraAt();
    private ArrayMap<String,String> atNameMap = new ArrayMap<>();
    private AtMemberReceiver atMemberReceiver;
    private TextView newMessageBar;
    private long newMessageCount;

    private Group mGroup;

    private static final int AT_SELECT_GROUP_MEMBER_REQUEST = 123;

    private static final int VIDEO_CONNECT_GROUP_MEMBER_REQUEST = 0x11;

    /** 选择的群发音视频的用户名
    * */
    private String mGroupVideoAccount;

    public static void navToAct(Context context,String gName,long gid){
        Intent intent=new Intent(context,GroupChatActivity.class);
        navToAct(context,intent,gName,gid);
    }
    public static void navToAct(Context context,Intent intent,String gName,long gid){
        intent.setClass(context, GroupChatActivity.class);
        intent.putExtra(Constant.CHAT_OTHRES_ID, gid);
        intent.putExtra(Constant.CHAT_OTHRES_NAME, gName);
        context.startActivity(intent);
    }

    @Override
    public void initComponents() {

        groupId = getIntent().getLongExtra(Constant.CHAT_OTHRES_ID,0L);
        getGroupInfo();

        groupName = getIntent().getStringExtra(Constant.CHAT_OTHRES_NAME) ;
        setToolbarTitle(groupName);

        newMessageCount = Math.max(ClientConfig.getNewGroupMessageCount(groupId), MessageRepository.countNewBySender(String.valueOf(groupId), Constant.MessageAction.ACTION_3));

        super.initComponents();


        atMemberReceiver = new AtMemberReceiver();
        LvxinApplication.registerLocalReceiver(atMemberReceiver,atMemberReceiver.getIntentFilter());
        newMessageBar = findViewById(R.id.newMessageBar);


    }

    /** 获取群资料
    * */
    private void getGroupInfo() {
        HttpServiceManager.queryGroupInfo(groupId, new HttpRequestListener<QueryGroupInfoResult>() {
            @Override
            public void onHttpRequestSucceed(QueryGroupInfoResult result, OriginalCall call) {
                if (result.isSuccess()){
                    if (GroupRepository.queryById(groupId) != null){
                        GroupRepository.update(result.getData());
                    }else {
                        GroupRepository.add(result.getData());
                    }
                    mGroup = result.getData();
                    mAdapter.setEnableCheckMemberInfo(mGroup.memberAble == 0);

                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }

    @Override
    public void initChatListViewListener()  {
        super.initChatListViewListener();
        if (newMessageCount > 0){
            mChatListView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        }
    }

    @Override
    public List<Message>  loadLocalMessageRecord(String otherId) {
        List<Message> data = MessageRepository.queryGroupMessageSection(otherId,mAdapter.getMessaeCount(),Math.max(newMessageCount, Constant.MESSAGE_PAGE_SIZE));
        return data;
    }

    @Override
    public void handleReadAllMessage(){
        ClientConfig.clearAtMeGroupMessage(groupId);
        ClientConfig.clearNewGroupMessageCount(groupId);
        MessageRepository.batchReadGroupMessage(String.valueOf(groupId));
    }

    private void handleNewMessageBar(){

        long unlookCount = newMessageCount - findFirstPageMessageCount();
        if (unlookCount > 0){
            newMessageBar.setVisibility(View.VISIBLE);
            newMessageBar.setOnClickListener(this);
            newMessageBar.setText(getString(R.string.hint_chat_new_message,unlookCount));
            final long itemCount = mAdapter.getItemCount(newMessageCount);
            newMessageBar.setTag(itemCount);
            final LinearLayoutManager linearLayoutManager =  ((LinearLayoutManager)mChatListView.getLayoutManager());
            mChatListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    int postition = linearLayoutManager.findFirstVisibleItemPosition();
                    if (postition <=  mAdapter.getItemCount() - itemCount){
                        newMessageBar.setVisibility(View.GONE);
                        mChatListView.removeOnScrollListener(this);
                    }
                }
            });
        }
        ClientConfig.clearAtMeGroupMessage(groupId);
        ClientConfig.clearNewGroupMessageCount(groupId);
        MessageRepository.batchReadGroupMessage(String.valueOf(groupId));
    }

    private long findFirstPageMessageCount() {
        LinearLayoutManager linearManager = (LinearLayoutManager) mChatListView.getLayoutManager();
        int count = 0;
        int postition = linearManager.findFirstVisibleItemPosition();
        if (postition >= 0){
            for (int i= postition;i<mAdapter.getItemCount();i++){
                if (mAdapter.getItem(i) instanceof Message){
                    count ++;
                }
            }
        }
        return  count;
    }
    @Override
    public String getMessageAction(String format,boolean checkReadDelete) {
        // TODO: 2019/3/20
        if(format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
            return Constant.MessageAction.ACTION_GrpRedPack;
        }else {
            return Constant.MessageAction.ACTION_3;
        }
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {

        Message msg = MessageUtil.transform(message);
        if ( MessageHelp.hasGrpActionByAction(message.getAction()) && msg.sender.equals(super.mMessageSource.getId())) {
            mAdapter.addMessage(msg);
            mChatListView.smartScrollToBottom();
            MessageRepository.updateStatus(msg.id, Message.STATUS_READ);
        }
    }

    @Override
    public MessageSource getMessageSource(HttpRequestListener<BasePersonInfoResult> mListener) {
        return GroupRepository.queryById(groupId);
    }

    private HttpRequestListener<BasePersonInfoResult> mListener = new HttpRequestListener<BasePersonInfoResult>() {
        @Override
        public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    @Override
    public int getMenuIcon() {
        return R.drawable.icon_menu_group;
    }

    @Override
    public void onToolbarMenuClicked() {
//        Group group = (Group) getMessageSource(mListener);
        if (mGroup != null){
            Intent intent = new Intent(GroupChatActivity.this, GroupDetailActivity.class);
            intent.putExtra("group", mGroup);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handleReadAllMessage();
        LvxinApplication.unregisterLocalReceiver(atMemberReceiver);
    }
    @Override
    public void onResume() {
        super.onResume();
        long groupId = getIntent().getLongExtra(Constant.CHAT_OTHRES_ID,0L);

        if (GroupRepository.queryById(groupId) == null) {
            isDeleted = true;
            finish();
        }
        //判断用户是否屏蔽该群消息
        if (ClientConfig.isIgnoredGroupMessage(groupId)) {
            setTitleDrawableRight(R.drawable.ic_notifications_off);
        } else {
            clearTitleDrawableRight();
        }
    }

    @Override
    public void sendRecentRefreshBroadcast() {
        if (!isDeleted) {
            super.sendRecentRefreshBroadcast();
        }
    }

    @Override
    public void onMessageInsertAt() {
//        Intent intent = new Intent(this, GroupMemberSelectorActivity.class);
//        intent.putExtra("groupId", groupId);
//        startActivityForResult(intent, AT_SELECT_GROUP_MEMBER_REQUEST);
        if (mGroup != null){
            Intent intent = new Intent(this, GroupMemberAtSelectActivity.class);
            intent.putExtra("group", mGroup);
            startActivityForResult(intent, AT_SELECT_GROUP_MEMBER_REQUEST);
        }
    }

    @Override
    public void onMessageTextDelete(String txt) {

    }

    @Override
    public void onClick(View v) {
        if (v == newMessageBar){
            long itemCount = (long) newMessageBar.getTag();
            int targetIndex = (int) (mAdapter.getItemCount() - itemCount);
            mChatListView.scrollToPosition(targetIndex);
            newMessageBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void saveAndDisplayMessage(String context, String extra, String format) {
        for (String name : atNameMap.keySet()){
            if (!context.contains(name)){
                extraAt.remove(atNameMap.get(name));
            }
        }
        if (format.equals(Constant.MessageFormat.FORMAT_RED_PACKET) || format.equals("" + (Integer.parseInt(Constant.MessageFormat.FORMAT_RED_PACKET) + FACTOR))){
            super.saveAndDisplayMessage(context,extra,format);
        }else if (format.equals(Constant.MessageFormat.FORMAT_SEND_CARDS) || format.equals("" + (Integer.parseInt(Constant.MessageFormat.FORMAT_SEND_CARDS) + FACTOR))){
            super.saveAndDisplayMessage(context,extra,format);
        }else {
            super.saveAndDisplayMessage(context,extraAt.toExtraString(),format);
        }


        atNameMap.clear();
        extraAt.clear();
    }


    private void appendAtGroupMeMber(GroupMember groupMember) {
        StringBuilder builder = new StringBuilder(inputPanelView.getInputText());
        if (builder.length() > 0 && builder.charAt(builder.length()-1) == '@'){
            builder.deleteCharAt(builder.length()-1);
        }
        builder.append('@').append(groupMember.name).append(" ");
        inputPanelView.applyMessageText(builder.toString());
        extraAt.add(groupMember.account);
        atNameMap.put("@" + groupMember.name,groupMember.account);
    }

    private void appendAtFriend(Friend groupMember) {
        StringBuilder builder = new StringBuilder(inputPanelView.getInputText());
        if (builder.length() > 0 && builder.charAt(builder.length()-1) == '@'){
            builder.deleteCharAt(builder.length()-1);
        }
        builder.append('@').append(groupMember.name).append(" ");
        inputPanelView.applyMessageText(builder.toString());
        extraAt.add(groupMember.account);
        atNameMap.put("@" + groupMember.name,groupMember.account);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AT_SELECT_GROUP_MEMBER_REQUEST && resultCode == RESULT_OK) {
            GroupMember groupMember = (GroupMember) data.getSerializableExtra(GroupMember.class.getName());
            appendAtGroupMeMber(groupMember);
            return;
        }
        if (requestCode == VIDEO_CONNECT_GROUP_MEMBER_REQUEST && resultCode == RESULT_OK) {
            mGroupVideoAccount = data.getStringExtra(GroupVideoMemberSelectActivity.SELECT_VIDEO_MEMBER);
            requestToken();
        }
    }

    /** 申请房间Token 并通知其他人
    * */
    private void requestToken(){
        showProgressDialog("");
        mRootName = createRootName(mSelf.account, String.valueOf(mGroup.id));
        HttpServiceManager.requestRoomToken(mRootName, mSelf.account, AppTools.getPackageName(this), requestRoomTokenListener);
    }

    /** 发送音视频消息
     * */
    @Override
    protected void sendVideoMessage(String action) {
        HttpServiceManager.sendVideoConnectMessage(mRootName, AppTools.getPackageName(this),
                mGroupVideoAccount, action, sendVideoMessage);
    }

    @Override
    protected void goToVideo(String token) {
        Intent intent = new Intent(GroupChatActivity.this, RoomActivity.class);
        intent.putExtra(RoomActivity.EXTRA_ROOM_ID, mRootName);
        intent.putExtra(RoomActivity.EXTRA_ROOM_TOKEN, token);
        intent.putExtra(RoomActivity.EXTRA_USER_ID, mSelf.account);
        intent.putExtra(RoomActivity.EXTRA_ISSENDER, true);
        intent.putExtra(RoomActivity.EXTRA_OPPOSITE_USER_ID, mMessageSource.getId());
        intent.putExtra(RoomActivity.EXTRA_IS_VIDEO, isVideo);
        intent.putExtra(RoomActivity.EXTRA_OPPOSITE_GROUP_MEMBER, mGroupVideoAccount);
        startActivity(intent);
    }

    @Override
    protected void initBottomInputPanelView() {
        super.initBottomInputPanelView();
//        inputPanelView.setRedPacketType(Constant.RedPacketType.COMMON_GROUP_LURKEY_RED_PACKET);
//        inputPanelView.setMenberId(groupId);
    }

    @Override
    public void onGlobalLayout() {
        mChatListView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
        handleNewMessageBar();
    }

    @Override
    protected void videoConnect() {
        Intent inviteIntent = new Intent(GroupChatActivity.this, GroupVideoMemberSelectActivity.class);
        inviteIntent.putExtra("group", mGroup);
        startActivityForResult(inviteIntent, VIDEO_CONNECT_GROUP_MEMBER_REQUEST);
    }

    @Override
    public void onChatInputRedPacket() {
        Intent intentRedPacket = new Intent(GroupChatActivity.this, RedPacketActivityV2.class);
        intentRedPacket.putExtra(Constant.RedPacketType.RED_PACKET_TYPE, Constant.RedPacketType.COMMON_GROUP_LURKEY_RED_PACKET);
        intentRedPacket.putExtra(Constant.CHAT_OTHRES_ID, String.valueOf(groupId));
        startActivityForResult(intentRedPacket, SEND_RED_PACKET_REQUEST_CODE);
    }

    /** 群禁言提示bar
    * */
    private void showGroupBannedBar(){
        TSnackbar snackbar = TSnackbar.make(findViewById(R.id.snake_bar_cooly), getResources().getString(R.string.label_group_banned_tips), TSnackbar.LENGTH_LONG);
        snackbar.setActionTextColor(Color.WHITE);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(Color.parseColor("#D63337"));
        TextView textView = (TextView) snackbarView.findViewById(com.androidadvance.topsnackbar.R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
        snackbar.show();
    }

    public class AtMemberReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(), Constant.Action.ACTION_ICON_LONGCLICKED)) {
                Friend friend = (Friend) intent.getSerializableExtra(Friend.NAME);
                appendAtFriend(friend);
            }
            if (Objects.equals(intent.getAction(), Constant.Action.ACTION_GROUP_BANNED)) {
                ChatItem target = (ChatItem) intent.getSerializableExtra(ChatItem.NAME);
                if (target.source instanceof Group){
                    Group group = (Group) target.source;
                    if (group != null && group.id == groupId){
                        showGroupBannedBar();
                    }
                }
            }
            if (Objects.equals(intent.getAction(), Constant.Action.ACTION_GROUP_UNABLE_CHECK_INFO)){
                Boolean enableCkeckInfo = intent.getBooleanExtra("enableCheckInfo", true);
                mGroup.setMemberAble(enableCkeckInfo ? 0 : 1);
                mAdapter.setEnableCheckMemberInfo(enableCkeckInfo);
            }
        }

        IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_ICON_LONGCLICKED);
            filter.addAction(Constant.Action.ACTION_GROUP_BANNED);
            filter.addAction(Constant.Action.ACTION_GROUP_UNABLE_CHECK_INFO);
            return filter;
        }
    }

}
