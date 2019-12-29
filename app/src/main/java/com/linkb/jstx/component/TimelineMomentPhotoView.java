
package com.linkb.jstx.component;


import android.content.Context;
import android.util.AttributeSet;

import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.model.SNSMomentImage;
import com.linkb.jstx.listener.OnCommentSelectedListener;
import com.linkb.jstx.model.Moment;
import com.linkb.R;
import com.google.gson.Gson;

public class TimelineMomentPhotoView extends TimelineMomentView {
    private MomentImageView imageView;

    public TimelineMomentPhotoView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        imageView = findViewById(R.id.singleImageView);
    }

    @Override
    public void displayMoment(Moment moment, User self, OnCommentSelectedListener commentSelectedListener) {
        super.displayMoment(moment, self, commentSelectedListener);
        SNSMomentImage snsImage = new Gson().fromJson(moment.content,SNSMomentImage.class);
        imageView.displayFitSize(moment, snsImage);
    }


}
