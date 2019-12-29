
package com.linkb.jstx.activity.setting;


import android.content.Intent;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;
import com.linkb.jstx.util.InputSoftUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 修改备注
 */
public class ModifyRemarkActivity extends BaseActivity implements HttpRequestListener<BaseDataResult> {
    private Friend friend;
    private String mRemark;
    @BindView(R.id.title_tv)
    TextView titleTv;


    @Override
    public void initComponents() {
        ButterKnife.bind(this);
        titleTv.setText(getString(R.string.label_set_remark));

        friend = (Friend) getIntent().getSerializableExtra(Friend.class.getName());
        mRemark = getIntent().getStringExtra("reMarkName");

        ((EditText) findViewById(R.id.modify_edit)).setText(mRemark);
        findViewById(R.id.modify_edit).requestFocus();


    }

    @OnClick(R.id.save_btn)
    public void save() {
        if (!TextUtils.isEmpty(((EditText) findViewById(R.id.modify_edit)).getText().toString())) {
            mRemark = ((EditText) findViewById(R.id.modify_edit)).getText().toString();

            HttpServiceManager.setRemark(friend.account, mRemark,this);
        }
    }

    @OnClick(R.id.back_btn)
    public void onBack(){finish();}

    @Override
    public int getContentLayout() {
        return R.layout.activity_modify_common;
    }



    @Override
    public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
        if (result.isSuccess()){
            friend.name = mRemark;
            FriendRepository.update(friend);
            showToastView(R.string.tip_save_complete);
            Intent intent = getIntent();
            intent.putExtra("name", mRemark);
            setResult(RESULT_OK, intent);

            LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));

            LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_FRIEND_MARK_CHANGE));

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