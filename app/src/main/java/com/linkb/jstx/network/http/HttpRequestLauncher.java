
package com.linkb.jstx.network.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.linkb.jstx.util.MLog;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.BuildConfig;
import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

 public class HttpRequestLauncher {
    private final static String TAG = HttpRequestLauncher.class.getSimpleName();


    private static Callback EMPTY_CALLBACK = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            if (BuildConfig.DEBUG){
                return;
            }
            e.printStackTrace();
        }
        @Override
        public void onResponse(Call call, Response response){
            if (BuildConfig.DEBUG){
                return;
            }
            try {
                MLog.i(TAG, new Gson().toJson(response.body().string()));
            } catch (IOException e) {
                IOUtils.closeQuietly(response);
                e.printStackTrace();
            }
        }
    };

    public static void executeQuietly(HttpRequestBody body) {
        body.setRunWithOtherThread();
        prefromHttpRequest(body,EMPTY_CALLBACK);
    }

    public static   void execute(@NonNull final HttpRequestBody body,@NonNull  final HttpRequestListener listener) {
        prefromHttpRequest(body, new CustomOkHttpCallback (body.getDataClass(),body.isMainTheadCallback()) {
            @Override
            public void onResponse(Call call, BaseResult response) {

                listener.onHttpRequestSucceed(response,new OriginalCall( call.request().tag().toString(),call.request().method()));
            }
            @Override
            public void onFailured(Call call, Exception e) {
                listener.onHttpRequestFailure(e, new OriginalCall( call.request().tag().toString(),call.request().method()));
            }
        });
    }
    private static void prefromHttpRequest(HttpRequestBody body, Callback callback) {




        OkHttpClient httpclient = OkHttpFactory.getOkHttpClient();

        /**
         * 组装 url path参数
         */
        String url = body.getUrl();

        for (String key : body.getPathVariableMap().keySet()) {
            url = url.replace("{"+key+"}",body.getPathVariable(key));
        }

        MLog.i(TAG, body.getMethod() + " " + url);
        MLog.i(TAG, new Gson().toJson(body.getHeader()));
        MLog.i(TAG, new Gson().toJson(body.getParameter()));

        Request.Builder requestBuilder = new Request.Builder().url(url);
        requestBuilder.tag(body.getUrl());


        if (!TextUtils.isEmpty(body.getContent()) && !TextUtils.isEmpty(body.getContentType())) {

            /** 组装ContentRequestBody  和 FormBody二者只能取其一*/

            requestBuilder.post(RequestBody.create(MediaType.parse(body.getContentType()), body.getContent()));

        }else if(!body.getParameter().isEmpty()){

            /** 组装FormBody  和 组装ContentRequestBody二者只能取其一*/

            FormBody.Builder formBuilder = new FormBody.Builder();
            for (String key : body.getParameter().keySet()) {
                formBuilder.add(key, body.getParameter().get(key));
            }
            requestBuilder.method(body.getMethod(),formBuilder.build());

        }else {
            requestBuilder.method(body.getMethod(),null);
        }




        /**
         * 组装 http header参数
         */
        for (String key : body.getHeader().keySet()) {
            String value = body.getHeaderValue(key);
            if (value != null) {
                requestBuilder.header(key, value);
            }
        }

        httpclient.newCall(requestBuilder.build()).enqueue(callback);
    }

}
