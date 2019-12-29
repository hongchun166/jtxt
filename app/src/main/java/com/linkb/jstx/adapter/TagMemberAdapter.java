
package com.linkb.jstx.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.linkb.R;
import com.linkb.jstx.activity.contact.ContactSelectorActivity;
import com.linkb.jstx.activity.contact.PersonInfoActivity;
import com.linkb.jstx.adapter.viewholder.LogoNameViewHolder;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class TagMemberAdapter extends RecyclerView.Adapter<LogoNameViewHolder> implements View.OnClickListener,View.OnLongClickListener {

    private final int TYPE_ADD = 1;
    private final int TYPE_MEMBER = 0;

    private List<FriendListResult.FriendShip> memberList = new ArrayList<>();
    private FriendListResult.FriendShip target;
    private int iconWidth;
    private int iconHeight;
    private int itemPadding;

    public TagMemberAdapter(){
        itemPadding = AppTools.dip2px(10);
        iconWidth = (Resources.getSystem().getDisplayMetrics().widthPixels - itemPadding * 10) / 5;
        iconHeight = iconWidth;
    }
    @Override
    public LogoNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new LogoNameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_tag_member, parent, false));
    }

    @Override
    public void onBindViewHolder(LogoNameViewHolder holder, int position) {

        holder.itemView.setPadding( itemPadding,  itemPadding,  itemPadding,  itemPadding);
        holder.icon.setLayoutParams(new LinearLayout.LayoutParams(iconWidth,iconHeight));
        if (position == memberList.size()) {
            holder.icon.setImageResource(R.drawable.icon_add_photo_selector);
            holder.itemView.setTag(null);
            holder.name.setText(null);
        }else {
            FriendListResult.FriendShip member = memberList.get(position);
            holder.name.setText(member.getName());
            holder.icon.load(FileURLBuilder.getUserIconUrl(member.getFriendAccount()), R.mipmap.lianxiren);
            holder.itemView.setTag(member);
            holder.itemView.setOnLongClickListener(this);
        }

        holder.itemView.setOnClickListener(this);
    }

    public void addAll(List<FriendListResult.FriendShip> list) {
        if (memberList.equals(list)) {
            return;
        }
        memberList.addAll(list);
        notifyDataSetChanged();
    }



    @Override
    public int getItemCount() {
        return memberList.size() + 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private ArrayList<String> getAccountList(){
        ArrayList<String> excludeList = new ArrayList<>();
        for (FriendListResult.FriendShip friend:memberList){
            excludeList.add(friend.getFriendAccount());
        }
        return excludeList;
    }

    @Override
    public boolean onLongClick(View v) {
        target = (FriendListResult.FriendShip) v.getTag();
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() == null){
            Intent intent = new Intent(v.getContext(), ContactSelectorActivity.class);
            intent.putStringArrayListExtra("excludeList",getAccountList());
            ((Activity)v.getContext()).startActivityForResult(intent, ContactSelectorActivity.RESULT_CODE);
            return;
        }
        FriendListResult.FriendShip member = (FriendListResult.FriendShip) v.getTag();
        Intent intent = new Intent(v.getContext(), PersonInfoActivity.class);
        intent.putExtra(Friend.class.getName(), member.getName());
        v.getContext().startActivity(intent);
    }


    public void remove(FriendListResult.FriendShip friend) {
        int index = memberList.indexOf(friend);
        this.memberList.remove(index);
        notifyItemRemoved(index);
    }

    public FriendListResult.FriendShip getTarget() {
        return target;
    }

    public void removeSelected() {
        remove(target);
    }
}
