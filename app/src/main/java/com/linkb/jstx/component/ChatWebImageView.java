
package com.linkb.jstx.component;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.jstx.listener.OnTransmitProgressListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.model.SNSChatImage;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

public class ChatWebImageView extends CardView implements OnClickListener, OnTransmitProgressListener, CloudImageApplyListener {

    private WebImageView image;
    private String key;
    private Message message;
    private View progressbar;
    private CircleProgressView uploadProgressView;


    public ChatWebImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        image = this.findViewById(R.id.image);
        progressbar = findViewById(R.id.loadImagePprogressbar);
    }

    public void setUploadProgressView(CircleProgressView view) {
        uploadProgressView = view;
    }

    public void initViews(SNSChatImage snsImage, Message msg) {



        this.message = msg;
        key = snsImage.thumb;
        String url = FileURLBuilder.getChatFileUrl(key);
        progressbar.setVisibility(View.VISIBLE);
        int minSide = getResources().getDimensionPixelOffset(R.dimen.chat_thumbnail_min_side);
        if (snsImage.tw > snsImage.th) {
            this.image.setLayoutParams(new FrameLayout.LayoutParams(minSide * snsImage.tw / snsImage.th, minSide));
        } else {
            this.image.setLayoutParams(new FrameLayout.LayoutParams(minSide, minSide * snsImage.th / snsImage.tw));
        }

        image.load(url, R.drawable.def_chat_image_background,0, this);
    }

    @Override
    public void onClick(View view) {
        SNSChatImage image = new Gson().fromJson(message.content, SNSChatImage.class);
        image.thumb = key;
        LvxinApplication.getInstance().startPhotoActivity(getContext(), image, this);
    }

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        onImageApplySucceed(key, target, null);
    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        ChatWebImageView chatWebImageView = (ChatWebImageView) target.getParent();
        chatWebImageView.progressbar.setVisibility(View.GONE);
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


}
