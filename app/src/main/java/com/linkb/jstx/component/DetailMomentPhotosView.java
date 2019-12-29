
package com.linkb.jstx.component;


import android.content.Context;
import android.support.v7.widget.GridLayout;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.linkb.jstx.network.model.SNSMomentImage;
import com.linkb.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class DetailMomentPhotosView extends DetailMomentView {
    private int cellWidth;
    private int spacing;
    private GridLayout gridLayout;

    public DetailMomentPhotosView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        gridLayout = findViewById(R.id.gridLayout);
        spacing = getResources().getDimensionPixelOffset(R.dimen.sns_photos_spacing);
    }

    private void displayImageOnMeasured() {
        List<SNSMomentImage> images = new Gson().fromJson(moment.content, new TypeToken<List<SNSMomentImage>>() {}.getType());
        for (int i = 0; i < images.size(); i++) {

            MomentImageView itemView = new MomentImageView(getContext());
            itemView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            itemView.display(moment,images.get(i));


            gridLayout.addView(itemView, cellWidth, cellWidth);


            boolean isRowFirst = i % gridLayout.getColumnCount() != 0;
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).leftMargin = isRowFirst ? spacing : 0;
            boolean isFirstRow = i < gridLayout.getColumnCount();
            ((GridLayout.LayoutParams) itemView.getLayoutParams()).topMargin = isFirstRow ? 0 : spacing;


        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (cellWidth == 0 && gridLayout.getMeasuredWidth() > 0) {
            cellWidth = (gridLayout.getMeasuredWidth() - (gridLayout.getColumnCount() - 1) * spacing) / gridLayout.getColumnCount();
            displayImageOnMeasured();
        }
    }

}
