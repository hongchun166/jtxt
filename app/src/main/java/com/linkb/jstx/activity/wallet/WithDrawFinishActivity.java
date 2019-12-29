package com.linkb.jstx.activity.wallet;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class WithDrawFinishActivity extends BaseActivity {


    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_withdraw_finish;
    }

    @OnClick(R.id.back_btn)
    public void backWalletPage(){
        finish();
    }

    @OnClick(R.id.button4)
    public void backWalletHomePage(){
        finish();
    }
}
