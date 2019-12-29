
package com.linkb.jstx.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.jstx.network.model.SNSChatImage;
import com.linkb.jstx.util.BitmapUtils;
import com.linkb.jstx.listener.CloudImageLoadListener;
import com.linkb.R;
import com.google.gson.Gson;

public class ToMessageImageView extends BaseToMessageView {
    private CircleProgressView circleProgressView;

    public ToMessageImageView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        circleProgressView = findViewById(R.id.circleProgressView);
    }

    @Override
    public View getContentView() {
        return imageView;
    }

    @Override
    public void displayMessage() {
        if (Constant.MessageStatus.STATUS_NO_SEND.equals(message.state)) {
            setVisibility(GONE);
            SNSChatImage chatImage = new Gson().fromJson(message.content, SNSChatImage.class);
            message.state = Constant.MessageStatus.STATUS_DELAY_SEND;
            loadNativeImageFile(Uri.parse(chatImage.image));

        } else {
            SNSChatImage chatImage = new Gson().fromJson(message.content, SNSChatImage.class);
            initImageView(chatImage);
        }

    }

    private void initImageView(SNSChatImage chatImage) {
        setVisibility(VISIBLE);
        circleProgressView.setVisibility(GONE);
        imageView.setTag(chatImage.image);
        imageView.initViews(chatImage, message);
        imageView.setOnClickListener(imageView);
        sendProgressbar.setVisibility(View.GONE);
        imageView.setUploadProgressView(circleProgressView);
    }


    private void loadNativeImageFile(final Uri file) {
        CloudImageLoaderFactory.get().downloadOnly(file, new CloudImageLoadListener() {
            @Override
            public void onImageLoadFailure(Object key) {
            }

            @Override
            public void onImageLoadSucceed(Object key, Bitmap resource) {
                message.state = Constant.MessageStatus.STATUS_NO_SEND;
                SNSChatImage snsImage = BitmapUtils.compressSNSImage(resource);
                message.content = new Gson().toJson(snsImage);
                MessageRepository.deleteById(message.id);
                MessageRepository.add(message);

                initImageView(snsImage);

                statusHandler();
            }
        });
    }

}
