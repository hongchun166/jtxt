
package com.linkb.jstx.activity.setting;


import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.countdownbutton.CountDownButton;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改支付密码
 */
public class ModifyApplyPasswordActivity extends BaseActivity implements HttpRequestListener {


    @BindView(R.id.newPassword)
    EditText newApplyPassword;
    @BindView(R.id.editText3)
    EditText verifyCodeEdt;
    @BindView(R.id.countbtn)
    CountDownButton countDownButton;
    @BindView(R.id.title_tv)
    TextView titleTv;

    @Override
    public void initComponents() {
        ButterKnife.bind(this);
        User user = Global.getCurrentUser();
        titleTv.setText(getString(R.string.label_setting_modify_apply_password));
    }

    @OnClick(R.id.countbtn)
    public void onCountDown(){
        countDownButton.startCount();
//        HttpServiceManager.getApplyVerifyCode(this);
        HttpServiceManagerV2.sendWeiquVCode(Global.getCurrentUser().getAccount(),this);
    }

    @OnClick(R.id.save_btn)
    public void save() {
        performUpdateRequest();
    }

    @OnClick(R.id.back_btn)
    public void onBack(){finish();}

    private void performUpdateRequest() {
        String newPassword = ((EditText) findViewById(R.id.newPassword)).getText().toString().trim();
        String verifyCode = verifyCodeEdt.getText().toString().trim();

        if (TextUtils.isEmpty(newPassword)){
            showToastView(R.string.new_apply_password_empty_error);
            return;
        }
        if (TextUtils.isEmpty(verifyCode)){
            showToastView(R.string.verify_code_empty_error);
            return;
        }
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_save)));
//        HttpServiceManager.updateApplyPassword(newPassword,verifyCode,this);
        User user=Global.getCurrentUser();
        HttpServiceManagerV2.updateTradePass(user.account,newPassword,verifyCode,this);
    }

   @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        hideProgressDialog();
       if(call.equalsPatch(URLConstant.updateTradePass)){
           if(result.isSuccess()){
               showToastView(R.string.tip_save_complete);
               finish();
           }else {
               showToastView(String.valueOf(result.message));
           }
       } else if ( call.equalsPatch(URLConstant.sendWeiquVCode)){
           if(result.isSuccess()){
               showToastView(R.string.tip_publish_complete);
           }else {
               showToastView(result.message);
           }
       }
//        if (result.isSuccess() && call.equalsPatch(URLConstant.MODIFY_APPLY_PASSWORD_URL)) {
//            hideProgressDialog();
//            showToastView(R.string.tip_save_complete);
//            finish();
//            return;
//        }
//        if (!result.isSuccess() && call.equalsPatch(URLConstant.MODIFY_APPLY_PASSWORD_URL)) {
//            hideProgressDialog();
//            showToastView(R.string.tip_oldpassword_error);
//        }
//        if (!result.isSuccess() && call.equalsPatch(URLConstant.APPLY_MESSAGE_VERIFY_CODE)){
//            showToastView(result.message);
//        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_modify_apply_passord;
    }



}
