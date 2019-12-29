
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.adapter.viewholder.ImageViewHolder;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.MessageSource;
import com.linkb.R;

import java.util.ArrayList;
import java.util.List;

public class SelectedMessageSourceAdapter extends RecyclerView.Adapter<ImageViewHolder> implements View.OnClickListener {


    private List<MessageSource> memberList = new ArrayList<>();

    private OnItemClickedListener<MessageSource> onItemClickedListener;

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_logo, parent, false));
    }

    public void setOnItemClickedListener(OnItemClickedListener<MessageSource> onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

            MessageSource member = memberList.get(position);
            holder.imageView.load(member.getWebIcon(), R.mipmap.lianxiren,999);
            holder.itemView.setTag(member);
            holder.itemView.setOnClickListener(this);
    }

    public void addAll(List<Friend> list) {
        if (memberList.equals(list)) {
            return;
        }
        memberList.clear();
        memberList.addAll(list);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return memberList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public void onClick(View v) {
        onItemClickedListener.onItemClicked((MessageSource) v.getTag(),v);
    }
    public void add(MessageSource friend) {
        memberList.add(friend);
        notifyItemInserted(memberList.size()-1);
    }

    public void remove(MessageSource friend) {
        int index = memberList.indexOf(friend);
        if (index >=0 ){
            this.memberList.remove(index);
            notifyItemRemoved(index);
        }
    }
}
