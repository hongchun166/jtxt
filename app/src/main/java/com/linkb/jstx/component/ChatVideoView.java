
package com.linkb.jstx.component;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.jstx.listener.OnTransmitProgressListener;
import com.linkb.jstx.model.Message;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

import org.apache.commons.io.FileUtils;

import java.io.File;

public class ChatVideoView extends CardView implements OnClickListener, OnTransmitProgressListener, CloudImageApplyListener, Palette.PaletteAsyncListener {
    private TextView extraTextView;
    private WebImageView image;
    private Message message;
    private CircleProgressView uploadProgressView;

    public ChatVideoView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        image = this.findViewById(R.id.image);
        extraTextView = this.findViewById(R.id.extra);
    }

    public void setSendProgressView(CircleProgressView circleProgressView) {
        this.uploadProgressView = circleProgressView;
    }

    public void initViews(SNSVideo video, Message msg) {
        this.message = msg;
        if (video.mode == SNSVideo.HORIZONTAL) {
            image.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.chat_video_height);
            image.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.chat_video_width);
        } else {
            image.getLayoutParams().width = getContext().getResources().getDimensionPixelOffset(R.dimen.chat_video_width);
            image.getLayoutParams().height = getContext().getResources().getDimensionPixelOffset(R.dimen.chat_video_height);
        }
        image.requestLayout();
        File thumbnailFile = new File(LvxinApplication.CACHE_DIR_VIDEO, video.image);
        if (thumbnailFile.exists()) {
            image.load(thumbnailFile,  this);
        } else {
            String url = FileURLBuilder.getChatFileUrl(video.image);
            image.load(url, R.drawable.def_chat_video_background, 0, this);
        }

        if (getParent() instanceof BaseFromMessageView) {
            extraTextView.setText(video.duration + "\" " + FileUtils.byteCountToDisplaySize(video.size));
        }
    }

    @Override
    public void onClick(View view) {
        SNSVideo video = new Gson().fromJson(message.content, SNSVideo.class);

        LvxinApplication.getInstance().startVideoActivity(getContext(), false, video, this);

    }

    @Override
    public void onProgress(float progress) {
        uploadProgressView.setProgress((int) progress);
        if (progress >= 100) {
            uploadProgressView.setVisibility(GONE);
            return;
        }
        if (progress > 0) {
            uploadProgressView.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {

    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        if (getParent() instanceof BaseFromMessageView) {
            Palette.Builder builder = new Palette.Builder(resource.getBitmap());
            builder.generate(this);
        }
    }

    @Override
    public void onGenerated(Palette palette) {
        Palette.Swatch vibrant = palette.getLightVibrantSwatch();
        if (vibrant != null) {
            extraTextView.setTextColor(vibrant.getTitleTextColor());
        }
    }
}
