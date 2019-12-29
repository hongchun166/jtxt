package com.linkb.jstx.adapter.trend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.github.siyamed.shapeimageview.mask.PorterShapeImageView;
import com.linkb.R;
import com.linkb.jstx.model.Group;

import java.util.ArrayList;
import java.util.List;

public class HotGroupListAdapter extends RecyclerView.Adapter<HotGroupListAdapter.HotGroupViewHolder> {

    private Context mContext;
    private List<Group> groupList = new ArrayList<>();

    @NonNull
    @Override
    public HotGroupListAdapter.HotGroupViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view  = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_hot_group, viewGroup, false);
        return new HotGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotGroupViewHolder hotGroupViewHolder, int i) {
        Group group = groupList.get(i);
        hotGroupViewHolder.groupNameTextView.setText(group.getName());
        Glide.with(mContext)
                .setDefaultRequestOptions(
                        new RequestOptions()
                                .frame(1000000)
                                .centerCrop()
                                .placeholder(R.mipmap.newfriend))
                .load(group.getId())
                .into(hotGroupViewHolder.imageView);
    }

    public void addAll(List<Group> list) {
        int index = groupList.size();
        groupList.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }


    @Override
    public int getItemCount() {
        return groupList.size();
    }

    class HotGroupViewHolder extends RecyclerView.ViewHolder {
        PorterShapeImageView imageView;
        TextView groupNameTextView, recentlyMessageTextView, recentTimeTextView;


        public HotGroupViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView =itemView.findViewById(R.id.hot_group_imageView);
            groupNameTextView =itemView.findViewById(R.id.hot_group_name_text_view);
            recentlyMessageTextView =itemView.findViewById(R.id.recent_message_textView);
            recentTimeTextView =itemView.findViewById(R.id.time_textView);
        }
    }
}
