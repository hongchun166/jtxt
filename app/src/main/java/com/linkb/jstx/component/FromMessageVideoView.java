
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.linkb.jstx.network.model.SNSVideo;
import com.google.gson.Gson;

public class FromMessageVideoView extends BaseFromMessageView {

    public FromMessageVideoView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public View getContentView() {
        return videoView;
    }

    @Override
    public void displayMessage() {
        SNSVideo video = new Gson().fromJson(message.content, SNSVideo.class);
        videoView.initViews(video, message);
        videoView.setOnClickListener(videoView);
    }
}
