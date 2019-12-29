
package com.linkb.jstx.listener;

import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public interface CloudImageApplyListener {

    void onImageApplyFailure(Object key, ImageView target);

    void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource);

}
