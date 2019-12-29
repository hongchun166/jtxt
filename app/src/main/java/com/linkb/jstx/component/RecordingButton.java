
package com.linkb.jstx.component;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Handler;
import android.support.v7.widget.ViewStubCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.R;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.network.model.ChatVoice;
import com.linkb.jstx.util.StringUtils;

import org.apache.commons.io.FileUtils;

import java.io.File;


public class RecordingButton extends android.support.v7.widget.AppCompatTextView {

    private long startTime;
    private RecordingHintView recordingHintView;
    private long endTime;
    private MediaRecorder mediaRecorder;
    private File voiceFile;
    private OnRecordCompletedListener onRecordCompletedListener;
    private Handler recordHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {

            long t = System.currentTimeMillis();
            int seconds = (int) ((t - startTime) / 1000);
            if (seconds >= 60 * 3) {
                endTime = t;
                recordCompleted(startTime > 0);
            } else {
                if (seconds < 10) {
                    recordingHintView.setTimeText("00:0" + seconds);
                } else {
                    recordingHintView.setTimeText("00:" + seconds);
                }
            }
            if (recordingHintView.getRecording()) {
                recordHandler.sendMessageDelayed(recordHandler.obtainMessage(), 1000);
            }
        }
    };

    public RecordingButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public void setOnRecordCompletedListener(OnRecordCompletedListener onRecordCompletedListener) {
        this.onRecordCompletedListener = onRecordCompletedListener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }

    private void inintHintView() {
        if (recordingHintView == null) {
            View view = ViewStubCompat.inflate(getContext(), R.layout.layout_chat_recording_panel, (ViewGroup) getParent().getParent().getParent());
            recordingHintView = view.findViewById(R.id.recordingHintView);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:


                inintHintView();

                if (isTouchInside(event)) {

                    recordingHintView.setRecording(true);
                    recordHandler.sendMessage(recordHandler.obtainMessage());
                    setSelected(true);
                    setText(R.string.label_chat_soundrecord_send);
                    recordingHintView.setHintText(R.string.label_chat_soundcancle);
                    try {

                        recordingHintView.setVisibility(View.VISIBLE);
                        mediaRecorder = new MediaRecorder();
                        voiceFile = new File(LvxinApplication.CACHE_DIR_VOICE, StringUtils.getUUID() +".mp3" );
                        voiceFile.createNewFile();
                        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
                        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                        mediaRecorder.setOutputFile(voiceFile.getAbsolutePath());
                        mediaRecorder.prepare();
                        mediaRecorder.start();

                        startTime = System.currentTimeMillis();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:

                if (recordingHintView.getRecording()) {
                    if (!isTouchInside(event)) {
                        recordingHintView.setTouchOutSide(true);
                        setText(R.string.label_chat_soundrecord_abort);
                    } else {
                        recordingHintView.setTouchOutSide(false);
                        setSelected(true);
                        setText(R.string.label_chat_soundrecord_send);
                    }
                }
                break;

            case MotionEvent.ACTION_CANCEL:
                endRecordHandle(event);
                break;
            case MotionEvent.ACTION_UP:
                endRecordHandle(event);
                break;

        }
        return super.dispatchTouchEvent(event);
    }

    private void endRecordHandle(MotionEvent event) {
        endTime = System.currentTimeMillis();
        recordCompleted(isTouchInside(event) && (endTime - startTime) / 1000 > 0 && startTime > 0);
    }

    private void recordCompleted(boolean isValid) {
        if (recordingHintView.getRecording()) {

            try {
                mediaRecorder.stop();
                mediaRecorder.release();
            } catch (Exception e) {
            }
            setSelected(false);
            recordingHintView.setVisibility(View.GONE);
            setText(R.string.label_chat_soundrecord_normal);
            recordingHintView.setTimeText("00:00");
            if (isValid) {
                // 消息内容为 语音时长
                ChatVoice chatVoice = new ChatVoice();
                chatVoice.length = (endTime - startTime) / 1000;
                chatVoice.file = voiceFile.getName();
                onRecordCompletedListener.onRecordCompleted(chatVoice);
            } else {
                FileUtils.deleteQuietly(voiceFile);
            }
        }

        recordingHintView.setRecording(false);


    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        recordHandler.removeCallbacksAndMessages(null);
    }

    private boolean isTouchInside(MotionEvent event) {
        int[] loc = new int[2];
        getLocationInWindow(loc);
        return event.getRawY() >= loc[1] && event.getRawY() <= loc[1] + getHeight()
                && event.getRawX() >= loc[0] && event.getRawX() <= loc[0] + getWidth();
    }


    public interface OnRecordCompletedListener {
        void onRecordCompleted(ChatVoice voice);
    }


}
