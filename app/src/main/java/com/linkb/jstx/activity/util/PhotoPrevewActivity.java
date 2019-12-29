
package com.linkb.jstx.activity.util;

import android.content.res.Configuration;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.component.WebPhotoView;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.R;

import java.io.File;


public class PhotoPrevewActivity extends BaseActivity {

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }
    @Override
    public void initComponents() {
        super.setStatusBarColor(Color.TRANSPARENT);
        setBackIcon(R.drawable.abc_ic_clear_material);
        getWindow().setBackgroundDrawableResource(android.R.color.black);
        WebPhotoView photoView = findViewById(R.id.photoView);
        File file = new File(getIntent().getStringExtra("path"));
        CloudImageLoaderFactory.get().loadAndApply(photoView,file,0);

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_single_photoview;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.send, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_send) {
            setResult(RESULT_OK);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
