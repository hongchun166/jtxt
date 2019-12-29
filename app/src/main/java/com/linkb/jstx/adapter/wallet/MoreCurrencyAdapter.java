package com.linkb.jstx.adapter.wallet;

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
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.result.CurrencyListResult;
import com.linkb.jstx.network.result.QueryAssetsResult;
import com.linkb.jstx.util.ConvertUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.linkb.jstx.activity.wallet.ChargeCoinActivity.CURRENCY_ID;

public class MoreCurrencyAdapter extends RecyclerView.Adapter<MoreCurrencyAdapter.MoreCurrencyViewHolder> {

    private List<CurrencyListResult.DataListBean> mList = new ArrayList<>();
    private Context mContext;
    private CurrencySelectListener mListener;

    public MoreCurrencyAdapter(List<CurrencyListResult.DataListBean> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    public void setListener(CurrencySelectListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MoreCurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_currency, viewGroup, false);
        return new MoreCurrencyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoreCurrencyViewHolder moreCurrencyViewHolder, int i) {
        final CurrencyListResult.DataListBean dataListBean = mList.get(i);
        moreCurrencyViewHolder.coinImageView.load(BuildConfig.API_HOST + dataListBean.getCurrencyIcon(), R.mipmap.btc);
        moreCurrencyViewHolder.currencyNameTv.setText(dataListBean.getCurrencyMark().toUpperCase());
        moreCurrencyViewHolder.currencyValueTv.setText(ConvertUtils.doubleToString(dataListBean.getAmount()));
        moreCurrencyViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onCurrencySelected(dataListBean);
                }
            }
        });
        moreCurrencyViewHolder.chargeCoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChargeCoinActivity.class);
                intent.putExtra(CURRENCY_ID, dataListBean.getId());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void addAll(List<CurrencyListResult.DataListBean> list) {
        int index = mList.size();
        mList.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }

    public void replaceAll(List<CurrencyListResult.DataListBean> list){
        mList.clear();
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public class MoreCurrencyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.root_view)
        View rootView;

        @BindView(R.id.imageView27)
        WebImageView coinImageView;

        @BindView(R.id.textView80)
        TextView currencyNameTv;

        @BindView(R.id.textView81)
        TextView currencyValueTv;

        @BindView(R.id.button3)
        TextView chargeCoinBtn;

        public MoreCurrencyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface CurrencySelectListener{
        void  onCurrencySelected(CurrencyListResult.DataListBean dataListBean);
    }
}
