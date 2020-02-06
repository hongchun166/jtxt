package com.linkb.jstx.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.network.result.GroupQueryResult;
import com.linkb.jstx.network.result.v2.FindGroupsResult;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupAdapterV2 extends RecyclerView.Adapter<SearchGroupAdapterV2.SearchContactsViewHolder> {

    private Context mContext;
    private List<FindGroupsResult.DataBean.ContentBean> mList = new ArrayList<>();
    private OnSearchGroupClickedListener mListener;

    public SearchGroupAdapterV2(Context mContext, List<FindGroupsResult.DataBean.ContentBean> mList, OnSearchGroupClickedListener listener) {
        this.mContext = mContext;
        this.mList = mList;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public SearchContactsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_list_search_group_v2, viewGroup, false);
        return new SearchContactsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchContactsViewHolder hodler, int i) {
        final FindGroupsResult.DataBean.ContentBean dataBean = mList.get(i);
        hodler.dataBean=dataBean;
        hodler.viewGroupName.setText(dataBean.getName());
        hodler.viewGroupDesc.setText(dataBean.getSummary());
        hodler.viewHead.load(FileURLBuilder.getUserIconUrl(String.valueOf(dataBean.getId())), R.mipmap.lianxiren, 999);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void replaceAll(List<FindGroupsResult.DataBean.ContentBean> list){
        mList.addAll(list);
        notifyDataSetChanged();
    }

    public class SearchContactsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView viewGroupName;
        public WebImageView viewHead;
        public TextView viewGroupDesc;
        public Button viewApplyJoinGroup;
        public View viewRoot;
        FindGroupsResult.DataBean.ContentBean dataBean;

        public SearchContactsViewHolder(View itemView) {
            super(itemView);
            viewHead = itemView.findViewById(R.id.viewHead);
            viewGroupName = itemView.findViewById(R.id.viewGroupName);
            viewGroupDesc = itemView.findViewById(R.id.viewGroupDesc);
            viewApplyJoinGroup = itemView.findViewById(R.id.viewApplyJoinGroup);
            viewRoot= itemView.findViewById(R.id.viewRoot);
            viewRoot.setOnClickListener(this);
            viewApplyJoinGroup.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.viewApplyJoinGroup){
                if(mListener!=null){
                    mListener.onClickJoinGroup(dataBean);
                }
            }else {
                if(mListener!=null){
                    mListener.onClickItemGroup(dataBean);
                }
            }
        }
    }
    public interface OnSearchGroupClickedListener{
        void onClickJoinGroup(FindGroupsResult.DataBean.ContentBean dataBean);
        void onClickItemGroup(FindGroupsResult.DataBean.ContentBean dataBean);
    }
}
