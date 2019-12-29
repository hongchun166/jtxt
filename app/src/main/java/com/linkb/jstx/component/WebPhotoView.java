
package com.linkb.jstx.component;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.github.chrisbanes.photoview.PhotoView;


public class WebPhotoView extends PhotoView {
    private boolean isDetachedFromWindow = false;

    public WebPhotoView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public void display(String imageUrl, CloudImageApplyListener listener) {
        CloudImageLoaderFactory.get().loadAndApply(this, imageUrl,listener);
    }

    public void display(String imageUrl, Bitmap placeholder, CloudImageApplyListener listener) {

        CloudImageLoaderFactory.get().loadAndApply(this, imageUrl, placeholder, listener);
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        isDetachedFromWindow = false;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isDetachedFromWindow = true;
    }

    public boolean isDetachedFromWindow() {
        return isDetachedFromWindow;
    }
}
