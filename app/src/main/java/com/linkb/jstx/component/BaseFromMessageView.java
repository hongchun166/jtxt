
package com.linkb.jstx.component;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewStub;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.linkb.R;
import com.linkb.jstx.activity.contact.MessageForwardActivity;
import com.linkb.jstx.activity.contact.PersonInfoActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.dialog.ContentMenuWindow;
import com.linkb.jstx.listener.OnMenuClickedListener;
import com.linkb.jstx.listener.OnMessageDeleteListener;
import com.linkb.jstx.message.extra.MessageExtra;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.SendCardsResult;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.MessageUtil;
import com.linkb.jstx.util.StringUtils;

import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;

public abstract class BaseFromMessageView extends RelativeLayout implements OnMenuClickedListener, OnLongClickListener, OnClickListener, View.OnTouchListener {
    Message message;
    private WebImageView icon;
    EmoticonTextView textView;
    ChatWebImageView imageView;
    ChatReadDeleteView chatReadDeleteView;
    ChatVoiceView voiceView;
    ChatFileView fileView;
    ChatMapView mapView;
    ChatVideoView videoView;
    ChatCoinTransferView transferView;
    ChatPersonCardsView personCardsView;
    private ContentMenuWindow optionsDialog;
    private ViewStub fromNameStub;
    private TextView fromName;

    /** 是否可以查看群员资料
    * */
    private boolean enableCkeckMemberInfo = true;

    private OnMessageDeleteListener onMessageDeleteListener;

    public BaseFromMessageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    protected abstract View getContentView();

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        fromNameStub =  findViewById(R.id.fromNameStub);
        icon = findViewById(R.id.logo);
        View container = findViewById(R.id.container);
        if (container instanceof EmoticonTextView) {
            textView = (EmoticonTextView) container;
        }
        if (container instanceof ChatWebImageView) {
            imageView = (ChatWebImageView) container;
        }
        if (container instanceof ChatVoiceView) {
            voiceView = (ChatVoiceView) container;
        }
        if (container instanceof ChatFileView) {
            fileView = (ChatFileView) container;
        }
        if (container instanceof ChatMapView) {
            mapView = (ChatMapView) container;
        }
        if (container instanceof ChatVideoView) {
            videoView = (ChatVideoView) container;
        }
        if (container instanceof  ChatPersonCardsView){
            personCardsView = (ChatPersonCardsView) container;
        }
        if (container instanceof ChatCoinTransferView){
            transferView = (ChatCoinTransferView) container;
        }
        if (container instanceof ChatReadDeleteView) {
            chatReadDeleteView = (ChatReadDeleteView) container;
        }
        getContentView().setOnLongClickListener(this);
        optionsDialog = new ContentMenuWindow(getContext());
        optionsDialog.setOnMenuClickedListener(this);
        icon.setOnClickListener(this);
    }

    private TextView getFromNameTextView(){
        if (fromName==null)
        {
            fromName = (TextView) fromNameStub.inflate();
        }
        return fromName;
    }
    public final void displayMessage(Message message, MessageSource others) {

        icon.setTag(R.id.target, others);
        this.message = message;

        if (message.action.equals(Constant.MessageAction.ACTION_3)
                || message.action.equals(Constant.MessageAction.ACTION_GrpRedPack)
                || ((others instanceof Group) && message.action.equals(Constant.MessageAction.ACTION_1))) {
            if (!TextUtils.isEmpty(message.extra)){
                Friend friend = null;
                if (message.extra.contains(MessageExtra.TYPE_AT)){  // @人的时候的消息格式 为发送者@接受者: extra:13298682700AT://17602060697
                    int atIndex = message.extra.indexOf(MessageExtra.TYPE_AT);
                    String sender = message.extra.substring(0,atIndex);
                    friend = FriendRepository.queryFriend(sender, mListener);
                }else if (message.format.equals(Constant.MessageFormat.FORMAT_SEND_CARDS)){
                    SendCardsResult.DataBean dataBean = new Gson().fromJson(message.extra, SendCardsResult.DataBean.class);
                    friend = FriendRepository.queryFriend(dataBean.getSendCardAccount(), mListener);
                }else {
                    friend = FriendRepository.queryFriend(message.extra, mListener);
                }


                if (friend != null){
                    icon.setTag(R.id.target, friend);
//                icon.load(friend.getWebIcon(),R.mipmap.lianxiren);

                    icon.setOnLongClickListener(this);

                    getFromNameTextView().setVisibility(VISIBLE);
                    getFromNameTextView().setText(friend.getName());
                    icon.load(FileURLBuilder.getUserIconUrl(friend.account), R.mipmap.lianxiren, 999);
                }

            }else if (others instanceof Group) {
                Group group = (Group) others;
                icon.load(FileURLBuilder.getUserIconUrl(group.founder), R.mipmap.lianxiren,999);
            }
        }else {
//            icon.load(FileURLBuilder.getUserIconUrl(message.sender), R.mipmap.lianxiren);
            icon.load(others.getWebIcon(), others.getDefaultIconRID(), 999);
        }

        setTag(message);
        getContentView().setOnClickListener(null);
        getContentView().setOnTouchListener(this);

        displayMessage();

        handleMessageState();

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

    private void postQueryFriend(Friend friend){
        icon.setTag(R.id.target, friend);
        icon.setOnLongClickListener(this);

        getFromNameTextView().setVisibility(VISIBLE);
        getFromNameTextView().setText(friend.getName());
    }

    void handleMessageState() {
        //阅读新消息的时候，像对方发送阅读状态
        if (Message.STATUS_NOT_READ.equals(message.state)) {

            HttpServiceManager.read(message);
            message.state = Message.STATUS_READ;
        }

    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        v.setTag(R.id.x,event.getX());
        v.setTag(R.id.y,event.getY());
        return false;
    }

    @Override
    public Object getTag() {
        return icon.getTag(R.id.CHAT_RECORD_TARGET);
    }

    @Override
    public void setTag(Object obj) {
        icon.setTag(R.id.CHAT_RECORD_TARGET, obj);
    }


    protected abstract void displayMessage();


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logo && icon.getTag(R.id.target) instanceof Friend && enableCkeckMemberInfo) {


            Intent intent = new Intent(getContext(), PersonInfoActivity.class);
            Friend friend = (Friend) icon.getTag(R.id.target);
            intent.putExtra(Friend.class.getName(), friend);

            if (message.action.equals(Constant.MessageAction.ACTION_3)) {
                Friend friend2 = FriendRepository.queryFriend(friend.account, mListener2);
                if (friend2 != null){
                    intent.putExtra(Friend.class.getName(), friend2);
                    getContext().startActivity(intent);
                }

            }else {
                getContext().startActivity(intent);
            }
        }
    }

    private HttpRequestListener<BasePersonInfoResult> mListener2 = new HttpRequestListener<BasePersonInfoResult>() {
        @Override
        public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
            if (result.isSuccess()){
                Friend friend = User.UserToFriend(result.getData());
                FriendRepository.save(friend);
                postQueryFriend2(friend);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    private void postQueryFriend2(Friend friend){
        Intent intent = new Intent(getContext(), PersonInfoActivity.class);
        intent.putExtra(Friend.class.getName(), friend);
        getContext().startActivity(intent);
    }

    @Override
    public void onMenuItemClicked(int id) {
        if (id == R.id.menu_qr_code_copy){
            //识别二维码
            // TODO: 2019/3/28
            imageView.setDrawingCacheEnabled(true);
            Bitmap bitmap = imageView.getDrawingCache();
            decode(bitmap, "");
        }

        if (id == R.id.menu_copy) {
            ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, textView.getText().toString()));
            Snackbar.make(this, R.string.tip_copy_successed, Snackbar.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_delete) {
            onMessageDeleteListener.onDelete(message);
        }
        if (id == R.id.menu_forward) {
            Intent intent = new Intent(getContext(), MessageForwardActivity.class);
            intent.putExtra(Message.NAME, message);
            if (textView != null) {
                Message target = MessageUtil.clone(message);
                target.content = textView.getText().toString();
                intent.putExtra(Message.NAME, target);
            }
            getContext().startActivity(intent);
        }
    }

    /** 识别二维码
    * */
    private void decode(final Bitmap bitmap, final String errorTip) {
        /*
        这里为了偷懒，就没有处理匿名 AsyncTask 内部类导致 Activity 泄漏的问题
        请开发在使用时自行处理匿名内部类导致Activity内存泄漏的问题，处理方式可参考 https://github
        .com/GeniusVJR/LearningNotes/blob/master/Part1/Android/Android%E5%86%85%E5%AD%98%E6%B3%84%E6%BC%8F%E6%80%BB%E7%BB%93.md
         */
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                return QRCodeDecoder.syncDecodeQRCode(bitmap);
            }

            @Override
            protected void onPostExecute(String result) {
                if (TextUtils.isEmpty(result)) {
                    Toast.makeText(getContext(), errorTip, Toast.LENGTH_SHORT).show();
                } else {
                    if (!TextUtils.isEmpty(result) && result.contains(Constant.QrCodeFormater.QR_CODE_SPLIT)){
                        String[] str = result.split(Constant.QrCodeFormater.QR_CODE_SPLIT);
                        if (str.length > 0) {
                            if (str[0].equals(Constant.QrCodeFormater.GROUP_QR_CODE)){
                                //申请加入群组
                                HttpServiceManager.applyJoinGroup(Long.valueOf(str[1]),  applyJoinGroupListener);
                            }else if (str[0].equals(Constant.QrCodeFormater.PERSON_QR_CODE)){
                                //添加好友
                                HttpServiceManager.addFriend(str[1], str[2],addFriendRequest);
                            }
                        }
                    }
                }
            }
        }.execute();
    }

    /** 申请好友
     * */
    private HttpRequestListener<BaseResult> addFriendRequest = new HttpRequestListener<BaseResult>() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
            if (result.isSuccess()){
                Toast.makeText(getContext(), getResources().getString(R.string.add_friend_success_tips), Toast.LENGTH_SHORT).show();
                LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
            }else {
                Toast.makeText(getContext(), result.message, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            Toast.makeText(getContext(), getResources().getString(R.string.read_qr_code_error), Toast.LENGTH_SHORT).show();
        }
    };

    private HttpRequestListener<BaseDataResult> applyJoinGroupListener = new HttpRequestListener<BaseDataResult>() {
        @Override
        public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
            if (result.isSuccess()){
                Toast.makeText(getContext(), getResources().getString(R.string.apply_join_group_tips), Toast.LENGTH_SHORT).show();
            }else {
                if (result.code.equals("470")){
                    Toast.makeText(getContext(), getResources().getString(R.string.apply_join_group_error_tips), Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getContext(), getResources().getString(R.string.read_qr_code_error), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            Toast.makeText(getContext(), getResources().getString(R.string.read_qr_code_error), Toast.LENGTH_SHORT).show();
        }
    };

    public void setOnMessageDeleteListener(OnMessageDeleteListener onMessageDeleteListener) {
        this.onMessageDeleteListener = onMessageDeleteListener;
        if(chatReadDeleteView!=null){
            chatReadDeleteView.setOnMessageDeleteListener(onMessageDeleteListener);
        }
    }

    public void setEnableCheckMemberInfo(boolean enableCheckMemberInfo){
        this.enableCkeckMemberInfo = enableCheckMemberInfo;
    }

    @Override
    public boolean onLongClick(View view) {

        if (view == icon){
            Intent intent = new Intent(Constant.Action.ACTION_ICON_LONGCLICKED);
            Friend friend = (Friend) icon.getTag(R.id.target);
            intent.putExtra(Friend.NAME,friend);
            LvxinApplication.sendLocalBroadcast(intent);
        }else{
            boolean canCopy = Constant.MessageFormat.FORMAT_TEXT.equals(message.format);
            if (view instanceof ChatWebImageView){
                optionsDialog.buildChatRecordMenuGroup(canCopy, true,false, true);
            }else {
                optionsDialog.buildChatRecordMenuGroup(canCopy, true,false, false);
            }

            optionsDialog.show(view);
        }

        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(Resources.getSystem().getDisplayMetrics().widthPixels, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


}
