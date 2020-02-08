
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.linkb.R;
import com.linkb.jstx.activity.chat.ChatReadDeleteDigOpt;
import com.linkb.jstx.listener.OnMessageDeleteListener;
import com.linkb.jstx.model.Message;

public class ChatReadDeleteView extends RelativeLayout implements OnClickListener {

    private ImageView image;
    private String key;
    private Message message;
    OnMessageDeleteListener onMessageDeleteListener;
    public ChatReadDeleteView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }
    public void setOnMessageDeleteListener(OnMessageDeleteListener onMessageDeleteListener) {
        this.onMessageDeleteListener = onMessageDeleteListener;

    }
    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        image = this.findViewById(R.id.image);
    }
    public void initViews(Message msg) {
        this.message=msg;
    }
    @Override
    public void onClick(View view) {
        System.out.println("点击了阅读即焚消息：");
        ChatReadDeleteDigOpt chatReadDeleteDig=new ChatReadDeleteDigOpt();
        chatReadDeleteDig.setMessage(message).build(getContext()).show();
        chatReadDeleteDig.setOnReadDelteCallback(new ChatReadDeleteDigOpt.OnReadDelteCallback() {
            @Override
            public void onReadDelteCallback(Message message) {
                if(onMessageDeleteListener!=null){
                    onMessageDeleteListener.onDelete(message);
                }
            }
        });
    }


}
