package com.linkb.jstx.activity.wallet;

import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.result.CoinTransferResult;
import com.linkb.jstx.util.ConvertUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 转账领取详情
* */
public class CoinTransferReceivedDetailActivity extends BaseActivity {

    @BindView(R.id.textView165)
    TextView receivedAmountTv;
    @BindView(R.id.textView166)
    TextView receivedTipsTv;

    private CoinTransferResult.DataBean mData;
    private User mSelf;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        mSelf = Global.getCurrentUser();
        mData = (CoinTransferResult.DataBean) getIntent().getSerializableExtra(CoinTransferResult.DataBean.class.getName());
        receivedAmountTv.setText(getString(R.string.coin_transfer_to_amount, ConvertUtils.doubleToString(mData.getSendMoney())));
        if (mSelf.account.equals(mData.getUserAccount())){
            receivedTipsTv.setText(getString(R.string.coin_transfer_received_tips));
        }else {
            receivedTipsTv.setText(getString(R.string.coin_transfer_store_to_wallet));
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_coin_transfer_received_detail;
    }

    @OnClick(R.id.back_btn)
    public void onBack() {finish();}

}
