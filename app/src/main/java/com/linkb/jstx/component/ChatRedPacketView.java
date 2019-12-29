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
import com.linkb.jstx.fragment.RedPacketFragment;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.QueryRedPacketStatusResult;
import com.linkb.jstx.network.result.SendRedPacketResult;

import static com.linkb.jstx.network.result.QueryRedPacketStatusResult.REDPACKET_STATUS;
import static com.linkb.jstx.network.result.QueryRedPacketStatusResult.RED_PACKET_MIME;
import static com.linkb.jstx.network.result.QueryRedPacketStatusResult.RED_PACKET_NO_EXITED;
import static com.linkb.jstx.network.result.QueryRedPacketStatusResult.RED_PACKET_RECEIVEDED;
import static com.linkb.jstx.network.result.QueryRedPacketStatusResult.RED_PACKET_RECEIVEDED_BY;
import static com.linkb.jstx.network.result.QueryRedPacketStatusResult.RED_PACKET_RECEIVEDED_EMPTY;

public class ChatRedPacketView extends CardView implements View.OnClickListener {

    private View backGroundView;
    private TextView remarkTv;

    /** 是否已经领取过红包，false表示未领取
    * */
    private Boolean enableOpenStatus = false;

    private Message message;

    private SendRedPacketResult.DataBean mDataBean;

    /** 红包状态， 0 ： 可领取， 1： 不存在， 2：已过期， 3： 已领完 ， 4：已领取
     * */
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



    public void initRedPacket(SendRedPacketResult.DataBean dataBean, Message msg){
        this.message = msg;
        this.mDataBean = dataBean;
        if (dataBean != null && !TextUtils.isEmpty(dataBean.getRemark())) {
            remarkTv.setText(dataBean.getRemark());
        }

        //查询红包是否已经被领取成功
        HttpServiceManager.queryRedPacketEnabled(mDataBean.getUserAccount(), mDataBean.getRedFlag(), mQueryRedPacketEnabledListener);
    }

    @Override
    public void onClick(View view) {
        //查询红包是否已经被领取成功
        clickRedPacket = true;
        HttpServiceManager.queryRedPacketEnabled(mDataBean.getUserAccount(), mDataBean.getRedFlag(), mQueryRedPacketEnabledListener);
    }

    private void receiveRedPacket(){
        if (enableOpenStatus || mRedPacketStatus == RED_PACKET_MIME){
            //红包已经领取过, 直接进入领取详情页面
            Intent intent = new Intent(getContext(), RedPacketReceivedActivity.class);
            intent.putExtra(SendRedPacketResult.DataBean.class.getName(), mDataBean);
            intent.putExtra(REDPACKET_STATUS, mRedPacketStatus);
            getContext().startActivity(intent);
        }else {
            //红包有效，可以领取
            if (mRedPacketStatus != RED_PACKET_NO_EXITED){
                if (mRedPacketFragment == null){
                    mRedPacketFragment = RedPacketFragment.getInstance(mDataBean, mRedPacketStatus);
                }
                mRedPacketFragment.show(((FragmentActivity)getContext()).getSupportFragmentManager(), "RedPacketFragment");

                updateRedPackedStatus(true);
            }else {
                Toast.makeText(getContext(), getResources().getString(R.string.red_packet_un_invailable), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateRedPackedStatus(boolean enableOpen){
        if (enableOpen){
            backGroundView.setBackgroundColor(getResources().getColor(R.color.background_yellow_FCE1C4));
        }else {
            backGroundView.setBackgroundColor(getResources().getColor(R.color.background_yellow_FEA841));
        }
    }

    private HttpRequestListener<QueryRedPacketStatusResult> mQueryRedPacketEnabledListener = new HttpRequestListener<QueryRedPacketStatusResult>() {
        @Override
        public void onHttpRequestSucceed(QueryRedPacketStatusResult result, OriginalCall call) {
            if (result.isSuccess()){
                mRedPacketStatus = result.getData();
                //红包没有被用户领取状态
                enableOpenStatus = (result.getData() == RED_PACKET_RECEIVEDED || result.getData() == RED_PACKET_RECEIVEDED_EMPTY || result.getData() == RED_PACKET_RECEIVEDED_BY);
                updateRedPackedStatus(enableOpenStatus);
                if (clickRedPacket){
                    clickRedPacket = false;
                    receiveRedPacket();
                }
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };
}
