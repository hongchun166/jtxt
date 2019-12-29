package com.linkb.jstx.adapter.wallet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.result.WithdrawBillResult;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WithdrawBillAdapter extends RecyclerView.Adapter<WithdrawBillAdapter.WithdrawBillViewHolder> {

    private List<WithdrawBillResult.DataBean>  mList = new ArrayList<>();
    private Context mContext;

    public WithdrawBillAdapter(List<WithdrawBillResult.DataBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public WithdrawBillViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_withdraw_bill, viewGroup, false);
        return new WithdrawBillViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WithdrawBillViewHolder withdrawBillViewHolder, int i) {
        WithdrawBillResult.DataBean dataBean = mList.get(i);

        withdrawBillViewHolder.coinIconImageView.load(BuildConfig.API_HOST + dataBean.getIcon(), R.mipmap.btc);
        withdrawBillViewHolder.currencyName.setText(dataBean.getCurrencyMark());
        withdrawBillViewHolder.transferAddressTv.setText(dataBean.getWallet_address());
        withdrawBillViewHolder.transferMoneyTv.setText(ConvertUtils.doubleToString(dataBean.getMoney()));
        withdrawBillViewHolder.dateTV.setText(TimeUtils.millis2String(dataBean.getAdd_date(), TimeUtils.getCustomFormat2()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void replaceAll(List<WithdrawBillResult.DataBean> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public class WithdrawBillViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView27)
        WebImageView coinIconImageView;
        @BindView(R.id.textView80)
        TextView currencyName;
        @BindView(R.id.textView81)
        TextView transferAddressTv;
        @BindView(R.id.textView82)
        TextView transferMoneyTv;
        @BindView(R.id.textView83)
        TextView dateTV;


        public WithdrawBillViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
