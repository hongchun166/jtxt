package com.linkb.jstx.adapter.wallet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.activity.wallet.ChargeCoinActivity;
import com.linkb.jstx.activity.wallet.WithdrawCoinActivity;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.result.QueryAssetsResult;
import com.linkb.jstx.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.linkb.jstx.activity.wallet.ChargeCoinActivity.CURRENCY_ID;

public class CoinExchangeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<QueryAssetsResult.DataListBean> mDate = new ArrayList<>();
    private Context mContext;
    private Boolean enableHideEmptyCurrency = false;

    public CoinExchangeListAdapter(Context mContext, List<QueryAssetsResult.DataListBean> mDate) {
        this.mDate.addAll(mDate);
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_coin_exchange_price, viewGroup, false);
        return new CoinExchangeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CoinExchangeViewHolder ){
            CoinExchangeViewHolder holder = (CoinExchangeViewHolder) viewHolder;
            final QueryAssetsResult.DataListBean dataListBean = mDate.get(i);
            holder.coinText.setText(dataListBean.getCurrency().getCurrencyMark() + "\t" + " " + dataListBean.getCurrency().getCurrencyName());
            holder.withdrawCoinLogo.load(BuildConfig.API_HOST + dataListBean.getCurrency().getCurrencyIcon(), R.mipmap.btc);
            holder.coinExchangeBalanceText.setText(ConvertUtils.doubleToString(dataListBean.getAmount()));
            holder.coinRmbBalanceText.setText(String.valueOf(dataListBean.getPrice()));
            holder.coinLockBalanceText.setText(ConvertUtils.doubleToString(dataListBean.getFrozenAmount()));
            holder.coinAvailableBalanceText.setText(ConvertUtils.doubleToString(dataListBean.getAmount() - dataListBean.getFrozenAmount()));

            holder.chargeCoinFLy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChargeCoinActivity.class);
                    intent.putExtra(CURRENCY_ID, dataListBean.getCurrency().getId());
                    mContext.startActivity(intent);
                }
            });
            holder.withdrawCoinFly.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, WithdrawCoinActivity.class);
                    intent.putExtra(QueryAssetsResult.DataListBean.class.getName(), dataListBean);
                    ((Activity)mContext).startActivityForResult(intent, 0x10);
                }
            });

            if (enableHideEmptyCurrency && dataListBean.getPrice() == 0){
                holder.rootView.setVisibility(View.GONE);
            }else {
                holder.rootView.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (enableHideEmptyCurrency){
            int NoEmptyCurrencyCounter = 0;
            for (int i = 0; i < mDate.size(); i++) {
                if (mDate.get(i).getCurrency().getTotalAmount() > 0){
                    NoEmptyCurrencyCounter++;
                }else {
                    continue;
                }
            }
            return NoEmptyCurrencyCounter;
        }else {
            return mDate.size();
        }
    }

    public void addAll(List<QueryAssetsResult.DataListBean> list) {
        int index = mDate.size();
        mDate.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }

    public void replaceAll(List<QueryAssetsResult.DataListBean> list){
        mDate.clear();
        mDate.addAll(list);
        notifyDataSetChanged();
    }

    public class CoinExchangeViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.coin_logo_image_view)
        WebImageView withdrawCoinLogo;
        @BindView(R.id.coin_name_text_view) TextView coinText;
        @BindView(R.id.coin_exchange_value_tv) TextView coinExchangeBalanceText;
        @BindView(R.id.coin_rmb_value_tv) TextView coinRmbBalanceText;
        @BindView(R.id.coin_available_tv) TextView coinAvailableBalanceText;
        @BindView(R.id.coin_lock_tv) TextView coinLockBalanceText;
        @BindView(R.id.charge_coin_fly) View chargeCoinFLy;
        @BindView(R.id.withdraw_coin_fly) View withdrawCoinFly;
        @BindView(R.id.root_view) View rootView;

        public CoinExchangeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this ,itemView);
        }
    }
}
