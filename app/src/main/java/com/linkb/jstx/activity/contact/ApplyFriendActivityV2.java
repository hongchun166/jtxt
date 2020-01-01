package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.video.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 提交好友申请页面
* */
public class ApplyFriendActivityV2 extends BaseActivity implements HttpRequestListener<BaseResult> {

    @BindView(R.id.editText5)
    EditText leaveMessageEdt;

    @BindView(R.id.editText6)
    EditText friendRemarkEdt;

    private Friend mFriend;
    private User mSelt;


    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        mSelt = Global.getCurrentUser();
        mFriend = (Friend) getIntent().getSerializableExtra(Friend.class.getName());

        leaveMessageEdt.setText(getString(R.string.verify_apply_friend_leave_message_default, mSelt.name));
        if (!TextUtils.isEmpty(leaveMessageEdt.getText())){
            leaveMessageEdt.setSelection(leaveMessageEdt.getText().length());
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_apply_friend;
    }

    @OnClick(R.id.back_btn)
    public void onBack(){finish();}

    @OnClick(R.id.send_btn)
    public void onSend() {
        showProgressDialog("");
        HttpServiceManager.applyFriendV2(mSelt.account,mFriend.account, mFriend.getName(),addFriendRequest);
    }

    private HttpRequestListener<BaseResult> addFriendRequest = new HttpRequestListener<BaseResult>() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
            hideProgressDialog();
            if (result.isSuccess()){
                if(TextUtils.isEmpty(result.message)){
                    result.message=getString(R.string.already_commit_apply_friend);
                }
                showToastView(result.message);
                LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
                finish();
            }else {
                showToastView(result.message);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };

    @OnClick(R.id.delete_fly)
    public void onDelectLeaveMessage(){
        leaveMessageEdt.setText("");
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        hideProgressDialog();
        if (result.isSuccess()){
            ToastUtils.s(ApplyFriendActivityV2.this, getString(R.string.already_commit_apply_friend));
            setResult(RESULT_OK, getIntent());
            finish();
        }else {
            ToastUtils.s(ApplyFriendActivityV2.this, getString(R.string.commit_apply_friend_failure));
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
        ToastUtils.s(ApplyFriendActivityV2.this, getString(R.string.commit_apply_friend_failure));
    }
}
