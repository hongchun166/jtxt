
package com.linkb.jstx.app;

import android.media.MediaPlayer;

//声音播放
public class GlobalMediaPlayer {

    public static void play(int rid) {
        try {
            final MediaPlayer mMediaPlayer = MediaPlayer.create(LvxinApplication.getInstance(), rid);
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mMediaPlayer.release();
                }
            });
            mMediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
