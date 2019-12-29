
package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.R;
import com.google.gson.Gson;

public class ToMessageVideoView extends BaseToMessageView {

    private CircleProgressView circleProgressView;

    public ToMessageVideoView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        circleProgressView = findViewById(R.id.circleProgressView);
    }

    @Override
    public View getContentView() {
        return videoView;
    }

    @Override
    public void displayMessage() {
        circleProgressView.setVisibility(GONE);
        SNSVideo video = new Gson().fromJson(message.content, SNSVideo.class);
        videoView.initViews(video, message);
        videoView.setOnClickListener(videoView);
        videoView.setTag(video.video);
        videoView.setSendProgressView(circleProgressView);
    }

    @Override
    public void onMessageSendFailure() {
        circleProgressView.setVisibility(GONE);
    }
}
