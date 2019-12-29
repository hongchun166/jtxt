
package com.linkb.jstx.activity.util;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.MediaController;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.CircleProgressView;
import com.linkb.jstx.component.TextureVideoView;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.listener.OSSFileDownloadListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.network.CloudFileDownloader;
import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class VideoPlayerActivity extends BaseActivity implements OnItemClickedListener, OSSFileDownloadListener, MediaPlayer.OnPreparedListener, CloudImageApplyListener {
    private TextureVideoView videoView;
    private CircleProgressView progressView;
    private WebImageView thumbnailView;
    private SNSVideo video;
    private String url;
    private File videoFile;

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }
    @Override
    public void initComponents() {
//        supportPostponeEnterTransition();
        setBackIcon(R.drawable.abc_ic_clear_material);
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        videoView = findViewById(R.id.videoView);
        thumbnailView = findViewById(R.id.thumbnailView);
        progressView = findViewById(R.id.progressView);
        videoView.setMediaController(new MediaController(this));
        videoView.setOnPreparedListener(this);
        this.video = (SNSVideo) getIntent().getSerializableExtra(SNSVideo.class.getName());

        initVideo();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_video_player;
    }

    private void initVideo() {
        videoFile = new File(LvxinApplication.CACHE_DIR_VIDEO, video.video);
        File thumbnailFile = new File(LvxinApplication.CACHE_DIR_VIDEO, video.image);

        if (thumbnailFile.exists()) {
            thumbnailView.load(Uri.fromFile(thumbnailFile).toString(), this);
        } else {
            String url = FileURLBuilder.getFileUrl(video.getBucket(),video.image);
            thumbnailView.load(url, this);
        }
    }

    private void downloadVideoFile(String url, File file) {
        this.url = url;
        progressView.setVisibility(View.VISIBLE);
        CloudFileDownloader.asyncDownload(url, file, this);
    }

    private void startPlayVideo() {
        videoView.setVideoURI(Uri.fromFile(videoFile));
        progressView.setVisibility(View.GONE);
        videoView.start();
    }


    @Override
    public void onItemClicked(Object obj, View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        videoView.stopPlayback();
        videoView.setVisibility(View.GONE);
        CloudFileDownloader.stop(url);
        supportFinishAfterTransition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean buttonVisable = getIntent().getBooleanExtra("buttonVisable", false);
        if (buttonVisable) {
            getMenuInflater().inflate(R.menu.send, menu);
        } else {
            getMenuInflater().inflate(R.menu.download, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_download) {
            downloadAndNotify();
        }
        if (item.getItemId() == R.id.menu_send) {
            setResult(RESULT_OK);
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }



    private void downloadAndNotify() {

        try {
            File desFile = new File(Constant.SYSTEM_LVXIN_DIR, videoFile.getName());
            FileUtils.copyFile(videoFile, desFile);
            showToastView(getString(R.string.tip_photo_video_download_complete));

            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(desFile));
            LvxinApplication.sendGlobalBroadcast(intent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDownloadCompleted(File file, String currentKey) {
        startPlayVideo();
    }

    @Override
    public void onDownloadFailured(File file, String currentKey) {
        progressView.loadError();
        showToastView(getResources().getString(R.string.video_source_delected));
    }

    @Override
    public void onDownloadProgress(String key, float progress) {
        progressView.setVisibility(View.VISIBLE);
        progressView.setProgress((int) progress);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        thumbnailView.postDelayed(new Runnable() {
            @Override
            public void run() {
                toggleThumbVideoView();
            }
        }, 300);
    }

    private void toggleThumbVideoView() {
        videoView.setAlpha(1f);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.disappear);
        animation.setFillAfter(true);
        animation.setDuration(0);
        thumbnailView.startAnimation(animation);
    }

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        onImageApplySucceed(key,target,null);
    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        supportStartPostponedEnterTransition();
        if (videoFile.exists() && videoFile.length() == video.size) {
            startPlayVideo();
            return;
        }
        if (videoFile.exists() && videoFile.length() > video.size) {
            videoFile.delete();
        }
        downloadVideoFile(FileURLBuilder.getFileUrl(video.getBucket(),video.video), videoFile);

    }
}