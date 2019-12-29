package com.linkb.jstx.activity;

import android.Manifest;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.countdownbutton.CountDownButton;
import com.linkb.jstx.database.GlobalDatabaseHelper;
import com.linkb.jstx.fragment.MutilLanguageDialogFragment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.LoginResult;
import com.linkb.jstx.util.LocalManageUtil;
import com.linkb.jstx.util.MD5;
import com.linkb.jstx.util.PermissionUtils;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginAndRegisterActivity extends BaseActivity implements MutilLanguageDialogFragment.OnSelectLanguageListener {

    @BindView(R.id.editText_apply_password)
    EditText applyPassword;
    @BindView(R.id.editText_password_confirm)
    EditText passwordConfirm;
    @BindView(R.id.editText_apply_password_confirm)
    EditText applyPasswordConfirm;
    @BindView(R.id.editText3)
    EditText verifyCodeEdt;
    @BindView(R.id.constraintLayout2)
    View verifyCodeView;
    @BindView(R.id.countbtn)
    CountDownButton countDownButton;

    private TextView loginTv, registerTv, forgotPasswordTv, serviceRuleTv, serviceRuleTipsTv, sexTipsTv;
    private EditText accountEdt, passwordEdt, nickNameEdt;
    private CheckBox manCkb, womanCkb;
    private Button nextStepBtn;
    /** 是登录 还是 注册
    * */
    private Boolean enableLogin = true;
    /** 性别是男 还是 女
    * */
    private Boolean enableMan  = true;
    protected long lastBackClickTime;
    private String account;
    private String password;

    private boolean enableLoginBtn = false;

    private int language = 1 ;
    private MutilLanguageDialogFragment mMutilLanguageDialogFragment;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        verifyPermission();

        loginTv = findViewById(R.id.login_textView);
        loginTv.setOnClickListener(this);
        registerTv = findViewById(R.id.register_textView);
        registerTv.setOnClickListener(this);
        accountEdt = findViewById(R.id.editText_account);
        passwordEdt = findViewById(R.id.editText_password);
        nickNameEdt = findViewById(R.id.editText_nickname);
        sexTipsTv = findViewById(R.id.sex_tips_textView);
        manCkb = findViewById(R.id.checkBox_sex_man);
        manCkb.setOnClickListener(this);
        womanCkb = findViewById(R.id.checkBox_sex_woman);
        womanCkb.setOnClickListener(this);
        nextStepBtn = findViewById(R.id.next_button);
        nextStepBtn.setOnClickListener(this);
        forgotPasswordTv = findViewById(R.id.forgot_password_text_view);
        serviceRuleTv = findViewById(R.id.service_rule_text_view);
        serviceRuleTipsTv = findViewById(R.id.service_rule_tips_text_view);

        changeUI(enableLogin);
        changeSexStatus(enableMan);


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
                if (enableLogin && !TextUtils.isEmpty(charSequence) && charSequence.length() >= 6){
                    nextStepBtn.setEnabled(true);
                }else {
                    nextStepBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        nickNameEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!enableLogin && !TextUtils.isEmpty(charSequence)){
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

    private void changeUI(boolean enableLogin){
        this.enableLogin = enableLogin;
        loginTv.setTextColor(enableLogin ? getResources().getColor(R.color.tex_color_blue_1068ed) : getResources().getColor(R.color.tex_color_gray_999999));
        registerTv.setTextColor(enableLogin ? getResources().getColor(R.color.tex_color_gray_999999) : getResources().getColor(R.color.tex_color_blue_1068ed));
        passwordEdt.setHint(enableLogin ? getResources().getText(R.string.password_tip_accent) : getResources().getText(R.string.password_tips));
        nextStepBtn.setText(enableLogin ? getResources().getText(R.string.login) : getResources().getText(R.string.register));
        serviceRuleTv.setVisibility(enableLogin ? View.INVISIBLE : View.VISIBLE);
        serviceRuleTipsTv.setVisibility(enableLogin ? View.INVISIBLE : View.VISIBLE);
        nickNameEdt.setVisibility(enableLogin ? View.GONE : View.VISIBLE);
        manCkb.setVisibility(enableLogin ? View.GONE : View.VISIBLE);
        womanCkb.setVisibility(enableLogin ? View.GONE : View.VISIBLE);
        sexTipsTv.setVisibility(enableLogin ? View.GONE : View.VISIBLE);
        forgotPasswordTv.setVisibility(enableLogin ? View.VISIBLE : View.INVISIBLE);
        passwordConfirm.setVisibility(enableLogin ? View.GONE : View.VISIBLE);
        verifyCodeView.setVisibility(enableLogin ? View.GONE : View.VISIBLE);
    }

    private void changeSexStatus(boolean enableMan) {
        this.enableMan = enableMan;
        manCkb.setChecked(enableMan);
        womanCkb.setChecked(!enableMan);
    }

    private void verifyPermission(){
        PermissionUtils.requestPermissions(this, 1, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS}, new PermissionUtils.OnPermissionListener() {
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
        return R.layout.activity_login_register;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                account = accountEdt.getText().toString().trim();
                password = passwordEdt.getText().toString().trim();
                String nickMame = nickNameEdt.getText().toString().trim();
                String verifyCode = verifyCodeEdt.getText().toString().trim();
                if (TextUtils.isEmpty(account)){
                    showToastView(R.string.account_empty_error);
                    return;
                }
                if (account.length() != 11  || !account.startsWith("1")){
                    showToastView(R.string.account_error);
                    return;
                }
                if (!enableLogin && TextUtils.isEmpty(verifyCode)){
                    showToastView(R.string.verify_code_empty_error);
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    showToastView(R.string.password_empty_error);
                    return;
                }
                if (!enableLogin && !password.equals(passwordConfirm.getText().toString())){
                    showToastView(R.string.password_confirm_error);
                    return;
                }
//
//                if (!enableLogin && TextUtils.isEmpty(applyPassword.getText().toString())){
//                    showToastView(R.string.apply_password_empty_error);
//                    return;
//                }
//                if (!enableLogin && !applyPassword.getText().toString().equals(applyPasswordConfirm.getText().toString())){
//                    showToastView(R.string.apply_password_confirm_error);
//                    return;
//                }
                if (!enableLogin && TextUtils.isEmpty(nickMame)){
                    showToastView(R.string.nick_name_empty_error);
                    return;
                }
                if (enableLogin){
                    gotoLogin(account, password);
                }else {
                    gotoRegister(account, password, nickMame, enableMan, verifyCode);
                }
                break;
            case R.id.checkBox_sex_man:
                changeSexStatus(true);
                break;
            case R.id.checkBox_sex_woman:
                changeSexStatus(false);
                break;
            case R.id.login_textView:
                changeUI(false);
                break;
            case R.id.register_textView:
                changeUI(true);
                break;
        }
    }

    private void gotoLogin(String account, String password) {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_login)));
        nextStepBtn.setEnabled(false);
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        HttpServiceManager.login(account,password, android_id,loginListener);
    }

    private void gotoRegister(String account, String password, String nickMame, Boolean enableMan, String verifyCode) {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.register)));
        nextStepBtn.setEnabled(false);
        String sex = enableMan ? "1" : "0";
        HttpServiceManager.registerAccount(account, sex, nickMame, password, verifyCode , "", registerListener);
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
                        user.password = MD5.digest(passwordEdt.getText().toString().trim());
                        Global.addAccount(user);
                        Global.saveAccessToken(result.getToken());
                        handleLogined(result);
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

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
            showToastView(R.string.tip_conn_server_failed);
            nextStepBtn.setEnabled(true);
        }
    };

    private HttpRequestListener<BaseResult> registerListener = new HttpRequestListener<BaseResult>() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
            hideProgressDialog();
            nextStepBtn.setEnabled(true);
            if (result.isSuccess()){
                showToastView(result.message);
                gotoLogin(account, password);
            }else {
                showToastView(result.message);
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

    @OnClick(R.id.countbtn)
    public void onCountDown(){
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        HttpServiceManager.getRegisterVerifyCode(accountEdt.getText().toString().trim(), android_id,  listener);
    }

    @OnClick(R.id.textView124)
    public void changeLanguage(){
        FragmentManager fm = getSupportFragmentManager();
        if (mMutilLanguageDialogFragment == null){
            mMutilLanguageDialogFragment = new MutilLanguageDialogFragment();
            mMutilLanguageDialogFragment.setListener(this);
        }
        mMutilLanguageDialogFragment.show(fm, "MutilLanguageDialogFragment");
    }

    public void reStart(Context context) {
        Intent intent = new Intent(context, LoginAndRegisterActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public void selectCn() {
        ClientConfig.setLanguageType(1);
        LocalManageUtil.saveSelectLanguage(this, 1);
        reStart(this);
    }

    @Override
    public void selectEn() {
        ClientConfig.setLanguageType(3);
        LocalManageUtil.saveSelectLanguage(this, 3);
        reStart(this);
    }

    private HttpRequestListener<BaseResult> listener = new HttpRequestListener<BaseResult>() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
            if (!result.isSuccess()){
                showToastView(result.message);
            }else {
                countDownButton.startCount();
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };
}
