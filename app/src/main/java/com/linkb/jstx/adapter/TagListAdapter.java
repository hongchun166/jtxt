
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.adapter.viewholder.TagListViewHolder;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Tag;
import com.linkb.R;

import java.util.ArrayList;
import java.util.List;

public class TagListAdapter extends RecyclerView.Adapter<TagListViewHolder> implements View.OnClickListener {

    private List<Tag> list = new ArrayList<>();
    private OnItemClickedListener onItemClickedListener;

    public TagListAdapter(OnItemClickedListener onItemClickedListener) {
        super();
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public TagListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TagListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(TagListViewHolder holder, int position) {
        Tag target = list.get(position);
        int count = TextUtils.isEmpty(target.account) ? 0 : target.account.split(",").length;
        holder.name.setText(holder.itemView.getContext().getString(R.string.label_tag_format,target.name,count));
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(target);
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
    public void onClick(View v) {
        onItemClickedListener.onItemClicked(v.getTag(), v);
    }

    public void add(Tag tag) {
        this.list.add(tag);
        notifyItemInserted(list.size()-1);
    }
    public void remove(Tag tag) {
        if (tag == null){
            return;
        }
        int index = list.indexOf(tag);
        this.list.remove(index);
        notifyItemRemoved(index);
    }
    public void update(Tag tag) {
        if (tag == null){
            return;
        }
        int index = list.indexOf(tag);
        this.list.set(index,tag);
        notifyItemChanged(index);
    }
    public void addAll(List<Tag> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
