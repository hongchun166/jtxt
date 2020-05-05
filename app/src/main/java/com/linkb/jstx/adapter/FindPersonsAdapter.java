package com.linkb.jstx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.FriendRepository;
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_find_person_layout, viewGroup, false);
        return new SearchContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchContactsViewHolder searchContactsViewHolder, int i) {
        final FindPersonsResult.DataBean.ContentBean dataBean = mList.get(i);
        searchContactsViewHolder.dataBean = dataBean;
        searchContactsViewHolder.viewHead.load(FileURLBuilder.getUserIconUrl(dataBean.getAccount()), R.mipmap.lianxiren, 999);
        searchContactsViewHolder.viewName.setText(dataBean.getName());

        if (!TextUtils.isEmpty(dataBean.getArea()))
            searchContactsViewHolder.viewArea.setText(String.valueOf(dataBean.getArea()));
        if (!"0".equals(dataBean.getIsFriends())) {
            searchContactsViewHolder.viewAddFriend.setBackgroundResource(R.color.white);
            searchContactsViewHolder.viewAddFriend.setText(R.string.added);
        }else if(FriendRepository.hasFriendRelationSndApply(dataBean.getAccount())){
            searchContactsViewHolder.viewAddFriend.setBackgroundResource(R.color.white);
            searchContactsViewHolder.viewAddFriend.setText(R.string.applyed);
        } else {
            searchContactsViewHolder.viewAddFriend.setBackgroundResource(R.mipmap.ic_bg_btn_add_friend);
            searchContactsViewHolder.viewAddFriend.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void replaceAll(List<FindPersonsResult.DataBean.ContentBean> list) {
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public class SearchContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView viewName;
        public WebImageView viewHead;
        public TextView viewTagArr;
        public TextView viewArea;
        public TextView viewAddFriend;
        View viewRootItem;
        FindPersonsResult.DataBean.ContentBean dataBean;

        public SearchContactsViewHolder(View itemView) {
            super(itemView);
            viewRootItem = itemView.findViewById(R.id.viewRootItem);
            viewHead = itemView.findViewById(R.id.viewHead);
            viewName = itemView.findViewById(R.id.viewName);
            viewTagArr = itemView.findViewById(R.id.viewTagArr);
            viewArea = itemView.findViewById(R.id.viewArea);
            viewAddFriend = itemView.findViewById(R.id.viewAddFriend);
            viewAddFriend.setOnClickListener(this);
            viewRootItem.setOnClickListener(this);
            viewHead.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.viewRootItem) {
                if (mListener != null) mListener.onAddFirend(dataBean);
            } else if (view.getId() == R.id.viewAddFriend) {
                if (mListener != null) mListener.onAddFirend(dataBean);
            }else if (view.getId() == R.id.viewHead) {
                //nav
                if (mListener != null) mListener.onClickHead(dataBean);
            }
        }
    }

    public interface OnSearchFriendClickedListener {
        void onAddFirend(FindPersonsResult.DataBean.ContentBean dataBean);
        void onClickHead(FindPersonsResult.DataBean.ContentBean dataBean);
    }
}
