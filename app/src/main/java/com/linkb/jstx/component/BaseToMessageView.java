
package com.linkb.jstx.component;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.contact.MessageForwardActivity;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.dialog.ContentMenuWindow;
import com.linkb.jstx.listener.OnMenuClickedListener;
import com.linkb.jstx.listener.OnMessageDeleteListener;
import com.linkb.jstx.listener.OnMessageSendListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.R;
import com.linkb.jstx.network.result.v2.SendRedPacketResultV2;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.MessageUtil;

import java.util.Objects;

import static com.linkb.jstx.app.Constant.MessageFormat.FORMAT_RED_PACKET;
import static com.linkb.jstx.app.URLConstant.MESSAGE_REVOKE_URL;

public abstract class BaseToMessageView extends RelativeLayout implements OnMessageSendListener, OnLongClickListener, OnMenuClickedListener, OnClickListener, HttpRequestListener, View.OnTouchListener {
    ProgressBar sendProgressbar;
    private WebImageView icon;
    EmoticonTextView textView;
    ChatWebImageView imageView;
    ChatReadDeleteView chatReadDeleteView;
    ChatVoiceView voiceView;
    ChatFileView fileView;
    ChatMapView mapView;
    ChatVideoView videoView;
    ChatRedPacketView redPacketView;
    ChatCoinTransferView transferView;
    ChatPersonCardsView personCardsView;

    Message message;
    private MessageSource others;
    private OnMessageDeleteListener onMessageDeleteListener;
    private ContentMenuWindow optionsDialog;
    private View icon_resend;
    private TextView readMark;

    public BaseToMessageView(Context context) {
        super(context);
    }


    public BaseToMessageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    protected abstract View getContentView();

    public void setOnMessageDeleteListener(OnMessageDeleteListener onMessageDeleteListener) {
        this.onMessageDeleteListener = onMessageDeleteListener;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        readMark = findViewById(R.id.read_status);

        icon_resend = findViewById(R.id.icon_resend);
        icon_resend.setOnClickListener(this);
        sendProgressbar = findViewById(R.id.sendProgressbar);
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
        if (container instanceof ChatRedPacketView){
            redPacketView = (ChatRedPacketView) container;
        }
        if (container instanceof ChatPersonCardsView){
            personCardsView = (ChatPersonCardsView) container;
        }
        if (container instanceof ChatCoinTransferView){
            transferView = (ChatCoinTransferView) container;
        }
        if (container instanceof ChatReadDeleteView) {
            chatReadDeleteView = (ChatReadDeleteView) container;
        }
        optionsDialog = new ContentMenuWindow(getContext());
        optionsDialog.setOnMenuClickedListener(this);
    }


    public final void displayMessage(Message message, User self, MessageSource others) {
        this.message = message;
        this.others = others;
        setTag(message);
        readMark.setVisibility(View.INVISIBLE);
        if (message.format.equals(FORMAT_RED_PACKET) && message.extra != null){
            Log.i("fromJson==",message.extra);
            SendRedPacketResultV2.DataBean  dataBean =null;
            try {
                dataBean=new Gson().fromJson(message.extra, SendRedPacketResultV2.DataBean.class);
                icon.load(FileURLBuilder.getUserIconUrl(dataBean.getSendAccount()), R.mipmap.lianxiren, 999);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }else {
            icon.load(FileURLBuilder.getUserIconUrl(self.account), R.mipmap.lianxiren, 999);
        }

        getContentView().setOnLongClickListener(this);
        getContentView().setOnTouchListener(this);
        getContentView().setOnClickListener(null);
        displayMessage();

        statusHandler();
    }


    protected abstract void displayMessage();

    void statusHandler() {

        if (Constant.MessageStatus.STATUS_SENDING.equals(message.state)) {
            showSendProgressbar();
        } else {
            sendProgressbar.setVisibility(View.INVISIBLE);
        }

        if (Constant.MessageStatus.STATUS_SEND_FAILURE.equals(message.state)) {
            icon_resend.setVisibility(View.VISIBLE);
            icon_resend.setOnClickListener(this);
        } else {
            icon_resend.setVisibility(View.INVISIBLE);
        }

        if (Constant.MessageStatus.STATUS_NO_SEND.equals(message.state)) {

            sendMessage();
        }

        if (Constant.MessageStatus.STATUS_OTHERS_READ.equals(message.state) && message.isNeedShowReadStatus()) {
            showReadMark();
        }
        if (Constant.MessageStatus.STATUS_SEND.equals(message.state) && message.isNeedShowReadStatus()) {
            showSendMark();
        }
    }

    public void showReadMark() {
        message.state = Constant.MessageStatus.STATUS_OTHERS_READ;
        if (ClientConfig.getMessageStatusVisable() && Objects.equals(message.action,Constant.MessageAction.ACTION_0)) {
            readMark.setVisibility(View.VISIBLE);
            readMark.setText(R.string.tip_has_read);
            readMark.setBackgroundResource(R.drawable.bg_status_already_read);
        }
    }

    private void showSendMark() {
        if (ClientConfig.getMessageStatusVisable() && message.isNeedShowReadStatus()) {
            readMark.setVisibility(View.VISIBLE);
            readMark.setText(R.string.tip_has_sent);
            readMark.setBackgroundResource(R.drawable.bg_status_already_sent);
        }
    }


    private void showSendProgressbar() {
        if (message.format.equals(Constant.MessageFormat.FORMAT_TEXT)) {
            sendProgressbar.setVisibility(View.VISIBLE);
        }
    }

    private void sendMessage() {

        Message message = (Message) this.getTag();
        showSendProgressbar();
        icon_resend.setVisibility(View.INVISIBLE);
        message.state = Constant.MessageStatus.STATUS_SENDING;
        MessageRepository.updateStatus(message.id, Constant.MessageStatus.STATUS_SENDING);

        //发送状态，通知相关界面更新UI
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        ChatItem chat = new ChatItem(message, others);
        intent.putExtra(ChatItem.NAME, chat);
        LvxinApplication.sendLocalBroadcast(intent);

        HttpServiceManager.send(message);

    }

    @Override
    public void onMessageSendSuccess(Message msg) {
        icon_resend.setVisibility(View.INVISIBLE);
        sendProgressbar.setVisibility(View.INVISIBLE);
        showSendMark();
        message.state = msg.state;
    }

    @Override
    public void onMessageSendFailure(Message msg) {
        icon_resend.setVisibility(View.VISIBLE);
        sendProgressbar.setVisibility(View.INVISIBLE);
        message.state = msg.state;

        onMessageSendFailure();
    }

    void onMessageSendFailure() {
    }

    @Override
    public boolean onLongClick(View arg0) {

        Message message = (Message) this.getTag();
        boolean canCopy = Constant.MessageFormat.FORMAT_TEXT.equals(message.format);
        optionsDialog.buildChatRecordMenuGroup(canCopy,true, true, false);
        optionsDialog.show(arg0);
        return true;
    }

    @Override
    public void onMenuItemClicked(int id) {

        Message message = (Message) this.getTag();
        if (id == R.id.menu_copy) {
            ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, textView.getText().toString()));
            Snackbar.make(this, R.string.tip_copy_successed, Snackbar.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_delete) {

            onMessageDeleteListener.onDelete(message);
        }
        if (id == R.id.menu_revoke) {
            HttpServiceManager.revokeMessage(message.id,this);

        }
        if (id == R.id.menu_forward) {
            Intent intent = new Intent(getContext(), MessageForwardActivity.class);
            if (textView != null) {
                Message target = MessageUtil.clone(message);
                target.content = textView.getText().toString();
                intent.putExtra(Message.NAME, target);
            } else {
                intent.putExtra(Message.NAME, message);
            }
            getContext().startActivity(intent);
        }
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icon_resend) {
            sendMessage();
        }
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
//        ((BaseActivity) getContext()).hideProgressDialog();
        if (result.isSuccess() && call.equalsDelete(MESSAGE_REVOKE_URL)) {
            onMessageDeleteListener.onDelete((Message) this.getTag());
        }
        if (Objects.equals(result.code,Constant.ReturnCode.CODE_423) && call.equalsDelete(MESSAGE_REVOKE_URL)) {
            ((BaseActivity) getContext()).showToastView(R.string.tip_message_revoke_failed);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
//        ((BaseActivity) getContext()).hideProgressDialog();
    }


    @Override
    public boolean onTouch(View v, MotionEvent event){
        v.setTag(R.id.x,event.getX());
        v.setTag(R.id.y,event.getY());
        return false;
    }
}
