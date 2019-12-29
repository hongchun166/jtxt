
package com.linkb.jstx.app;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import java.io.File;

//声音播放
public class GlobalVoicePlayer implements OnCompletionListener {

    private static GlobalVoicePlayer player;
    private MediaPlayer mMediaPlayer;
    private OnPlayListener onPlayListener;
    private String token;

    private GlobalVoicePlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(this);
    }

    public static GlobalVoicePlayer getPlayer() {
        if (player == null) {
            player = new GlobalVoicePlayer();
        }
        return player;
    }

    public void start(File file, OnPlayListener l) {


        if (token != null) {
            if (isPlaying() && file.getAbsolutePath().equals(token)) {
                stop();
                return;
            }
            if (isPlaying() && !file.getAbsolutePath().equals(token) && onPlayListener != null) {
                onPlayListener.onVoiceStop();
            }

        }
        onPlayListener = l;
        token = file.getAbsolutePath();
        start();
    }

    private void start() {
        try {
            if (isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(token);
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        if (isPlaying()) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
        }
        mMediaPlayer = null;
        if (onPlayListener != null) {
            onPlayListener.onVoiceStop();
            onPlayListener = null;
        }
    }

    private boolean isPlaying() {
        try {
            return mMediaPlayer.isPlaying();
        } catch (Exception e) {
        }

        return false;

    }

    public void onSwitchMode() {
        if (token != null) {
            if (isPlaying()) {
                mMediaPlayer.stop();
                mMediaPlayer.release();
            }
            start();
        }

    }

    public void pause() {
        if (token != null) {
            mMediaPlayer.pause();
        }

    }

    @Override
    public void onCompletion(MediaPlayer mediaplayer) {
        if (onPlayListener != null) {
            onPlayListener.onCompletion(mediaplayer);
        }
        token = null;
    }

    public interface OnPlayListener {

        void onCompletion(MediaPlayer mediaplayer);

        void onVoiceStop();
    }

}
