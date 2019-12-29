package com.linkb.jstx.activity.wallet;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;

import butterknife.ButterKnife;

/**账单详情
* */
public class BillDetailActivity extends BaseActivity {
    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill_detail;
    }


}
