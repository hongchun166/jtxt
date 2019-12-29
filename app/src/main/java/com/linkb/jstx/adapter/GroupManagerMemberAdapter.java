
package com.linkb.jstx.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.linkb.R;
import com.linkb.jstx.activity.contact.PersonInfoActivity;
import com.linkb.jstx.adapter.viewholder.LogoNameViewHolder;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.GroupMember;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

/** 群管理员Adapter
* */
public class GroupManagerMemberAdapter extends RecyclerView.Adapter<LogoNameViewHolder> implements View.OnClickListener {

    private List<GroupMember> memberList = new ArrayList<>();
    private int iconWidth;
    private int iconHeight;
    private int itemPadding;
    private Context mContext;

    private boolean mEnableCheckMenberInfo;

    public GroupManagerMemberAdapter(Context context, boolean enableCheckMenberInfo){
        itemPadding = AppTools.dip2px(8);
        iconWidth = (Resources.getSystem().getDisplayMetrics().widthPixels - itemPadding * 10) / 5;
        iconHeight = iconWidth;
        mContext = context;
        this.mEnableCheckMenberInfo = enableCheckMenberInfo;
    }
    @Override
    public LogoNameViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LogoNameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_group_member, parent, false));
    }

    @Override
    public void onBindViewHolder(LogoNameViewHolder holder, int position) {

        GroupMember member = memberList.get(position);
        holder.name.setText(member.name);

        holder.itemView.setPadding( 0,  0,  itemPadding,  0);

        holder.icon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        holder.icon.load(FileURLBuilder.getUserIconUrl(member.account), R.mipmap.lianxiren, 999);
        holder.itemView.setTag(member);
        holder.itemView.setOnClickListener(this);
    }

    public void setmEnableCheckMenberInfo(boolean mEnableCheckMenberInfo) {
        this.mEnableCheckMenberInfo = mEnableCheckMenberInfo;
        notifyDataSetChanged();
    }

    boolean isFirstColumn(int pos) {
        return pos % 5 == 0;
    }

    boolean isFirstRow(int pos) {
        return pos < 5 ;
    }


    boolean isLastColumn(int pos) {
        return (pos+1) % 5 == 0;
    }


    public void addAll(List<GroupMember> list) {
        if (memberList.equals(list)) {
            return;
        }
        memberList.clear();
        if (list.size() > 5 ){
            memberList.addAll(list.subList(0, 5));
        }else {
            memberList.addAll(list);
        }

        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return Math.min(Constant.MAX_GRID_MEMBER,memberList.size());
    }


    @Override
    public void onClick(View v) {
        GroupMember member = (GroupMember) v.getTag();
        Friend friend = FriendRepository.queryFriend(member.account, mListener);
        if (friend != null){
            postQueryFriend(friend);
        }
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
        if (mEnableCheckMenberInfo){
            Intent intent = new Intent(mContext, PersonInfoActivity.class);
            intent.putExtra(Friend.class.getName(), friend);
            mContext.startActivity(intent);
        }
    }
}
