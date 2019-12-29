
package com.linkb.jstx.listener;

import android.graphics.Bitmap;

public interface CloudImageLoadListener {

    void onImageLoadFailure(Object key);

    void onImageLoadSucceed(Object key, Bitmap resource);

}
