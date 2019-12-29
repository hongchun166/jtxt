
package com.linkb.jstx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.linkb.R;
import com.linkb.jstx.activity.contact.GroupMemberListActivity;
import com.linkb.jstx.activity.contact.PersonInfoActivity;
import com.linkb.jstx.adapter.viewholder.GroupMemberViewHolder;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.GroupMember;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberListViewAdapter extends RecyclerView.Adapter<GroupMemberViewHolder> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Context context;
    private List<GroupMember> list = new ArrayList<>();
    private String creator;
    private GroupMember selected;
    private boolean isMangmentMode = false;
    private boolean mEnableCheckMenberInfo = true;
    private User self;

    public GroupMemberListViewAdapter(Context c, String creator, boolean enableCheckMenberInfo) {
        super();
        this.context = c;
        this.creator = creator;
        this.mEnableCheckMenberInfo = enableCheckMenberInfo;
        self = Global.getCurrentUser();
    }

    public boolean getMangmentMode() {
        return isMangmentMode;
    }

    public void setMangmentMode(boolean f) {
        isMangmentMode = f;
        notifyDataSetChanged();
    }

    public void setmEnableCheckMenberInfo(boolean mEnableCheckMenberInfo) {
        this.mEnableCheckMenberInfo = mEnableCheckMenberInfo;
        notifyDataSetChanged();
    }

    @Override
    public GroupMemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GroupMemberViewHolder(LayoutInflater.from(context).inflate(R.layout.item_group_member, parent, false));
    }

    @Override
    public void onBindViewHolder(GroupMemberViewHolder holder, int position) {
        GroupMember target = list.get(position);
        holder.icon.load(FileURLBuilder.getUserIconUrl(target.account), R.mipmap.lianxiren);
        holder.name.setText(target.name);
        if (target.name == null) {
            Friend.asynTextViewName(holder.name, target.account);
//            holder.name.setText(FriendRepository.queryFriendName(target.account));
        }
        holder.radioButton.setTag(target);
        holder.radioButton.setVisibility(View.GONE);
        holder.radioButton.setOnCheckedChangeListener(null);
        holder.radioButton.setChecked(false);
        if (target.account.equals(creator)) {
            holder.mark.setVisibility(View.VISIBLE);
        } else {
            holder.mark.setVisibility(View.GONE);
            if (isMangmentMode) {
                if (selected == target) {
                    holder.radioButton.setChecked(true);
                }
                if (!self.account.equals(target.account))holder.radioButton.setVisibility(View.VISIBLE);
                holder.radioButton.setOnCheckedChangeListener(this);
            }
        }
        holder.itemView.setTag(target);
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addAll(List<GroupMember> groupMembers) {
        list.clear();
        list.addAll(groupMembers);
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        if (isMangmentMode) {
            ((RadioButton) view.findViewById(R.id.radioButton)).toggle();
            return;
        }
        if (!mEnableCheckMenberInfo) return;
        GroupMember member = (GroupMember) view.getTag();
        Friend friend = FriendRepository.queryFriend(member.account, mListener);
        if (friend != null){
            postQueryFriend(friend);
        }
    }

    public GroupMember getSelected() {
        return selected;
    }

    public void remove(GroupMember member) {
        int index = list.indexOf(member);
        list.remove(index);
        notifyItemRemoved(index);
        selected = null;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        selected = (GroupMember) compoundButton.getTag();
        notifyDataSetChanged();
        ((GroupMemberListActivity) context).onMemberSelected();
    }

    private HttpRequestListener<BasePersonInfoResult> mListener = new HttpRequestListener<BasePersonInfoResult>() {
        @Override
        public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
            if (result.isSuccess()){
                Friend friend = User.UserToFriend(result.getData());
                FriendRepository.save(friend);
                postQueryFriend(friend);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    private void postQueryFriend(Friend friend){
        Intent intent = new Intent(context, PersonInfoActivity.class);
        intent.putExtra(Friend.class.getName(), friend);
        context.startActivity(intent);
    }
}
