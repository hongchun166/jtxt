package com.linkb.video.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.model.GroupMember;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupVideoMemberAdapter extends RecyclerView.Adapter<GroupVideoMemberAdapter.GroupVideoMemberViewHolder> {


    private List<GroupMember> mSelectedMemberList = new ArrayList<>();
    private GroupMemberSelectedListener mGroupMemberSelectedListener;

    public void setGroupMemberSelectedListener(GroupMemberSelectedListener groupMemberSelectedListener) {
        this.mGroupMemberSelectedListener = groupMemberSelectedListener;
    }

    @NonNull
    @Override
    public GroupVideoMemberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new GroupVideoMemberViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_group_video_member_select, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupVideoMemberViewHolder viewHolder, int i) {
        final GroupMember groupMember = mSelectedMemberList.get(i);
        viewHolder.icon.load(FileURLBuilder.getUserIconUrl(groupMember.account), R.mipmap.lianxiren,999);
        viewHolder.nameTv.setText(groupMember.name);
        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (mGroupMemberSelectedListener != null){
                    if (b){
                        mGroupMemberSelectedListener.onGroupMemberSelected(groupMember);
                    }else {
                        mGroupMemberSelectedListener.onGroupMemberCancel(groupMember);
                    }
                }
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

    public class GroupVideoMemberViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.checkBox)
        CheckBox checkBox;
        @BindView(R.id.imageView39)
        WebImageView icon;
        @BindView(R.id.textView128)
        TextView nameTv;

        public GroupVideoMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface GroupMemberSelectedListener {
        void onGroupMemberSelected(GroupMember groupMember);
        void onGroupMemberCancel(GroupMember groupMember);
    }

}
