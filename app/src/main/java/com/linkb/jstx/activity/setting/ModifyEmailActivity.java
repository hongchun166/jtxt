
package com.linkb.jstx.activity.setting;


import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;
import com.linkb.jstx.util.InputSoftUtils;
import com.linkb.jstx.util.RegexUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改邮箱
 */
public class ModifyEmailActivity extends BaseActivity implements HttpRequestListener<ModifyPersonInfoResult> {
    private User user;
    private String mEmail;
    @BindView(R.id.title_tv)
    TextView titleTv;


    @Override
    public void initComponents() {
        user = Global.getCurrentUser();

        ButterKnife.bind(this);
        titleTv.setText(getString(R.string.label_setting_modify_email));

        ((EditText) findViewById(R.id.modify_edit)).setText(user.email);
        findViewById(R.id.modify_edit).requestFocus();
    }



    @OnClick(R.id.save_btn)
    public void save() {
        if (RegexUtil.checkEmail(((EditText) findViewById(R.id.modify_edit)).getText().toString())){
            if (!TextUtils.isEmpty(((EditText) findViewById(R.id.modify_edit)).getText().toString())) {
                mEmail = ((EditText) findViewById(R.id.modify_edit)).getText().toString();

                HttpServiceManager.modifyPersonInfo("email", mEmail, this);
            }
        }else {
            showToastView(getResources().getString(R.string.email_format_error));
        }
    }

    @OnClick(R.id.back_btn)
    public void onBack(){finish();}

    @Override
    public int getContentLayout() {
        return R.layout.activity_modify_common;
    }


    @Override
    public void onHttpRequestSucceed(ModifyPersonInfoResult result, OriginalCall call) {
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

    @Override
    public void finish() {
        super.finish();
        InputSoftUtils.hideSoftInput(this);
    }
}