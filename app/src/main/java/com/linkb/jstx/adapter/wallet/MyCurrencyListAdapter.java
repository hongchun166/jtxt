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
import com.linkb.jstx.network.result.v2.ListMyCurrencyResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyCurrencyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<ListMyCurrencyResult.DataBean> mDate = new ArrayList<>();
    public MyCurrencyListAdapter(Context mContext, List<ListMyCurrencyResult.DataBean> mDate) {
        this.mDate.addAll(mDate);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_currency, viewGroup, false);
        return new MyCurrencyListHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        MyCurrencyListHolder holder= (MyCurrencyListHolder) viewHolder;
        ListMyCurrencyResult.DataBean dataBean=mDate.get(position);
        holder.viewCurrencyName.setText(dataBean.getCurrencyName());
        holder.viewCurrencyQuota.setText(String.valueOf(dataBean.getLockBalance()));
        holder.viewCurrencyEffective.setText(String.valueOf(dataBean.getBalance()));
        holder.viewCurrencyImg.load(BuildConfig.API_HOST + dataBean.getCurrencyIcon(), R.mipmap.btc);
    }
    @Override
    public int getItemCount() {
        return mDate.size();
    }
    public void addAll(List<ListMyCurrencyResult.DataBean> list) {
        int index = mDate.size();
        mDate.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }
    public void replaceAll(List<ListMyCurrencyResult.DataBean> list){
        mDate.clear();
        mDate.addAll(list);
        notifyDataSetChanged();
    }

    public class MyCurrencyListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.viewCurrencyImg)
        WebImageView viewCurrencyImg;
        @BindView(R.id.viewCurrencyItem) View viewCurrencyItem;
        @BindView(R.id.viewCurrencyName) TextView viewCurrencyName;
        @BindView(R.id.viewCurrencyQuota) TextView viewCurrencyQuota;
        @BindView(R.id.viewCurrencyEffective) TextView viewCurrencyEffective;

        public MyCurrencyListHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this ,itemView);
            viewCurrencyItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
