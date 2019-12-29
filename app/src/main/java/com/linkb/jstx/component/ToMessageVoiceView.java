
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class ToMessageVoiceView extends BaseToMessageView {


    public ToMessageVoiceView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public View getContentView() {
        return voiceView;
    }

    @Override
    public void displayMessage() {

        voiceView.display(message, true);
        voiceView.setOnClickListener(voiceView);
    }


}
