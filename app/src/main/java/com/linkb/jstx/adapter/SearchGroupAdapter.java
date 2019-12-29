package com.linkb.jstx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.network.result.FriendQueryResult;
import com.linkb.jstx.network.result.GroupQueryResult;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupAdapter extends RecyclerView.Adapter<SearchGroupAdapter.SearchContactsViewHolder> {

    private Context mContext;
    private List<GroupQueryResult.DataListBean> mList = new ArrayList<>();
    private OnSearchGroupClickedListener mListener;

    public SearchGroupAdapter(Context mContext, List<GroupQueryResult.DataListBean> mList, OnSearchGroupClickedListener listener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public SearchContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_contact_friend, viewGroup, false);
        return new SearchContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchContactsViewHolder searchContactsViewHolder, int i) {
        final GroupQueryResult.DataListBean dataBean = mList.get(i);
        searchContactsViewHolder.name.setText(dataBean.getName());
//        searchContactsViewHolder.icon.load(FileURLBuilder.getUserIconUrl(dataBean.getId()), R.mipmap.lianxiren, 999);
        searchContactsViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果用户已经在群里，直接跳到群聊天；否则用户一点击就申请进群
                if (mListener != null){
                    if (GroupRepository.queryById(dataBean.getId()) != null){
                        mListener.onGroupChat(dataBean);
                    }else {
                        mListener.onJoinGroup(dataBean);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void replaceAll(List<GroupQueryResult.DataListBean> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public class SearchContactsViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public WebImageView icon;
        public View dividerView;
        public View rootView;

        public SearchContactsViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
            dividerView = itemView.findViewById(R.id.divider_view);
            rootView = itemView.findViewById(R.id.root_view);
        }
    }

    public interface OnSearchGroupClickedListener{
        void onJoinGroup(GroupQueryResult.DataListBean dataBean);
        void onGroupChat(GroupQueryResult.DataListBean dataBean);
    }
}
