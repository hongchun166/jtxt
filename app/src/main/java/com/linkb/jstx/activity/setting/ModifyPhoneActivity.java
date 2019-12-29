
package com.linkb.jstx.activity.setting;


import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;
import com.linkb.jstx.util.InputSoftUtils;
import com.linkb.jstx.util.RegexUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改电话
 */
public class ModifyPhoneActivity extends BaseActivity implements HttpRequestListener<ModifyPersonInfoResult> {
    private User user;
    private String mPhone;
    @BindView(R.id.title_tv)
    TextView titleTv;

    @Override
    public void initComponents() {
        ButterKnife.bind(this);
        user = Global.getCurrentUser();

        ((EditText) findViewById(R.id.modify_edit)).setText(user.telephone);
        findViewById(R.id.modify_edit).requestFocus();

        titleTv.setText(getString(R.string.label_setting_modify_phone));
    }


    @OnClick(R.id.save_btn)
    public void save() {
        if (RegexUtil.checkMobile(((EditText) findViewById(R.id.modify_edit)).getText().toString())){
            if (!TextUtils.isEmpty(((EditText) findViewById(R.id.modify_edit)).getText().toString())) {
                mPhone = ((EditText) findViewById(R.id.modify_edit)).getText().toString();

                HttpServiceManager.modifyPersonInfo("telephone", mPhone, this);
            }
        }else {
            showToastView(getResources().getString(R.string.account_error));
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
            user.telephone = mPhone;
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