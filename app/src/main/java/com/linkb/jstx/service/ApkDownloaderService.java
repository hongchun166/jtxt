
package com.linkb.jstx.service;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.listener.OSSFileDownloadListener;
import com.linkb.jstx.network.CloudFileDownloader;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.activity.AppNewVersionActivity;
import com.linkb.jstx.bean.AppVersion;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.AppVersionResult;
import com.linkb.R;

import java.io.File;
import java.util.Objects;

public class ApkDownloaderService extends Service implements OSSFileDownloadListener, HttpRequestListener<AppVersionResult> {

    public static String ACTION_CHECK_APP_VERSION = "ACTION_CHECK_APP_VERSION";
    public static String ACTION_DOWNLOAD_APK_FILE = "ACTION_DOWNLOAD_APK_FILE";

    private final int NOTIFICATION_ID = 2462;
    private NotificationManager notificationMgr;
    private String url;
    private NotificationCompat.Builder mBuilder;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        this.notificationMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        if (intent != null && Objects.equals(ACTION_DOWNLOAD_APK_FILE,intent.getAction())) {
            url = intent.getStringExtra("url");
            File file = (File) intent.getSerializableExtra("file");
            PendingIntent mContentIntent = PendingIntent.getBroadcast(this, 1, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            if (mBuilder == null) {
                mBuilder = new NotificationCompat.Builder(this,null);
                mBuilder.setAutoCancel(false);
                mBuilder.setOngoing(true);
                mBuilder.setSmallIcon(R.drawable.icon);
                mBuilder.setContentIntent(mContentIntent);
                mBuilder.setProgress(100, 0, false);
                mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
            }
            mBuilder.setContentTitle(getString(R.string.title_apk_downloading));
            mBuilder.setContentText(getString(R.string.title_apk_downloading_progress, 0));
            mBuilder.setTicker(getString(R.string.title_apk_begin_download));
            CloudFileDownloader.asyncDownload(url, file, this);
            notificationMgr.notify(NOTIFICATION_ID, mBuilder.build());

        }
        if (intent != null && Objects.equals(ACTION_CHECK_APP_VERSION,intent.getAction())) {
            HttpServiceManager.queryNewAppVersion(this);
        }

        return super.onStartCommand(intent, flags, startId);
    }


    private void gotoInstallApkActivity(File file) {

        notificationMgr.cancel(NOTIFICATION_ID);
        Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = AppTools.getUriFromFile(file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        startActivity(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notificationMgr.cancel(NOTIFICATION_ID);
    }


    @Override
    public void onDownloadCompleted(File file, String currentKey) {

        gotoInstallApkActivity(file);
    }

    @Override
    public void onDownloadFailured(File file, String currentKey) {
        Intent intent = new Intent(this, this.getClass());
        intent.setAction(ACTION_DOWNLOAD_APK_FILE);
        intent.putExtra("url", url);
        intent.putExtra("file", file);
        PendingIntent contentIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setTicker(getString(R.string.title_apk_download_failed));
        mBuilder.setContentText(getString(R.string.title_apk_download_failed));
        mBuilder.setContentIntent(contentIntent);
        notificationMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onDownloadProgress(String key, float progress) {
        mBuilder.setProgress(100, (int) progress, false);
        mBuilder.setContentText(getString(R.string.title_apk_downloading_progress, (int) progress));
        notificationMgr.notify(NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onHttpRequestSucceed(AppVersionResult versionResult, OriginalCall call) {
        if (versionResult.code.equals(Constant.ReturnCode.CODE_200)) {
            Intent  intent = new Intent(this, AppNewVersionActivity.class);
            intent.putExtra(AppVersion.class.getName(),versionResult.data);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }
}
