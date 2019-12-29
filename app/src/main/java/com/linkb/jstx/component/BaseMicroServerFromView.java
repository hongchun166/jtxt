
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.RelativeLayout;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.dialog.ContentMenuWindow;
import com.linkb.jstx.listener.OnMenuClickedListener;
import com.linkb.jstx.listener.OnMessageDeleteListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.R;

public abstract class BaseMicroServerFromView extends RelativeLayout implements OnMenuClickedListener, OnLongClickListener,View.OnTouchListener {
    MessageSource others;
    Message message;
    private OnMessageDeleteListener onMessageDeleteListener;
    private ContentMenuWindow optionsDialog;


    public BaseMicroServerFromView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        optionsDialog = new ContentMenuWindow(getContext());
        optionsDialog.setOnMenuClickedListener(this);
    }

    public final void displayMessage(Message message,MessageSource others) {
        this.message = message;
        this.others = others;
        setTag(message);

        if (Message.STATUS_NOT_READ.equals(message.state)) {
            message.state = Message.STATUS_READ;
            MessageRepository.updateStatus(message.id, Message.STATUS_READ);
        }

        displayMessage();
    }


    protected abstract void displayMessage();


    @Override
    public void onMenuItemClicked(int id) {

        if (id == R.id.menu_delete) {
            onMessageDeleteListener.onDelete(message);
        }

    }

    public void setOnMessageDeleteListener(OnMessageDeleteListener onMessageDeleteListener) {
        this.onMessageDeleteListener = onMessageDeleteListener;
    }

    @Override
    public boolean onLongClick(View arg0) {
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

}
