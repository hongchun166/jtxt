
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;

public class FromMessageTextView extends BaseFromMessageView   {

    public FromMessageTextView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public View getContentView() {
        return textView;
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        int padding = textView.getPaddingLeft() + textView.getPaddingRight();
        textView.setMaxWidth(padding + Global.getChatTextMaxWidth());
    }
    @Override
    public void displayMessage() {
        textView.setFaceSize(Constant.EMOTION_FACE_SIZE);
        textView.setText(message.content);
    }
}
