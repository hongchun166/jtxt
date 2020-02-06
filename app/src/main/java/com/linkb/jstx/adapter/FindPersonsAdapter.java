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
import com.linkb.jstx.network.result.FriendQueryResult;
import com.linkb.jstx.network.result.v2.FindPersonsResult;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class FindPersonsAdapter extends RecyclerView.Adapter<FindPersonsAdapter.SearchContactsViewHolder> {

    private Context mContext;
    private List<FindPersonsResult.DataBean.ContentBean> mList = new ArrayList<>();
    private OnSearchFriendClickedListener mListener;

    public FindPersonsAdapter(Context mContext, List<FindPersonsResult.DataBean.ContentBean> mList, OnSearchFriendClickedListener listener) {
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
        final FindPersonsResult.DataBean.ContentBean dataBean = mList.get(i);
        searchContactsViewHolder.name.setText(dataBean.getName());
        searchContactsViewHolder.icon.load(FileURLBuilder.getUserIconUrl(dataBean.getAccount()), R.mipmap.lianxiren, 999);
        searchContactsViewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户一点击就自动添加好友
                if (mListener != null){
                    mListener.onAddFirend(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void replaceAll(List<FindPersonsResult.DataBean.ContentBean> list){
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

    public interface OnSearchFriendClickedListener{
        void onAddFirend(FindPersonsResult.DataBean.ContentBean dataBean);
    }
}
