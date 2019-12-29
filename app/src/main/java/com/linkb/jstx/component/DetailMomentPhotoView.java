
package com.linkb.jstx.component;
import android.content.Context;
import android.util.AttributeSet;

import com.linkb.jstx.network.model.SNSMomentImage;
import com.linkb.jstx.model.Moment;
import com.linkb.R;
import com.google.gson.Gson;

public class DetailMomentPhotoView extends DetailMomentView {
    private MomentImageView imageView;

    public DetailMomentPhotoView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        imageView = findViewById(R.id.singleImageView);
    }

    @Override
    public void displayMoment(Moment moment) {
        super.displayMoment(moment);
        SNSMomentImage snsImage = new Gson().fromJson(moment.content,SNSMomentImage.class);
        imageView.displayFitSize(moment, snsImage);
    }

}
