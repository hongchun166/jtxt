
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.bean.Bucket;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.adapter.viewholder.AlbumBucketViewHolder;
import com.linkb.R;

import java.util.ArrayList;
import java.util.List;

public class AlbumBucketListAdapter extends RecyclerView.Adapter<AlbumBucketViewHolder> implements View.OnClickListener {
    private Bucket mTarget;
    private List<Bucket> list  =new ArrayList<>();
    private OnItemClickedListener onItemClickedListener;
    @Override
    public AlbumBucketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlbumBucketViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_bucket, parent, false));
    }
    @Override
    public void onBindViewHolder(AlbumBucketViewHolder viewHolder, int position) {
        Bucket bucket = list.get(position);
        viewHolder.itemView.setOnClickListener(this);
        viewHolder.itemView.setTag(bucket);
        viewHolder.imageView.load(bucket.cover, R.color.theme_window_color);
        viewHolder.name.setText(viewHolder.name.getContext().getString(R.string.label_album_name_count,bucket.name,bucket.size));
        viewHolder.mark.setVisibility(bucket == mTarget ? View.VISIBLE:View.GONE);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void addAll(List<Bucket> list) {
        this.list.clear();
        this.list.addAll(list);
        if (!list.isEmpty() && mTarget == null){
            mTarget = list.get(0);
        }
        notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
        mTarget = (Bucket) v.getTag();
        notifyDataSetChanged();
        onItemClickedListener.onItemClicked(v.getTag(),v);
    }
}
