package com.linkb.jstx.adapter.wallet;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.result.ReceivedRedPacketListResult;
import com.linkb.jstx.network.result.v2.RedpackgeListRcvHistroyResult;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReceivedRedPacketAdapter extends RecyclerView.Adapter<ReceivedRedPacketAdapter.ReceivedViewHolder> {

    private List<RedpackgeListRcvHistroyResult.DataBean.ReceivListBean> mList = new ArrayList<>();
    User self;
    public ReceivedRedPacketAdapter(List<RedpackgeListRcvHistroyResult.DataBean.ReceivListBean> mList) {
        this.mList = mList;
        self= Global.getCurrentUser();
    }

    @NonNull
    @Override
    public ReceivedViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_received_red_packet, viewGroup, false);
        return new ReceivedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReceivedViewHolder receivedViewHolder, int i) {
        RedpackgeListRcvHistroyResult.DataBean.ReceivListBean dataBean = mList.get(i);
        receivedViewHolder.nameTv.setText(self.getName());
        receivedViewHolder.TimeTv.setText(dataBean.getReceiveTimerFinalStr());
        receivedViewHolder.moneyTv.setText(ConvertUtils.doubleToString(dataBean.getReceiverMoney()));
        receivedViewHolder.currencyTv.setText(dataBean.getCurrencyName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ReceivedViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textView82)
        TextView nameTv;
        @BindView(R.id.textView101)
        TextView TimeTv;
        @BindView(R.id.textView102)
        TextView moneyTv;
        @BindView(R.id.textView106)
        TextView currencyTv;


        public ReceivedViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
