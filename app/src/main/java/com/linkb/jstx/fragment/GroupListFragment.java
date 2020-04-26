package com.linkb.jstx.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.linkb.jstx.listener.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.R;
import com.linkb.jstx.adapter.GroupListViewAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.QueryMineGroupResult;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GroupListFragment extends LazyLoadFragment {

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.groupList)
    RecyclerView recyclerView;
    @BindView(R.id.emptyView)
    GlobalEmptyView emptyView;
    private GroupListViewAdapter adapter;
    private GroupChangeReceiver groupChangeReceiver;
    private Handler mHandler = new Handler();
    private String account;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grouplist, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void requestData() {
        account = Global.getCurrentAccount();

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadMineGroupInfo();
            }
        });

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new GroupListViewAdapter();
        recyclerView.setAdapter(adapter);
        emptyView.setTips(R.string.tips_empty_group);


        adapter.clearAll();
        List<Group> queryCreatedList= GroupRepository.queryCreatedList(account);
        List<Group> queryJoinList=  GroupRepository.queryJoinList(account);
        adapter.addAll(queryCreatedList);
        adapter.addAll(queryJoinList);
        emptyView.toggle(adapter);

        groupChangeReceiver = new GroupChangeReceiver();
        LvxinApplication.registerLocalReceiver(groupChangeReceiver,groupChangeReceiver.getIntentFilter());
    }

    /** 查询我的群组资料
     * */
    private void loadMineGroupInfo() {
        HttpServiceManager.queryPersonGroup(listener);
    }

    private HttpRequestListener<QueryMineGroupResult> listener = new HttpRequestListener<QueryMineGroupResult>() {
        @Override
        public void onHttpRequestSucceed(QueryMineGroupResult result, OriginalCall call) {
            refreshLayout.finishRefresh();
            if (result.isSuccess()){
                GroupRepository.saveAll(result.getDataList());
                adapter.clearAll();
                adapter.addAll(GroupRepository.queryCreatedList(account));
                adapter.addAll(GroupRepository.queryJoinList(account));
                emptyView.toggle(adapter);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            refreshLayout.finishRefresh();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        LvxinApplication.unregisterLocalReceiver(groupChangeReceiver);
    }

    public class GroupChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Objects.equals(intent.getAction(),Constant.Action.ACTION_GROUP_REFRESH)){
                loadMineGroupInfo();
                return;
            }
            Group group = (Group) intent.getSerializableExtra(Group.NAME);
            if (Objects.equals(intent.getAction(), Constant.Action.ACTION_GROUP_DELETE)){
                adapter.remove(group);
            }
            if (Objects.equals(intent.getAction(),Constant.Action.ACTION_GROUP_ADD)){
                adapter.add(group);
            }
            if (Objects.equals(intent.getAction(),Constant.Action.ACTION_GROUP_UPDATE)){
                adapter.update(group);
            }
            emptyView.toggle(adapter);
        }
        IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_GROUP_DELETE);
            filter.addAction(Constant.Action.ACTION_GROUP_ADD);
            filter.addAction(Constant.Action.ACTION_GROUP_UPDATE);
            filter.addAction(Constant.Action.ACTION_GROUP_REFRESH);
            return filter;
        }

    }
}
