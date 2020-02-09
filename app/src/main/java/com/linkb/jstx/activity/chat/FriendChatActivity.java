
package com.linkb.jstx.activity.chat;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.linkb.R;
import com.linkb.jstx.activity.base.CIMMonitorActivityWithoutImmersion;
import com.linkb.jstx.activity.contact.PersonInfoActivity;
import com.linkb.jstx.activity.contact.PhoneContactsActivity;
import com.linkb.jstx.activity.contact.SendCardsSelectContactActivity;
import com.linkb.jstx.activity.util.PhotoAlbumActivity;
import com.linkb.jstx.activity.util.VideoRecorderActivity;
import com.linkb.jstx.activity.wallet.CoinTransferActivity;
import com.linkb.jstx.activity.wallet.RedPacketActivity;
import com.linkb.jstx.adapter.ChatRecordListViewAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.GlobalVoicePlayer;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.BaseToMessageView;
import com.linkb.jstx.component.ChatRecordListView;
import com.linkb.jstx.component.ChattingInputPanelView;
import com.linkb.jstx.component.CopyPhotoTipsPopupWindow;
import com.linkb.jstx.component.CustomSwipeRefreshLayout;
import com.linkb.jstx.component.RecordingButton;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.dialog.PermissionDialog;
import com.linkb.jstx.dialog.ReadDelteSetTimeDialog;
import com.linkb.jstx.dialog.SendCardDialog;
import com.linkb.jstx.listener.OnInputPanelEventListener;
import com.linkb.jstx.listener.OnMessageSendListener;
import com.linkb.jstx.listener.OnTouchDownListenter;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.model.ChatFile;
import com.linkb.jstx.network.model.ChatMap;
import com.linkb.jstx.network.model.ChatVoice;
import com.linkb.jstx.network.model.SNSChatImage;
import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.CoinTransferResult;
import com.linkb.jstx.network.result.SendCardsResult;
import com.linkb.jstx.network.result.SendMessageResult;
import com.linkb.jstx.network.result.SendRedPacketResult;
import com.linkb.jstx.network.result.v2.GetMessageDestroySwithResult;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.ClipboardUtils;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.InputSoftUtils;
import com.linkb.jstx.util.MessageUtil;
import com.linkb.jstx.util.TimeUtils;
import com.linkb.video.RoomActivity;
import com.linkb.video.utils.ToastUtils;

import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.EasyPermissions;

import static com.linkb.jstx.activity.contact.SendCardsSelectContactActivity.SELECT_CONTACTS_CARDS_REQUEST;


public class FriendChatActivity extends CIMMonitorActivityWithoutImmersion implements SendCardDialog.SendCardsClickListener, EasyPermissions.PermissionCallbacks, OnInputPanelEventListener, OnTouchDownListenter, SwipeRefreshLayout.OnRefreshListener, RecordingButton.OnRecordCompletedListener, OnMessageSendListener, CopyPhotoTipsPopupWindow.BitmapClickListener, ChattingInputPanelView.ChattingPanelClickListener {

    private final static int PERMISSION_RADIO_SETTING = 0x101;

    protected User mSelf;
    private RecordingButton mVoiceButton;
    private CustomSwipeRefreshLayout mSwipeRefreshLayout;
    protected ChatRecordListViewAdapter mAdapter;
    protected ChatRecordListView mChatListView;
    protected ChattingInputPanelView inputPanelView;

    LinearLayout viewReadDeleteSetTimeItem;
    TextView viewviewReadDeleteSetTime;

    protected MessageSource mMessageSource;

    private String reMarikName;

    public static final int SEND_RED_PACKET_REQUEST_CODE = 0x19;

    /** 转账
    * */
    public static final int COIN_TRANSFER_REQUEST_CODE = 0x22;

    public static final int PERSONINFO_REQUEST_CODE = 0x20;

    /** 发送名片选择联系人
    * */
    public static final int SEND_CARDS_CHOOSE_CONTACTS_REQUEST_CODE = 0x21;

    private boolean isDeleteFriend;

    private CopyPhotoTipsPopupWindow mCopyPhotoTipsPopupWindow;

    private static final String[] MANDATORY_PERMISSIONS = {
//            "android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.RECORD_AUDIO",
    };

    /** 视频还是语音通话
    * */
    protected boolean isVideo = true;
    /** 房间名称
    * */
    protected String mRootName;

    boolean isSendMsgInReadDelte=false;
    String frienAccount;

    @Override
    public void initComponents() {
        frienAccount=getIntent().getStringExtra(Constant.CHAT_OTHRES_ID);
        reMarikName = getIntent().getStringExtra(Constant.CHAT_OTHRES_NAME);
        if (!TextUtils.isEmpty(reMarikName)) setToolbarTitle(reMarikName);

        mMessageSource = getMessageSource(mListener);

        if (mMessageSource != null){
            postQueryFriend(mMessageSource);
        }
        httpCheckMessageDestroySwith();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        reMarikName = intent.getStringExtra(Constant.CHAT_OTHRES_NAME);
        if (!TextUtils.isEmpty(reMarikName)) setToolbarTitle(reMarikName);

        mMessageSource = getMessageSource(mListener);

        if (mMessageSource != null){
            postQueryFriend(mMessageSource);
        }

    }

    private void postQueryFriend(MessageSource friend){
        if (friend == null) {
            MessageRepository.deleteBySenderOrReceiver(getIntent().getStringExtra(Constant.CHAT_OTHRES_ID));
            Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            intent.putExtra(ChatItem.NAME, new ChatItem(null, new Friend(getIntent().getStringExtra(Constant.CHAT_OTHRES_ID))));
            LvxinApplication.sendLocalBroadcast(intent);
            showToastView(R.string.tips_chat_object_notexist);
            finish();
            return;
        }

        mMessageSource = friend;


        mSelf = Global.getCurrentUser();

        initBottomInputPanelView();

        mVoiceButton = findViewById(R.id.voiceButton);
        mVoiceButton.setOnRecordCompletedListener(this);

        mChatListView = findViewById(R.id.chat_list);
        mChatListView.setAdapter(mAdapter = new ChatRecordListViewAdapter(mMessageSource));
        mAdapter.setFriendAccount(frienAccount);
        initChatListViewListener();
        loadLocalMessageListView(mMessageSource.getId());

        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        viewReadDeleteSetTimeItem=findViewById(R.id.viewReadDeleteSetTimeItem);
        viewviewReadDeleteSetTime=findViewById(R.id.viewviewReadDeleteSetTime);
        viewReadDeleteSetTimeItem.setVisibility(View.GONE);
        viewviewReadDeleteSetTime.setOnClickListener(this);

        handleReadAllMessage();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if(view.getId()==R.id.viewviewReadDeleteSetTime){
            ReadDelteSetTimeDialog setTimeDialog=new ReadDelteSetTimeDialog(this);
            setTimeDialog.show();
            setTimeDialog.setFriendAccount(frienAccount);
            setTimeDialog.setOnSetTimeCallback(new ReadDelteSetTimeDialog.OnSetTimeCallback() {
                @Override
                public void onSetTimeCallback(int state, int time) {
                    System.out.println("==onSetTimeCallback=="+state+","+time);
                }
            });
        }
    }

    private HttpRequestListener<BasePersonInfoResult> mListener = new HttpRequestListener<BasePersonInfoResult>() {
        @Override
        public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
            if (result.isSuccess()){
                Friend friend = User.UserToFriend(result.getData());
                FriendRepository.save(friend);
                postQueryFriend(friend);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    protected void initBottomInputPanelView(){
        inputPanelView = findViewById(R.id.inputPanel);
        inputPanelView.setOnInputPanelEventListener(this);
        inputPanelView.setChattingPanelClickListener(this);
//        inputPanelView.setRedPacketType(Constant.RedPacketType.COMMON_RED_PACKET);
        inputPanelView.setInputText(Global.getLastChatDraft(mMessageSource));
        inputPanelView.setTag(R.id.target,mMessageSource);
    }

    void initChatListViewListener() {
        mChatListView.setOnTouchDownListenter(this);
        mChatListView.setOnMessageSendListener(this);
    }

    void handleReadAllMessage(){
        if (mMessageSource != null){
            MessageRepository.batchReadFriendMessage(mMessageSource.getId());
        }
    }


    private void loadLocalMessageListView(String otherId) {
        List<Message> data = loadLocalMessageRecord(otherId);
        if (data != null && !data.isEmpty()) {
            int position = mAdapter.getItemCount();
            mAdapter.addAllMessage(data);
            if (position == 0) {
                mChatListView.scrollToBottom();
            } else {
                mChatListView.scrollToPosition(mAdapter.getItemCount() - position - 1);
            }

        }
    }

    protected List<Message>  loadLocalMessageRecord(String otherId) {
        List<Message> data = MessageRepository.queryUserMessageSection(otherId,mAdapter.getMessaeCount(), Constant.MESSAGE_PAGE_SIZE);
        return data;
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {
        Message msg = MessageUtil.transform(message);
        if ((Objects.equals(message.getAction(), Constant.MessageAction.ACTION_0)
                || Objects.equals(message.getAction(), Constant.MessageAction.ACTION_ReadDelete)
             )
                && Objects.equals(msg.sender, mMessageSource.getId())) {

            mAdapter.addMessage(MessageUtil.transform(message));
            mChatListView.smartScrollToBottom();
        }

        //收到好友阅读某条消息
        if (Objects.equals(message.getAction(),Constant.MessageAction.ACTION_108)) {
            //阅读的消息ID为 消息的content字段
            msg.id = Long.parseLong(message.getContent());
            BaseToMessageView view = mChatListView.findViewWithTag(msg);
            if (view != null) {
                view.showReadMark();
            }
        }
    }

    @Override
    public void onRefresh() {

        loadLocalMessageListView(mMessageSource.getId());
        mSwipeRefreshLayout.onRefreshCompleted();
    }

    @Override
    public void finish() {
        InputSoftUtils.hideSoftInput(this);
        super.finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1 && !Objects.equals("image/jpeg",data.getType())) {
            // 视频
            SNSVideo video = (SNSVideo) data.getSerializableExtra(SNSVideo.class.getName());
            saveAndDisplayMessage(new Gson().toJson(video), Constant.MessageFormat.FORMAT_VIDEO);
            return;
        }

        // 图片
        if (resultCode == RESULT_OK &&  (requestCode == 2 || (requestCode == 1 && Objects.equals("image/jpeg",data.getType())))) {
            SNSChatImage chatImage = new SNSChatImage();
            chatImage.image = data.getData().toString();
            saveAndDisplayMessage(new Gson().toJson(chatImage), Constant.MessageFormat.FORMAT_IMAGE);
            return;
        }
        // 文件
        if (resultCode == RESULT_OK && requestCode == 3) {
            ChatFile file = (ChatFile) data.getSerializableExtra(ChatFile.class.getName());
            saveAndDisplayMessage(new Gson().toJson(file), Constant.MessageFormat.FORMAT_FILE);
            return;
        }

        // 地图
        if (resultCode == RESULT_OK && requestCode == 4) {
            ChatMap map = (ChatMap) data.getSerializableExtra(ChatMap.class.getName());
            saveAndDisplayMessage(new Gson().toJson(map), Constant.MessageFormat.FORMAT_MAP);
            return;
        }

        //红包
        if (resultCode == RESULT_OK && requestCode == SEND_RED_PACKET_REQUEST_CODE){
            SendRedPacketResult.DataBean dataBean = (SendRedPacketResult.DataBean) data.getSerializableExtra(SendRedPacketResult.DataBean.class.getName());
            String messageExtra =  new Gson().toJson(dataBean);

            saveAndDisplayMessage(getResources().getString(R.string.label_message_from_red_packet, dataBean.getRemark()),
                    messageExtra, Constant.MessageFormat.FORMAT_RED_PACKET);
            return;
        }

        //转账
        if (resultCode == RESULT_OK && requestCode == COIN_TRANSFER_REQUEST_CODE) {
            CoinTransferResult.DataBean dataBean = (CoinTransferResult.DataBean) data.getSerializableExtra(CoinTransferResult.DataBean.class.getName());
            String messageExtra =  new Gson().toJson(dataBean);
            saveAndDisplayMessage(getResources().getString(R.string.label_message_from_red_packet, dataBean.getRemark()),
                    messageExtra, Constant.MessageFormat.FORMAT_COIN_TRANSFER);
            return;
        }


        //修改了备注
        if (resultCode == RESULT_OK && requestCode ==PERSONINFO_REQUEST_CODE){
            reMarikName = data.getStringExtra("reMarkName");
            if (mMessageSource instanceof  Friend){
                Friend friend = (Friend) mMessageSource;
                friend.name = reMarikName;
            }

            isDeleteFriend = data.getBooleanExtra("deleteFriend", false);
            if (isDeleteFriend) finish();
            setToolbarTitle(reMarikName);
            return;
        }

        //发送名片选择了好友
        if (resultCode == RESULT_OK && requestCode ==SELECT_CONTACTS_CARDS_REQUEST){
            String userAccount = data.getStringExtra(SendCardsSelectContactActivity.SELECT_CONTACTS_CARDS_REQUEST_KEY_ACCOUNT);
            String userName = data.getStringExtra(SendCardsSelectContactActivity.SELECT_CONTACTS_CARDS_REQUEST_KEY_NAME);

            SendCardDialog mSendCardDialog = new SendCardDialog(this, mMessageSource.getId(), mMessageSource.getName(), userAccount, userName);
            mSendCardDialog.loadAvatar(mMessageSource.getId());
            mSendCardDialog.setOnDialogButtonClickListener(this);
            mSendCardDialog.show();
        }

    }

    protected String getMessageAction(String format,boolean checkReadDelete){
        if(!checkReadDelete){
            return Constant.MessageAction.ACTION_0;
        }else {
            if(Constant.MessageFormat.FORMAT_TEXT.equals(format)
                    || Constant.MessageFormat.FORMAT_VOICE.equals(format)
                    || Constant.MessageFormat.FORMAT_IMAGE.equals(format)){
                return isSendMsgInReadDelte()?Constant.MessageAction.ACTION_ReadDelete:Constant.MessageAction.ACTION_0;
            }else {
                return Constant.MessageAction.ACTION_0;
            }
        }
    }
    protected MessageSource getMessageSource(HttpRequestListener<BasePersonInfoResult> mListener) {
        String account = getIntent().getStringExtra(Constant.CHAT_OTHRES_ID);
        return FriendRepository.queryFriend(account, mListener);
    }


    @Override
    public void onSendButtonClicked(String content) {
        saveAndDisplayMessage(content, Constant.MessageFormat.FORMAT_TEXT);
        inputPanelView.clearText();
    }

    @Override
    public void onMessageInsertAt() {

    }

    @Override
    public void onMessageTextDelete(String txt) {

    }

    @Override
    public void onKeyboardStateChanged(boolean visable) {
        if (visable){
//            mChatListView.scrollToBottom();
        }
    }

    @Override
    public void onPanelStateChanged(boolean switchToPanel) {
       if (switchToPanel){
           mChatListView.scrollToBottom();
       }
    }


    protected void saveAndDisplayMessage(String context, String format) {
        saveAndDisplayMessage(context, null, format);
    }

    public void saveAndDisplayMessage(String context, String extra, String format) {
        Message message = new Message();
        message.id = System.currentTimeMillis();
        message.content = context;
        message.sender = mSelf.account;
        message.receiver = mMessageSource.getId();
        message.format = format;
        message.extra = extra;
        message.action = getMessageAction(format,true);
        message.timestamp = System.currentTimeMillis();
        message.state = Constant.MessageStatus.STATUS_NO_SEND;
        message.setEffectiveTime(Global.getFriendMsgReadDelteTime(frienAccount));
        mAdapter.addMessage(message);

        //发送的消息存储数据库
        saveMessage(message);

        mChatListView.scrollToBottom();

        if (Constant.MessageFormat.FORMAT_IMAGE.equals(format)
                || Constant.MessageFormat.FORMAT_FILE.equals(format)
                || Constant.MessageFormat.FORMAT_MAP.equals(format)
                || Constant.MessageFormat.FORMAT_VIDEO.equals(format)) {
            inputPanelView.reset();
        }

    }


    private void saveMessage(Message message) {
        //发送的消息存储数据库
        MessageRepository.add(message);
    }

    /**
     * 当删除最后一个消息，或者有草稿时刷新最近消息列表
     */
    protected void sendRecentRefreshBroadcast(){

        if (mMessageSource == null){
            Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            intent.putExtra(ChatItem.NAME, new ChatItem(mMessageSource));
            intent.setAction(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            LvxinApplication.sendLocalBroadcast(intent);
            return;
        }

        String draft = inputPanelView.getInputTextTrim();
        Global.saveChatDraft(mMessageSource, draft);
        if (mAdapter.getItemCount() == 0 && TextUtils.isEmpty(draft)) {
            Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            intent.putExtra(ChatItem.NAME, new ChatItem(mMessageSource));
            intent.setAction(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            LvxinApplication.sendLocalBroadcast(intent);
            return;
        }

        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        intent.putExtra(ChatItem.NAME, new ChatItem(mAdapter.getLastMessage(),mMessageSource));
        if (!TextUtils.isEmpty(draft)) {
            Message message = new Message();
            message.action = getMessageAction("",false);
            message.id = System.currentTimeMillis();
            message.timestamp = System.currentTimeMillis();
            intent.putExtra(ChatItem.NAME, new ChatItem(message, mMessageSource));
        }
        LvxinApplication.sendLocalBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handleReadAllMessage();
        GlobalVoicePlayer.getPlayer().stop();
        if (!isDeleteFriend){
            sendRecentRefreshBroadcast();
        }
        if(mAdapter!=null){
            mAdapter.stopTime();
            mAdapter=null;
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_chatting_friend;
    }

    Menu menu;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //single_icon
        this.menu=menu;
        getMenuInflater().inflate(R.menu.chat_friend_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(getMenuIcon());
        menu.findItem(R.id.menu_read_delete).setIcon(getMenuReadDeleteIcon());
        return super.onCreateOptionsMenu(menu);
    }

    public void updateMenuIc(){
       if(menu!=null) menu.findItem(R.id.menu_read_delete).setIcon(getMenuReadDeleteIcon());
    }
    protected int getMenuIcon() {
        return R.mipmap.more;
    }
    protected int getMenuReadDeleteIcon() {
        return isSendMsgInReadDelte()?R.mipmap.ic_msg_read_delete_flag2_sma:R.mipmap.ic_msg_read_delete_flag2_un;
    }

    protected void onToolbarMenuClicked() {
        Intent intent = new Intent(this, PersonInfoActivity.class);
        intent.putExtra(Friend.class.getName(), mMessageSource);
        intent.putExtra("reMarkName", reMarikName);
        startActivityForResult(intent, PERSONINFO_REQUEST_CODE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            onToolbarMenuClicked();
        }else if(item.getItemId() == R.id.menu_read_delete){
            httpUpdateMessageDestroySwith();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTouchDown() {
        inputPanelView.hidePanelAndKeyboard();
    }

    @Override
    public void onRecordCompleted(ChatVoice voice) {
        saveAndDisplayMessage(new Gson().toJson(voice), Constant.MessageFormat.FORMAT_VOICE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onMessageSendSuccess(Message msg) {

    }

    @Override
    public void onMessageSendFailure(Message msg) {

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if (requestCode == ChattingInputPanelView.PERMISSION_RADIO ) {
            inputPanelView.onRadioButtonClicked();
        }
        if (requestCode == ChattingInputPanelView.PERMISSION_RADIO_VIDEO){
            onChatInputCamera();
        }
        if (requestCode == ChattingInputPanelView.PERMISSION_LOCATION ) {
            onChatInputLocation();
        }
        if (requestCode == PERMISSION_RADIO_SETTING){
            videoConnect();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            PermissionDialog.create(this).show();
            return;
        }

        if (requestCode == ChattingInputPanelView.PERMISSION_RADIO ) {
            showToastView(R.string.tip_permission_audio_rejected);
        }
        if (requestCode == ChattingInputPanelView.PERMISSION_LOCATION ) {
            showToastView(R.string.tip_permission_location_rejected);
        }

    }

    public void sendCopyUriBitmap(Uri uri, View view){
        if (mCopyPhotoTipsPopupWindow == null){
            mCopyPhotoTipsPopupWindow = new CopyPhotoTipsPopupWindow(this, uri, this);
        }
        mCopyPhotoTipsPopupWindow.showAsDropDown(view, -ConvertUtils.dp2px(40), -ConvertUtils.dp2px(150), Gravity.TOP); //设置layout在PopupWindow中显示的位置
    }


    @Override
    public void sendBitmap(Uri uri) {
        SNSChatImage chatImage = new SNSChatImage();
        chatImage.image = uri.toString();
        saveAndDisplayMessage(new Gson().toJson(chatImage), Constant.MessageFormat.FORMAT_IMAGE);
        ClipboardUtils.clearUri();
    }

    @Override
    public void onChatInputCamera() {
        Intent intent = new Intent(FriendChatActivity.this, VideoRecorderActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onChatInputPhoto() {
        Intent intentFromGallery = new Intent(FriendChatActivity.this, PhotoAlbumActivity.class);
       startActivityForResult(intentFromGallery, 2);
    }

    @Override
    public void onChatInputFile() {
        Intent intentFile = new Intent(FriendChatActivity.this, FileChoiceActivity.class);
        startActivityForResult(intentFile, 3);
    }

    @Override
    public void onChatInputLocation() {
        Intent intentLoc = new Intent(FriendChatActivity.this, MapLocationActivity.class);
        startActivityForResult(intentLoc, 4);
    }

    @Override
    public void onChatInputSendCards() {
        Intent intent = new Intent(FriendChatActivity.this, SendCardsSelectContactActivity.class);
        if (mMessageSource instanceof Friend){
            Friend friend = (Friend)mMessageSource;
            intent.putExtra(Friend.class.getSimpleName(), friend);
        }
       startActivityForResult(intent , SendCardsSelectContactActivity.SELECT_CONTACTS_CARDS_REQUEST);
    }

    @Override
    public void onClickVideoConnect() {
        // 进入视频连接
        isVideo = true;
        if (checkAudioPermission()){
            videoConnect();
        }
    }

    @Override
    public void onClickVoiceConnect() {
        // 进入语音连接
        isVideo = false;
        if (checkAudioPermission()){
            videoConnect();
        }
    }

    @Override
    public void onChatInputRedPacket() {
        Intent intentRedPacket = new Intent(FriendChatActivity.this, RedPacketActivity.class);
        intentRedPacket.putExtra(Constant.RedPacketType.RED_PACKET_TYPE, Constant.RedPacketType.COMMON_RED_PACKET);
        startActivityForResult(intentRedPacket, SEND_RED_PACKET_REQUEST_CODE);
    }

    @Override
    public void onCoinTransfer() {
        if (mMessageSource instanceof Friend){
            Friend friend = (Friend)mMessageSource;
            Intent intentTransfer = new Intent(FriendChatActivity.this, CoinTransferActivity.class);
            intentTransfer.putExtra(Friend.class.getSimpleName(), friend);
            startActivityForResult(intentTransfer, FriendChatActivity.COIN_TRANSFER_REQUEST_CODE);
        }else {
            ToastUtils.s(FriendChatActivity.this, getString(R.string.coin_transfer_usdt_not_enough));
        }
    }

    @Override
    public void onRecommendContact() {
        startActivity(new Intent(FriendChatActivity.this, PhoneContactsActivity.class));
    }

    protected void videoConnect(){
        showProgressDialog("");
        mRootName = createRootName(mSelf.account, mMessageSource.getId());
        HttpServiceManager.requestRoomToken(mRootName, mSelf.account, AppTools.getPackageName(this), requestRoomTokenListener);
    }

    /** 生成房间号（3~64位字母、数组、_和-的组合）
     *  命名规则: blink-房间管理员-接收者-时间戳
     * */
    protected String createRootName(String sender, String receiver){
        long time = TimeUtils.getCurrentTime();
        return  "blink" + "-" + sender + "-" + receiver + "-" + String.valueOf(time) ;
    }

    private boolean checkAudioPermission() {
        if (EasyPermissions.hasPermissions(this, MANDATORY_PERMISSIONS)){
            return true;
        }
        EasyPermissions.requestPermissions(this, getString(R.string.tip_permission_audio_disable), PERMISSION_RADIO_SETTING, MANDATORY_PERMISSIONS );
        return false;
    }

    protected HttpRequestListener<BaseDataResult> requestRoomTokenListener = new HttpRequestListener<BaseDataResult>() {
        @Override
        public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
            hideProgressDialog();
            if (result.isSuccess()){
                String token = result.getData();
                goToVideo(token);
                if (isVideo){
                    sendVideoMessage( Constant.MessageAction.ACTION_5);
                }else {
                    sendVideoMessage( Constant.MessageAction.ACTION_4);
                }

            }else {
                showToastView(getString(R.string.null_room_token_toast));
            }

        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };


    /** 进入音视频界面
    * */
    protected void goToVideo(String token) {
        Intent intent = new Intent(FriendChatActivity.this, RoomActivity.class);
        intent.putExtra(RoomActivity.EXTRA_ROOM_ID, mRootName);
        intent.putExtra(RoomActivity.EXTRA_ROOM_TOKEN, token);
        intent.putExtra(RoomActivity.EXTRA_USER_ID, mSelf.account);
        intent.putExtra(RoomActivity.EXTRA_ISSENDER, true);
        intent.putExtra(RoomActivity.EXTRA_OPPOSITE_USER_ID, mMessageSource.getId());
        intent.putExtra(RoomActivity.EXTRA_IS_VIDEO, isVideo);
        startActivity(intent);
    }

    /** 发送音视频消息
    * */
    protected void sendVideoMessage(String action) {
        HttpServiceManager.sendVideoConnectMessage(mRootName, AppTools.getPackageName(this),
                mMessageSource.getId(), action, sendVideoMessage);

//        Message message = new Message();
//        message.id = System.currentTimeMillis();
//        message.content = "";
//        message.sender = mSelf.account;
//        message.receiver = mMessageSource.getId();
//        message.format = format;
//        message.extra = token;
//        message.action = action;
//        message.timestamp = System.currentTimeMillis();
//        message.state = Constant.MessageStatus.STATUS_NO_SEND;
//
//        HttpServiceManager.sendOnly(message);
    }

    protected HttpRequestListener<SendMessageResult> sendVideoMessage = new HttpRequestListener<SendMessageResult>() {
        @Override
        public void onHttpRequestSucceed(SendMessageResult result, OriginalCall call) {
            if (result.isSuccess()){

            }else {
                showToastView(result.message);
            }

        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    @Override
    public void onSendClick(String leaveMessage, String senderAccount, String sendName) {
        SendCardsResult.DataBean dataBean = new SendCardsResult.DataBean();
        dataBean.setToUserAccount(mMessageSource.getId());
        dataBean.setToUsername(mMessageSource.getName());
        dataBean.setLeaveMessage(leaveMessage);
        dataBean.setCardsUserAccount(senderAccount);
        dataBean.setCardsUserName(sendName);
        dataBean.setSendCardAccount(mSelf.account);
        dataBean.setSendCardName(mSelf.name);
        String messageExtra =  new Gson().toJson(dataBean);

        saveAndDisplayMessage(getResources().getString(R.string.label_message_from_send_cards, sendName),
                messageExtra, Constant.MessageFormat.FORMAT_SEND_CARDS);
    }

    @Override
    public void onCancelClick() {

    }


    public void httpUpdateMessageDestroySwith(){
        String account=frienAccount;//Global.getCurrentUser().account;
        final boolean isOpen=isSendMsgInReadDelte()?false:true;
        HttpServiceManagerV2.updateMessageDestroySwith(account, isOpen ? 1 : 0, new HttpRequestListener<GetMessageDestroySwithResult>() {
            @Override
            public void onHttpRequestSucceed(GetMessageDestroySwithResult result, OriginalCall call) {
                if(result.isSuccess()){
                    setSendMsgInReadDelte(result.getData().getMessageDestroySwith()==1?true:false);
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }
    public void httpCheckMessageDestroySwith(){
        String account=frienAccount;//Global.getCurrentUser().account;
        HttpServiceManagerV2.getMessageDestroySwith(account, new HttpRequestListener<GetMessageDestroySwithResult>() {
            @Override
            public void onHttpRequestSucceed(GetMessageDestroySwithResult result, OriginalCall call) {
                if(result.isSuccess()){
                    setSendMsgInReadDelte(result.getData().getMessageDestroySwith()==1);
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
            }
        });
    }

    public boolean isSendMsgInReadDelte() {
        return isSendMsgInReadDelte;
    }
    public void setSendMsgInReadDelte(boolean sendMsgInReadDelte) {
        isSendMsgInReadDelte = sendMsgInReadDelte;
        updateMenuIc();
        if(viewReadDeleteSetTimeItem!=null){
            viewReadDeleteSetTimeItem.setVisibility(sendMsgInReadDelte?View.VISIBLE:View.GONE);
        }
    }
}
