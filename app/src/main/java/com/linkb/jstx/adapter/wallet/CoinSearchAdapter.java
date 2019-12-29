package com.linkb.jstx.adapter.wallet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.network.result.CoinSearchResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CoinSearchAdapter extends RecyclerView.Adapter<CoinSearchAdapter.SearchViewHolder> {

    private List<CoinSearchResult.DataBean> mList = new ArrayList<>();
    private Context mContext;
    private SearchCoinClickListener mListener;

    public CoinSearchAdapter(List<CoinSearchResult.DataBean> mList, Context mContext, SearchCoinClickListener mListener) {
        this.mList = mList;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search_coin, viewGroup, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchViewHolder, int i) {
        final CoinSearchResult.DataBean dataBean = mList.get(i);
        searchViewHolder.currencyNameTv.setText(dataBean.getEthToken());
        searchViewHolder.currencyValueTv.setText(dataBean.getAddress());
        searchViewHolder.addCoinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null){
                    mListener.onAddCoin(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class SearchViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.root_view)
        View rootView;

        @BindView(R.id.textView80)
        TextView currencyNameTv;

        @BindView(R.id.textView81)
        TextView currencyValueTv;

        @BindView(R.id.button3)
        TextView addCoinBtn;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    public interface SearchCoinClickListener {
        void onAddCoin(CoinSearchResult.DataBean dataBean);
    }


}
