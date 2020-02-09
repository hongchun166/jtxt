
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


public class FromMessageReadDeleteView extends BaseFromMessageView {

    public FromMessageReadDeleteView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public View getContentView() {
        return chatReadDeleteView;
    }


    @Override
    public void displayMessage() {
        chatReadDeleteView.initViews(message);
        chatReadDeleteView.setOnClickListener(chatReadDeleteView);
    }


}
