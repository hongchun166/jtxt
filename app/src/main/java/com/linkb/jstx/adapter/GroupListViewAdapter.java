
package com.linkb.jstx.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.activity.chat.GroupChatActivity;
import com.linkb.jstx.adapter.viewholder.LogoNameViewHolder;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.model.Group;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class GroupListViewAdapter extends RecyclerView.Adapter<LogoNameViewHolder> {

    private String account;
    private List<Group> groupList = new ArrayList<>();

    public GroupListViewAdapter() {
        super();
        account = Global.getCurrentAccount();
    }


    @Override
    public LogoNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LogoNameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact_group, parent, false));
    }

    @Override
    public void onBindViewHolder(LogoNameViewHolder holder, int position) {
        final Group group = groupList.get(position);
        holder.name.setText(group.name);
        holder.icon.load(FileURLBuilder.getGroupIconUrl(group.id), R.drawable.logo_group_normal, 999);
        holder.badge.setVisibility(account.equals(group.founder) ? View.VISIBLE : View.GONE);
        holder.itemView.setTag(group.getId());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GroupChatActivity.navToAct(view.getContext(),group.name, Long.valueOf(view.getTag().toString()));
            }
        });
    }

    public void addAll(List<Group> list) {
        int index = groupList.size();
        groupList.addAll(list);
        notifyItemRangeInserted(index, list.size());
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }


    public void remove(Group group) {
        int index = groupList.indexOf(group);
        if (index >=0){
            groupList.remove(index);
            notifyItemRemoved(index);
        }

    }

    public void clearAll() {
        notifyItemRangeRemoved(0, groupList.size());
        groupList.clear();

    }

    public void update(Group group) {
        int index = groupList.indexOf(group);
        if (index >=0){
            groupList.set(index,group);
            notifyItemChanged(index);
        }
    }

    public void add(Group group) {
        groupList.add(group);
        notifyItemInserted(getItemCount()-1);
    }
}
