package com.linkb.jstx.activity.trend;

import android.content.Intent;
import android.net.Uri;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameDetailActivity extends BaseActivity {


    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_game_detail;
    }

    @OnClick(R.id.back_btn)
    public void onBack(){ finish();}

    @OnClick(R.id.download_button)
    public void onDownload(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(BuildConfig.GAME_DOWNLOAD_URL));
        startActivity(intent);
    }
}
