
package com.linkb.jstx.component;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.model.SNSMomentVideo;
import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

import java.io.File;

public class DetailMomentVideoView extends DetailMomentView {
    private WebImageView thumbnailView;
    private SNSMomentVideo video;

    public DetailMomentVideoView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        thumbnailView = findViewById(R.id.thumbnailView);
        ((ViewGroup) thumbnailView.getParent()).setOnClickListener(this);
    }

    @Override
    public void displayMoment(Moment moment) {
        super.displayMoment(moment);
        video = new Gson().fromJson(moment.content, SNSMomentVideo.class);
        if (video.mode == SNSVideo.HORIZONTAL) {
            thumbnailView.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.sns_video_height);
            thumbnailView.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.sns_video_width);
        } else {
            thumbnailView.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.sns_video_width);
            thumbnailView.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.sns_video_height);
        }
        thumbnailView.requestLayout();

        File thumbnailFile = new File(LvxinApplication.CACHE_DIR_VIDEO, video.image);
        if (thumbnailFile.exists()) {
            thumbnailView.load(thumbnailFile, R.color.video_background);
        } else {
            String url = FileURLBuilder.getMomentFileUrl(video.image);
            thumbnailView.load(url, R.color.video_background);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == thumbnailView.getParent()) {
            LvxinApplication.getInstance().startVideoActivity(getContext(), false, video, (View) thumbnailView.getParent());
            return;
        }
        super.onClick(view);
    }
}
