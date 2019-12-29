package com.linkb.jstx.adapter.wallet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.result.QueryAssetsResult;
import com.linkb.jstx.network.result.RedPacketReceivedMemberResult;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.jstx.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RedPacketReceiveDetailAdapter extends RecyclerView.Adapter {

    private static final int HEADER = 0;
    private static final int CONTENT = 1;

    private List<RedPacketReceivedMemberResult.DataBean> mList = new ArrayList<>();
    private Context mContext;
    /**红包总数
     * */
    private int mRedPacketTotalCount;

    public RedPacketReceiveDetailAdapter( Context mContext, List<RedPacketReceivedMemberResult.DataBean> mList, int totalCount) {
        this.mList = mList;
        this.mContext = mContext;
        this.mRedPacketTotalCount = totalCount;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == HEADER){
            View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_red_packet_receive_detail_header, viewGroup, false);
            return new RedPacketReceiveDetailViewHeaderHolder(view);
        }else {
            View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_red_packet_receive_detail, viewGroup, false);
            return new RedPacketReceiveDetailViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof  RedPacketReceiveDetailViewHeaderHolder){
            RedPacketReceiveDetailViewHeaderHolder headerHolder = (RedPacketReceiveDetailViewHeaderHolder) viewHolder;
            headerHolder.headerTv.setText(mContext.getResources().getString(R.string.red_packet_open_detail, mList.size(), mRedPacketTotalCount));
        }else if (viewHolder instanceof  RedPacketReceiveDetailViewHolder){
            RedPacketReceiveDetailViewHolder holder = (RedPacketReceiveDetailViewHolder) viewHolder;
            RedPacketReceivedMemberResult.DataBean dataBean = mList.get(i - 1);

            holder.avatarImg.load(FileURLBuilder.getUserIconUrl(dataBean.getAccount()), R.mipmap.lianxiren);
            holder.nameTv.setText(dataBean.getUsername());
            holder.timeTv.setText(TimeUtils.millis2String(dataBean.getCreateTime(), TimeUtils.getDefaultFormat()));
            holder.receivedRedPacketTv.setText(mContext.getResources().getString(R.string.red_packet_receive_money,
                    ConvertUtils.doubleToString(dataBean.getMoney()), dataBean.getCurrencyName()));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        }else {
            return CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    public void addAll(List<RedPacketReceivedMemberResult.DataBean> list) {
        int index = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }

    public void replaceAll(List<RedPacketReceivedMemberResult.DataBean> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public class RedPacketReceiveDetailViewHeaderHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.textView74)
        TextView headerTv;
        public RedPacketReceiveDetailViewHeaderHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class RedPacketReceiveDetailViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imageView24)
        WebImageView avatarImg;
        @BindView(R.id.textView71)
        TextView nameTv;
        @BindView(R.id.textView72)
        TextView timeTv;
        @BindView(R.id.textView73)
        TextView receivedRedPacketTv;

        public RedPacketReceiveDetailViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
