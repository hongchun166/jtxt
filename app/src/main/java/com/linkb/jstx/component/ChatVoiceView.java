
package com.linkb.jstx.component;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.jstx.app.GlobalMediaPlayer;
import com.linkb.jstx.app.GlobalVoicePlayer;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.network.CloudFileDownloader;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.listener.OSSFileDownloadListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.model.ChatVoice;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

import java.io.File;
import java.util.Objects;


public class ChatVoiceView extends FrameLayout implements OnClickListener, GlobalVoicePlayer.OnPlayListener, OSSFileDownloadListener {
    private Message message;
    private File voiceFile;
    private int mMaxWidth;
    private TextView lengthTextView;
    private ChatVoice chatVoice;
    private ValueAnimator loadingAnimator;

    public ChatVoiceView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        float scale = context.getResources().getDisplayMetrics().density;
        mMaxWidth = (int) (Resources.getSystem().getDisplayMetrics().widthPixels - (160 * scale + 0.5f));
    }


    public void display(Message msg, boolean self) {
        this.message = msg;
        chatVoice = new Gson().fromJson(message.content, ChatVoice.class);
        if (getChildCount() == 0) {
            LayoutInflater.from(getContext()).inflate(self ? R.layout.layout_chat_voice_item_self : R.layout.layout_chat_voice_item_other, this);
            lengthTextView = this.findViewById(R.id.lengthTextView);
        }
        loadVoiceFile();
        lengthTextView.setText(chatVoice.length + "\"");
        lengthTextView.setTextColor(self ? Color.WHITE : Color.LTGRAY);
        int normalWidth = getContext().getResources().getDimensionPixelOffset(R.dimen.chat_voice_width);
        int realWidth = normalWidth + (int) ((mMaxWidth - normalWidth) * (chatVoice.length / 60f));
        this.getLayoutParams().width = Math.min(realWidth, mMaxWidth);
    }


    /**
     * 设置download url，开始下载
     */
    private void loadVoiceFile() {

        if (chatVoice.file == null) {
            return;
        }
        this.setTag(chatVoice.file);
        voiceFile = new File(LvxinApplication.CACHE_DIR_VOICE, chatVoice.file);
        if (!voiceFile.exists()) {
            setOnClickListener(null);
            loadingAnimator = ObjectAnimator.ofFloat(this, "alpha", 0.5F, 1, 0.5F);
            loadingAnimator.setDuration(1200);
            loadingAnimator.setRepeatCount(ValueAnimator.INFINITE);
            loadingAnimator.setRepeatMode(ValueAnimator.RESTART);
            loadingAnimator.start();
            findViewById(R.id.waveformImage).setVisibility(View.GONE);
            lengthTextView.setVisibility(GONE);
            CloudFileDownloader.asyncDownload(FileURLBuilder.BUCKET_CHAT, chatVoice.file, LvxinApplication.CACHE_DIR_VOICE, this);
        }else {
            setOnClickListener(this);
            cancelLoadingAnimator();
        }
    }

    private void  cancelLoadingAnimator(){

        setAlpha(1F);
        findViewById(R.id.waveformImage).setVisibility(View.VISIBLE);
        lengthTextView.setVisibility(VISIBLE);
        if (loadingAnimator!=null && loadingAnimator.isRunning()){
            loadingAnimator.cancel();
        }
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        cancelLoadingAnimator();
    }

    @Override
    public void onClick(View v) {
        if (voiceFile.exists()) {
            findViewById(R.id.waveformImage).setVisibility(View.GONE);
            findViewById(R.id.waveformAnim).setVisibility(View.VISIBLE);
            ((AnimationDrawable) ((ImageView) findViewById(R.id.waveformAnim)).getDrawable()).start();
            GlobalVoicePlayer.getPlayer().start(voiceFile, this);
        }
    }


    @Override
    public void onCompletion(MediaPlayer mp) {
        GlobalMediaPlayer.play(R.raw.play_completed);
        onVoiceStop();
    }

    @Override
    public void onVoiceStop() {
        findViewById(R.id.waveformImage).setVisibility(View.VISIBLE);
        findViewById(R.id.waveformAnim).setVisibility(View.GONE);
        ((AnimationDrawable) ((ImageView) findViewById(R.id.waveformAnim)).getDrawable()).stop();

        if (getParent() instanceof FromMessageVoiceView && !Objects.equals(message.state,Message.STATUS_READ_OF_VOICE)) {
            message.handle = Message.STATUS_READ_OF_VOICE;
            MessageRepository.updateHandleState(message.id,Message.STATUS_READ_OF_VOICE);
            ((FromMessageVoiceView) getParent()).hideReadDot();
            HttpServiceManager.read(message);
        }
    }


    @Override
    public void onDownloadCompleted(File file, String currentKey) {
        Object listView = this.getParent().getParent();
        if (listView != null) {
            Object target = ((View) listView).findViewWithTag(currentKey);
            if (target instanceof ChatVoiceView) {
                ChatVoiceView chatVoiceView = (ChatVoiceView) target;
                chatVoiceView.cancelLoadingAnimator();
                chatVoiceView.setOnClickListener(chatVoiceView);
            }
        }
    }

    @Override
    public void onDownloadFailured(File file, String currentKey) {
        setOnClickListener(this);
        cancelLoadingAnimator();
    }

    @Override
    public void onDownloadProgress(String key, float progress) {
    }

}
