package com.linkb.jstx.adapter.trend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.model.Information;
import com.linkb.jstx.model.gsonmodel.InformationResponse;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.ArrayList;
import java.util.List;

/** 资讯列表
* */
public class InformationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
    * */
    private static final int HEADER = 0x55;
    private static final int CONTENT = 0x65;

    private Context mContext;
    private List<Information>  informationList = new ArrayList<>();

    public InformationAdapter(Context mContext, List<Information> informationList) {
        this.mContext = mContext;
        this.informationList = informationList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == HEADER) {
            View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_time_header, viewGroup, false);
            return new TimeHeaderViewHolder(view);
        }else{
            View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_information, viewGroup, false);
            return new InformationViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if ( viewHolder instanceof InformationViewHolder){
            final InformationViewHolder informationViewHolder = (InformationViewHolder) viewHolder;
            Information information = informationList.get(i - 1);
            informationViewHolder.titleTv.setText(information.getTitle());

            informationViewHolder.contentCollapsTv.setHtml(information.getContent());

            informationViewHolder.contentTv.setHtml(information.getContent());
            informationViewHolder.contentCollapsTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    informationViewHolder.contentCollapsTv.setVisibility(View.GONE);
                    informationViewHolder.contentTv.setVisibility(View.VISIBLE);
                }
            });
            informationViewHolder.contentTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    informationViewHolder.contentCollapsTv.setVisibility(View.VISIBLE);
                    informationViewHolder.contentTv.setVisibility(View.GONE);
                }
            });

            informationViewHolder.timeTv.setText(information.getTimestampStr());
        }else if (viewHolder instanceof TimeHeaderViewHolder){
            Information information = informationList.get(i);
            TimeHeaderViewHolder timeHeaderViewHolder = (TimeHeaderViewHolder) viewHolder;
            timeHeaderViewHolder.timeTextView.setText(information.getTimestampHeaderStr());
        }else {
            try {
                throw  new  Exception("can' t find suit viewHolder");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void addAll(List<Information> list) {
        int index = informationList.size();
        informationList.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }

    public void replaceAll(List<Information> list){
        informationList.clear();
        informationList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? HEADER : CONTENT;
    }

    @Override
    public int getItemCount() {
        return informationList.size() > 0 ? informationList.size() + 1 : 0;
    }

    class TimeHeaderViewHolder extends RecyclerView.ViewHolder{

        TextView timeTextView;

        public TimeHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTextView = itemView.findViewById(R.id.time_text_view);
        }
    }

    class InformationViewHolder extends RecyclerView.ViewHolder{
        TextView timeTv, titleTv;
        HtmlTextView contentTv, contentCollapsTv;

        public InformationViewHolder(@NonNull View itemView) {
            super(itemView);
            timeTv = itemView.findViewById(R.id.time_text_view);
            titleTv = itemView.findViewById(R.id.title_textView);
            contentTv = itemView.findViewById(R.id.content_textView);
            contentCollapsTv = itemView.findViewById(R.id.content_collapse_textView);
        }
    }


}
