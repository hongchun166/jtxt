package com.linkb.jstx.activity.chat.bean;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.linkb.jstx.activity.chat.MMWebViewActivity;

import java.io.Serializable;

public  class WebViewNavToParam implements Serializable, Parcelable {
    public Uri url;
    public String urlStr;
    public String beanId;
    int intentFlag=-1;
    public WebViewNavToParam() {
    }
    public WebViewNavToParam(Uri url, String beanId) {
        this.url = url;
        this.beanId = beanId;
    }

    protected WebViewNavToParam(Parcel in) {
        url = in.readParcelable(Uri.class.getClassLoader());
        beanId = in.readString();
        intentFlag = in.readInt();
    }

    public WebViewNavToParam setBeanId(String beanId) {
        this.beanId = beanId;
        return this;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(url, flags);
        dest.writeString(beanId);
        dest.writeInt(intentFlag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<WebViewNavToParam> CREATOR = new Creator<WebViewNavToParam>() {
        @Override
        public WebViewNavToParam createFromParcel(Parcel in) {
            return new WebViewNavToParam(in);
        }

        @Override
        public WebViewNavToParam[] newArray(int size) {
            return new WebViewNavToParam[size];
        }
    };

    public WebViewNavToParam setIntentFlag(int intentFlag) {
        this.intentFlag = intentFlag;
        return this;
    }
    public void start(Context context){
        WebViewNavToParam navToParam=new WebViewNavToParam(url,beanId);
        navToParam.setIntentFlag(intentFlag);

        Intent intent=new Intent(context, MMWebViewActivity.class);
        intent.putExtra("NavToParam", (Parcelable) navToParam);
        if(navToParam.intentFlag!=-1){
            intent.addFlags(navToParam.intentFlag);
        }
        context.startActivity(intent);
    }
}