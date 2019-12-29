
package com.linkb.jstx.network;


import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.jstx.listener.CloudImageLoadListener;

import java.io.File;

public interface CloudImageLoader {

    void loadAndApply(ImageView target, String url, @DrawableRes int defResId, float round, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, File file, @DrawableRes int defResId, float round, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, File file, @DrawableRes int defResId, float round);

    void loadAndApply(ImageView target, String url, @DrawableRes int defResId, float round);

    void loadAndApply(ImageView target, File file, @DrawableRes int defResId);

    void loadAndApply(ImageView target, String url, @DrawableRes int defResId);

    void loadAndApply(ImageView target, File file, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, String url, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, String url, Bitmap defResBitmap, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, File file, @DrawableRes int defResId, CloudImageApplyListener listener);

    void loadAndApply(ImageView target, String url, @DrawableRes int defResId, CloudImageApplyListener listener);

    void loadGifAndApply(ImageView target, String url);

    void downloadOnly(String url);

    void downloadOnly(String url, CloudImageLoadListener listener);

    void downloadOnly(File file, CloudImageLoadListener listener);

    void downloadOnly(Uri file, CloudImageLoadListener listener);

    void downloadOnly(Uri file, int size, CloudImageLoadListener listener);

    void clearMemory();

    void clearDiskCache();

}
