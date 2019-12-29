
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.linkb.jstx.network.model.SNSChatImage;
import com.google.gson.Gson;

public class FromMessageImageView extends BaseFromMessageView {

    public FromMessageImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public View getContentView() {
        return imageView;
    }


    @Override
    public void displayMessage() {
        SNSChatImage chatImage = new Gson().fromJson(message.content, SNSChatImage.class);
        imageView.initViews(chatImage, message);
        imageView.setOnClickListener(imageView);
    }


}
