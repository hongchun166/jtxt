
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
import com.linkb.jstx.event.UserInfoChangeEvent;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;
import com.linkb.jstx.util.InputSoftUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改名称
 */
public class ModifyNameActivity extends BaseActivity implements HttpRequestListener<ModifyPersonInfoResult> {
    private User user;
    private String name;
    @BindView(R.id.title_tv)
    TextView titleTv;


    @Override
    public void initComponents() {
        user = Global.getCurrentUser();

        ButterKnife.bind(this);
        titleTv.setText(getString(R.string.label_setting_modify_name));

        ((EditText) findViewById(R.id.modify_edit)).setText(user.name);
        findViewById(R.id.modify_edit).requestFocus();

    }


    @OnClick(R.id.save_btn)
    public void save() {
        if (!TextUtils.isEmpty(((EditText) findViewById(R.id.modify_edit)).getText().toString())) {
            name = ((EditText) findViewById(R.id.modify_edit)).getText().toString();

            HttpServiceManager.modifyPersonInfo("name",name, this);
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
            user.name = this.name;
            Global.modifyAccount(user);
            EventBus.getDefault().post(new UserInfoChangeEvent("name"));
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
