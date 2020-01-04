package com.linkb.jstx.activity.setting;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
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

public class ModifyPhoneActivityV2 extends BaseActivity implements HttpRequestListener<ModifyPersonInfoResult> {
    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.tv_region)
    TextView tvRegion;
    @BindView(R.id.tv_region_code)
    TextView tvRegionCode;
    @BindView(R.id.modify_edit)
    TextView modifyEdit;

    private User user;
    private String mPhone;

    @Override
    public void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        user = Global.getCurrentUser();
        modifyEdit.setText((user == null || TextUtils.isEmpty(user.telephone)) ? "" : user.telephone);
        modifyEdit.requestFocus();
        titleTv.setText(getString(R.string.label_setting_modify_phone));
    }


    @OnClick(R.id.tv_ok)
    public void save() {
        String phone = modifyEdit.getText().toString().trim();
        if (RegexUtil.checkMobile(phone)) {
            if (!TextUtils.isEmpty(phone)) {
                mPhone = phone;
                HttpServiceManager.modifyPersonInfo("telephone", mPhone, this);
            }
        } else {
            showToastView(getResources().getString(R.string.account_error));
        }

    }

    @OnClick(R.id.tv_back)
    public void onBack() {
        finish();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_update_phone_v2;
    }

    @Override
    public void onHttpRequestSucceed(ModifyPersonInfoResult result, OriginalCall call) {
        if (result.isSuccess()) {
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