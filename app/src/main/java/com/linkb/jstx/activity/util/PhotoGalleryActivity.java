
package com.linkb.jstx.activity.util;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.app.SharedElementCallback;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.linkb.jstx.component.GalleryViewPager;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.GalleryPhotoViewAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.ProgressbarPhotoView;
import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.network.model.SNSImage;
import com.linkb.R;
import com.linkb.jstx.util.BackgroundThreadHandler;
import com.linkb.jstx.util.BitmapUtils;
import com.linkb.jstx.util.FileURLBuilder;

import java.io.File;
import java.util.List;
import java.util.Map;


public class PhotoGalleryActivity extends BaseActivity implements ViewPager.OnPageChangeListener, OnItemClickedListener, CloudImageApplyListener {
    private GalleryViewPager viewPager;
    private GalleryPhotoViewAdapter adapter;
    private LinearLayout tagPanel;
    private int selectedIndex = 0;
    private boolean enterAnim = false;
    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }
    @Override
    public void initComponents() {
//        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
//        supportPostponeEnterTransition();
        setBackIcon(R.drawable.abc_ic_clear_material);
        viewPager = findViewById(R.id.viewPager);
        tagPanel = findViewById(R.id.ViewPagerTagPanel);
        List<SNSImage> ossImages = (List<SNSImage>) getIntent().getSerializableExtra(SNSImage.class.getName());
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        buildPagerIndicator(ossImages);
        adapter = new GalleryPhotoViewAdapter(ossImages);
        adapter.setOnItemClickedListener(this);
        adapter.setCloudImageApplyListener(this);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(this);

        viewPager.setOffscreenPageLimit(2);
        tagPanel.getChildAt(0).setSelected(true);

        /** 这里会触发Bug,暂时隐藏
        * */
//        setEnterSharedElementCallback(sharedElementCallback);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    private void buildPagerIndicator(List<SNSImage> ossImages) {
        selectedIndex = 0;
        for (int i = 0; i < ossImages.size(); i++) {
            ImageView tag = new ImageView(this);
            tag.setImageResource(R.drawable.icon_pager_tag);
            tag.setPadding(12, 0, 0, 0);
            tagPanel.addView(tag);
        }
    }


    @Override
    public void onPageScrollStateChanged(int arg0) {
    }


    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }


    @Override
    public void onPageSelected(int index) {
        tagPanel.getChildAt(selectedIndex).setSelected(false);
        tagPanel.getChildAt(index).setSelected(true);
        selectedIndex = index;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_gallery_photoview;
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
    public void onItemClicked(Object obj, View view) {
        onBackPressed();
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
        ProgressbarPhotoView photoView = viewPager.findViewWithTag(viewPager.getCurrentItem());
        if (photoView.getBitmap() == null) {
            return;
        }
        File desFile = new File(Constant.SYSTEM_LVXIN_DIR, System.currentTimeMillis() + ".jpg");
        BitmapUtils.savePhotoBitmap2File(photoView.getBitmap(), desFile);
        showToastView(getString(R.string.tip_photo_video_download_complete));

        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(Uri.fromFile(desFile));
        LvxinApplication.sendGlobalBroadcast(intent);
    }

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        onImageApplySucceed(key,target,null);
    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
       if (!enterAnim){
           enterAnim = true;

           supportStartPostponedEnterTransition();
       }
    }

    @Override
    public void finishAfterTransition() {
        Intent data = new Intent();
        SNSImage snsImage = adapter.getItem(selectedIndex);
        data.putExtra("url", FileURLBuilder.getFileUrl(snsImage.getBucket(),snsImage.thumb));
        setResult(RESULT_OK, data);
        super.finishAfterTransition();
    }



    private final SharedElementCallback sharedElementCallback = new SharedElementCallback() {
        @Override
        public void onMapSharedElements(List<String> names, Map<String, View> sharedElements) {
            SNSImage snsImage = adapter.getItem(selectedIndex);
            String url = FileURLBuilder.getFileUrl(snsImage.getBucket(),snsImage.thumb);
            ImageView sharedElement =viewPager.findViewWithTag(url);
            names.clear();
            names.add(sharedElement.getTransitionName());
            sharedElements.clear();
            sharedElements.put(sharedElement.getTransitionName(), sharedElement);
        }
    };


}
