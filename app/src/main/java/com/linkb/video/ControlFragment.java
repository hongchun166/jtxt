package com.linkb.video;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.util.FileURLBuilder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;



/**
 * Fragment for call control.
 */
public class ControlFragment extends Fragment {

    private View mControlView;
    private ImageButton mDisconnectButton;
    private ImageButton mCameraSwitchButton;
    private ImageButton mToggleMuteButton;
    private ImageButton mToggleBeautyButton;
    private ImageButton mToggleSpeakerButton;
    private ImageButton mToggleVideoButton;
    private ImageButton mLogShownButton;
    private LinearLayout mLogView;
    private TextView mLocalTextViewForVideo;
    private TextView mLocalTextViewForAudio;
    private TextView mRemoteTextView;
    private StringBuffer mRemoteLogText;
    private Chronometer mTimer;
    private OnCallEvents mCallEvents;
    private boolean mIsVideoEnabled = true;
    private boolean mIsShowingLog = false;
    private boolean mIsScreenCaptureEnabled = false;
    private boolean mIsAudioOnly = false;

    @BindView(R.id.bottom_button_layout) View bottomItemView;

    @BindView(R.id.connecting_tips_lly) View connectingTipsLLy;
    @BindView(R.id.avatar_img) WebImageView avatarImg;
    @BindView(R.id.name_tv) TextView nameTv;
    @BindView(R.id.video_connect_tips_tv) TextView videoConnectTipsTv;
    @BindView(R.id.connecting_btn_lly) View connectingBtnLly;
    @BindView(R.id.reject_image_btn) TextView rejectImageBtn;
    @BindView(R.id.receive_image_btn) TextView receiveImageBtn;

    @BindView(R.id.waiting_connecting_btn_lly)  View cancelLLy;
    @BindView(R.id.cancel_btn_tv) TextView cancelBtnTv;


    /** 判断是发送视频还是接受音视频
     * */
    private boolean isSender = false;
    /** 用户账户
     * */
    private String accunt = "";

    /** 用户是否处于正在连接中
     * */
    private boolean isContecting = true;

    /** 视频还是语音通话
     * */
    private boolean isVideo = true;

    /**  房间名
    * */
    private String rootName = "";

    private User mSelf;


    /**
     * Call control interface for container activity.
     */
    public interface OnCallEvents {
        void onCallHangUp();

        void onCameraSwitch();

        boolean onToggleMic();

        boolean onToggleVideo();

        boolean onToggleSpeaker();

        boolean onToggleBeauty();

    }

    public static ControlFragment getInstance(boolean isSender, boolean isVideo, String accunt, String rootName){
        ControlFragment controlFragment = new ControlFragment();
        controlFragment.isSender = isSender;
        controlFragment.accunt = accunt;
        controlFragment.isVideo = isVideo;
        controlFragment.rootName = rootName;
        return controlFragment;
    }

    public void setScreenCaptureEnabled(boolean isScreenCaptureEnabled) {
        mIsScreenCaptureEnabled = isScreenCaptureEnabled;
    }

    public void setAudioOnly(boolean isAudioOnly) {
        mIsAudioOnly = isAudioOnly;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mControlView = inflater.inflate(R.layout.fragment_room, container, false);

        ButterKnife.bind(this, mControlView);

        mDisconnectButton = (ImageButton) mControlView.findViewById(R.id.disconnect_button);
        mCameraSwitchButton = (ImageButton) mControlView.findViewById(R.id.camera_switch_button);
        mToggleBeautyButton = (ImageButton) mControlView.findViewById(R.id.beauty_button);
        mToggleMuteButton = (ImageButton) mControlView.findViewById(R.id.microphone_button);
        mToggleSpeakerButton = (ImageButton) mControlView.findViewById(R.id.speaker_button);
        mToggleVideoButton = (ImageButton) mControlView.findViewById(R.id.camera_button);
        mLogShownButton = (ImageButton) mControlView.findViewById(R.id.log_shown_button);
        mLogView = (LinearLayout) mControlView.findViewById(R.id.log_text);
        mLocalTextViewForVideo = (TextView) mControlView.findViewById(R.id.local_log_text_video);
        mLocalTextViewForVideo.setMovementMethod(ScrollingMovementMethod.getInstance());
        mLocalTextViewForAudio = (TextView) mControlView.findViewById(R.id.local_log_text_audio);
        mLocalTextViewForAudio.setMovementMethod(ScrollingMovementMethod.getInstance());
        mRemoteTextView = (TextView) mControlView.findViewById(R.id.remote_log_text);
        mRemoteTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mTimer = (Chronometer) mControlView.findViewById(R.id.timer);

        mDisconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallEvents.onCallHangUp();
            }
        });

        if (!mIsScreenCaptureEnabled && !mIsAudioOnly) {
            mCameraSwitchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mCallEvents.onCameraSwitch();
                }
            });
        }

        if (!mIsScreenCaptureEnabled && !mIsAudioOnly) {
            mToggleBeautyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean enabled = mCallEvents.onToggleBeauty();
                    mToggleBeautyButton.setImageResource(enabled ? R.mipmap.face_beauty_open : R.mipmap.face_beauty_close);
                }
            });
        }

        mToggleMuteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean enabled = mCallEvents.onToggleMic();
                mToggleMuteButton.setImageResource(enabled ? R.mipmap.microphone : R.mipmap.microphone_disable);
            }
        });

        if (mIsScreenCaptureEnabled || mIsAudioOnly) {
            mToggleVideoButton.setImageResource(R.mipmap.video_close);
        } else {
            mToggleVideoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean enabled = mCallEvents.onToggleVideo();
                    mToggleVideoButton.setImageResource(enabled ? R.mipmap.video_open : R.mipmap.video_close);
                }
            });
        }

        mToggleSpeakerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean enabled = mCallEvents.onToggleSpeaker();
                mToggleSpeakerButton.setImageResource(enabled ? R.mipmap.loudspeaker : R.mipmap.loudspeaker_disable);
            }
        });

        mLogShownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLogView.setVisibility(mIsShowingLog ? View.INVISIBLE : View.VISIBLE);
                mIsShowingLog = !mIsShowingLog;
            }
        });
        return mControlView;
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mSelf = Global.getCurrentUser();

        avatarImg.load(FileURLBuilder.getUserIconUrl(accunt), R.mipmap.lianxiren, 999);
        Friend friend = FriendRepository.queryFriend(accunt);
        nameTv.setText(TextUtils.isEmpty(friend.name) ? accunt : friend.name);

        changeUI(isContecting, isSender, isVideo);
    }

    /** 根据接通状态变更UI
    * */
     private void changeUI(boolean isContecting, boolean isSender, boolean isVideo){
         bottomItemView.setVisibility(isContecting ? View.INVISIBLE : View.VISIBLE);
         connectingTipsLLy.setVisibility(isContecting ? View.VISIBLE : View.INVISIBLE);
         cancelLLy.setVisibility(isContecting && isSender ? View.VISIBLE : View.INVISIBLE);
         connectingBtnLly.setVisibility(isContecting && !isSender ? View.VISIBLE : View.INVISIBLE);

         if (isSender){
             videoConnectTipsTv.setText(getString(R.string.waiting_for_receive_video));
         } else {
             videoConnectTipsTv.setText(isVideo ? getString(R.string.invite_video_connect) : getString(R.string.invite_voice_connect));
         }
     }

     /** 对方同意音视频并且建立了连接
     * */
     public void onVideoConnectSuccess(){
         isContecting = false;
         changeUI(isContecting, isSender, isVideo);
     }

    /** 解析房间名获取群id
     *  房间名格式blink-17602060697-135-1556070757
     * */
    private String getGroupId(String rootName) {
        String[] strings = rootName.split("-");
        if (strings.length > 2 && strings[2].length() < 11){    //11位数表示是单独聊天，不是群聊
            return strings[2];
        }
        return "";
    }

     @OnClick(R.id.reject_image_btn)
     public void onRejectVideo(){
         sendRejectMessage();
         getActivity().finish();
     }

    @OnClick(R.id.cancel_btn_tv)
    public void onCancelVideo(){
        ((RoomActivity)getActivity()).sendCancelMessage(mSelf.account, getGroupId(rootName) );
        getActivity().finish();
    }

    @OnClick(R.id.receive_image_btn)
    public void receiveVideoConnect(){
        onVideoConnectSuccess();
        ((RoomActivity)getActivity()).onJoinRoom();
    }

    /** 发送增加了拒绝通话的消息
    * */
    private void sendRejectMessage() {
        Message message = new Message();
        message.id = System.currentTimeMillis();
        message.content = getGroupId(rootName);
        message.sender = mSelf.account;
        message.receiver = accunt;
        message.format = Constant.MessageFormat.FORMAT_TEXT;
        message.extra = "";
        message.action = Constant.MessageAction.ACTION_6;
        message.timestamp = System.currentTimeMillis();
        message.state = Constant.MessageStatus.STATUS_NO_SEND;

        HttpServiceManager.rejectVideoConnectMessage(message);
    }

//    /** 发送增加了取消通话的消息
//     * */
//    private void sendCancelMessage() {
//        Message message = new Message();
//        message.id = System.currentTimeMillis();
//        message.content = getGroupId(rootName);
//        message.sender = mSelf.account;
//        message.receiver = accunt;
//        message.format = Constant.MessageFormat.FORMAT_TEXT;
//        message.extra = "";
//        message.action = Constant.MessageAction.ACTION_7;
//        message.timestamp = System.currentTimeMillis();
//        message.state = Constant.MessageStatus.STATUS_NO_SEND;
//
//        HttpServiceManager.rejectVideoConnectMessage(message);
//    }

    public void startTimer() {
        mTimer.setBase(SystemClock.elapsedRealtime());
        mTimer.start();
    }

    public void stopTimer() {
        mTimer.stop();
    }

    public void updateLocalVideoLogText(String logText) {
        if (mLogView.getVisibility() == View.VISIBLE) {
            mLocalTextViewForVideo.setText(logText);
        }
    }

    public void updateLocalAudioLogText(String logText) {
        if (mLogView.getVisibility() == View.VISIBLE) {
            mLocalTextViewForAudio.setText(logText);
        }
    }

    public void updateRemoteLogText(String logText) {
        if (mRemoteLogText == null) {
            mRemoteLogText = new StringBuffer();
        }
        if (mLogView != null) {
            mRemoteTextView.setText(mRemoteLogText.append(logText + "\n"));
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!mIsVideoEnabled) {
            mCameraSwitchButton.setVisibility(View.INVISIBLE);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallEvents = (OnCallEvents) activity;
    }
}
