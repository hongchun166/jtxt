
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Comment;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.adapter.viewholder.MomentMessageViewHolder;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MomentMessageAdapter extends RecyclerView.Adapter<MomentMessageViewHolder> implements View.OnClickListener {

    private List<Message> list = new ArrayList<>();

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener mOnItemClickedListener) {
        this.mOnItemClickedListener = mOnItemClickedListener;
    }

    @Override
    public MomentMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MomentMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circle_message, parent, false));
    }

    @Override
    public void onBindViewHolder(MomentMessageViewHolder viewHolder, int position) {

        Comment comment = new Gson().fromJson(list.get(position).content, Comment.class);
        viewHolder.icon.load(FileURLBuilder.getUserIconUrl(comment.account), R.mipmap.lianxiren, 200);
        Friend.asynTextViewName(viewHolder.name, comment.account);
//        viewHolder.name.setText(FriendRepository.queryFriendName(comment.account));
        viewHolder.time.setText(AppTools.howTimeAgo(comment.timestamp));
        viewHolder.itemView.setOnClickListener(this);
        viewHolder.itemView.setTag(comment);
        if (Comment.TYPE_2.equals(comment.type)) {
            viewHolder.content.setText(R.string.tip_moment_praise);
        } else {
            viewHolder.content.setText(comment.content);
        }
    }

    public void addAll(List<Message> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public void onClick(View view) {
        mOnItemClickedListener.onItemClicked(view.getTag(), view);
    }

    public void remove(Message notice) {
        int i = list.indexOf(notice);
        notifyItemRemoved(i);
        list.remove(i);
    }

}
