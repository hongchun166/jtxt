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
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;
import com.linkb.jstx.util.InputSoftUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyNameActivityV2 extends BaseActivity implements HttpRequestListener<ModifyPersonInfoResult>, TextWatcher {
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_ok)
    TextView tvOK;
    @BindView(R.id.modify_edit)
    EditText editModify;

    private User user;
    private String name;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        user = Global.getCurrentUser();
        editModify.addTextChangedListener(this);
        editModify.setText(user == null ? "" : TextUtils.isEmpty(user.name) ? "" : user.name);
        editModify.requestFocus();
        editModify.setSelection(user == null ? 0 : TextUtils.isEmpty(user.name) ? 0 : user.name.length());
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
        if (!TextUtils.isEmpty(editModify.getText().toString())) {
            name = editModify.getText().toString();

            HttpServiceManager.modifyPersonInfo("name", name, this);
        }
    }

    @OnClick(R.id.close_name)
    public void closeName() {
        editModify.setText("");
    }

    @Override
    public void onHttpRequestSucceed(ModifyPersonInfoResult result, OriginalCall call) {
        if (result.isSuccess()) {
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
