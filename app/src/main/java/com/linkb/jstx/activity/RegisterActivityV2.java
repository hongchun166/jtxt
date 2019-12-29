package com.linkb.jstx.activity;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.content.Intent;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.countdownbutton.CountDownButton;
import com.linkb.jstx.database.GlobalDatabaseHelper;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.LoginResult;
import com.linkb.jstx.util.MD5;
import com.linkb.jstx.util.RegexUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 邮箱注册/手机注册页面
* */
public class RegisterActivityV2 extends BaseActivity {

    @BindView(R.id.editText_account) EditText accountEdt;

    @BindView(R.id.editText_password) EditText passwordEdt;
    @BindView(R.id.editText_password_confirm) EditText passwordConfirmEdt;
    @BindView(R.id.editText3) EditText verifyCodeEdt;
    @BindView(R.id.countbtn) CountDownButton countDownButton;
    @BindView(R.id.editText_nickname) EditText nickNameEdt;

    @BindView(R.id.checkBox_sex_man) CheckBox manCkb;
    @BindView(R.id.checkBox_sex_woman) CheckBox womanCkb;

    @BindView(R.id.next_button) Button nextStepBtn;

    @BindView(R.id.password_confirm_visible_image) ImageView passwordConfirmVisibleImage;
    @BindView(R.id.password_visible_image) ImageView passwordVisibleImage;

    /** 是邮箱注册还是 手机注册
     * */
    private Boolean enableEmailRegister = false;
    /** 性别是男 还是 女
     * */
    private Boolean enableMan  = true;
    /** 密码可视状态
     * */
    private Boolean passwordVisibleStatus = false;
    /** 密码可视状态
     * */
    private Boolean passwordConfirmVisibleStatus = false;

    private String account;
    private String password;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        changeUI(enableEmailRegister);
        changeSexStatus(enableMan);

        nickNameEdt.addTextChangedListener(new TextWatcher() {
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

    @Override
    protected int getContentLayout() {
        return R.layout.activity_register_v2;
    }

    private void changeUI(boolean enableEmailRegister){
        this.enableEmailRegister = enableEmailRegister;
        accountEdt.setInputType(enableEmailRegister ? InputType.TYPE_CLASS_TEXT : InputType.TYPE_CLASS_PHONE);
        accountEdt.setHint(enableEmailRegister ? getResources().getText(R.string.email_account_tips) : getResources().getText(R.string.account_tips));
        accountEdt.setFilters(enableEmailRegister ? new InputFilter[]{new InputFilter.LengthFilter(30)} : new InputFilter[]{new InputFilter.LengthFilter(11)});
        verifyCodeEdt.setHint(enableEmailRegister ? getResources().getText(R.string.email_verify_code) : getResources().getText(R.string.telephone_verify_code));
    }

    private void changeSexStatus(boolean enableMan) {
        this.enableMan = enableMan;
        manCkb.setChecked(enableMan);
        womanCkb.setChecked(!enableMan);
    }

    @OnClick(R.id.next_button)
    public void registerBtn(){
        account = accountEdt.getText().toString().trim();
        password = passwordEdt.getText().toString().trim();
        String nickMame = nickNameEdt.getText().toString().trim();
        String verifyCode = verifyCodeEdt.getText().toString().trim();
        if (TextUtils.isEmpty(account)){
            showToastView(R.string.account_empty_error);
            return;
        }
        if (!enableEmailRegister && (account.length() != 11  || !account.startsWith("1"))){
            showToastView(R.string.account_error);
            return;
        }
        if (TextUtils.isEmpty(verifyCode)){
            showToastView(R.string.verify_code_empty_error);
            return;
        }
        if (TextUtils.isEmpty(password)){
            showToastView(R.string.password_empty_error);
            return;
        }
        if (!password.equals(passwordConfirmEdt.getText().toString())){
            showToastView(R.string.password_confirm_error);
            return;
        }
        if (TextUtils.isEmpty(nickMame)){
            showToastView(R.string.nick_name_empty_error);
            return;
        }
        gotoRegister(account, password, nickMame, enableMan, verifyCode);
    }

    @OnClick(R.id.checkBox_sex_man)
    public void checkSexMan(){
        changeSexStatus(true);
    }

    @OnClick(R.id.checkBox_sex_woman)
    public void checkSexWoMan(){
        changeSexStatus(false);
    }

    @OnClick(R.id.countbtn)
    public void onCountDown(){
        if (!enableEmailRegister){
            HttpServiceManager.getRegisterVerifyCode(accountEdt.getText().toString().trim(), listener);
        }else {
            HttpServiceManager.getEmailVerifyCode(accountEdt.getText().toString().trim(), listener);
        }
    }

    @OnClick(R.id.password_confirm_visible_image_fly)
    public void passwordConfirmVisibleChange(){

        if (passwordConfirmVisibleStatus){
            passwordConfirmVisibleStatus = false;
        }else {
            passwordConfirmVisibleStatus = true;
        }
        changePasswordVisible(passwordConfirmVisibleStatus, passwordConfirmEdt , passwordConfirmVisibleImage);
    }

    @OnClick(R.id.password_visible_image_fly)
    public void passwordVisibleChange(){

        if (passwordVisibleStatus){
            passwordVisibleStatus = false;
        }else {
            passwordVisibleStatus = true;
        }
        changePasswordVisible(passwordVisibleStatus, passwordEdt, passwordVisibleImage);
    }

    private void changePasswordVisible(Boolean enablePasswordVisible, EditText editText, ImageView imageView){
        if (enablePasswordVisible){
            imageView.setImageResource(R.mipmap.open_gray);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);//设置密码可见
            if (!TextUtils.isEmpty(editText.getText())) editText.setSelection(editText.getText().length());
        }else {
            imageView.setImageResource(R.mipmap.close_gray);
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT );//设置密码不可见
            if (!TextUtils.isEmpty(editText.getText())) editText.setSelection(editText.getText().length());
        }
    }

    @OnClick(R.id.textView127)
    public void gotoLogin(){
        finish();
    }

    private void gotoRegister(String account, String password, String nickMame, Boolean enableMan, String verifyCode) {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.register)));
        nextStepBtn.setEnabled(false);
        String sex = enableMan ? "1" : "0";
        String locale = enableEmailRegister ? "1" : "0";
        HttpServiceManager.registerAccount(account, sex, nickMame, password, verifyCode ,locale ,  registerListener);
    }

    private void gotoLogin(String account, String password) {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_login)));
        nextStepBtn.setEnabled(false);
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        HttpServiceManager.login(account,password, android_id,loginListener);

    }

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

    private void handleLogined(LoginResult result) {
        GlobalDatabaseHelper.onAccountChanged();
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(LoginResult.DataBean.class.getName(), result.getData());
        startActivity(intent);
        overridePendingTransition(R.anim.activity_appear, R.anim.activity_disappear);
        finish();
    }
}
