
package com.linkb.jstx.activity.util;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.WebPhotoView;
import com.linkb.jstx.network.model.SNSImage;
import com.linkb.jstx.util.BitmapUtils;
import com.linkb.jstx.util.StringUtils;
import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.R;
import com.linkb.jstx.util.BackgroundThreadHandler;
import com.linkb.jstx.util.FileURLBuilder;
import com.github.chrisbanes.photoview.OnPhotoTapListener;

import java.io.File;
import java.util.Objects;


public class PhotoVewActivity extends BaseActivity implements OnPhotoTapListener,   CloudImageApplyListener {
    private SNSImage snsImage;
    private ProgressBar progressbar;
    private WebPhotoView photoView;
    private Bitmap mBitmap;

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }
    @Override
    public void initComponents() {
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        supportPostponeEnterTransition();
        setBackIcon(R.drawable.abc_ic_clear_material);
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        photoView = findViewById(R.id.photoView);
        progressbar = findViewById(R.id.progress);
        photoView.setOnPhotoTapListener(this);
        snsImage = (SNSImage) getIntent().getSerializableExtra(SNSImage.class.getName());
        if (snsImage == null) {
            display(getIntent().getStringExtra("url"));
        } else {
            display(snsImage);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void display(final SNSImage image) {
        final String thumbnailUrl = getImageUrl(image.image);
        this.display(thumbnailUrl);
    }

    private void display(String imageUrl) {
        photoView.display(imageUrl, this);
    }

    @Override
    public void onPhotoTap(ImageView imageView, float v, float v1) {
        onBackPressed();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_single_photoview;
    }


    private String getImageUrl(String key){
        return FileURLBuilder.getFileUrl(snsImage.getBucket(),key);
    }

    @Override
    public void onBackPressed() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        BackgroundThreadHandler.postUIThreadDelayed(new Runnable() {
            @Override
            public void run() {
                supportFinishAfterTransition();
            }
        },16);
    }

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        if (isDestroyed()) {
            return;
        }
        if (snsImage == null){
            progressbar.setVisibility(View.GONE);
            supportStartPostponedEnterTransition();
            return;
        }

        if (Objects.equals(key,getImageUrl(snsImage.thumb))){
            supportStartPostponedEnterTransition();
            photoView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final String imageUrl = getImageUrl(snsImage.image);
                    progressbar.setVisibility(View.VISIBLE);
                    photoView.display(imageUrl,PhotoVewActivity.this);
                }
            },500);
        }else {
            progressbar.setVisibility(View.GONE);
            photoView.setImageResource(R.drawable.picture_def);
        }
    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target,final BitmapDrawable resource) {
        if (isDestroyed()) {
            return;
        }


        if (snsImage == null){
            mBitmap = resource.getBitmap();
            progressbar.setVisibility(View.GONE);
            supportStartPostponedEnterTransition();
            return;
        }

        if (Objects.equals(key,getImageUrl(snsImage.thumb))){
            supportStartPostponedEnterTransition();
            photoView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final String imageUrl = getImageUrl(snsImage.image);
                    progressbar.setVisibility(View.VISIBLE);
                    photoView.display(imageUrl, resource.getBitmap(),PhotoVewActivity.this);
                }
            },500);
        }else {
            progressbar.setVisibility(View.GONE);
            mBitmap = resource.getBitmap();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.download, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_download) {
            downloadAndNotify();
        }
        return super.onOptionsItemSelected(item);
    }



    private void downloadAndNotify() {

        if (mBitmap == null) {
            return;
        }
        File desFile = new File(Constant.SYSTEM_LVXIN_DIR, (snsImage == null ? StringUtils.getUUID() :snsImage.image) + ".jpg");
        BitmapUtils.savePhotoBitmap2File(mBitmap, desFile);
        showToastView(getString(R.string.tip_photo_video_download_complete));

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(desFile));
        LvxinApplication.sendGlobalBroadcast(intent);
    }

}
