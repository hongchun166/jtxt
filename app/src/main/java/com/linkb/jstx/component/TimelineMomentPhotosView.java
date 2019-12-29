
package com.linkb.jstx.component;


import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.GridLayout;
import android.widget.ImageView;

import com.linkb.jstx.bean.User;
import com.linkb.jstx.listener.OnCommentSelectedListener;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.network.model.SNSMomentImage;
import com.linkb.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class TimelineMomentPhotosView extends TimelineMomentView {
    private GridLayout gridLayout;
    private int cellWidth;
    private int spacing;

    public TimelineMomentPhotosView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        gridLayout = findViewById(R.id.imageGridLayout);
        spacing = getResources().getDimensionPixelOffset(R.dimen.sns_photos_spacing);
        float density = Resources.getSystem().getDisplayMetrics().density;
        cellWidth = (int) ((Resources.getSystem().getDisplayMetrics().widthPixels - 90 * density) / gridLayout.getColumnCount());
    }

    @Override
    public void displayMoment(Moment moment, User self, OnCommentSelectedListener commentSelectedListener) {
        super.displayMoment(moment, self, commentSelectedListener);
        gridLayout.removeAllViews();
        List<SNSMomentImage> images = new Gson().fromJson(moment.content, new TypeToken<List<SNSMomentImage>>() {}.getType());
        for (int i = 0; i < images.size(); i++) {
            MomentImageView itemView = new MomentImageView(getContext());
            itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            itemView.display(moment, images.get(i));
            gridLayout.addView(itemView, cellWidth, cellWidth);

            boolean isRowFirst = i % gridLayout.getColumnCount() != 0;
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;

            boolean isFirstRow = i < gridLayout.getColumnCount();
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;
        }
    }

}
