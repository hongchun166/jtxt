
package com.linkb.jstx.activity.setting;


import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.linkb.R;
import com.linkb.jstx.util.InputSoftUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改签名
 */
public class ModifyMottoActivity extends BaseActivity {
    private User user;
    @BindView(R.id.title_tv)
    TextView titleTv;

    @Override
    public void initComponents() {
        user = Global.getCurrentUser();

        ButterKnife.bind(this);
        titleTv.setText(getString(R.string.label_setting_modify_motto));

        ((EditText) findViewById(R.id.modify_edit)).setText(user.motto);
        findViewById(R.id.modify_edit).requestFocus();
    }

    @OnClick(R.id.save_btn)
    public void save() {
        if (!TextUtils.isEmpty(((EditText) findViewById(R.id.modify_edit)).getText().toString())) {
            user.motto = ((EditText) findViewById(R.id.modify_edit)).getText().toString();
            Global.modifyAccount(user);

            SentBody sent = new SentBody();
            sent.setKey(Constant.CIMRequestKey.CLIENT_MODIFY_PROFILE);
            sent.put("account", user.account);
            sent.put("motto", user.motto);
            sent.put("name", user.name);
            CIMPushManager.sendRequest(this, sent);

            showToastView(R.string.tip_save_complete);
            finish();
        }
    }

    @OnClick(R.id.back_btn)
    public void onBack(){finish();}

    @Override
    public int getContentLayout() {
        return R.layout.activity_modify_common;
    }

    @Override
    public void finish() {
        super.finish();
        InputSoftUtils.hideSoftInput(this);
    }
}
