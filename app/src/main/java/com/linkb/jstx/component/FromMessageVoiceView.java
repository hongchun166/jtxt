
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.model.Message;
import com.linkb.R;

import java.util.Objects;

public class FromMessageVoiceView extends BaseFromMessageView {
    private View readDot;

    public FromMessageVoiceView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public View getContentView() {
        return voiceView;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        readDot = findViewById(R.id.voiceReadDot);
    }

    @Override
    public void handleMessageState() {
        if (Message.STATUS_NOT_READ.equals(message.state)) {
            message.state = Message.STATUS_READ;
            MessageRepository.updateStatus(message.id, Message.STATUS_READ);
        }
    }

    @Override
    public void displayMessage() {
        voiceView.display(message, false);

        if (!Objects.equals(Message.STATUS_READ_OF_VOICE,message.handle)) {
            readDot.setVisibility(VISIBLE);
        } else {
            readDot.setVisibility(INVISIBLE);
        }
    }

    public void hideReadDot() {
        readDot.setVisibility(INVISIBLE);
    }


}
