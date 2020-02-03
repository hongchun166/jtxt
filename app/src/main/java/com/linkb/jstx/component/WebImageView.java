
package com.linkb.jstx.component;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.jstx.util.FileURLBuilder;

import java.io.File;

public class WebImageView extends AppCompatImageView implements View.OnClickListener {

    private String originalUrl;

    public WebImageView(Context context) {
        super(context);
    }

    public WebImageView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }


    /**
     * @return download url
     */
    public String getUrl() {
        return originalUrl;
    }

    public void setUrl(String url) {
        originalUrl = url;
    }

    /**
     * 设置download url，开始下载
     *
     * @param url
     */
    public void load(String url, int defBackgroundId, float rounded, CloudImageApplyListener listener) {

        this.originalUrl = url;
        CloudImageLoaderFactory.get().loadAndApply(this, url, defBackgroundId, rounded, listener);
    }

    public void load(String url, @DrawableRes int defBackgroundId, int rounded) {

        Log.d("imageViewWebImageView", url);
        this.originalUrl = url;
        load(url, defBackgroundId, rounded, null);
    }

    public void load(String url, int defBackgroundId) {

        this.originalUrl = url;
        load(url, defBackgroundId, 0, null);
    }

    public void load(String url, float rounded, CloudImageApplyListener listener) {


        this.originalUrl = url;

        load(url, 0, rounded, listener);
    }


    public void load(String url) {
        load(url, 0, 0);
    }

    public void load(Uri uri) {
        load(uri.toString(), 0, 0);
    }

    public void load(Uri uri, int defBackgroundId) {
        load(uri.toString(), defBackgroundId, 0);
    }

    public void load(File file) {
        originalUrl = Uri.fromFile(file).toString();
        load(originalUrl, 0, 0);
    }

    public void load(File file,int defBackgroundId,int rounded) {
        originalUrl = Uri.fromFile(file).toString();
        load(originalUrl, defBackgroundId, rounded);
    }
    public void load(File file, CloudImageApplyListener listener) {
        originalUrl = Uri.fromFile(file).toString();
        load(originalUrl, 0, 0, listener);
    }

    public void load(String url, CloudImageApplyListener listener) {
        originalUrl = url;
        load(originalUrl, 0, 0, listener);
    }

    public void load(File file, int defBackgroundId) {
        load(file, defBackgroundId, 0);
    }

    public void display(String thumb, String originalUrl) {
        load(thumb, 0, 0);
        this.originalUrl = originalUrl;
    }


    /**
     * 设置download url，开始下载
     */
    public void loadLocale(String key, int defBackgroundId) {

        originalUrl = FileURLBuilder.getChatFileUrl(key);
        load(originalUrl, defBackgroundId, 0);
    }

    public void setPopuShowAble() {

        setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        LvxinApplication.getInstance().startPhotoActivity(getContext(), originalUrl, this);
    }
}
