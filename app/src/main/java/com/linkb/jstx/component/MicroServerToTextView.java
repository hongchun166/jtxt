
package com.linkb.jstx.component;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.dialog.ContentMenuWindow;
import com.linkb.jstx.listener.OnMenuClickedListener;
import com.linkb.jstx.listener.OnMessageSendListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.activity.contact.MessageForwardActivity;
import com.linkb.jstx.listener.OnMessageDeleteListener;
import com.linkb.jstx.network.MicroServerMenuHander;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;

public class MicroServerToTextView extends RelativeLayout implements OnMessageSendListener, OnLongClickListener, OnMenuClickedListener, OnClickListener,View.OnTouchListener {
    private OnMessageDeleteListener onMessageDeleteListener;
    private ContentMenuWindow optionsDialog;
    private View icon_resend;
    private ProgressBar sendProgressbar;
    private WebImageView icon;
    private EmoticonTextView textView;
    private Message message;
    private MessageSource others;


    public MicroServerToTextView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public void setOnMessageDeleteListener(OnMessageDeleteListener onMessageDeleteListener) {
        this.onMessageDeleteListener = onMessageDeleteListener;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        icon_resend = findViewById(R.id.icon_resend);
        icon_resend.setOnClickListener(this);
        sendProgressbar = findViewById(R.id.sendProgressbar);
        icon = findViewById(R.id.icon);
        textView = findViewById(R.id.container);
        optionsDialog = new ContentMenuWindow(getContext());
        optionsDialog.setOnMenuClickedListener(this);
        sendProgressbar.setVisibility(View.GONE);

        int padding = textView.getPaddingLeft() + textView.getPaddingRight();
        textView.setMaxWidth(padding + Global.getChatTextMaxWidth());
    }


    public final void displayMessage(Message message,MessageSource others) {
        this.message = message;
        this.others = others;
        setTag(message);
        icon.load(FileURLBuilder.getUserIconUrl(Global.getCurrentAccount()), R.mipmap.lianxiren);
        textView.setFaceSize(Constant.EMOTION_FACE_SIZE);
        textView.setText(message.content);
        textView.setOnLongClickListener(this);
        textView.setOnTouchListener(this);
        statusHandler();
    }


    private void statusHandler() {
        if (Constant.MessageStatus.STATUS_SENDING.equals(message.state)) {
            sendProgressbar.setVisibility(View.VISIBLE);
        } else {
            sendProgressbar.setVisibility(View.GONE);
        }

        if (Constant.MessageStatus.STATUS_SEND_FAILURE.equals(message.state)) {
            icon_resend.setVisibility(View.VISIBLE);
            icon_resend.setOnClickListener(this);
        } else {
            icon_resend.setVisibility(View.GONE);
        }

        if (Constant.MessageStatus.STATUS_NO_SEND.equals(message.state)) {

            sendProgressbar.setVisibility(View.VISIBLE);
            sendMessage();
        }
    }


    private void sendMessage() {

        MessageRepository.add(message);
        icon_resend.setVisibility(View.GONE);
        message.state = Constant.MessageStatus.STATUS_SENDING;
        MessageRepository.updateStatus(message.id, Constant.MessageStatus.STATUS_SENDING);


        //发送状态，通知相关界面更新UI
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        ChatItem chat = new ChatItem(message, others);
        intent.putExtra(ChatItem.NAME, chat);
        LvxinApplication.sendLocalBroadcast(intent);

        MicroServerMenuHander.execute((MicroServer) others, message);
    }

    @Override
    public boolean onLongClick(View arg0) {

        Message message = (Message) this.getTag();
        boolean isTextMessage = Constant.MessageFormat.FORMAT_TEXT.equals(message.format);
        optionsDialog.buildChatRecordMenuGroup(isTextMessage, true,false, false);
        optionsDialog.show(arg0);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        v.setTag(R.id.x,event.getX());
        v.setTag(R.id.y,event.getY());
        return false;
    }

    @Override
    public void onMenuItemClicked(int id) {

        Message message = (Message) this.getTag();
        if (id == R.id.menu_copy) {
            ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, message.content));
            Snackbar.make(this, R.string.tip_copy_successed, Snackbar.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_delete) {

            onMessageDeleteListener.onDelete(message);
        }
        if (id == R.id.menu_forward) {
            Intent intent = new Intent(getContext(), MessageForwardActivity.class);
            intent.putExtra(Message.NAME, message);
            getContext().startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.icon_resend) {
            MessageRepository.deleteById(message.id);
            sendMessage();
        }
    }

    @Override
    public void onMessageSendSuccess(Message msg) {
        icon_resend.setVisibility(View.GONE);
        sendProgressbar.setVisibility(View.GONE);
        message.state = msg.state;
    }

    @Override
    public void onMessageSendFailure(Message msg) {
        icon_resend.setVisibility(View.VISIBLE);
        sendProgressbar.setVisibility(View.GONE);
        message.state = msg.state;
    }
}
