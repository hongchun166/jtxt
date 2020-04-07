/**
 * Copyright 2013-2023 Xia Jun(3979434@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <p>
 * **************************************************************************************
 * *
 *                         Website :                            *
 * *
 * **************************************************************************************
 */
package com.linkb.jstx.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.alive.JobSchedulerManager;
import com.linkb.alive.ScreenManager;
import com.linkb.alive.ScreenReceiverUtil;
import com.linkb.jstx.activity.base.CIMMonitorActivity;
import com.linkb.jstx.activity.contact.ContactsFragment;
import com.linkb.jstx.activity.contact.SearchActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.dialog.BetterySavingDialog;
import com.linkb.jstx.fragment.RegisterGiftFragment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.network.result.LoginResult;
import com.linkb.jstx.network.result.QueryMineGroupResult;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

public class HomeActivity extends CIMMonitorActivity implements OnTabChangeListener {

    private View mConversationTab;
    private View mContactsTab;
    private View mDiscoverTab;
    private View mSettingTab;
    private TabHost mHost;

    private LoginResult.DataBean mDataBean;
    private RegisterGiftFragment mRegisterGiftFragment;
    private boolean enableGift = true;

    // 动态注册锁屏等广播
    private ScreenReceiverUtil mScreenListener;
    // 1像素Activity管理类
    private ScreenManager mScreenManager;
    // JobService，执行系统任务
    private JobSchedulerManager mJobManager;

    private ScreenReceiverUtil.SreenStateListener mScreenListenerer = new ScreenReceiverUtil.SreenStateListener() {
        @Override
        public void onSreenOn() {
            // 亮屏，移除"1像素"
            mScreenManager.finishActivity();
        }

        @Override
        public void onSreenOff() {
            // 接到锁屏广播，将HomeActivity切换到可见模式
            mScreenManager.startActivity();
        }

        @Override
        public void onUserPresent() {
            // 解锁，暂不用，保留
        }
    };

    @Override
    public void initComponents() {
        uploadCrashReport();

        mDataBean = (LoginResult.DataBean) getIntent().getSerializableExtra(LoginResult.DataBean.class.getName());
        if (mDataBean != null && enableGift) showFragment(mDataBean.getIsLoginFlag().equals("0"));

        hideNavicationBar();
        setDisplayHomeAsUpEnabled(false);
        mHost = this.findViewById(android.R.id.tabhost);
        mHost.setup();
        mHost.setOnTabChangedListener(this);
        initBottomTabs();

//        toolbar.setOnClickListener(this);

        ((TextView) getConversationTab().findViewWithTag("TAB_NAME")).setText(R.string.common_message);
        ((TextView) getContactsTab().findViewWithTag("TAB_NAME")).setText(R.string.common_contacts);
        ((TextView) getDiscoverTab().findViewWithTag("TAB_NAME")).setText(R.string.common_trend);
        ((TextView) getSettingTab().findViewWithTag("TAB_NAME")).setText(R.string.common_my);
        mHost.addTab(mHost.newTabSpec(getString(R.string.common_message)).setIndicator(mConversationTab).setContent(R.id.conversationFragment));
        mHost.addTab(mHost.newTabSpec(getString(R.string.common_contacts)).setIndicator(mContactsTab).setContent(R.id.contactFragment));
        mHost.addTab(mHost.newTabSpec(getString(R.string.common_trend)).setIndicator(mDiscoverTab).setContent(R.id.trendCenterFragment));
        mHost.addTab(mHost.newTabSpec(getString(R.string.common_my)).setIndicator(mSettingTab).setContent(R.id.settingCenterFragment));

//        initApplicationFeatures();

        loadMineGroupInfo();

        collectDiviceInfo();

        // 1. 注册锁屏广播监听器
        mScreenListener = new ScreenReceiverUtil(this);
        mScreenManager = ScreenManager.getScreenManagerInstance(this);
        mScreenListener.setScreenReceiverListener(mScreenListenerer);
        // 2. 启动系统任务
        mJobManager = JobSchedulerManager.getJobSchedulerInstance(this);
        mJobManager.startJobScheduler();

    }

    @Override
    public void onResume() {
        super.onResume();
        LvxinApplication.getInstance().startCheckVersionService();
    }

    /** 上传崩溃日志
    * */
    private void uploadCrashReport() {

        File file = new File(this.getFilesDir() + "/tombstones");
        File[] files = file.listFiles();
        if (files == null || files.length < 1) {
            Log.e("crash", "No Crash repoet");
            return;
        }

        for (int i = 0; i < files.length; i++) {
            LvxinApplication.getInstance().startUploadCrashReport(files[i]);
        }

    }


    /** 查询我的群组资料
    * */
    private void loadMineGroupInfo() {
        HttpServiceManager.queryPersonGroup(listener);
    }

    /** 查询我的好友资料
    * */
    private void loadMineFriendInfo() {
    }

    private HttpRequestListener<QueryMineGroupResult> listener = new HttpRequestListener<QueryMineGroupResult>() {
        @Override
        public void onHttpRequestSucceed(QueryMineGroupResult result, OriginalCall call) {
            if (result.isSuccess()){
                GroupRepository.saveAll(result.getDataList());
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };


    private void showFragment(boolean enableShow){
//        if (enableShow){
//            enableGift = false;
//            if (mRegisterGiftFragment == null) {
//                mRegisterGiftFragment = RegisterGiftFragment.getInstance(mDataBean);
//            }
//            mRegisterGiftFragment.show(HomeActivity.this.getSupportFragmentManager(), "RegisterGiftFragment");
//        }
    }


    @TargetApi(Build.VERSION_CODES.M)
    private void initApplicationFeatures() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            boolean hasIgnored = powerManager.isIgnoringBatteryOptimizations(getPackageName());
            if(!hasIgnored) {
                BetterySavingDialog.create(this).show();
            }

            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
            if (!EasyPermissions.hasPermissions(this,permissions)){
                ActivityCompat.requestPermissions(this,permissions,0);
            }
        }
    }

    @Override
    public void onThemeChanged() {
        super.onThemeChanged();
        initBottomTabs();
    }

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }

    private void initBottomTabs() {
        ((ImageView) getConversationTab().findViewWithTag("TAB_ICON")).setImageResource(R.drawable.tab_message_selector);
        ((ImageView) getContactsTab().findViewWithTag("TAB_ICON")).setImageResource(R.drawable.tab_contacts_selector);
        ((ImageView) getDiscoverTab().findViewWithTag("TAB_ICON")).setImageResource(R.drawable.tab_trend_selector);
        ((ImageView) getSettingTab().findViewWithTag("TAB_ICON")).setImageResource(R.drawable.tab_my_selector);
        ((TextView) getConversationTab().findViewWithTag("TAB_NAME")).setTextColor(ContextCompat.getColorStateList(this, R.color.tab_text_selector));
        ((TextView) getContactsTab().findViewWithTag("TAB_NAME")).setTextColor(ContextCompat.getColorStateList(this, R.color.tab_text_selector));
        ((TextView) getDiscoverTab().findViewWithTag("TAB_NAME")).setTextColor(ContextCompat.getColorStateList(this, R.color.tab_text_selector));
        ((TextView) getSettingTab().findViewWithTag("TAB_NAME")).setTextColor(ContextCompat.getColorStateList(this, R.color.tab_text_selector));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_home;
    }



//    @Override
//    public void onBackPressed() {

//        this.moveTaskToBack(true);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        startActivity(new Intent(this, SearchActivity.class));
        return super.onOptionsItemSelected(item);
    }


    public View getConversationTab() {
        if (mConversationTab == null) {
            mConversationTab = LayoutInflater.from(this).inflate(R.layout.layout_home_tab_item, null);
        }
        return mConversationTab;
    }

    private View getContactsTab() {
        if (mContactsTab == null) {
            mContactsTab = LayoutInflater.from(this).inflate(R.layout.layout_home_tab_item, null);
        }
        return mContactsTab;
    }

    public View getDiscoverTab() {
        if (mDiscoverTab == null) {
            mDiscoverTab = LayoutInflater.from(this).inflate(R.layout.layout_home_tab_item, null);
        }
        return mDiscoverTab;
    }

    private View getSettingTab() {
        if (mSettingTab == null) {
            mSettingTab = LayoutInflater.from(this).inflate(R.layout.layout_home_tab_item, null);
        }
        return mSettingTab;
    }

    @Override
    public void onTabChanged(String tag) {
        setToolbarTitle(tag);
    }

    @Override
    public void onClick(View v) {
        if (v == toolbar && mHost.getCurrentTab() ==1){
            ContactsFragment contactsFragment = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.contactFragment);
//            contactsFragment.onTabLongClicked();
        }
    }

    /** 处理intent事项
    * */
    private void handleIntent(Intent intent) {
        if (intent == null) return;
        changePage(0);
    }

    /** 切换页面
    * */
    private void changePage(int index) {
        mHost.setCurrentTab(index);
    }

    /** 搜集登录信息
    * */
    private void collectDiviceInfo(){
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        HttpServiceManager.setGroupCheckMenberInfo(android_id, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {

            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }
}
