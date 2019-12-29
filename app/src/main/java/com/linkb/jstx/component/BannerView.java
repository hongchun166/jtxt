
package com.linkb.jstx.component;


import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class BannerView extends AppCompatImageView{
    private final String TAG_ME="me";
    public final String TAG_UCENTER="uc";

    public BannerView(Context context) {
        super(context);
    }

    public BannerView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int w = getMeasuredWidth();
        int h = getMeasuredHeight();
        int newHeight = getBannerHeight(w,h);
        setMeasuredDimension(w,newHeight);
    }

    private int getBannerHeight(int w,int h){
        if (TAG_ME.equals(getTag())){
            return (9 * w) / 16;
        }
        return h;
    }
}
