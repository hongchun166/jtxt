package com.linkb.jstx.component;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.linkb.R;
import com.linkb.jstx.activity.wallet.RedPacketReceivedActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.fragment.RedPacketFragment;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.QueryRedPacketStatusResult;
import com.linkb.jstx.network.result.v2.RedpackgeGetInfoResult;
import com.linkb.jstx.network.result.v2.SendRedPacketResultV2;

import java.util.Objects;


public class ChatRedPacketView extends CardView implements View.OnClickListener {

    private View backGroundView;
    private TextView remarkTv;

    /**
     * 是否已经领取过红包，false表示未领取
     */
    private Boolean enableOpenStatus = false;

    private Message message;

    private SendRedPacketResultV2.DataBean mDataBean;

    /**
     * 红包状态， 0 ： 可领取， 1： 不存在， 2：已过期， 3： 已领完 ， 4：已领取
     */
    private int mRedPacketStatus;

    private RedPacketFragment mRedPacketFragment;

    private boolean clickRedPacket = false;

    public ChatRedPacketView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        backGroundView = this.findViewById(R.id.red_packet_content_fly);
        remarkTv = this.findViewById(R.id.remark_tv);
    }


    public void initRedPacket(SendRedPacketResultV2.DataBean dataBean, Message msg) {
        this.message = msg;
        this.mDataBean = dataBean;
        if (dataBean != null && !TextUtils.isEmpty(dataBean.getRemark())) {
            remarkTv.setText(dataBean.getRemark());
        }

        //查询红包是否已经被领取成功
        HttpServiceManagerV2.redpackgeGetInfo(String.valueOf(mDataBean.getId()), mQueryRedPacketEnabledListener);
    }

    @Override
    public void onClick(View view) {
        //查询红包是否已经被领取成功
        clickRedPacket = true;
        HttpServiceManagerV2.redpackgeGetInfo(String.valueOf(mDataBean.getId()), mQueryRedPacketEnabledListener);
    }

    private void receiveRedPacket(RedpackgeGetInfoResult result) {
        if (Objects.requireNonNull(Global.getCurrentUser()).account.equals(mDataBean.getSendAccount()) ||enableOpenStatus) {
            //红包已经领取过, 直接进入领取详情页面
            Intent intent = new Intent(getContext(), RedPacketReceivedActivity.class);
            intent.putExtra(RedpackgeGetInfoResult.DataBean.class.getName(), result.getData());
            intent.putExtra(QueryRedPacketStatusResult.REDPACKET_STATUS, mRedPacketStatus);
            getContext().startActivity(intent);
        } else {
            //红包有效，可以领取
            if (mRedPacketFragment == null) {
                mRedPacketFragment = RedPacketFragment.getInstance(result.getData(), mRedPacketStatus);
            }
            mRedPacketFragment.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "RedPacketFragment");
            updateRedPackedStatus(true);
        }
    }

    private void updateRedPackedStatus(boolean enableOpen) {
        if (enableOpen) {
            backGroundView.setBackgroundColor(getResources().getColor(R.color.background_yellow_FCE1C4));
        } else {
            backGroundView.setBackgroundColor(getResources().getColor(R.color.background_yellow_FEA841));
        }
    }

    private HttpRequestListener<RedpackgeGetInfoResult> mQueryRedPacketEnabledListener = new HttpRequestListener<RedpackgeGetInfoResult>() {
        @Override
        public void onHttpRequestSucceed(RedpackgeGetInfoResult result, OriginalCall call) {
            if (result.isSuccess()) {
                mRedPacketStatus = result.getData().getState();
                //红包没有被用户领取状态
                enableOpenStatus = (mRedPacketStatus ==QueryRedPacketStatusResult.RED_PACKET_Receiveed);
                updateRedPackedStatus(enableOpenStatus);
                if (clickRedPacket) {
                    clickRedPacket = false;
                    receiveRedPacket(result);
                }
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };
}
