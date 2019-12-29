package com.linkb.jstx.adapter.wallet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;

import java.util.ArrayList;
import java.util.List;

public class ExchangeRateAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mDate = new ArrayList<>();
    private Context mContext;

    public ExchangeRateAdapter(Context mContext, List<String> mDate) {
        this.mDate = mDate;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_exchange_rate, viewGroup, false);
        return new CoinExchangeRateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof CoinExchangeRateViewHolder) {
            CoinExchangeRateViewHolder holder = (CoinExchangeRateViewHolder) viewHolder;
            holder.coinTypeText.setText(mDate.get(i));
        }
    }

    @Override
    public int getItemCount() {
        return mDate.size();
    }

    public void addAll(List<String> list) {
        int index = mDate.size();
        mDate.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }

    public void replaceAll(List<String> list) {
        mDate.clear();
        mDate.addAll(list);
        notifyDataSetChanged();
    }

    public class CoinExchangeRateViewHolder extends RecyclerView.ViewHolder {

        public TextView coinTypeText;

        public CoinExchangeRateViewHolder(@NonNull View itemView) {
            super(itemView);
            coinTypeText = itemView.findViewById(R.id.coin_type_textView);
        }
    }
}
