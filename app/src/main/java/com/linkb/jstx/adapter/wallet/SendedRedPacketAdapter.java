package com.linkb.jstx.adapter.wallet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.network.result.SendedRedPacketListResult;
import com.linkb.jstx.network.result.v2.RedpackgeListSndHistoryResult;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SendedRedPacketAdapter extends RecyclerView.Adapter<SendedRedPacketAdapter.SendedViewHolder> {

    private List<RedpackgeListSndHistoryResult.DataBean.SendListBean> mList = new ArrayList<>();
    private Context mContext;

    public SendedRedPacketAdapter(List<RedpackgeListSndHistoryResult.DataBean.SendListBean> mList, Context context) {
        this.mList = mList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public SendedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sended_red_packet, viewGroup, false);
        return new SendedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SendedViewHolder sendViewHolder, int i) {
        RedpackgeListSndHistoryResult.DataBean.SendListBean dataBean = mList.get(i);

        sendViewHolder.redPacketTypeTv.setText(dataBean.getType() == Constant.RedPacketType.COMMON_GROUP_LURKEY_RED_PACKET ? R.string.luck_red_packet : R.string.general_red_packet);
        sendViewHolder.timeTv.setText(dataBean.getSendTimeFinalStr());
        sendViewHolder.moneyTv.setText(ConvertUtils.doubleToString(dataBean.getMoney())+" "+ dataBean.getCurrencyName());
        if (dataBean.getState() == 5) {
            //有过期红包
            sendViewHolder.redPacketTimeOutStatusTv.setText(mContext.getResources().getString(R.string.time_out_red_packet_count, dataBean.getSendNumber() - dataBean.getSurplusNumber(), dataBean.getSendNumber()));
        }else {
            sendViewHolder.redPacketTimeOutStatusTv.setText(mContext.getResources().getString(R.string.red_packet_send_count, dataBean.getSendNumber() - dataBean.getSurplusNumber(), dataBean.getSendNumber()));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SendedViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textView82)
        TextView redPacketTypeTv;
        @BindView(R.id.textView101)
        TextView timeTv;
        @BindView(R.id.textView102)
        TextView moneyTv;
        @BindView(R.id.textView105)
        TextView redPacketTimeOutStatusTv;

        public SendedViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
