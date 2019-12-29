package com.linkb.jstx.adapter.trend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.network.result.NearlyPeopleResult;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NearlyPeopleAdapter extends RecyclerView.Adapter<NearlyPeopleAdapter.NearlyPeopleViewHolder> {


    private List<NearlyPeopleResult.DataBean> mNearlyPeopleList = new ArrayList<>();
    private OnItemClickedListener<NearlyPeopleResult.DataBean> onItemClickedListener;
    private Context mContext;

    public NearlyPeopleAdapter(List<NearlyPeopleResult.DataBean> mNearlyPeopleList, Context mContext) {
        this.mNearlyPeopleList = mNearlyPeopleList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public NearlyPeopleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NearlyPeopleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_nearly_people, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NearlyPeopleViewHolder nearlyPeopleViewHolder, int i) {
        final NearlyPeopleResult.DataBean dataBean = mNearlyPeopleList.get(i);
        nearlyPeopleViewHolder.nameTv.setText(dataBean.getUsername());
        if (dataBean.getGender() == 0){
            nearlyPeopleViewHolder.nameTv.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.mipmap.nv), null);
        }else {
            nearlyPeopleViewHolder.nameTv.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.mipmap.nan), null);
        }

        nearlyPeopleViewHolder.avatarImageView.load(FileURLBuilder.getUserIconUrl(dataBean.getAccount()), R.mipmap.lianxiren,999);
        nearlyPeopleViewHolder.mottoTv.setText(dataBean.getMotto());

        if(dataBean.getDistance() < 100){
            nearlyPeopleViewHolder.distanceTv.setText(mContext.getString(R.string.nearly_people_distance, ConvertUtils.doubleToString2(dataBean.getDistance())));
        }else if (dataBean.getDistance() >= 100 && dataBean.getDistance() < 1000){
            nearlyPeopleViewHolder.distanceTv.setText(mContext.getString(R.string.hundred_kilometer));
        }else {
            nearlyPeopleViewHolder.distanceTv.setText(mContext.getString(R.string.thousand_kilometer));
        }

        nearlyPeopleViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickedListener.onItemClicked(dataBean, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNearlyPeopleList.size();
    }

    public void setOnItemClickedListener(OnItemClickedListener<NearlyPeopleResult.DataBean> onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    public void addAll(List<NearlyPeopleResult.DataBean> list) {
        if (mNearlyPeopleList.equals(list)) {
            return;
        }
        mNearlyPeopleList.clear();
        mNearlyPeopleList.addAll(list);
        notifyDataSetChanged();
    }

    class NearlyPeopleViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imageView27)
        WebImageView avatarImageView;
        @BindView(R.id.textView80)
        TextView nameTv;
        @BindView(R.id.textView81)
        TextView mottoTv;
        @BindView(R.id.distance_tv)
        TextView distanceTv;

        public NearlyPeopleViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
