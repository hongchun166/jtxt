package com.linkb.jstx.activity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.component.countdownbutton.CountDownButton;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.util.RegexUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 找回密码
* */
public class FindPasswordActivity extends BaseActivity {

    @BindView(R.id.next_button)
    Button nextStepBtn;

    @BindView(R.id.editText_account) EditText accountEdt;

    @BindView(R.id.editText_password) EditText passwordEdt;
    @BindView(R.id.editText_password_confirm) EditText passwordConfirmEdt;

    @BindView(R.id.editText3) EditText verifyCodeEdt;
    @BindView(R.id.countbtn)
    CountDownButton countDownButton;

    @BindView(R.id.password_confirm_visible_image) ImageView passwordConfirmVisibleImage;
    @BindView(R.id.password_visible_image) ImageView passwordVisibleImage;

    /** 密码可视状态
     * */
    private Boolean passwordVisibleStatus = false;

    /** 确认密码可视状态
     * */
    private Boolean passwordComfirmVisibleStatus = false;

    private String account;
    private String password;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        passwordConfirmEdt.addTextChangedListener(new TextWatcher() {
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
        return R.layout.activity_find_password;
    }

    @OnClick(R.id.back_btn)
    public void onBack(){finish();}

    @OnClick(R.id.countbtn)
    public void onCountDown(){
        if (RegexUtil.checkMobile(accountEdt.getText().toString().trim())){
            HttpServiceManager.getRegisterVerifyCode(accountEdt.getText().toString().trim(), "1",  listener);
        }else if (RegexUtil.checkEmail(accountEdt.getText().toString().trim())){
            HttpServiceManager.getEmailVerifyCode(accountEdt.getText().toString().trim(), "1",  listener);
        }else {
            showToastView(getResources().getString(R.string.please_input_availd_telephone_or_email));
        }
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

    private HttpRequestListener<BaseResult> findPasswordListener = new HttpRequestListener<BaseResult>() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
            if (!result.isSuccess()){
                showToastView(result.message);
            }else {
                showToastView(R.string.set_new_password_success);
                finish();
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };


    @OnClick(R.id.next_button)
    public void onNextStep(){
        account = accountEdt.getText().toString().trim();
        password = passwordEdt.getText().toString().trim();
        String verifyCode = verifyCodeEdt.getText().toString().trim();
        if (TextUtils.isEmpty(account)){
            showToastView(R.string.account_empty_error);
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

        HttpServiceManager.findPassword(account, password, verifyCode, findPasswordListener);
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

    @OnClick(R.id.password_confirm_visible_image_fly)
    public void passwordConfirmVisibleChange(){

        if (passwordComfirmVisibleStatus){
            passwordComfirmVisibleStatus = false;
        }else {
            passwordComfirmVisibleStatus = true;
        }
        changePasswordVisible(passwordComfirmVisibleStatus, passwordConfirmEdt , passwordConfirmVisibleImage);
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
}
