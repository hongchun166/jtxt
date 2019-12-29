
package com.linkb.jstx.activity.util;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.LinearProgressView;
import com.linkb.jstx.dialog.PermissionDialog;
import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.BackgroundThreadHandler;
import com.linkb.jstx.util.StringUtils;
import com.qiniu.pili.droid.shortvideo.PLAudioEncodeSetting;
import com.qiniu.pili.droid.shortvideo.PLCameraSetting;
import com.qiniu.pili.droid.shortvideo.PLDisplayMode;
import com.qiniu.pili.droid.shortvideo.PLMicrophoneSetting;
import com.qiniu.pili.droid.shortvideo.PLRecordSetting;
import com.qiniu.pili.droid.shortvideo.PLRecordStateListener;
import com.qiniu.pili.droid.shortvideo.PLShortVideoRecorder;
import com.qiniu.pili.droid.shortvideo.PLVideoEncodeSetting;
import com.qiniu.pili.droid.shortvideo.PLVideoSaveListener;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

import static android.content.res.Configuration.ORIENTATION_PORTRAIT;


public class VideoRecorderActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks,  PLRecordStateListener, PLVideoSaveListener {

    public static final int REQUEST_CODE = 3685;
    /**
     * 拍摄按钮
     */
    private Button mRecordController;


    /**
     * 摄像头数据显示画布
     */
    private GLSurfaceView mSurfaceView;
    /**
     * 录制进度
     */
    private LinearProgressView mProgressView;

    /**
     * 是否是点击状态
     */
    private boolean isRecordingMode;

    private boolean lightMode = false;
    private SNSVideo video = new SNSVideo();
    private PLShortVideoRecorder mShortVideoRecorder;
    private ProgressDialog progressDialog;
    private File videoFile;
    private int RECORD_TIME_MAX = 6 * 1000;
    private int RECORD_TIME_MIN = 3 * 1000;

    //1横 0 竖
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setStatusBarColor(Color.TRANSPARENT);
        super.setWindowFullscreen();
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN , WindowManager.LayoutParams. FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 防止锁屏
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_small_video_recorder;
    }


    /**
     * 加载视图
     */
    @Override
    public void initComponents() {

        setBackIcon(R.drawable.abc_ic_clear_material);
        mSurfaceView = findViewById(R.id.record_preview);
        mProgressView = findViewById(R.id.record_progress);
        mRecordController = findViewById(R.id.recorder);
        mRecordController.setSelected(true);
        mProgressView.setMaxDuration(RECORD_TIME_MAX);
        mProgressView.setMinDuration(RECORD_TIME_MIN);
        findViewById(R.id.confirm).setOnClickListener(this);

        initProcessDialog();

        if (checkCameraPermission()){
            initMediaRecorder();
        }
    }



    public void initProcessDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(100);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage(getString(R.string.tips_saveing_video));
        progressDialog.setTitle(null);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);
    }


    private  boolean checkCameraPermission() {
        if (EasyPermissions.hasPermissions(this,Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)){
            return true;
        }
        EasyPermissions.requestPermissions(this, getString(R.string.tip_permission_camera_disable), 8, Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO);
        return false;
    }



    /**
     * 初始化拍摄SDK
     */
    private void initMediaRecorder() {
        mShortVideoRecorder = new PLShortVideoRecorder();
        mShortVideoRecorder.setRecordStateListener(this);
        PLCameraSetting cameraSetting = new PLCameraSetting();
        cameraSetting.setCameraId(PLCameraSetting.CAMERA_FACING_ID.CAMERA_FACING_BACK);
        cameraSetting.setCameraPreviewSizeRatio(PLCameraSetting.CAMERA_PREVIEW_SIZE_RATIO.RATIO_16_9);
        cameraSetting.setCameraPreviewSizeLevel(PLCameraSetting.CAMERA_PREVIEW_SIZE_LEVEL.PREVIEW_SIZE_LEVEL_240P);
        // 麦克风采集选项
        PLMicrophoneSetting microphoneSetting = new PLMicrophoneSetting();
        // 视频编码选项
        PLVideoEncodeSetting videoEncodeSetting = new PLVideoEncodeSetting(this);
        videoEncodeSetting.setEncodingSizeLevel(PLVideoEncodeSetting.VIDEO_ENCODING_SIZE_LEVEL.VIDEO_ENCODING_SIZE_LEVEL_240P_1); // 480x480
        videoEncodeSetting.setEncodingBitrate(1000*1024); // 1000kbps
        videoEncodeSetting.setEncodingFps(24);
        videoEncodeSetting.setHWCodecEnabled(true); // true:硬编 false:软编
        // 音频编码选项
        PLAudioEncodeSetting audioEncodeSetting = new PLAudioEncodeSetting();
        audioEncodeSetting.setHWCodecEnabled(true); // true:硬编 false:软编
        PLRecordSetting recordSetting = new PLRecordSetting();
        recordSetting.setMaxRecordDuration(RECORD_TIME_MAX); // 10s
        recordSetting.setDisplayMode(PLDisplayMode.FULL);
        videoFile = new File(LvxinApplication.CACHE_DIR_VIDEO, StringUtils.getUUID()+".mp4");
        recordSetting.setVideoCacheDir(LvxinApplication.CACHE_DIR_VIDEO);
        recordSetting.setVideoFilepath(videoFile.getAbsolutePath());
        mShortVideoRecorder.prepare(mSurfaceView, cameraSetting, microphoneSetting,videoEncodeSetting, audioEncodeSetting, null, recordSetting);
        mSurfaceView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN && isTouchInside(event)) {
            mRecordController.setSelected(true);
            startRecord();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (!isTouchInside(event) && isRecordingMode) {
                mRecordController.setSelected(false);
                stopRecord();
                return super.dispatchTouchEvent(event);
            }

        }

        if ((event.getAction() == MotionEvent.ACTION_CANCEL || event.getAction() == MotionEvent.ACTION_UP)) {
            mRecordController.setSelected(true);
            stopRecord();
            return super.dispatchTouchEvent(event);
        }
        return super.dispatchTouchEvent(event);
    }

    private boolean isTouchInside(MotionEvent event) {
        int[] loc = new int[2];
        mRecordController.getLocationInWindow(loc);
        return event.getRawY() >= loc[1] && event.getRawY() <= loc[1] + mRecordController.getHeight()
                && event.getRawX() >= loc[0] && event.getRawX() <= loc[0] + mRecordController.getWidth();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (mShortVideoRecorder != null){
            mShortVideoRecorder.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mShortVideoRecorder != null){
            mShortVideoRecorder.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mShortVideoRecorder != null){
            mShortVideoRecorder.destroy();
        }
    }

    /**
     * 开始录制
     */
    private void startRecord() {
        mProgressView.start();
        mShortVideoRecorder.beginSection();
        isRecordingMode = true;
    }

    /**
     * 停止录制
     */
    private void stopRecord() {
        isRecordingMode = false;
        mShortVideoRecorder.endSection();
        mProgressView.stop();
        if (mProgressView.isDurationQualified()) {
            findViewById(R.id.confirm).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.confirm).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.confirm) {
            onRecordCompleted();
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.video_recorder, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_toggle) {
            mShortVideoRecorder.switchCamera();
        }
        if (item.getItemId() == R.id.menu_light) {
            lightMode = !lightMode;
            mShortVideoRecorder.setFlashEnabled(lightMode);
        }
        if (item.getItemId() == R.id.menu_screen && !isRecordingMode) {
            mShortVideoRecorder.deleteAllSections();
            int screenNum = getResources().getConfiguration().orientation;
            if (screenNum == ORIENTATION_PORTRAIT ){
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onReady() {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onDurationTooShort() {

    }

    @Override
    public void onRecordStarted() {

    }

    @Override
    public void onRecordStopped() {

    }

    @Override
    public void onSectionIncreased(long l, long l1, int i) {

    }

    @Override
    public void onSectionDecreased(long l, long l1, int i) {

    }


    @Override
    public void onRecordCompleted() {
        BackgroundThreadHandler.postUIThread(new Runnable() {
            @Override
            public void run() {
                stopRecord();
                findViewById(R.id.confirm).setVisibility(View.GONE);
                progressDialog.show();
                mShortVideoRecorder.concatSections(VideoRecorderActivity.this);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 789) {
            Intent intent = new Intent();
            intent.putExtra(SNSVideo.class.getName(), video);
            setResult(RESULT_OK, intent);
            finish();
        }
        if (resultCode != RESULT_OK && requestCode == 789) {
            FileUtils.deleteQuietly(new File(LvxinApplication.CACHE_DIR_VIDEO, video.image));
            FileUtils.deleteQuietly(new File(LvxinApplication.CACHE_DIR_VIDEO, video.video));
            mProgressView.reset();
            mShortVideoRecorder.deleteAllSections();
            findViewById(R.id.confirm).setVisibility(View.GONE);
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        initMediaRecorder();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            PermissionDialog.create(this).show();
            return;
        }

        showToastView(R.string.tip_permission_camera_rejected);
    }



    @Override
    public void onSaveVideoSuccess(String s) {
        video.size = videoFile.length();
        video.duration = mProgressView.getDurationSecond();
        video.video = videoFile.getName();
        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            video.mode = SNSVideo.HORIZONTAL;
        } else  {
            video.mode = SNSVideo.VERTICAL;
        }
        progressDialog.dismiss();
        video.image = AppTools.getVideoThumbnailFile(videoFile).getName();
        LvxinApplication.getInstance().startVideoActivity(this, true, video, null);
    }

    @Override
    public void onSaveVideoFailed(int i) {
        progressDialog.dismiss();

    }

    @Override
    public void onSaveVideoCanceled() {
        progressDialog.dismiss();
    }

    @Override
    public void onProgressUpdate(float v) {
        progressDialog.setProgress((int) (v * 100));

    }


}
