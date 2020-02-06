package com.linkb.jstx.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.linkb.jstx.util.InputSoftUtils;
import com.linkb.jstx.util.RegexUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改职务
 */
public class ModifyPositionActivityV2 extends BaseActivity
        implements TextWatcher {
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_ok)
    TextView tvOK;
    @BindView(R.id.modify_edit)
    EditText editModify;

    private User user;
    private String job;

    public static void navToAct(Context context,int requestCode){
        Intent intent=new Intent(context,ModifyPositionActivityV2.class);
       if(context instanceof Activity){
           ( (Activity)context).startActivityForResult(intent,requestCode);
       }else {
           context.startActivity(intent);
       }
    }
    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        user = Global.getCurrentUser();
        tv_title.setText(R.string.label_setting_modify_position);
        editModify.addTextChangedListener(this);
        editModify.setText(user == null ? "" : TextUtils.isEmpty(user.position) ? "" : user.position);
        editModify.requestFocus();
        editModify.setSelection(user == null ? 0 : TextUtils.isEmpty(user.position) ? 0 : user.position.length());
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
            job = editModify.getText().toString();
            final User userTemp=new User();
            userTemp.position=job;
            HttpServiceManagerV2.updateUserInfo(userTemp, new HttpRequestListener() {
                @Override
                public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
                    if(result.isSuccess()){
                        user.position = userTemp.position;
                        Global.modifyAccount(user);
//                        EventBus.getDefault().post(new UserInfoChangeEvent("name"));
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
    }

    @OnClick(R.id.close_name)
    public void closeName() {
        editModify.setText("");
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
        String inputStr = charSequence.toString().trim();
        tvOK.setEnabled(!TextUtils.isEmpty(inputStr) && !inputStr.equals(user.position));
    }
    @Override
    public void afterTextChanged(Editable editable) {

    }
}