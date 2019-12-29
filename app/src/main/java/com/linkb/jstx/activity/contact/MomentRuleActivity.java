
package com.linkb.jstx.activity.contact;


import android.annotation.SuppressLint;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.model.MomentRule;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.database.MomentRuleRepository;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.R;

/**
 * 消息配置
 */
public class MomentRuleActivity extends BaseActivity implements OnCheckedChangeListener, HttpRequestListener {

    public static final int REQUEST_CODE = 7583;
    private Friend mFriend;
    private String mSelfAccount;
    private MomentRule mRule;

    @Override
    @SuppressLint("NewApi")
    public void initComponents() {

        mFriend = (Friend) getIntent().getSerializableExtra(Friend.NAME);
        mSelfAccount = Global.getCurrentAccount();

        if (MomentRuleRepository.hasExist(mSelfAccount, mFriend.account, MomentRule.TYPE_0)) {
            ((Switch) findViewById(R.id.momentShieldSwitch)).setChecked(true);
        }

        if (MomentRuleRepository.hasExist(mSelfAccount, mFriend.account, MomentRule.TYPE_1)) {
            ((Switch) findViewById(R.id.momentIgnoreSwitch)).setChecked(true);
        }

        ((Switch) findViewById(R.id.momentIgnoreSwitch)).setOnCheckedChangeListener(this);
        ((Switch) findViewById(R.id.momentShieldSwitch)).setOnCheckedChangeListener(this);
    }


    @Override
    public void onDestroy() {
        setResult(RESULT_OK);
        super.onDestroy();
    }


    @Override
    public void onCheckedChanged(CompoundButton arg0, boolean flag) {
        if (arg0.getId() == R.id.momentShieldSwitch) {
            performSettingRuleRequest(flag, MomentRule.TYPE_0);
        }
        if (arg0.getId() == R.id.momentIgnoreSwitch) {
            performSettingRuleRequest(flag, MomentRule.TYPE_1);
        }
    }

    private void performSettingRuleRequest(boolean isOpen, String type) {

        showProgressDialog(getString(R.string.tip_save_loading));

        mRule = new MomentRule();
        mRule.type = type;
        mRule.source = mSelfAccount;
        mRule.target = mFriend.account;

        if (!isOpen){
            HttpServiceManager.removeMomentRule(mFriend.account,type,this);
        }else {
            HttpServiceManager.settingMomentRule(mFriend.account,type,this);
        }
    }




    @Override
    public int getContentLayout() {
        return R.layout.activity_moment_rule;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_moment_rule;
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        if (call.equalsPost(URLConstant.MOMENT_RULE_URL)) {
            MomentRuleRepository.add(mRule);
        }
        if (call.equalsDelete(URLConstant.MOMENT_RULE_URL)) {
            MomentRuleRepository.remove(mRule);
        }
        hideProgressDialog();
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }

}
