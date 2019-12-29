
package com.linkb.jstx.network;

import android.content.Intent;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.FileResource;
import com.linkb.jstx.listener.OSSFileUploadListener;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.listener.OnTransmitProgressListener;
import com.linkb.jstx.network.http.HttpRequestBody;
import com.linkb.jstx.util.BackgroundThreadHandler;
import com.linkb.jstx.util.FileURLBuilder;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 全局的文件上传工具
 */
public class CloudFileUploader {

    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    private CloudFileUploader() {
    }

    public static void asyncUpload(final String objectKey, final File file,
                                   final OSSFileUploadListener callback) {
        asyncUpload(FileURLBuilder.BUCKET_CHAT, objectKey, file, callback);
    }

    public static void asyncUpload(final String bucket, final long objectKey, final File file) {
        FileResource rerource = new FileResource(String.valueOf(objectKey),bucket,file);
        asyncUpload(rerource,null);
    }

    public static void asyncUpload(final String bucket, final String objectKey, final File file) {
        FileResource rerource = new FileResource(objectKey,bucket,file);
        asyncUpload(rerource,null);
    }

    public static void asyncUpload(final String bucket, final String objectKey, final File file, final OSSFileUploadListener callback) {
        FileResource rerource = new FileResource(objectKey,bucket,file);
        asyncUpload(rerource,callback);
    }

    public static void asyncUpload(final FileResource rerource, final OSSFileUploadListener callback) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient httpclient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).build();
                    MultipartBody.Builder build = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    build.addFormDataPart("file", rerource.file.getName(), new FileRequestBody(rerource.file, new OnTransmitProgressListener() {
                        @Override
                        public void onProgress(final float progress) {
                            if (callback != null) {
                                BackgroundThreadHandler.postUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onUploadProgress(rerource.key, progress);
                                    }
                                });
                            }
                        }
                    }));

                    String url = URLConstant.FILE_UPLOAD_URL.replace("{bucket}",rerource.bucket).replace("{filename}",rerource.key);
                    Request request = new Request.Builder()
                            .url(url)
                            .header(HttpRequestBody.ACCESS_TOKEN, Global.getAccessToken())
                            .post(build.build())
                            .build();

                    httpclient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (callback != null) {
                                callback.onUploadFailured(rerource, e);
                            }
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (response.code() == Integer.valueOf(Constant.ReturnCode.CODE_200)){
                                if (callback != null) {
                                    callback.onUploadCompleted(rerource);
                                }
                            }else {
                                if (callback != null) {
                                    callback.onUploadFailured(rerource, new IOException() );
                                }
                            }

                        }
                    });

//                    if (callback != null) {
//                        BackgroundThreadHandler.postUIThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                callback.onUploadCompleted(rerource);
//                            }
//                        });
//                    }
                    Intent intent = new Intent(Constant.Action.ACTION_UPLOAD_PROGRESS);
                    intent.putExtra("objectKey", rerource.key);
                    intent.putExtra("progress", 100f);
                    LvxinApplication.sendLocalBroadcast(intent);


                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        BackgroundThreadHandler.postUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onUploadFailured(rerource,e);
                            }
                        });
                    }
                    Intent intent = new Intent(Constant.Action.ACTION_UPLOAD_PROGRESS);
                    intent.putExtra("objectKey", rerource.key);
                    intent.putExtra("progress", -1f);
                    LvxinApplication.sendLocalBroadcast(intent);
                }
            }
        });
    }

    /** 上传崩溃日志
    * */
    public static void asyncCrashReportUpload(final FileResource rerource, final OSSFileUploadListener callback) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {

                    OkHttpClient httpclient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).build();
                    MultipartBody.Builder build = new MultipartBody.Builder().setType(MultipartBody.FORM);
                    build.addFormDataPart("file", rerource.file.getName(), new FileRequestBody(rerource.file, new OnTransmitProgressListener() {
                        @Override
                        public void onProgress(final float progress) {
                            if (callback != null) {
                                BackgroundThreadHandler.postUIThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        callback.onUploadProgress(rerource.key, progress);
                                    }
                                });
                            }
                        }
                    }));

                    String url = URLConstant.UPLOAD_CRASH_LOG;
                    Request request = new Request.Builder()
                            .url(url)
                            .header(HttpRequestBody.ACCESS_TOKEN, Global.getAccessToken())
                            .post(build.build())
                            .build();

                    httpclient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            if (callback != null) {
                                callback.onUploadFailured(rerource, e);
                            }
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            if (callback != null) {
                                callback.onUploadCompleted(rerource);
                            }
                        }
                    });
//
//                    if (callback != null) {
//                        BackgroundThreadHandler.postUIThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                callback.onUploadCompleted(rerource);
//                            }
//                        });
//                    }
                    Intent intent = new Intent(Constant.Action.ACTION_UPLOAD_PROGRESS);
                    intent.putExtra("objectKey", rerource.key);
                    intent.putExtra("progress", 100f);
                    LvxinApplication.sendLocalBroadcast(intent);


                } catch (final Exception e) {
                    e.printStackTrace();
                    if (callback != null) {
                        BackgroundThreadHandler.postUIThread(new Runnable() {
                            @Override
                            public void run() {
                                callback.onUploadFailured(rerource,e);
                            }
                        });
                    }
                    Intent intent = new Intent(Constant.Action.ACTION_UPLOAD_PROGRESS);
                    intent.putExtra("objectKey", rerource.key);
                    intent.putExtra("progress", -1f);
                    LvxinApplication.sendLocalBroadcast(intent);
                }
            }
        });
    }
}
