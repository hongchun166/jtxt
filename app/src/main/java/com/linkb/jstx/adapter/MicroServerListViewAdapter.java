
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.adapter.viewholder.LogoNameViewHolder;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class MicroServerListViewAdapter extends RecyclerView.Adapter<LogoNameViewHolder> implements View.OnClickListener {

    private List<MicroServer> list = new ArrayList<>();

    private OnItemClickedListener mOnItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }

    @Override
    public LogoNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LogoNameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_microserver, parent, false));
    }

    @Override
    public void onBindViewHolder(LogoNameViewHolder holder, int position) {
        MicroServer target = list.get(position);
        holder.name.setText(target.name);
        holder.icon.load(FileURLBuilder.getServerLogoUrl(target.account), R.drawable.icon_microserver);
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

    public void addAll(List<MicroServer> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void onClick(View view) {
        mOnItemClickedListener.onItemClicked(view.getTag(), view);
    }

}
