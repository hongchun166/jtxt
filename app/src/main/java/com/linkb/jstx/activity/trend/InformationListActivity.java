package com.linkb.jstx.activity.trend;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.trend.InformationAdapter;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.model.Information;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.InformationListResult;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;


/** 资讯列表页面
* */
public class InformationListActivity extends BaseActivity implements HttpRequestListener<InformationListResult> {

    private GlobalEmptyView emptyView;
    private RecyclerView recyclerView;
    private RefreshLayout refreshLayout;
    private InformationAdapter mAdpter;
    private List<Information> informationList = new ArrayList<>();
    private boolean enableLoading = true;

    @Override
    protected void initComponents() {
        refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData(false);
            }
        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdpter = new InformationAdapter(this, informationList));
        emptyView = findViewById(R.id.emptyView);
        setToolbarTitle(getResources().getString(R.string.information));
        initData(true);
    }

    private void initData(boolean enableLoading) {
        // TODO: 2019/1/21
//        InformationResponse result = new Gson().fromJson(InformationResponse.getFakeDate(), InformationResponse.class);
//        mAdpter.addAll(result.getDataList());

        if (enableLoading) showProgressDialog(getString(R.string.tips_list_loading));
        HttpServiceManager.getInformationList(this);
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    public void onHttpRequestSucceed(InformationListResult result, OriginalCall call) {
        hideProgressDialog();
        refreshLayout.finishRefresh();
        if (result.isNotEmpty()) {
            mAdpter.replaceAll(result.dataList);
        }
        emptyView.toggle(mAdpter);
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
        refreshLayout.finishRefresh();
    }
}
