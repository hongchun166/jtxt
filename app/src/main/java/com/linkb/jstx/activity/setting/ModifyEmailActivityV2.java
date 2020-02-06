package com.linkb.jstx.activity.setting;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.event.UserInfoChangeEvent;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;
import com.linkb.jstx.network.result.v2.ListTagsResult;
import com.linkb.jstx.util.InputSoftUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyEmailActivityV2 extends BaseActivity implements TextWatcher {
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_ok)
    TextView tvOK;
    @BindView(R.id.modify_edit)
    EditText editModify;

    private User user;
    private String mEmail;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        tvTitle.setText(R.string.label_setting_modify_email);
        user = Global.getCurrentUser();
        editModify.addTextChangedListener(this);
        editModify.setText(user == null ? "" : TextUtils.isEmpty(user.email) ? "" : user.email);
        editModify.requestFocus();
        editModify.setSelection(user == null ? 0 : TextUtils.isEmpty(user.email) ? 0 : user.email.length());
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_common_v2;
    }

    @OnClick(R.id.tv_back)
    public void backFinish() {
        finish();
    }

    @OnClick(R.id.tv_ok)
    public void OK() {
        String email=editModify.getText().toString();
        if (!TextUtils.isEmpty(email)) {
            mEmail =email;
//            HttpServiceManager.modifyPersonInfo("email", mEmail, this);
            httpUpdateEmail(email);
        }
    }

    @OnClick(R.id.close_name)
    public void closeName() {
        editModify.setText("");
    }

    private void httpUpdateEmail(String email){
        User userTemp=new User();
        userTemp.email=email;
        HttpServiceManagerV2.updateUserInfo(userTemp, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
                if (result.isSuccess()){
                    user.email = mEmail;
                    Global.modifyAccount(user);
                    showToastView(R.string.tip_save_complete);
                    setResult(RESULT_OK, getIntent());
                    finish();
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }
//    @Override
//    public void onHttpRequestSucceed(ModifyPersonInfoResult result, OriginalCall call) {
//        if (result.isSuccess()){
//            user.email = mEmail;
//            Global.modifyAccount(user);
//            showToastView(R.string.tip_save_complete);
//            setResult(RESULT_OK, getIntent());
//            finish();
//        }
//    }
//
//    @Override
//    public void onHttpRequestFailure(Exception e, OriginalCall call) {
//
//    }

    @Override
    public void finish() {
        super.finish();
        InputSoftUtils.hideSoftInput(this);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String mName = charSequence.toString().trim();
        tvOK.setEnabled(!TextUtils.isEmpty(mName) && !mName.equals(user.name));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}

