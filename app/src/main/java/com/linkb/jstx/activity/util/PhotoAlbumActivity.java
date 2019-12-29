
package com.linkb.jstx.activity.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.ImageChooseViewAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.bean.Bucket;
import com.linkb.jstx.component.AlbumPaddingDecoration;
import com.linkb.jstx.dialog.AlbumBucketWindow;
import com.linkb.jstx.listener.OnItemCheckedListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.util.AlbumPhotoLoader;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.app.Global;
import com.linkb.R;
import com.linkb.jstx.util.AlbumBucketLoader;

import java.io.File;
import java.util.List;
import java.util.Objects;


public class PhotoAlbumActivity extends BaseActivity implements OnItemCheckedListener, OnItemClickedListener {

    public final static String KEY_MAX_COUNT = "KEY_MAX_COUNT";
    public static final int REQUEST_CODE_ONE = 1287;
    public static final int REQUEST_CODE_MULT = 1587;
    private ImageChooseViewAdapter adapter;
    private RecyclerView recyclerView;
    private float density;
    private Button button;
    private int maxCount;
    private AlbumBucketWindow bucketWindow;
    @Override
    public void initComponents() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new AlbumPaddingDecoration());
        maxCount = getIntent().getIntExtra(KEY_MAX_COUNT, 1);
        adapter = new ImageChooseViewAdapter(getIntent().getAction(),maxCount);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemCheckedListener(this);
        adapter.setOnItemClickedListener(this);
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        density = mDisplayMetrics.density;
        bucketWindow = new AlbumBucketWindow(this, this);
        searchImageList();
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            onBackResultAndFinish();
        }
    }


    private void searchImageList() {
        loadAlbumPhoto(null);
        loadAlbumDir();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_image_choice;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_album;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (Objects.equals(Constant.Action.ACTION_MULTIPLE_PHOTO_SELECTOR,getIntent().getAction())) {
            getMenuInflater().inflate(R.menu.menu_album_multiple, menu);
            button = menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
            button.setOnClickListener(this);
            button.setText(R.string.common_confirm);
            button.setEnabled(false);
        } else {
            getMenuInflater().inflate(R.menu.menu_album, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onItemChecked(Object file, View view, boolean checked) {


        if (adapter.getSelectedSize() > 0) {
            button.setEnabled(true);
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
            button.setText(getString(R.string.label_album_selected_count, adapter.getSelectedSize() + "/" + maxCount));
        } else {
            button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            button.setEnabled(false);
            button.setText(R.string.common_confirm);
        }

    }

    private void loadAlbumPhoto(String bucket) {


        List<Uri> list = AlbumPhotoLoader.newInstance(this, bucket).syncLoadList();
        adapter.clear();
        adapter.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void loadAlbumDir() {
        bucketWindow.setAlbumBucketList(new AlbumBucketLoader(this).syncLoadList());
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        if (obj instanceof Uri) {
            onBackResultAndFinish(new File(obj.toString()));
        }
        if (obj instanceof Bucket) {
            bucketWindow.dismiss();
            loadAlbumPhoto(((Bucket) obj).id);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_camera) {
            AppTools.startCameraActivity(this);
        }
        if (item.getItemId() == R.id.menu_bucket) {
            bucketWindow.show();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == Constant.RESULT_CAMERA) {
            File photo = new File(Global.getPhotoGraphFilePath());
            onBackResultAndFinish(photo);
        }
    }

    private void onBackResultAndFinish(File file) {
        Intent intent = new Intent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = AppTools.getUriFromFile(file);
            intent.setData(contentUri);
        } else {
            intent.setData(Uri.fromFile(file));
        }
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void onBackResultAndFinish() {
        Intent intent = new Intent();
        intent.putExtra("files", adapter.getFinalSelectedFiles());
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
