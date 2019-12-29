
package com.linkb.jstx.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.jstx.database.MicroServerRepository;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.adapter.viewholder.LogoNameViewHolder;
import com.linkb.jstx.component.ListFooterView;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class MicroServerLookViewAdapter extends RecyclerView.Adapter<LogoNameViewHolder> implements View.OnClickListener {
    private int TYPE_FOOTER = 0;
    private int TYPE_ITEM = 1;
    private List<MicroServer> list = new ArrayList<>();
    private ListFooterView mFooterView;

    private OnItemClickedListener mOnItemClickedListener;

    public MicroServerLookViewAdapter(Context context) {
        super();
        mFooterView = (ListFooterView) LayoutInflater.from(context).inflate(R.layout.layout_list_footer, null);
    }

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.mOnItemClickedListener = onItemClickedListener;
    }

    @Override
    public int getItemViewType(int position) {

        if (position == getItemCount() - 1) {
            return TYPE_FOOTER;
        } else {
            return TYPE_ITEM;
        }
    }

    @Override
    public LogoNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            return new LogoNameViewHolder(mFooterView);
        } else {
            return new LogoNameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_look_microserver, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(LogoNameViewHolder holder, int position) {
        if (position == list.size()) {
            return;
        }
        MicroServer target = list.get(position);
        holder.name.setText(target.name);
        holder.icon.load(FileURLBuilder.getServerLogoUrl(target.account), R.drawable.icon_microserver);
        if (isSubscriabled(target.account)) {
            ((TextView) holder.badge).setText(R.string.label_subscribed);
            holder.badge.setSelected(true);
        } else {
            holder.badge.setSelected(false);
            ((TextView) holder.badge).setText(R.string.label_unsubscribed);
        }
        holder.itemView.setOnClickListener(this);
        holder.itemView.setTag(target);
    }

    private boolean isSubscriabled(String account) {
        return MicroServerRepository.queryById(account) != null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public void addAll(List<MicroServer> list) {
        int start = list.size();
        this.list.addAll(list);
        notifyItemRangeInserted(start, list.size());
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public void onClick(View view) {
        mOnItemClickedListener.onItemClicked(view.getTag(), view);
    }

    public ListFooterView getFooterView() {
        return mFooterView;
    }
}
