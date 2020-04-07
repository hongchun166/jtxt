package com.linkb.jstx.adapter.trend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.network.result.NewsDataResult;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpressAdapter extends RecyclerView.Adapter<ExpressAdapter.ExpressViewHolder> {

    private List<NewsDataResult.DataListBean> mNewsList = new ArrayList<>();
    private Context mContext;
    private ExpressCommentListener mExpressCommentListener;

    public ExpressAdapter(List<NewsDataResult.DataListBean> newsList, Context context, ExpressCommentListener expressCommentListener) {
        this.mNewsList = newsList;
        this.mContext = context;
        this.mExpressCommentListener = expressCommentListener;
    }

    @NonNull
    @Override
    public ExpressAdapter.ExpressViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_express, viewGroup, false);
        return new ExpressViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpressAdapter.ExpressViewHolder viewHolder, int i) {
        final NewsDataResult.DataListBean dataListBean = mNewsList.get(i);
        viewHolder.timeTv.setText(dataListBean.getTimestampStr());
        viewHolder.titleTv.setHtml(dataListBean.getTitle());
        viewHolder.contentTv.setHtml(dataListBean.getContent());
        viewHolder.sourceTv.setText(mContext.getResources().getString(R.string.express_source, dataListBean.getAuthor()));
        viewHolder.goodNewsNumber.setText(mContext.getResources().getString(R.string.good_news_number, dataListBean.getReplyCount3()));
        viewHolder.badNewsNumber.setText(mContext.getResources().getString(R.string.bad_news_number, dataListBean.getReplyCount4()));

        if(dataListBean.getLotteryAmount()!=null){
            viewHolder.viewGetRedBag.setEnabled(false);
            String getRedBagHint=mContext.getResources().getString(R.string.hint_red_receive_ed)+dataListBean.getLotteryAmount().doubleValue()+"KKC";
            viewHolder.viewGetRedBag.setText(getRedBagHint);
        }else {
            viewHolder.viewGetRedBag.setText(R.string.hint_red_receive);
            viewHolder.viewGetRedBag.setEnabled(true);
        }

        if (mExpressCommentListener != null){
            viewHolder.goodNewsNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mExpressCommentListener.onGoodNews(dataListBean);
                }
            });

            viewHolder.badNewsNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mExpressCommentListener.onBadNews(dataListBean);
                }
            });
//            viewHolder.shareImg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mExpressCommentListener.onShareNews(dataListBean);
//                }
//            });
            viewHolder.viewGetRedBag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mExpressCommentListener.onGetRedBag(dataListBean);
                }
            });
        }
    }

    public void updateRedStateSuc(long itemId,double amount ){
        for (NewsDataResult.DataListBean dataListBean : mNewsList) {
            if(dataListBean.getId()==itemId){
                dataListBean.setLotteryAmount(amount);
                break;
            }
        }
        notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    public class ExpressViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.time_text_view)
        TextView timeTv;
        @BindView(R.id.textView150)
        TextView sourceTv;
        @BindView(R.id.title_textView)
        HtmlTextView titleTv;
        @BindView(R.id.content_collapse_textView)
        HtmlTextView contentTv;
        @BindView(R.id.imageView42)
        ImageView shareImg;
        @BindView(R.id.button11)
        TextView goodNewsNumber;
        @BindView(R.id.button12)
        TextView badNewsNumber;
        @BindView(R.id.viewGetRedBag)
        TextView viewGetRedBag;

        public ExpressViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            shareImg.setVisibility(View.INVISIBLE);
        }
    }

    public interface ExpressCommentListener{
        void onGoodNews(NewsDataResult.DataListBean dataListBean);
        void onBadNews(NewsDataResult.DataListBean dataListBean);
        void onShareNews(NewsDataResult.DataListBean dataListBean);
        void onGetRedBag(NewsDataResult.DataListBean dataListBean);
    }
}
