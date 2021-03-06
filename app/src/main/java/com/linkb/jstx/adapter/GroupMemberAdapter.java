
package com.linkb.jstx.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.contact.PersonInfoActivity;
import com.linkb.jstx.adapter.viewholder.LogoNameViewHolder;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
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

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupMemberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private List<GroupMember> memberList = new ArrayList<>();
    private int iconWidth;
    private int iconHeight;
    private int itemPadding;
    private Context mContext;

    private boolean mEnableCheckMenberInfo;
    private View.OnClickListener mAddGroupListener;

    private static final int CONTENT = 0;
    private static final int ADD_BTN = 1;

    public GroupMemberAdapter(Context context, boolean enableCheckMenberInfo, View.OnClickListener listener){
        itemPadding = AppTools.dip2px(8);
        iconWidth = (Resources.getSystem().getDisplayMetrics().widthPixels - itemPadding ) / 7;
        iconHeight = iconWidth;
        mContext = context;
        this.mAddGroupListener = listener;
        this.mEnableCheckMenberInfo = enableCheckMenberInfo;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == CONTENT){
            return new LogoNameViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_group_member, parent, false));
        }else {
            return new AddMemberViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_grid_group_member, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LogoNameViewHolder){

            LogoNameViewHolder holder1 = (LogoNameViewHolder) holder;
            GroupMember member = memberList.get(position);
            holder1.name.setText(member.name);

            holder1.itemView.setPadding( 0,  0,  itemPadding,  0);

            holder1.icon.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder1.icon.load(FileURLBuilder.getUserIconUrl(member.account), R.mipmap.lianxiren, 999);
            holder1.itemView.setTag(member);
            holder1.itemView.setOnClickListener(this);
        }else if (holder instanceof AddMemberViewHolder){
            AddMemberViewHolder holder2 = (AddMemberViewHolder) holder;
            holder2.imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            holder2.imageView.setOnClickListener(mAddGroupListener);
            holder2.nameTv.setTextColor(mContext.getResources().getColor(R.color.tex_color_blue_1068ed));
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (memberList.size() >  5){
            return position > 4 ? ADD_BTN : CONTENT;
        }else {
            return position < memberList.size() ? CONTENT : ADD_BTN;
        }
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
        return memberList.size() + 1;
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

    public class AddMemberViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.icon)
        WebImageView imageView;
        @BindView(R.id.name)
        TextView nameTv;

        public AddMemberViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
