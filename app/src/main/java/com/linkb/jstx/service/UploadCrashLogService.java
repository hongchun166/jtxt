package com.linkb.jstx.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.linkb.jstx.bean.FileResource;
import com.linkb.jstx.listener.OSSFileUploadListener;
import com.linkb.jstx.network.CloudFileUploader;

import java.io.File;

/** 上传崩溃日志的后台服务
* */
public class UploadCrashLogService extends Service implements OSSFileUploadListener {


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null && intent.getSerializableExtra("file") != null){
            final File file = (File) intent.getSerializableExtra("file");
            FileResource fileResource = new FileResource(file.getName(), file);
            CloudFileUploader.asyncCrashReportUpload(fileResource, new OSSFileUploadListener() {
                @Override
                public void onUploadCompleted(FileResource resource) {
                    Log.d("crash", "onUploadCompleted");
                    file.delete();
                }

                @Override
                public void onUploadProgress(String key, float progress) {
                    Log.d("crash", "onUploadProgress");
                }

                @Override
                public void onUploadFailured(FileResource resource, Exception e) {
                    Log.d("crash", "onUploadFailured");
                }
            });
        }


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onUploadCompleted(FileResource resource) {

    }

    @Override
    public void onUploadProgress(String key, float progress) {

    }

    @Override
    public void onUploadFailured(FileResource resource, Exception e) {

    }
}
