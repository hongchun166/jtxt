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
 *                                                                                      *
 *                         Website :                            *
 *                                                                                      *
 * **************************************************************************************
 */
package com.linkb.jstx.activity;

import android.Manifest;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.LoginBallsView;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.GlobalDatabaseHelper;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.util.MD5;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.linkb.jstx.activity.setting.ServerSettingActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.LoginResult;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.PermissionUtils;

public class LoginOldActivity extends BaseActivity implements TextWatcher, CloudImageApplyListener, HttpRequestListener<LoginResult> {
    private EditText accountEdit;
    private EditText passwordEdit;
    private View loginButton;
    private WebImageView icon;
    private LoginBallsView ballsView;
    private TextWatcher passwordTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            checkAccountAndPassword();
        }

    };

    @Override
    public void initComponents() {
        super.setStatusBarColor(Color.TRANSPARENT);
        super.setWindowFullscreen();
        verifyPermission();
        cancelNotification();
        setDisplayHomeAsUpEnabled(false);
        User self = Global.getCurrentUser();
        ballsView = findViewById(R.id.balls);
        icon = findViewById(R.id.icon);
        findViewById(R.id.label_config).setOnClickListener(this);
        accountEdit = this.findViewById(R.id.account);
        passwordEdit = this.findViewById(R.id.password);
        loginButton = this.findViewById(R.id.next_button);
        loginButton.setOnClickListener(this);
        accountEdit.addTextChangedListener(this);
        passwordEdit.addTextChangedListener(passwordTextWatcher);

        if (self != null) {
            icon.load(FileURLBuilder.getUserIconUrl(FileURLBuilder.getUserIconUrl(self.account)), 200, LoginOldActivity.this);
            accountEdit.setText(self.account);
            passwordEdit.requestFocus();
        }
    }

    private void verifyPermission(){
        PermissionUtils.requestPermissions(this, 1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, new PermissionUtils.OnPermissionListener() {
            @Override
            public void onPermissionGranted() {

            }

            @Override
            public void onPermissionDenied() {
                //权限获取失败，进行你需要的操作
            }
        });
    }

    @Override
    public boolean getSwipeBackEnable(){
        return false;
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_login_old;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.next_button:
                if (!checkServerConfig()) {
                    showToastView(R.string.tip_please_configure_serverinfo);
                    return;
                }

                performLoginRequest();
                break;
            case R.id.label_config:
                startActivity(new Intent(this, ServerSettingActivity.class));
                break;
        }
    }

    private void performLoginRequest() {

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_login)));

        String account = accountEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        HttpServiceManager.login(account,password, android_id,this);
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

        if (!TextUtils.isEmpty(accountEdit.getText().toString().trim())) {
            icon.load(FileURLBuilder.getUserIconUrl(accountEdit.getText().toString().trim()), R.mipmap.lianxiren, 200, LoginOldActivity.this);
        } else {
            icon.setImageResource(R.mipmap.lianxiren);
        }
        checkAccountAndPassword();
    }

    private void checkAccountAndPassword() {
        if ((accountEdit.getText().toString().length() > 0 && passwordEdit.getText().toString().length() > 0)) {
            loginButton.setEnabled(true);
        } else {
            loginButton.setEnabled(false);
        }
    }

    @Override
    public void onHttpRequestSucceed(final LoginResult result, OriginalCall call) {
        hideProgressDialog();
        if (result.isSuccess()) {
            ballsView.runaway();
            Global.removeAccount(new AccountManagerCallback() {
                @Override
                public void run(AccountManagerFuture accountManagerFuture) {
                    User user = result.getData().getUser();
                    user.password = MD5.digest(passwordEdit.getText().toString().trim());
                    Global.addAccount(user);
                    Global.saveAccessToken(result.getToken());
                    handleLogined();
                }
            });
        }
        if (Constant.ReturnCode.CODE_403.equals(result.code)) {
            showToastView(R.string.tip_account_or_password_error);
        }
        if (Constant.ReturnCode.CODE_404.equals(result.code)) {
            showToastView(R.string.tip_account_invailed);
        }
        if (Constant.ReturnCode.CODE_402.equals(result.code)) {
            showToastView(R.string.tip_account_disabled);
        }
    }

    private void handleLogined() {

        GlobalDatabaseHelper.onAccountChanged();
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.activity_appear, R.anim.activity_disappear);
        finish();
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
        showToastView(R.string.tip_conn_server_failed);
    }

    private boolean checkServerConfig() {
        return !(ClientConfig.getServerHost() == null || ClientConfig.getServerPath() == null || ClientConfig.getServerCIMPort() == 0);
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getAction() == null) {
            CIMPushManager.destroy(this);
            android.os.Process.killProcess(android.os.Process.myPid());
        }
        this.finish();
    }

    @Override
    public void onImageApplyFailure(Object key, ImageView target) {
        icon.setImageBitmap(null);
    }

    @Override
    public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
        icon.startAnimation(AnimationUtils.loadAnimation(this, R.anim.appear));
    }

    private void cancelNotification() {
        ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).cancelAll();
    }

}
