package com.linkb.jstx.activity;

import android.Manifest;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.GlobalDatabaseHelper;
import com.linkb.jstx.fragment.MutilLanguageDialogFragment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.LoginResult;
import com.linkb.jstx.util.LocalManageUtil;
import com.linkb.jstx.util.MD5;
import com.linkb.jstx.util.PermissionUtils;
import com.linkb.jstx.util.RegexUtil;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 邮箱/手机登录页面
* */
public class LoginActivityV2 extends BaseActivity  {

    @BindView(R.id.editText_account) EditText accountEdt;
    @BindView(R.id.editText_password) EditText passwordEdt;

    @BindView(R.id.next_button) Button nextStepBtn;

    @BindView(R.id.password_visible_image) ImageView passwordVisibleImg;
    @BindView(R.id.viewCountryCode)
    TextView viewCountryCode;

    /** 是邮箱登录还是 手机登录
     * */
    private Boolean enableEmailLogin = false;
    /** 密码可视状态
    * */
    private Boolean passwordVisibleStatus = false;

    private String account;
    private String password;

    protected long lastBackClickTime;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        verifyPermission();

        changeUI(enableEmailLogin);

        boolean enableFirstLogin = ClientConfig.getEnableFirstLogin();
        if (enableFirstLogin) {
            ClientConfig.setEnableFirstLogin(false);
            try {
                URL url = new URL(BuildConfig.API_HOST);
                ClientConfig.setServerHost(url.getHost());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            ClientConfig.setServerPath(BuildConfig.API_HOST);
            ClientConfig.setServerCIMPort(Integer.parseInt(BuildConfig.CIM_PORT));
        }

        passwordEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)){
                    nextStepBtn.setEnabled(true);
                }else {
                    nextStepBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void verifyPermission(){
        PermissionUtils.requestPermissions(this, 1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS, Manifest.permission.CAMERA}, new PermissionUtils.OnPermissionListener() {
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
    protected int getContentLayout() {
        return R.layout.activity_login_v2;
    }

    private void changeUI(boolean enableEmailLogin){
        this.enableEmailLogin = enableEmailLogin;
        accountEdt.setHint(enableEmailLogin ? getResources().getText(R.string.email_account_tips) : getResources().getText(R.string.account_tips));
        accountEdt.setInputType(enableEmailLogin ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_PHONE);
        accountEdt.setFilters(enableEmailLogin ? new InputFilter[]{new InputFilter.LengthFilter(30)} : new InputFilter[]{new InputFilter.LengthFilter(11)});
    }

    @OnClick(R.id.next_button)
    public void loginBtn(){
        account = accountEdt.getText().toString().trim();
        password = passwordEdt.getText().toString().trim();
        if (TextUtils.isEmpty(account)){
            showToastView(R.string.account_empty_error);
            return;
        }
        if (!enableEmailLogin && (account.length() != 11  || !account.startsWith("1"))){
            showToastView(R.string.account_error);
            return;
        }
        if (TextUtils.isEmpty(password)){
            showToastView(R.string.password_empty_error);
            return;
        }
        gotoLogin(account, password);
    }

    @OnClick(R.id.textView126)
    public void gotoRegister(){
        startActivity(new Intent(this, RegisterActivityV2.class));
    }

    @OnClick(R.id.password_visible_image_fly)
    public void passwordVisibleChange(){

        if (passwordVisibleStatus){
            passwordVisibleStatus = false;
        }else {
            passwordVisibleStatus = true;
        }
        changePasswordVisible(passwordVisibleStatus);
    }

    @OnClick(R.id.forgot_password_text_view)
    public void findPassword(){
        startActivity(new Intent(this, FindPasswordActivity.class));
    }
    @OnClick(R.id.viewCountryCode)
    public void selectCounytry(){
        startActivityForResult(new Intent(this,SelectCountryCodeActivity.class),100);
    }
    private void changePasswordVisible(Boolean enablePasswordVisible){
        if (enablePasswordVisible){
            passwordVisibleImg.setImageResource(R.mipmap.open_gray);
            passwordEdt.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//设置密码可见
            if (!TextUtils.isEmpty(passwordEdt.getText())) passwordEdt.setSelection(passwordEdt.getText().length());
        }else {
            passwordVisibleImg.setImageResource(R.mipmap.close_gray);
            passwordEdt.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT );//设置密码不可见
            if (!TextUtils.isEmpty(passwordEdt.getText())) passwordEdt.setSelection(passwordEdt.getText().length());
        }
    }


    private void gotoLogin(String account, String password) {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_login)));
        nextStepBtn.setEnabled(false);

        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        HttpServiceManager.login(account,password, android_id,loginListener);
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastBackClickTime < 2 * 1000){
            super.onBackPressed();
        }else {
            showToastView(R.string.exit_app_tips);
            lastBackClickTime = currentTime;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode== Activity.RESULT_OK){
            String countryCode=data.getStringExtra("CountryCode");
            viewCountryCode.setText(countryCode);
        }
    }

    private HttpRequestListener<LoginResult> loginListener = new HttpRequestListener<LoginResult>() {
        @Override
        public void onHttpRequestSucceed(final LoginResult result, OriginalCall call) {
            hideProgressDialog();
            nextStepBtn.setEnabled(true);
            if (result.isSuccess()) {
                Global.removeAccount(new AccountManagerCallback() {
                    @Override
                    public void run(AccountManagerFuture accountManagerFuture) {
                        User user = result.getData().getUser();
//                        user.password = MD5.digest(passwordEdt.getText().toString().trim());
                        user.password =passwordEdt.getText().toString().trim();
                        Global.addAccount(user);
                        Global.saveAccessToken(result.getToken());
                        handleLogined(result);
                    }
                });
            }else {
                showToastView(String.valueOf(result.message));
//                if (Constant.ReturnCode.CODE_403.equals(result.code) || "2009".equals(result.code)) {
//                    showToastView(R.string.tip_account_or_password_error);
//                }else if (Constant.ReturnCode.CODE_404.equals(result.code)) {
//                    showToastView(R.string.tip_account_invailed);
//                }else   if (Constant.ReturnCode.CODE_402.equals(result.code)) {
//                    showToastView(R.string.tip_account_disabled);
//                }else {
//                    showToastView(R.string.tip_account_or_password_error);
//                }
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
            showToastView(R.string.tip_conn_server_failed);
            nextStepBtn.setEnabled(true);
        }
    };

    private void handleLogined(LoginResult result) {
        GlobalDatabaseHelper.onAccountChanged();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(LoginResult.DataBean.class.getName(), result.getData());
        startActivity(intent);
        overridePendingTransition(R.anim.activity_appear, R.anim.activity_disappear);
        finish();
    }
}
