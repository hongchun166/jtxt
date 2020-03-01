package com.linkb.jstx.adapter.trend;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.chat.MMWebViewActivity;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.result.NewsDataResult;
import com.linkb.jstx.util.TimeUtils;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    public static interface OnItemClickCallback{
        void onItemClickCallback(View view,NewsDataResult.DataListBean bean);
    }
    private List<NewsDataResult.DataListBean> mNewsList = new ArrayList<>();
    private Context mContext;
    OnItemClickCallback onItemClickCallback;

    public NewsAdapter(List<NewsDataResult.DataListBean> newsList, Context context) {
        this.mNewsList = newsList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public NewsAdapter.NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_news, viewGroup, false);
        return new NewsAdapter.NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.NewsViewHolder viewHolder, int position) {
        final NewsDataResult.DataListBean dataListBean = mNewsList.get(position);
        viewHolder.scaleWebImage.load(dataListBean.getLitimg(), R.mipmap.img_chart, 10);
        viewHolder.contentTv.setHtml(dataListBean.getTitle());
        viewHolder.sourceTv.setText(dataListBean.getAuthor());
        viewHolder.timeTv.setText(TimeUtils.getTimeAgo(dataListBean.getTimestamp(), mContext));
        viewHolder.goodReceptionNumberTv.setText(mContext.getResources().getString(R.string.good_reception_number, dataListBean.getReplyCount1()));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(view,dataListBean);
            }
        });
        viewHolder.contentTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick(view,dataListBean);
            }
        });
    }

    private void onItemClick(View view,NewsDataResult.DataListBean dataListBean) {
        if (!TextUtils.isEmpty(dataListBean.getUrl())) {
            Intent intent = new Intent(mContext, MMWebViewActivity.class);
            intent.setData(Uri.parse(dataListBean.getUrl()));
            mContext.startActivity(intent);
        }else {
            if(onItemClickCallback!=null){
                onItemClickCallback.onItemClickCallback(view,dataListBean);
            }
        }
    }

    public void addAll(List<NewsDataResult.DataListBean> list) {
        if (mNewsList.equals(list)) {
            return;
        }
        mNewsList.clear();
        mNewsList.addAll(list);
        notifyDataSetChanged();
    }

    public void add(NewsDataResult.DataListBean dataListBean) {
        mNewsList.add(dataListBean);
        notifyItemInserted(mNewsList.size()-1);
    }

    public void remove(NewsDataResult.DataListBean dataListBean) {
        int index = mNewsList.indexOf(dataListBean);
        if (index >=0 ){
            this.mNewsList.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imageView41)
        WebImageView scaleWebImage;
        @BindView(R.id.textView145)
        HtmlTextView contentTv;
        @BindView(R.id.textView146)
        TextView sourceTv;
        @BindView(R.id.textView148)
        TextView timeTv;
        @BindView(R.id.textView149)
        TextView goodReceptionNumberTv;


        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
