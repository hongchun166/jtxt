package com.linkb.jstx.activity.wallet;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

/** 理财
* */
public class FinancialActivity extends BaseActivity {
    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_financial;
    }

    @OnClick(R.id.back_btn)
    public void onBackTv(){
        finish();
    }
}
