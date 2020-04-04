package com.linkb.jstx.component;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linkb.R;
import com.linkb.jstx.activity.wallet.CoinTransferReceivedDetailActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.CoinReceiveResult;
import com.linkb.jstx.network.result.CoinTransferResult;
import com.linkb.jstx.network.result.QueryCoinTransferStatusResult;
import com.linkb.jstx.util.ConvertUtils;

import static com.linkb.jstx.network.result.QueryCoinTransferStatusResult.COIN_TRANSFER_AVAILABLE;
import static com.linkb.jstx.network.result.QueryCoinTransferStatusResult.COIN_TRANSFER_NO_EXITED;
import static com.linkb.jstx.network.result.QueryCoinTransferStatusResult.COIN_TRANSFER_RECEIVEDED_BY;
import static com.linkb.jstx.network.result.QueryCoinTransferStatusResult.COIN_TRANSFER_TIME_OUT;


/** 个人转账布局
* */
public class ChatCoinTransferView extends CardView implements View.OnClickListener{

    private View backGroundView;
    private TextView remarkTv;
    private TextView coinTransferAmountTv;

    /** 转账状态， 0 ： 可领取， 1： 不存在， 2：已过期，自动退回 3： 对方已经领取 ， 4：本人不可领取
     * */
    private int mRedPacketStatus;

    /** 是否已经领取过转账，false表示未领取
     * */
    private Boolean enableOpenStatus = false;
    private Message message;
    private CoinTransferResult.DataBean mDataBean;

    private boolean clickRedPacket = false;
    private User mSelf;

    public ChatCoinTransferView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mSelf = Global.getCurrentUser();
        coinTransferAmountTv = this.findViewById(R.id.coin_transfer_amount_tv);
        remarkTv = this.findViewById(R.id.remark_tv);
        backGroundView = this.findViewById(R.id.background_transfer);
    }

    public void initCoinTransfer(CoinTransferResult.DataBean dataBean, Message msg) {
        this.message = msg;
        this.mDataBean = dataBean;
        if (dataBean != null && !TextUtils.isEmpty(dataBean.getRemark())) {
            remarkTv.setText(dataBean.getRemark());
        }
        if (dataBean != null &&  dataBean.getSendMoney() > 0) {
            coinTransferAmountTv.setText(getContext().getString(R.string.coin_transfer_to_amount, ConvertUtils.doubleToString(dataBean.getSendMoney())));
        }

        //查询转账是否已经被领取成功
        HttpServiceManager.queryCoinTransferEnabled(mSelf.account, mDataBean.getUserAccount(), mDataBean.getRedFlag(), mQueryRedPacketEnabledListener);
    }

    @Override
    public void onClick(View view) {
        //查询红包是否已经被领取成功
//        HttpServiceManager.receiveTransfer(mSelf.account, mDataBean.getCurrencyId() + "", mDataBean.getRedFlag(), mDataBean.getUserAccount(),mCoinTransferReceiveListener);
        clickRedPacket = true;
        HttpServiceManager.queryCoinTransferEnabled(mSelf.account, mDataBean.getUserAccount(), mDataBean.getRedFlag(), mQueryRedPacketEnabledListener);
    }


    private void updateRedPackedStatus(boolean enableOpen){
        if (enableOpen){
            backGroundView.setBackgroundColor(getResources().getColor(R.color.background_yellow_FCE1C4));
        }else {
            backGroundView.setBackgroundColor(getResources().getColor(R.color.background_yellow_FEA841));
        }
    }

    private HttpRequestListener<QueryCoinTransferStatusResult> mQueryRedPacketEnabledListener = new HttpRequestListener<QueryCoinTransferStatusResult>() {
        @Override
        public void onHttpRequestSucceed(QueryCoinTransferStatusResult result, OriginalCall call) {
            if (result.isSuccess()){
                mRedPacketStatus = result.getData();
                //红包没有被用户领取状态
                enableOpenStatus = (result.getData() == COIN_TRANSFER_NO_EXITED || result.getData() == COIN_TRANSFER_TIME_OUT
                        || result.getData() == COIN_TRANSFER_RECEIVEDED_BY);
                updateRedPackedStatus(enableOpenStatus);
                if (clickRedPacket){
                    clickRedPacket = false;
                    receiveCoinTransfer();
                }
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    private void receiveCoinTransfer(){
        if (mRedPacketStatus == COIN_TRANSFER_RECEIVEDED_BY){
            //直接进入领取详情页面
            Intent intent = new Intent(getContext(), CoinTransferReceivedDetailActivity.class);
            intent.putExtra(CoinTransferResult.DataBean.class.getName(), mDataBean);
            getContext().startActivity(intent);
        }else if (mSelf.account.equals(mDataBean.getUserAccount()) ){
            Toast.makeText(getContext(), getResources().getString(R.string.self_can_not_receive_coin_transfer), Toast.LENGTH_SHORT).show();
        } else if (!enableOpenStatus){
            //转账有效，可以领取
             HttpServiceManager.receiveTransfer(mSelf.account, mDataBean.getCurrencyId() + "", mDataBean.getRedFlag(), mDataBean.getUserAccount(),mCoinTransferReceiveListener);
             updateRedPackedStatus(true);
        }else {
             Toast.makeText(getContext(), getResources().getString(R.string.coin_transfer_invailable), Toast.LENGTH_SHORT).show();
        }
    }

    private HttpRequestListener<CoinReceiveResult> mCoinTransferReceiveListener = new HttpRequestListener<CoinReceiveResult>() {
        @Override
        public void onHttpRequestSucceed(CoinReceiveResult result, OriginalCall call) {
            if (result.isSuccess()){
                Intent intent = new Intent(getContext(), CoinTransferReceivedDetailActivity.class);
                intent.putExtra(CoinTransferResult.DataBean.class.getName(), mDataBean);
                getContext().startActivity(intent);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };
}
