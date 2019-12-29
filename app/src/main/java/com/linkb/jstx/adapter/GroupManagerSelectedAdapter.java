package com.linkb.jstx.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.R;
import com.linkb.jstx.adapter.viewholder.ImageViewHolder;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.GroupMember;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

/**   被选中的群成员
* */
public class GroupManagerSelectedAdapter extends RecyclerView.Adapter<ImageViewHolder> {

    private List<GroupMember> mSelectedMemberList = new ArrayList<>();

    private OnItemClickedListener<GroupMember> onItemClickedListener;

    private int itemPadding = AppTools.dip2px(8);

    public void setOnItemClickedListener(OnItemClickedListener<GroupMember> onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ImageViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_user_logo, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder viewHolder, int i) {
        GroupMember groupMember = mSelectedMemberList.get(i);
        viewHolder.imageView.load(FileURLBuilder.getUserIconUrl(groupMember.account), R.mipmap.lianxiren,999);
        viewHolder.itemView.setPadding( 0,  0,  itemPadding,  0);
        viewHolder.itemView.setTag(groupMember);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickedListener.onItemClicked((GroupMember) view.getTag(),view);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSelectedMemberList.size();
    }

    public void addAll(List<GroupMember> list) {
        if (mSelectedMemberList.equals(list)) {
            return;
        }
        mSelectedMemberList.clear();
        mSelectedMemberList.addAll(list);
        notifyDataSetChanged();
    }

    public void add(GroupMember friend) {
        mSelectedMemberList.add(friend);
        notifyItemInserted(mSelectedMemberList.size()-1);
    }

    public void remove(GroupMember friend) {
        int index = mSelectedMemberList.indexOf(friend);
        if (index >=0 ){
            this.mSelectedMemberList.remove(index);
            notifyItemRemoved(index);
        }
    }
}
