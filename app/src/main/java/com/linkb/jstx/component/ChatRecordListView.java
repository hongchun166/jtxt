
package com.linkb.jstx.component;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.listener.OnTouchDownListenter;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.listener.OnMessageSendListener;
import com.linkb.jstx.listener.OnTransmitProgressListener;

import java.util.Objects;

public class ChatRecordListView extends RecyclerView {
    private OnTouchDownListenter onTouchDownListenter;
    private MessageSendReceiver messageSendReceiver;
    private OnMessageSendListener onMessageSendListener;

    public ChatRecordListView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
        setOverScrollMode(OVER_SCROLL_NEVER);
        setLayoutManager(new LinearLayoutManager(getContext()));
        setItemAnimator(new DefaultItemAnimator());
        int padding = (int) Resources.getSystem().getDisplayMetrics().density * 18;
        addItemDecoration(new PaddingDecoration(padding));
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {


        if (event.getAction() == MotionEvent.ACTION_DOWN && onTouchDownListenter != null) {
            onTouchDownListenter.onTouchDown();
        }
        return super.onTouchEvent(event);
    }

    public void smartScrollToBottom() {
        LinearLayoutManager linearManager = (LinearLayoutManager) getLayoutManager();
        if (linearManager.findLastVisibleItemPosition() >= linearManager.getItemCount() - 2){
            scrollToBottom();
        }
    }

    public void scrollToBottom() {
        scrollToPosition(getAdapter().getItemCount() - 1);
    }

    public void setOnMessageSendListener(OnMessageSendListener onMessageSendListener) {
        this.onMessageSendListener = onMessageSendListener;
    }

    public void setOnTouchDownListenter(OnTouchDownListenter onTouchDownListenter) {
        this.onTouchDownListenter = onTouchDownListenter;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LvxinApplication.unregisterLocalReceiver(messageSendReceiver);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        messageSendReceiver = new MessageSendReceiver();
        LvxinApplication.registerLocalReceiver(messageSendReceiver, messageSendReceiver.getIntentFilter());
    }



    public class MessageSendReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (Objects.equals(intent.getAction(), Constant.Action.ACTION_WINDOW_REFRESH_MESSAGE)) {
                ChatItem target = (ChatItem) intent.getSerializableExtra(ChatItem.NAME);

                View view = findViewWithTag(target.message);

                if (Constant.MessageStatus.STATUS_SEND.equals(target.message.state)) {
                    if (onMessageSendListener != null) {
                        onMessageSendListener.onMessageSendSuccess(target.message);
                    }
                    if (view instanceof OnMessageSendListener) {
                        ((OnMessageSendListener) view).onMessageSendSuccess(target.message);
                    }
                }
                if (Constant.MessageStatus.STATUS_SEND_FAILURE.equals(target.message.state)) {
                    if (onMessageSendListener != null) {
                        onMessageSendListener.onMessageSendFailure(target.message);
                    }
                    if (view instanceof OnMessageSendListener) {
                        ((OnMessageSendListener) view).onMessageSendFailure(target.message);
                    }
                }
            }

            if (intent.getAction().equals(Constant.Action.ACTION_UPLOAD_PROGRESS)) {
                String key = intent.getStringExtra("objectKey");
                View view = findViewWithTag(key);
                if (view instanceof OnTransmitProgressListener) {
                    ((OnTransmitProgressListener) view).onProgress(intent.getFloatExtra("progress", 0f));
                }
            }
        }

        IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_WINDOW_REFRESH_MESSAGE);
            filter.addAction(Constant.Action.ACTION_UPLOAD_PROGRESS);
            return filter;
        }
    }
}
