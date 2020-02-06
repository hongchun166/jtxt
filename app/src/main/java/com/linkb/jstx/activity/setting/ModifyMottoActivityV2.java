package com.linkb.jstx.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.util.InputSoftUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyMottoActivityV2 extends BaseActivity implements TextWatcher {
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_ok)
    TextView tvOK;
    @BindView(R.id.tv_sign_number)
    TextView tvSignNumber;
    @BindView(R.id.modify_edit)
    EditText editModify;

    private User user;
    private String motto;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        user = Global.getCurrentUser();
        tvTitle.setText(R.string.label_setting_modify_motto);
        editModify.addTextChangedListener(this);
        editModify.setText(user == null ? "" : TextUtils.isEmpty(user.motto) ? "" : user.motto);
        editModify.requestFocus();
        editModify.setSelection(user == null ? 0 : TextUtils.isEmpty(user.motto) ? 0 : user.motto.length());
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_update_motto_v2;
    }

    @OnClick(R.id.tv_back)
    public void backFinish() {
        finish();
    }

    @OnClick(R.id.tv_ok)
    public void OK() {
        String mptto = editModify.getText().toString().trim();
        if (!TextUtils.isEmpty(mptto)) {
            httpUpdateMotto(mptto);
        }
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
        String motto = charSequence.toString().trim();
        tvOK.setEnabled(!TextUtils.isEmpty(motto) && !motto.equals(user.motto));
        tvSignNumber.setText(TextUtils.isEmpty(motto) ? "0" : String.valueOf(motto.length()));
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    private void httpUpdateMotto(String motto){
        final User userTemp=new User();
        userTemp.motto=motto;
        final Context context=ModifyMottoActivityV2.this;
        HttpServiceManagerV2.updateUserInfo(userTemp, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
                if(result.isSuccess()){
                    user.motto = userTemp.motto;
                    Global.modifyAccount(user);
                    SentBody sent = new SentBody();
                    sent.setKey(Constant.CIMRequestKey.CLIENT_MODIFY_PROFILE);
                    sent.put("account", user.account);
                    sent.put("motto", user.motto);
                    sent.put("name", user.name);
                    CIMPushManager.sendRequest(context, sent);
                    showToastView(R.string.tip_save_complete);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }
}
