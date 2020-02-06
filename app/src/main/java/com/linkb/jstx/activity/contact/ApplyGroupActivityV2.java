package com.linkb.jstx.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.message.builder.Action102Builder;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.v2.FindGroupsResult;
import com.linkb.video.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 提交群组申请页面
* */
public class ApplyGroupActivityV2 extends BaseActivity {
    @BindView(R.id.title_tv)
    TextView title_tv;
    @BindView(R.id.editText5)
    EditText leaveMessageEdt;

    @BindView(R.id.editText6)
    EditText friendRemarkEdt;

    Group group;
    private User mSelt;

    public static void navToAct(Context context, Group  groupB){
        Intent intent=new Intent(context,ApplyGroupActivityV2.class);
        intent.putExtra("groupB",groupB);
        context.startActivity(intent);
    }

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        mSelt = Global.getCurrentUser();
        group = (Group) getIntent().getSerializableExtra("groupB");

        title_tv.setText(R.string.label_group_apply_join_group);
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
         Message message = new Message();
        String token = ((EditText) findViewById(R.id.token)).getText().toString();
        User source = Global.getCurrentUser();
        message.action = Constant.MessageAction.ACTION_102;
        //接收者为 群创建者account
        message.receiver = group.founder;
        message.content = new Action102Builder().buildJsonString(source, group, token);
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        HttpServiceManager.sendOnly(message, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
                hideProgressDialog();
                showToastView(R.string.tip_send_request_complete);
                finish();
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
            }
        });

//        HttpServiceManager.applyJoinGroup(Long.valueOf(groupId), new HttpRequestListener() {
//            @Override
//            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
//                hideProgressDialog();
//                if (result.isSuccess()){
//                    showToastView(getResources().getString(R.string.apply_join_group_tips));
//                    setResult(RESULT_OK, getIntent());
//                    finish();
//                }else {
//                    showToastView(result.message);
//                }
//            }
//
//            @Override
//            public void onHttpRequestFailure(Exception e, OriginalCall call) {
//                hideProgressDialog();
//            }
//        });

    }
    @OnClick(R.id.delete_fly)
    public void onDelectLeaveMessage(){
        leaveMessageEdt.setText("");
    }
}
