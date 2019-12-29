
package com.linkb.jstx.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.R;

public class SplashActivity extends BaseActivity {
    private User user;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0x55){
                if (user == null) {
                    Intent intent = new Intent(SplashActivity.this, LoginActivityV2.class);
                    intent.setAction(SplashActivity.class.getName());
                    startActivity(intent);
                }else {
                    startActivity(new Intent(SplashActivity.this, user.password == null ? LoginActivityV2.class : HomeActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                finish();
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setWindowFullscreen();
        super.onCreate(savedInstanceState);
    }
    @Override
    public void initComponents() {
        user = Global.getCurrentUser();
        handler.sendEmptyMessageDelayed(0x55, 1500);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_splash_v2;
    }


    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }

    @Override
    public void onClick(View v) {
        // TODO: 2019/3/26  
        Intent intent = new Intent(SplashActivity.this, LoginActivityV2.class);
        intent.setAction(SplashActivity.class.getName());
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        CIMPushManager.destroy(this);
        CloudImageLoaderFactory.get().clearMemory();
        super.onBackPressed();
    }
}
