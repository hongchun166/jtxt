
package com.linkb.jstx.activity.chat;

import android.Manifest;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.linkb.jstx.listener.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.HomeActivity;
import com.linkb.jstx.activity.QrcodeScanActivity;
import com.linkb.jstx.activity.base.CIMMonitorFragment;
import com.linkb.jstx.activity.contact.CreateGroupActivity;
import com.linkb.jstx.activity.contact.SearchFriendActivityV3;
import com.linkb.jstx.activity.wallet.WalletActivityV2;
import com.linkb.jstx.adapter.ConversationListViewAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.QuickOperationPopupWindow;
import com.linkb.jstx.database.ChatTopRepository;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.dialog.ContentMenuWindow;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.listener.OnChatingHandlerListener;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.listener.OnMenuClickedListener;
import com.linkb.jstx.message.parser.MessageParserFactory;
import com.linkb.jstx.model.ChatTop;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.model.SystemMessage;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.QueryGroupInfoResult;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.MessageUtil;
import com.linkb.jstx.util.PermissionUtils;

import java.util.HashSet;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ConversationFragment extends CIMMonitorFragment implements OnDialogButtonClickListener, OnChatingHandlerListener, OnMenuClickedListener, View.OnClickListener, QuickOperationPopupWindow.QuickOperationClickListener, EasyPermissions.PermissionCallbacks {

    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    private static final HashSet<String> INCLUDED_MESSAGE_TYPES = new HashSet<>();

    static {
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_0);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_1);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_2);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_3);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_ReadDelete);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_GrpRedPack);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_102);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_103);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_104);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_105);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_106);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_107);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_112);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_113);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_200);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_201);
    }

    private ContentMenuWindow contentMenuWindow;
    private ConversationListViewAdapter adapter;
    private RecyclerView conversationListView;
    private View emptyView, maskView;
    private CustomDialog customDialog;
    private ChatBroadcastReceiver chatBroadcastReceiver;
    private ViewGroup disconnectView;
    private int connCounter = 0;
    private QuickOperationPopupWindow mQuickOperationPopupWindow;

    private Message mMsg;

    private static void log(String msg){
        System.out.println("testLog=="+msg);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LvxinApplication.getInstance().connectPushServer();
    }

    @Override
    public void requestData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_conversation, container, false);
        maskView = view.findViewById(R.id.mask_view);
        toolBarTitle = view.findViewById(R.id.tool_bar_title);
        imageBtn = view.findViewById(R.id.right_img_btn);
        imageBtn.setOnClickListener(this);
        toolBarTitle.setText(R.string.common_message);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        disconnectView = (ViewGroup) findViewById(R.id.header);
        disconnectView.setOnClickListener(this);
        conversationListView = (RecyclerView) findViewById(R.id.conversationList);
        conversationListView.setItemAnimator(new DefaultItemAnimator());
        conversationListView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        adapter = new ConversationListViewAdapter();
        conversationListView.setAdapter(adapter);
        adapter.setOnChatItemHandleListner(this);
        customDialog = new CustomDialog(this.getActivity());
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setIcon((R.drawable.icon_dialog_delete));
        customDialog.setMessage((R.string.tip_delete_message));
        contentMenuWindow = new ContentMenuWindow(this.getActivity());
        contentMenuWindow.setOnMenuClickedListener(this);

        emptyView = findViewById(R.id.emptyView);
        loadRecentConversation();
    }

    public void onResume() {
        super.onResume();
        showNewMessageBadge();
        ((NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
    }

    private void loadRecentConversation() {
        adapter.addAll(MessageRepository.getRecentMessage(INCLUDED_MESSAGE_TYPES.toArray()));
        toogleEmptyView();
    }

    private void toogleEmptyView() {
        emptyView.setVisibility(adapter.isEmpty() ? View.VISIBLE : View.GONE);
    }


    private void showNewMessageBadge() {
        View tabView = ((HomeActivity) getActivity()).getConversationTab();
        TextView newMsgBadge = tabView.findViewById(R.id.badge);
        int sum = MessageRepository.queryIncludedNewCount(INCLUDED_MESSAGE_TYPES.toArray());
        if (sum > 0) {
            newMsgBadge.setText(String.valueOf(sum));
            newMsgBadge.setVisibility(View.VISIBLE);
        } else {
            newMsgBadge.setVisibility(View.GONE);
        }
    }

    @Override
    public void onChatClicked(final ChatItem chatItem) {

        final Intent intent = new Intent();

        if (chatItem.source instanceof Group) {
            if (chatItem.message.action.equals(Constant.MessageAction.ACTION_1) ||chatItem.message.action.equals(Constant.MessageAction.ACTION_113) || chatItem.message.action.equals(Constant.MessageAction.ACTION_112)){
                markMessageRead(chatItem);
            }
            final long groupId = ((Group) chatItem.source).id;
            if (GroupRepository.queryById(groupId) == null){
                HttpServiceManager.queryGroupInfo(groupId, new HttpRequestListener<QueryGroupInfoResult>() {
                    @Override
                    public void onHttpRequestSucceed(QueryGroupInfoResult result, OriginalCall call) {
                        if (result.isSuccess()){
                            if (GroupRepository.queryById(groupId) != null){
                                GroupRepository.update(result.getData());
                            }else {
                                GroupRepository.add(result.getData());
                            }
                            gotoGroupChat(intent, chatItem);
                        }else {
                            showToastView(getResources().getString(R.string.the_group_has_been_dismiss));
                        }
                    }

                    @Override
                    public void onHttpRequestFailure(Exception e, OriginalCall call) {
                    }
                });
            }else {
                gotoGroupChat(intent, chatItem);
            }
        }

        if (chatItem.source instanceof Friend) {
            intent.setClass(getContext(), FriendChatActivity.class);
            intent.putExtra(Constant.CHAT_OTHRES_ID, ((Friend) chatItem.source).account);
            intent.putExtra(Constant.CHAT_OTHRES_NAME, ((Friend) chatItem.source).name);
            startActivity(intent);
        }

        if (chatItem.source instanceof MicroServer) {
            intent.setClass(getContext(), MicroServerWindowActivity.class);
            intent.putExtra(MicroServer.NAME, chatItem.source);
            startActivity(intent);
        }

        if (chatItem.source instanceof SystemMessage) {
            intent.setClass(getContext(), SystemMessageActivity.class);
            startActivity(intent);
        }


    }

    private void gotoGroupChat(Intent intent, ChatItem chatItem){
        GroupChatActivity.navToAct(getContext(),intent,((Group) chatItem.source).name,((Group) chatItem.source).id);
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {
        if (INCLUDED_MESSAGE_TYPES.contains(message.getAction())) {
            final Message msg = MessageUtil.transform(message);
            final ChatItem chatItem = new ChatItem();
            chatItem.message = msg;
            chatItem.source = MessageParserFactory.getFactory().parserMessageSource(msg);

            //用户信息为空
            MessageSource source = MessageParserFactory.getFactory().parserMessageSource(msg);
            if (source instanceof  Friend){
                Friend friend = (Friend) source;
                log("==onMessageReceived==="+friend.name);
                if (friend.name == null){
                    HttpServiceManager.queryPersonInfo(friend.account, new HttpRequestListener<BasePersonInfoResult>() {
                        @Override
                        public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
                            if (result.isSuccess()){
                                Friend friendTemp = User.UserToFriend(result.getData());
                                FriendRepository.update(friendTemp);
                                chatItem.source = friendTemp;
                                adapter.notifyItemMovedTop(chatItem);
                                toogleEmptyView();
                                showNewMessageBadge();
                            }
                        }

                        @Override
                        public void onHttpRequestFailure(Exception e, OriginalCall call) {

                        }
                    });
                }else {
                    adapter.notifyItemMovedTop(chatItem);
                    toogleEmptyView();
                    showNewMessageBadge();
                }
            }else {
                adapter.notifyItemMovedTop(chatItem);
                toogleEmptyView();
                showNewMessageBadge();
            }

            if (message.getAction().equals(Constant.MessageAction.ACTION_103)){
                //同意进群申请，刷新群组列表
                Intent joinIntent = new Intent(Constant.Action.ACTION_GROUP_REFRESH);
                LvxinApplication.sendLocalBroadcast(joinIntent);
            }
        }
    }


    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
        ChatItem chatItem = (ChatItem) customDialog.getTag();
        adapter.notifyItemRemoved(chatItem);
        String id = chatItem.source.getId();
        MessageRepository.deleteBySenderOrReceiver(id);
        Global.removeChatDraft(chatItem.source);
        toogleEmptyView();
        showNewMessageBadge();
    }


    @Override
    public void onChatLongClicked(ChatItem chat) {
        View view=conversationListView.findViewWithTag(chat.source);
        if(view!=null){
            TextView badge = view.findViewById(R.id.badge);
            if(badge!=null){
                customDialog.setTag(chat);
                contentMenuWindow.buildChatMenuGroup(adapter.hasChatTop(chat), badge.getVisibility() == View.VISIBLE);
                contentMenuWindow.show((View) badge.getParent());
            }
        }

    }

    @Override
    public void onMenuItemClicked(int id) {

        if (id == R.id.menu_delete_chat) {
            customDialog.show();
        }
        if (id == R.id.menu_mark_noread) {
            markMessageNoread((ChatItem) customDialog.getTag());
        }
        if (id == R.id.menu_mark_read) {
            markMessageRead((ChatItem) customDialog.getTag());
        }
        if (id == R.id.menu_chat_top) {
            moveChatTop((ChatItem) customDialog.getTag());
        }
        if (id == R.id.menu_cancel_top) {
            cancelChatTop((ChatItem) customDialog.getTag());
        }
    }

    /**
     * 置顶聊天
     *
     * @param chat
     */
    private void moveChatTop(ChatItem chat) {
        ChatTop top = ChatTopRepository.setTop(chat.getSourceClass(), chat.source.getId());
        adapter.notifyItemMovedTop(top, chat);
    }


    /**
     * 取消置顶聊天
     *
     * @param chat
     */
    private void cancelChatTop(ChatItem chat) {
        adapter.cancelChatTop(chat);
        conversationListView.scrollToPosition(0);
    }


    /**
     * 标记消息为未读
     *
     * @param chat
     */
    private void markMessageNoread(ChatItem chat) {
        Message message = MessageRepository.queryReceivedLastMessage(chat.source.getId(), chat.source.getMessageAction());
        TextView badge = conversationListView.findViewWithTag(chat.source).findViewById(R.id.badge);
        if (badge.getVisibility() == View.GONE && message != null) {
            MessageRepository.updateStatus(message.id, Message.STATUS_NOT_READ);
            badge.setVisibility(View.VISIBLE);
            badge.setText("1");
            showNewMessageBadge();
        }

    }

    /**
     * 标记消息为已读
     *
     * @param chat
     */
    private void markMessageRead(ChatItem chat) {
        MessageRepository.batchReadMessage(chat.source.getId(), chat.source.getMessageAction());
        TextView badge = conversationListView.findViewWithTag(chat.source).findViewById(R.id.badge);
        badge.setVisibility(View.GONE);
        badge.setText(null);
        showNewMessageBadge();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        chatBroadcastReceiver = new ChatBroadcastReceiver();
        LvxinApplication.registerLocalReceiver(chatBroadcastReceiver, chatBroadcastReceiver.getIntentFilter());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LvxinApplication.unregisterLocalReceiver(chatBroadcastReceiver);
    }

    @Override
    public void onConnectFinished(boolean hasAutoBind) {
        connCounter = 0;
        disconnectView.setVisibility(View.GONE);
    }
    @Override
    public void onConnectFailed() {
        connCounter++;
        if (connCounter >= 3) {
            disconnectView.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.right_img_btn:
                if (mQuickOperationPopupWindow == null){
                    mQuickOperationPopupWindow = new QuickOperationPopupWindow(getContext(), this);
                }
                //显示窗口
                mQuickOperationPopupWindow.showAsDropDown(imageBtn, -ConvertUtils.dp2px(105), ConvertUtils.dp2px(0),Gravity.LEFT); //设置layout在PopupWindow中显示的位置
//                maskView.setVisibility(View.VISIBLE);
                mQuickOperationPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
//                        maskView.setVisibility(View.GONE);
                    }
                });
                break;
        }
    }

    @Override
    public void createGroup() {
        startActivity(new Intent(getActivity(), CreateGroupActivity.class));
    }

    @Override
    public void addFriends() {
        startActivity(new Intent(getActivity(), SearchFriendActivityV3.class));
    }

    @Override
    public void mineWallet() {
        startActivity(new Intent(getActivity(), WalletActivityV2.class));
    }

    @Override
    public void scanQrCode() {
        requestCodeQRCodePermissions();
    }

    public class ChatBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (Constant.Action.ACTION_FRIEND_MARK_CHANGE.equals(intent.getAction())){
                loadRecentConversation();
                return;
            }

            if (Constant.Action.ACTION_DELETE_FRIEND.equals(intent.getAction())){
                loadRecentConversation();
                return;
            }

            ChatItem item = (ChatItem) intent.getSerializableExtra(ChatItem.NAME);

            /**
             * 如果是不需要再列表显示的消息，不处理
             */
            if (item.message != null && !INCLUDED_MESSAGE_TYPES.contains(item.message.action)) {
                return;
            }
            if (Constant.Action.ACTION_RECENT_APPEND_CHAT.equals(intent.getAction())) {
                log("Constant.Action.ACTION_RECENT_APPEND_CHAT");
                adapter.notifyItemMovedTop(item);
                toogleEmptyView();
            }

            if (Constant.Action.ACTION_RECENT_DELETE_CHAT.equals(intent.getAction())) {
                adapter.notifyItemRemoved(item);
                toogleEmptyView();
            }

            if (Constant.Action.ACTION_RECENT_REFRESH_CHAT.equals(intent.getAction())) {
                log("Constant.Action.ACTION_RECENT_REFRESH_CHAT");
                adapter.notifyItemChanged(item);
                toogleEmptyView();
                showNewMessageBadge();
            }

            /**
             * 收到刷新logo或者名称的通知
             */
            if (Constant.Action.ACTION_RECENT_REFRESH_LOGO.equals(intent.getAction())) {
                adapter.notifyItemChangedOnly(item);
            }
        }

        IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_RECENT_APPEND_CHAT);
            filter.addAction(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            filter.addAction(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
            filter.addAction(Constant.Action.ACTION_RECENT_REFRESH_LOGO);
            filter.addAction(Constant.Action.ACTION_DELETE_FRIEND);
            filter.addAction(Constant.Action.ACTION_FRIEND_MARK_CHANGE);
            return filter;
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(getActivity(), perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }else {
            startActivity(new Intent(getActivity(), QrcodeScanActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        startActivity(new Intent(getActivity(), QrcodeScanActivity.class));
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    }
}
