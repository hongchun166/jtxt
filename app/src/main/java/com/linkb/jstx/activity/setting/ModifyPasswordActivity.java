
package com.linkb.jstx.activity.setting;


import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.util.MD5;
import com.linkb.jstx.util.StringUtils;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改密码
 */
public class ModifyPasswordActivity extends BaseActivity implements HttpRequestListener {

    @BindView(R.id.title_tv)
    TextView titleTv;

    @Override
    public void initComponents() {

        User user = Global.getCurrentUser();
        ButterKnife.bind(this);

        titleTv.setText(getString(R.string.label_setting_modify_password));
        findViewById(R.id.modify_apply_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ModifyPasswordActivity.this, ModifyApplyPasswordActivity.class));
            }
        });
    }


    private void performUpdateRequest() {

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_save)));
        String oldPassword = ((EditText) findViewById(R.id.oldPassword)).getText().toString().trim();
        String newPassword = ((EditText) findViewById(R.id.newPassword)).getText().toString().trim();

        HttpServiceManager.updatePassword(oldPassword,newPassword,this);
    }

    @OnClick(R.id.save_btn)
    public void save() {
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.oldPassword)).getText().toString().trim())){
            showToastView(getString(R.string.password_empty_error));
            return;
        }
        if (TextUtils.isEmpty(((EditText) findViewById(R.id.newPassword)).getText().toString().trim())){
            showToastView(getString(R.string.new_password_empty_error));
            return;
        }
        performUpdateRequest();
    }


    @OnClick(R.id.back_btn)
    public void onBack(){finish();}

   @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        if (result.isSuccess() && call.equalsPatch(URLConstant.USER_PASSWORD_URL)) {
            hideProgressDialog();
            showToastView(R.string.tip_save_complete);
            finish();
            return;
        }
        if (!result.isSuccess() && call.equalsPatch(URLConstant.USER_PASSWORD_URL)) {
            hideProgressDialog();
            showToastView(R.string.tip_oldpassword_error);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_modify_passord;
    }




}
