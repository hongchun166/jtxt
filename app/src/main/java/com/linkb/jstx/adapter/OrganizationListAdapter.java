
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.jstx.adapter.viewholder.OrganizationViewHolder;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Organization;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.MessageSource;
import com.linkb.R;

import java.util.ArrayList;
import java.util.List;

public class OrganizationListAdapter extends RecyclerView.Adapter<OrganizationViewHolder> implements View.OnClickListener {

    private List<MessageSource> list = new ArrayList<>();
    private OnItemClickedListener onItemClickedListener;

    public OrganizationListAdapter(OnItemClickedListener onItemClickedListener) {
        super();
        this.onItemClickedListener = onItemClickedListener;
    }

    @Override
    public OrganizationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new OrganizationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_organization, parent, false));
        }
        return new OrganizationViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_organization_member, parent, false));

    }

    @Override
    public void onBindViewHolder(OrganizationViewHolder holder, int position) {
        MessageSource target = list.get(position);
        holder.name.setText(target.getName());
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(target);
        if (target instanceof Friend) {
            ((WebImageView) holder.icon).load(target.getWebIcon(), R.mipmap.lianxiren, 999);
        } else {
            ((TextView) holder.icon).setText(target.getName().substring(0, 1));
        }
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
    public int getItemViewType(int position) {
        if (list.get(position) instanceof Organization) {
            return 0;
        }
        return 1;
    }

    @Override
    public void onClick(View v) {
        onItemClickedListener.onItemClicked(v.getTag(), v);
    }

    public void addAll(List<MessageSource> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

}
