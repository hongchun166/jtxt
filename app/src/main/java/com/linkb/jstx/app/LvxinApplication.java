
package com.linkb.jstx.app;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.XmlResourceParser;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.activity.util.PhotoGalleryActivity;
import com.linkb.jstx.activity.util.PhotoVewActivity;
import com.linkb.jstx.activity.util.VideoPlayerActivity;
import com.linkb.jstx.network.model.SNSImage;
import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.jstx.service.ApkDownloaderService;
import com.linkb.jstx.service.UploadCrashLogService;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.LocalManageUtil;
import com.linkb.lib.xcrash.XCrash;
import com.qiniu.droid.rtc.QNLogLevel;
import com.qiniu.droid.rtc.QNRTCEnv;

import org.apache.commons.io.IOUtils;
import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LvxinApplication extends Application {

    /**
     * mock final定义，不能修改这个变量
     */
    public static Pattern EMOTION_PATTERN ;
    public final static LinkedHashMap<String, Integer> EMOTION_MAP = new LinkedHashMap<>();
    public static String CACHE_DIR_VOICE;
    public static String CACHE_DIR_IMAGE;
    public static String CACHE_DIR_FILE;

    public static String DOWNLOAD_DIR;
    public static String DATABASE_DIR;
    public static String CACHE_DIR_VIDEO;
    private static LvxinApplication context;

    protected void attachBaseContext(Context base) {

        MultiDex.install(this);
        //保存系统选择语言
        LocalManageUtil.saveSystemCurrentLanguage(base);
        super.attachBaseContext(LocalManageUtil.setLocal(base));

        XCrash.init(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocalManageUtil.onConfigurationChanged(getApplicationContext());
    }

    public static LvxinApplication getInstance() {
        return context;
    }

    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            IOUtils.closeQuietly(reader);
        }
        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        /**七牛云音视频初始化
         * init must be called before any other func
         */
        QNRTCEnv.setLogLevel(QNLogLevel.INFO);
        QNRTCEnv.init(getApplicationContext());
        QNRTCEnv.setLogFileEnabled(true);

        LocalManageUtil.setApplicationLanguage(this);

        context = this;

        initVectorCompat();
        initCacheFolders();

        // TODO: 2019/2/27
        SDKInitializer.initialize(this);
        boolean isMainProcess = isMainProcess();

        if (!isMainProcess) {
            return;
        }


        CIMPushManager.setLoggerEnable(this,BuildConfig.DEBUG);

        initChatTextViewMaxWidth();
        initEmotionMap();
        initEmotionPattern();

    }

    private void initCacheFolders(){
        CACHE_DIR_VOICE = this.getExternalCacheDir() + "/voice";
        CACHE_DIR_IMAGE = this.getExternalCacheDir() + "/image";
        DOWNLOAD_DIR = Environment.getExternalStorageDirectory().getPath() + "/download";
        DATABASE_DIR = this.getExternalCacheDir() + "/database";
        CACHE_DIR_VIDEO = this.getExternalCacheDir() + "/video";
        CACHE_DIR_FILE =  this.getExternalCacheDir() + "/recfile";
        AppTools.creatSDDir(CACHE_DIR_VOICE);
        AppTools.creatSDDir(CACHE_DIR_IMAGE);
        AppTools.creatSDDir(DOWNLOAD_DIR);
        AppTools.creatSDDir(DATABASE_DIR);
        AppTools.creatSDDir(CACHE_DIR_VIDEO);
        AppTools.creatSDDir(CACHE_DIR_FILE);
    }
    private void initEmotionPattern() {
        StringBuilder patternString = new StringBuilder();
        patternString.append("(");
        for  (Map.Entry<String, Integer> map: LvxinApplication.EMOTION_MAP.entrySet()) {
            patternString.append(Pattern.quote(map.getKey()));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");
        EMOTION_PATTERN =  Pattern.compile(patternString.toString());
    }
    private boolean isMainProcess() {
        String processName = getProcessName(android.os.Process.myPid());
        return processName == null || getPackageName().equals(processName);
    }

    private void initVectorCompat() {

        if (Build.VERSION.SDK_INT < 21) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }
    }

    private void initBuglyCrashReport(boolean isMainProcess) {

//        if (BuildConfig.DEBUG) {
//            return;
//        }
//
//        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
//        strategy.setUploadProcess(isMainProcess);
//        CrashReport.initCrashReport(getApplicationContext(), "491be1ab7f", BuildConfig.DEBUG, strategy);
//        CrashReport.initCrashReport(getApplicationContext(), "491be1ab7f", true);
//        CrashReport.setUserId(Global.getCurrentAccount());

    }

    public void startDownloadService(String url, File file) {
        Intent intent = new Intent(this, ApkDownloaderService.class);
        intent.setAction(ApkDownloaderService.ACTION_DOWNLOAD_APK_FILE);
        intent.putExtra("url", url);
        intent.putExtra("file", file);
        this.startService(intent);
    }

    public void startCheckVersionService() {
        Intent intent = new Intent(this, ApkDownloaderService.class);
        intent.setAction(ApkDownloaderService.ACTION_CHECK_APP_VERSION);
        this.startService(intent);
    }

    /**  开始上传崩溃日志
    * */
    public void startUploadCrashReport(File file) {
        if (file == null) return;
        Intent intent = new Intent(this, UploadCrashLogService.class);
        intent.putExtra("file", file);
        this.startService(intent);
    }

    private void initChatTextViewMaxWidth() {
        TextPaint paint = new TextPaint();
        paint.setTextSize(getResources().getDimension(R.dimen.chating_text_size));
//        int maxWidth = (int) (paint.measureText(getResources().getString(R.string.test_chat_width)) );
        int maxWidth = ConvertUtils.dp2px(240);
        Global.setChatTextMaxWidth(maxWidth);
    }

    private void initEmotionMap() {
        try {
            XmlResourceParser parser = getResources().getXml(R.xml.emotion);
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if (event == XmlPullParser.START_TAG && "item".equals(parser.getName())) {
                    String res = parser.getAttributeValue(null, "res");
                    String name = parser.getAttributeValue(null, "name");
                    int resId = getResources().getIdentifier(res, "drawable", getPackageName());
                    EMOTION_MAP.put(name, resId);
                }
                event = parser.next();
            }
            parser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void restartSelf() {

        LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_FINISH_ACTIVITY));
        Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public static void sendGlobalBroadcast(Intent intent){
        getInstance().sendBroadcast(intent);
    }

    public static void sendLocalBroadcast(Intent intent){
        LocalBroadcastManager.getInstance(getInstance()).sendBroadcast(intent);
    }

    public static void unregisterLocalReceiver(BroadcastReceiver receiver){
        LocalBroadcastManager.getInstance(getInstance()).unregisterReceiver(receiver);
    }
    public static void registerLocalReceiver(@NonNull BroadcastReceiver receiver,@NonNull IntentFilter filter){
        LocalBroadcastManager.getInstance(getInstance()).registerReceiver(receiver,filter);
    }
    @Override
    public void startActivity(Intent intent) {
        try {
            super.startActivity(intent);
        } catch (Exception e) {
        }
    }

    public boolean isConnectWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    public void startPhotoActivity(Context context, String url, View target) {
        Intent intent = new Intent(context, PhotoVewActivity.class);
        intent.putExtra("url", url);
        startSceneTransitionActivity(context, target, intent);
    }

    private void startSceneTransitionActivity(Context context, View target, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && context instanceof Activity) {
            ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation((Activity) context, target, "imageSenseView");
            ActivityCompat.startActivity(context, intent, option.toBundle());
        } else {
            ActivityCompat.startActivity(context, intent, null);
        }
    }

    public void startPhotoActivity(Context context, SNSImage bean, View target) {
        Intent intent = new Intent(context, PhotoVewActivity.class);
        intent.putExtra(SNSImage.class.getName(), bean);
        startSceneTransitionActivity(context, target, intent);
    }

    public void startGalleryPhotoActivity(Context context, ArrayList<? extends SNSImage> bean, View target) {
        Intent intent = new Intent(context, PhotoGalleryActivity.class);
        intent.putExtra(SNSImage.class.getName(), bean);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && context instanceof Activity) {
            String transitionName =  FileURLBuilder.getChatFileUrl(bean.get(0).thumb);
            ActivityOptionsCompat option = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) context,
                    new Pair(target,transitionName)
            );
            ActivityCompat.startActivity(context, intent, option.toBundle());
        } else {
            ActivityCompat.startActivity(context, intent, null);
        }
    }

    public void startVideoActivity(Context context, boolean bottonVisble, SNSVideo bean, View target) {
        Intent intent = new Intent(context, VideoPlayerActivity.class);
        intent.putExtra(SNSVideo.class.getName(), bean);
        intent.putExtra("buttonVisable", bottonVisble);
        if (target == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityCompat.startActivityForResult((Activity) context, intent, 789, null);
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions option = ActivityOptions.makeSceneTransitionAnimation((Activity) context, target, "imageSenseView");
            ActivityCompat.startActivity(context, intent, option.toBundle());
        }
    }

    public void connectPushServer() {
        CIMPushManager.connect(this, ClientConfig.getServerHost(), ClientConfig.getServerCIMPort());
    }

}
