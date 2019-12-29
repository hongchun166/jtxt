package com.linkb.video;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.video.utils.ToastUtils;
import com.linkb.video.utils.TrackWindowMgr;
import com.linkb.video.utils.VideoConfig;
import com.linkb.video.view.UserTrackView;
import com.qiniu.droid.rtc.QNBeautySetting;
import com.qiniu.droid.rtc.QNCameraSwitchResultCallback;
import com.qiniu.droid.rtc.QNErrorCode;
import com.qiniu.droid.rtc.QNRTCEngine;
import com.qiniu.droid.rtc.QNRTCEngineEventListener;
import com.qiniu.droid.rtc.QNRTCSetting;
import com.qiniu.droid.rtc.QNRoomState;
import com.qiniu.droid.rtc.QNSourceType;
import com.qiniu.droid.rtc.QNStatisticsReport;
import com.qiniu.droid.rtc.QNTrackInfo;
import com.qiniu.droid.rtc.QNTrackKind;
import com.qiniu.droid.rtc.QNVideoFormat;
import com.qiniu.droid.rtc.model.QNAudioDevice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.linkb.video.utils.VideoConfig.DEFAULT_BITRATE;
import static com.linkb.video.utils.VideoConfig.DEFAULT_FPS;
import static com.linkb.video.utils.VideoConfig.DEFAULT_RESOLUTION;


public class RoomActivity extends Activity implements QNRTCEngineEventListener, ControlFragment.OnCallEvents {
    private static final String TAG = "RoomActivity";
    private static final int BITRATE_FOR_SCREEN_VIDEO = (int) (1.5 * 1000 * 1000);

    public static final int CONNECT_TIME_OUT = 60;

    public static final String EXTRA_USER_ID = "USER_ID";
    public static final String EXTRA_ROOM_TOKEN = "ROOM_TOKEN";
    public static final String EXTRA_ROOM_ID = "ROOM_ID";

    public static final String EXTRA_ISSENDER = "is_sender";

    public static final String EXTRA_OPPOSITE_USER_ID = "OPPOSITE_USER_ID";

    public static final String EXTRA_OPPOSITE_GROUP_MEMBER = "OPPOSITE_GROUP_MEMBER";

    /** 是视频还是语音
    * */
    public static final String EXTRA_IS_VIDEO = "is_video";

    /** 如果是群聊，显示群id
    * */
    public static final String EXTRA_GROUP_ID = "group_id";

    /** 群组聊天群员列表
    * */
    private List<String> mGroupVideoConnectObjectList = new ArrayList<>();

    private static final String[] MANDATORY_PERMISSIONS = {
            "android.permission.MODIFY_AUDIO_SETTINGS",
            "android.permission.RECORD_AUDIO",
            "android.permission.INTERNET"
    };

    private Toast mLogToast;
    private List<String> mHWBlackList = new ArrayList<>();

    private UserTrackView mTrackWindowFullScreen;
    private List<UserTrackView> mTrackWindowsList;
    private AlertDialog mKickOutDialog;

    private QNRTCEngine mEngine;
    private String mRoomToken;
    private String mUserId;
    private String mRoomId;
    private boolean mMicEnabled = true;
    private boolean mBeautyEnabled = false;
    private boolean mVideoEnabled = true;
    private boolean mSpeakerEnabled = false;
    private boolean mIsError = false;
    private boolean mIsAdmin = false;
    private boolean mIsJoinedRoom = false;
    private ControlFragment mControlFragment;
    private List<QNTrackInfo> mLocalTrackList;

    private QNTrackInfo mLocalVideoTrack;
    private QNTrackInfo mLocalAudioTrack;
    private QNTrackInfo mLocalScreenTrack;

    private int mScreenWidth = 0;
    private int mScreenHeight = 0;
    private int mCaptureMode = VideoConfig.CAMERA_CAPTURE;

    private TrackWindowMgr mTrackWindowMgr;

    /** 判断是发送视频还是接受音视频
     * */
    private boolean isSender = false;
    /** 对方账户
     * */
    private String accout = "";

    /** 视频还是语音通话
     * */
    private boolean isVideo = true;

    /** 等待期间（暂定60s）内对方是否接受音视频请求
    * */
    private boolean isReceived = false;

    private VideoRoomBroadcastReceiver mVideoRoomBroadcastReceiver;

    private Handler handler = new Handler();

    /** 音视频呼叫铃声
    * */
    private MediaPlayer mMediaPlayer;
    /** 群聊的群成员，“，”隔开
    * */
    private String mGroupMember;

    /** 控制音量
    * */
    private AudioManager audioMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
//                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
//                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
//        getWindow().getDecorView().setSystemUiVisibility(getSystemUiVisibility());

        // 所有子类都将继承这些相同的属性,请在设置界面之后设置
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .init();

        final WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;
        mScreenHeight = outMetrics.heightPixels;

        setContentView(R.layout.activity_muti_track_room);

        initCallBell();

        Intent intent = getIntent();
        mRoomToken = intent.getStringExtra(EXTRA_ROOM_TOKEN);
        mUserId = intent.getStringExtra(EXTRA_USER_ID);
        mRoomId = intent.getStringExtra(EXTRA_ROOM_ID);
        mIsAdmin = mUserId.equals(QNAppServer.ADMIN_USER);

        accout = intent.getStringExtra(EXTRA_OPPOSITE_USER_ID);
        isSender = intent.getBooleanExtra(EXTRA_ISSENDER, false);
        isVideo = intent.getBooleanExtra(EXTRA_IS_VIDEO, true);
        mGroupMember = intent.getStringExtra(EXTRA_OPPOSITE_GROUP_MEMBER);
        if (!TextUtils.isEmpty(mGroupMember)) {
            resolveGroupMember(mGroupMember);
        }

        mTrackWindowFullScreen = (UserTrackView) findViewById(R.id.track_window_full_screen);
        mTrackWindowsList = new LinkedList<UserTrackView>(Arrays.asList(
                (UserTrackView) findViewById(R.id.track_window_a),
                (UserTrackView) findViewById(R.id.track_window_b),
                (UserTrackView) findViewById(R.id.track_window_c),
                (UserTrackView) findViewById(R.id.track_window_d),
                (UserTrackView) findViewById(R.id.track_window_e),
                (UserTrackView) findViewById(R.id.track_window_f),
                (UserTrackView) findViewById(R.id.track_window_g),
                (UserTrackView) findViewById(R.id.track_window_h),
                (UserTrackView) findViewById(R.id.track_window_i)
        ));

        for (final UserTrackView view : mTrackWindowsList) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mIsAdmin) {
                        showKickoutDialog(view.getUserId());
                    }
                    return false;
                }
            });
        }

        // init Control fragment
        mControlFragment = ControlFragment.getInstance(isSender, isVideo, accout, mRoomId);
        mControlFragment.setArguments(intent.getExtras());
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.control_fragment_container, mControlFragment);
        ft.commitAllowingStateLoss();

        // permission check
        for (String permission : MANDATORY_PERMISSIONS) {
            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                logAndToast("Permission " + permission + " is not granted");
                setResult(RESULT_CANCELED);
                finish();
                return;
            }
        }

        // init rtcEngine and local track info list.
        initQNRTCEngine();
        initLocalTrackInfoList();

        // init decorate and set default to p2p mode
        mTrackWindowMgr = new TrackWindowMgr(mUserId, mScreenWidth, mScreenHeight, outMetrics.density
                , mEngine, mTrackWindowFullScreen, mTrackWindowsList);

        List<QNTrackInfo> localTrackListExcludeScreenTrack = new ArrayList<>(mLocalTrackList);
        localTrackListExcludeScreenTrack.remove(mLocalScreenTrack);
        mTrackWindowMgr.addTrackInfo(mUserId, localTrackListExcludeScreenTrack);

        //注册广播
        mVideoRoomBroadcastReceiver = new VideoRoomBroadcastReceiver();
        LvxinApplication.registerLocalReceiver(mVideoRoomBroadcastReceiver, mVideoRoomBroadcastReceiver.getIntentFilter());



        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if ( !isReceived){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isSender){
                                ToastUtils.s(RoomActivity.this, getString(R.string.waiting_for_receive_video_time_out));
                            }
                        }
                    });
                    finish();
                }
            }
        }, 1000 * CONNECT_TIME_OUT);

    }

    /** 将groupMember字符串转化为列表
     *  groupMember字符串是用,隔开的
    * */
    private void resolveGroupMember(String groupMember) {
        if (!groupMember.contains(",")){
            mGroupVideoConnectObjectList.add(groupMember);
        }else {
            String[] strings = groupMember.split(",");
            for (int i = 0; i < strings.length; i++) {
                mGroupVideoConnectObjectList.add(strings[i]);
            }
        }
    }

    /** 初始化背景呼叫铃声
    * */
    private void initCallBell(){
        audioMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioMgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioMgr.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume / 3, AudioManager.FLAG_PLAY_SOUND);

        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.call_bell);
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setLooping(true);
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /** 停止播放并且回收资源
    * */
    private void stopPlayCallBell(){
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    private void initQNRTCEngine() {
        int videoWidth = DEFAULT_RESOLUTION[1][0];
        int videoHeight = DEFAULT_RESOLUTION[1][1];
        int fps = DEFAULT_FPS[1];
        boolean isHwCodec = true;
        int videoBitrate = DEFAULT_BITRATE[1];
        boolean isMaintainRes = false;
        mCaptureMode = isVideo ? VideoConfig.CAMERA_CAPTURE : VideoConfig.ONLY_AUDIO_CAPTURE;

        // get the items in hw black list, and set isHwCodec false forcibly
        String[] hwBlackList = getResources().getStringArray(R.array.hw_black_list);
        mHWBlackList.addAll(Arrays.asList(hwBlackList));
        if (mHWBlackList.contains(Build.MODEL)) {
            isHwCodec = false;
        }

        QNVideoFormat format = new QNVideoFormat(videoWidth, videoHeight, fps);
        QNRTCSetting setting = new QNRTCSetting();
        setting.setCameraID(QNRTCSetting.CAMERA_FACING_ID.FRONT)
                .setHWCodecEnabled(isHwCodec)
                .setMaintainResolution(isMaintainRes)
                .setVideoBitrate(videoBitrate)
                .setVideoEncodeFormat(format)
                .setVideoPreviewFormat(format);
        mEngine = QNRTCEngine.createEngine(getApplicationContext(), setting, this);
        mEngine.setSpeakerphoneOn(false);
    }

    private void initLocalTrackInfoList() {
        mLocalTrackList = new ArrayList<>();
        mLocalAudioTrack = mEngine.createTrackInfoBuilder()
                .setSourceType(QNSourceType.AUDIO)
                .setMaster(true)
                .create();
        mLocalTrackList.add(mLocalAudioTrack);

        QNVideoFormat screenEncodeFormat = new QNVideoFormat(mScreenWidth/2, mScreenHeight/2, 15);
        switch (mCaptureMode) {
            case VideoConfig.CAMERA_CAPTURE:
                mLocalVideoTrack = mEngine.createTrackInfoBuilder()
                        .setSourceType(QNSourceType.VIDEO_CAMERA)
                        .setMaster(true)
                        .setTag(UserTrackView.TAG_CAMERA).create();
                mLocalTrackList.add(mLocalVideoTrack);
                break;
            case VideoConfig.ONLY_AUDIO_CAPTURE:
                mControlFragment.setAudioOnly(true);
                break;
            case VideoConfig.SCREEN_CAPTURE:
                mLocalScreenTrack = mEngine.createTrackInfoBuilder()
                        .setVideoPreviewFormat(screenEncodeFormat)
                        .setBitrate(BITRATE_FOR_SCREEN_VIDEO)
                        .setSourceType(QNSourceType.VIDEO_SCREEN)
                        .setMaster(true)
                        .setTag(UserTrackView.TAG_SCREEN).create();
                mLocalTrackList.add(mLocalScreenTrack);
                mControlFragment.setAudioOnly(true);
                break;
            case VideoConfig.MUTI_TRACK_CAPTURE:
                mLocalScreenTrack = mEngine.createTrackInfoBuilder()
                        .setSourceType(QNSourceType.VIDEO_SCREEN)
                        .setVideoPreviewFormat(screenEncodeFormat)
                        .setBitrate(BITRATE_FOR_SCREEN_VIDEO)
                        .setMaster(true)
                        .setTag(UserTrackView.TAG_SCREEN).create();
                mLocalVideoTrack = mEngine.createTrackInfoBuilder()
                        .setSourceType(QNSourceType.VIDEO_CAMERA)
                        .setTag(UserTrackView.TAG_CAMERA).create();
                mLocalTrackList.add(mLocalScreenTrack);
                mLocalTrackList.add(mLocalVideoTrack);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isSender){
            onJoinRoom();
        }
//        onJoinRoom();
    }


    /** 加入房间
    * */
    public void onJoinRoom(){
        if (!isSender) {
            isReceived = true;
        }
        if (!mIsJoinedRoom) {
            mEngine.joinRoom(mRoomToken);
        }
        if (!isSender){
            //销毁铃声
            stopPlayCallBell();
        }
    }

    /** 发送增加了取消通话的消息
     * */
    public void sendCancelMessage(String sender, String content) {
        isReceived = true;
        if (mGroupVideoConnectObjectList.isEmpty()){
            singleSendCancelMessage(sender, content,accout);
        }else {
            for (String receiver : mGroupVideoConnectObjectList) {
                singleSendCancelMessage(sender, content, receiver);
            }
        }

    }

    /** 单独发送取消通话的消息
    * */
    private void singleSendCancelMessage(String sender, String content, String receiver){
        Message message = new Message();
        message.id = System.currentTimeMillis();
        message.content = content;
        message.sender = sender;
        message.receiver = receiver;
        message.format = Constant.MessageFormat.FORMAT_TEXT;
        message.extra = "";
        message.action = Constant.MessageAction.ACTION_7;
        message.timestamp = System.currentTimeMillis();
        message.state = Constant.MessageStatus.STATUS_NO_SEND;

        HttpServiceManager.rejectVideoConnectMessage(message);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mEngine != null) {
            mEngine.destroy();
            mEngine = null;
        }
        if (mTrackWindowFullScreen != null) {
            mTrackWindowFullScreen.dispose();
        }
        for (UserTrackView item : mTrackWindowsList) {
            item.dispose();
        }
        mTrackWindowsList.clear();

        //注销广播
        LvxinApplication.unregisterLocalReceiver(mVideoRoomBroadcastReceiver);
        //销毁铃声
        stopPlayCallBell();
    }

    private void logAndToast(final String msg) {
        Log.d(TAG, msg);
//        if (mLogToast != null) {
//            mLogToast.cancel();
//        }
//        mLogToast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
//        mLogToast.show();
    }

    private void disconnectWithErrorMessage(final String errorMessage) {
        new AlertDialog.Builder(this)
                .setTitle(getText(R.string.channel_error_title))
                .setMessage(errorMessage)
                .setCancelable(false)
                .setNeutralButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                finish();
                            }
                        })
                .create()
                .show();
    }

    private void reportError(final String description) {
        // TODO: handle error.
        if (!mIsError) {
            mIsError = true;
            disconnectWithErrorMessage(description);
        }
    }

    private void showKickoutDialog(final String userId) {
        if (mKickOutDialog == null) {
            mKickOutDialog = new AlertDialog.Builder(this)
                    .setNegativeButton(R.string.negative_dialog_tips, null)
                    .create();
        }
        mKickOutDialog.setMessage(getString(R.string.kickout_tips, userId));
        mKickOutDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.positive_dialog_tips),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mEngine.kickOutUser(userId);
                    }
                });
        mKickOutDialog.show();
    }

    private void updateRemoteLogText(final String logText) {
        Log.i(TAG, logText);
        mControlFragment.updateRemoteLogText(logText);
    }

    @TargetApi(19)
    private static int getSystemUiVisibility() {
        int flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            flags |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        return flags;
    }

    @Override
    public void onRoomStateChanged(QNRoomState state) {
        Log.i(TAG, "onRoomStateChanged:" + state.name());
        switch (state) {
            case RECONNECTING:
                logAndToast(getString(R.string.reconnecting_to_room));
                mControlFragment.stopTimer();
                break;
            case CONNECTED:
                mEngine.publishTracks(mLocalTrackList);
                logAndToast(getString(R.string.connected_to_room));
                mIsJoinedRoom = true;
                if (!isSender) mControlFragment.startTimer();
                break;
            case RECONNECTED:
                logAndToast(getString(R.string.connected_to_room));
//                mControlFragment.startTimer();
                break;
            case CONNECTING:
                logAndToast(getString(R.string.connecting_to, mRoomId));
                break;
        }
    }

    @Override
    public void onRemoteUserJoined(String remoteUserId, String userData) {
        //对方同意音视频请求
        //销毁铃声
        stopPlayCallBell();

        isReceived = true;
        mControlFragment.startTimer();
        mControlFragment.onVideoConnectSuccess();
        updateRemoteLogText("onRemoteUserJoined:remoteUserId = " + remoteUserId + " ,userData = " + userData);
    }

    @Override
    public void onRemoteUserLeft(final String remoteUserId) {
        updateRemoteLogText("onRemoteUserLeft:remoteUserId = " + remoteUserId);
        if (mGroupVideoConnectObjectList.isEmpty()){//单对单聊天
            //单对单对方挂断了音视频
            if (remoteUserId.equals(accout)){
                ToastUtils.s(RoomActivity.this, getString(R.string.video_connect_hang_up_tips));
                finish();
            }
        }else {//单对多聊天
            mGroupVideoConnectObjectList.remove(remoteUserId);
            if (mGroupVideoConnectObjectList.isEmpty()){
                ToastUtils.s(RoomActivity.this, getString(R.string.video_connect_dismiss));
                finish();
            }
        }
    }

    @Override
    public void onLocalPublished(List<QNTrackInfo> trackInfoList) {
        updateRemoteLogText("onLocalPublished");
        mEngine.enableStatistics();
    }

    @Override
    public void onRemotePublished(String remoteUserId, List<QNTrackInfo> trackInfoList) {
        updateRemoteLogText("onRemotePublished:remoteUserId = " + remoteUserId);
    }

    @Override
    public void onRemoteUnpublished(final String remoteUserId, List<QNTrackInfo> trackInfoList) {
        updateRemoteLogText("onRemoteUnpublished:remoteUserId = " + remoteUserId);
        if (mTrackWindowMgr != null) {
            mTrackWindowMgr.removeTrackInfo(remoteUserId, trackInfoList);
        }
    }

    @Override
    public void onRemoteUserMuted(String remoteUserId, List<QNTrackInfo> trackInfoList) {
        updateRemoteLogText("onRemoteUserMuted:remoteUserId = " + remoteUserId);
        if (mTrackWindowMgr != null) {
            mTrackWindowMgr.onTrackInfoMuted(remoteUserId);
        }
    }

    @Override
    public void onSubscribed(String remoteUserId, List<QNTrackInfo> trackInfoList) {
        updateRemoteLogText("onSubscribed:remoteUserId = " + remoteUserId);
        if (mTrackWindowMgr != null) {
            mTrackWindowMgr.addTrackInfo(remoteUserId, trackInfoList);
        }
    }

    @Override
    public void onKickedOut(String userId) {
        ToastUtils.s(RoomActivity.this, getString(R.string.kicked_by_admin));
        finish();
    }

    @Override
    public void onStatisticsUpdated(final QNStatisticsReport report) {
        if (report.userId == null || report.userId.equals(mUserId)) {
            if (QNTrackKind.AUDIO.equals(report.trackKind)) {
                final String log = "音频码率:" + report.audioBitrate / 1000 + "kbps \n" +
                        "音频丢包率:" + report.audioPacketLostRate;
                mControlFragment.updateLocalAudioLogText(log);
            } else if (QNTrackKind.VIDEO.equals(report.trackKind)) {
                final String log = "视频码率:" + report.videoBitrate / 1000 + "kbps \n" +
                        "视频丢包率:" + report.videoPacketLostRate + " \n" +
                        "视频的宽:" + report.width + " \n" +
                        "视频的高:" + report.height + " \n" +
                        "视频的帧率:" + report.frameRate;
                mControlFragment.updateLocalVideoLogText(log);
            }
        }
    }

    @Override
    public void onAudioRouteChanged(QNAudioDevice routing) {
        updateRemoteLogText("onAudioRouteChanged: " + routing.name());
    }

    @Override
    public void onCreateMergeJobSuccess(String mergeJobId) {
    }

    @Override
    public void onError(int errorCode, String description) {
        if (errorCode == QNErrorCode.ERROR_TOKEN_INVALID
                || errorCode == QNErrorCode.ERROR_TOKEN_ERROR
                || errorCode == QNErrorCode.ERROR_TOKEN_EXPIRED) {
            reportError("roomToken 错误，请重新加入房间");
        } else if (errorCode == QNErrorCode.ERROR_AUTH_FAIL
                || errorCode == QNErrorCode.ERROR_RECONNECT_TOKEN_ERROR) {
            // reset TrackWindowMgr
            mTrackWindowMgr.reset();
            // display local videoTrack
            List<QNTrackInfo> localTrackListExcludeScreenTrack = new ArrayList<>(mLocalTrackList);
            localTrackListExcludeScreenTrack.remove(mLocalScreenTrack);
            mTrackWindowMgr.addTrackInfo(mUserId, localTrackListExcludeScreenTrack);
            // rejoin Room
            mEngine.joinRoom(mRoomToken);
        } else if (errorCode == QNErrorCode.ERROR_PUBLISH_FAIL) {
            reportError("发布失败，请重新加入房间发布");
        } else {
            logAndToast("errorCode:" + errorCode + " description:" + description);
        }
    }

    // Demo control
    @Override
    public void onCallHangUp() {
        if (mEngine != null) {
            mEngine.leaveRoom();
        }
        finish();
    }

    @Override
    public void onCameraSwitch() {
        if (mEngine != null) {
            mEngine.switchCamera(new QNCameraSwitchResultCallback() {
                @Override
                public void onCameraSwitchDone(boolean isFrontCamera) {
                }

                @Override
                public void onCameraSwitchError(String errorMessage) {
                }
            });
        }
    }

    @Override
    public boolean onToggleMic() {
        if (mEngine != null && mLocalAudioTrack != null) {
            mMicEnabled = !mMicEnabled;
            mLocalAudioTrack.setMuted(!mMicEnabled);
            mEngine.muteTracks(Collections.singletonList(mLocalAudioTrack));
            if (mTrackWindowMgr != null) {
                mTrackWindowMgr.onTrackInfoMuted(mUserId);
            }
        }
        return mMicEnabled;
    }

    @Override
    public boolean onToggleVideo() {
        if (mEngine != null && mLocalVideoTrack != null) {
            mVideoEnabled = !mVideoEnabled;
            mLocalVideoTrack.setMuted(!mVideoEnabled);
            if (mLocalScreenTrack != null) {
                mLocalScreenTrack.setMuted(!mVideoEnabled);
                mEngine.muteTracks(Arrays.asList(mLocalScreenTrack, mLocalVideoTrack));
            } else {
                mEngine.muteTracks(Collections.singletonList(mLocalVideoTrack));
            }
            if (mTrackWindowMgr != null) {
                mTrackWindowMgr.onTrackInfoMuted(mUserId);
            }
        }
        return mVideoEnabled;
    }

    @Override
    public boolean onToggleSpeaker() {
        if (mEngine != null) {
            mSpeakerEnabled = !mSpeakerEnabled;
//            mEngine.muteRemoteAudio(!mSpeakerEnabled);  //静默对方的说话
            mEngine.setSpeakerphoneOn(mSpeakerEnabled); //  控制外放扩音
        }
        return mSpeakerEnabled;
    }

    @Override
    public boolean onToggleBeauty() {
        if (mEngine != null) {
            mBeautyEnabled = !mBeautyEnabled;
            QNBeautySetting beautySetting = new QNBeautySetting(0.5f, 0.5f, 0.5f);
            beautySetting.setEnable(mBeautyEnabled);
            mEngine.setBeauty(beautySetting);
        }
        return mBeautyEnabled;
    }

    public class VideoRoomBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Constant.Action.ACTION_OPPOSITE_REJECT_VIDEO_CONNECT)){
                //对方拒绝了你发起的音视频连接
                Message message = (Message) intent.getSerializableExtra(Message.class.getName());
                if (TextUtils.isEmpty(message.content)){ //单对单聊天
                    if (message.sender.equals(accout)){
                        ToastUtils.s(RoomActivity.this, getString(R.string.video_connect_reject_tips));
                        //销毁铃声
                        stopPlayCallBell();
                        finish();
                    }
                }else { //单对多聊天
                    // TODO: 2019/4/24 这里可以增加拒绝提示
                    mGroupVideoConnectObjectList.remove(message.sender);
                    if (mGroupVideoConnectObjectList.isEmpty()){
                        ToastUtils.s(RoomActivity.this, getString(R.string.group_video_connect_reject_tips));
                        //销毁铃声
                        stopPlayCallBell();
                        finish();
                    }
                }
            }

            if (intent.getAction().equals(Constant.Action.ACTION_OPPOSITE_CANCEL_VIDEO_CONNECT)){
                //对方取消了你发起的音视频连接
                Message message = (Message) intent.getSerializableExtra(Message.class.getName());
                if (message.sender.equals(accout)){
                    ToastUtils.s(RoomActivity.this, getString(R.string.video_connect_cancel_tips));
                    //销毁铃声
                    stopPlayCallBell();
                    finish();
                }
            }
        }

        IntentFilter getIntentFilter(){
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Constant.Action.ACTION_OPPOSITE_REJECT_VIDEO_CONNECT);
            intentFilter.addAction(Constant.Action.ACTION_OPPOSITE_CANCEL_VIDEO_CONNECT);
            return intentFilter;
        }
    }

}
