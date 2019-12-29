
package com.linkb.jstx.activity.trend;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.MicroAppLishAdapter;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.MicroAppListResult;
import com.linkb.R;

public class MicroAppListActivity extends BaseActivity implements HttpRequestListener<MicroAppListResult> {

    private MicroAppLishAdapter adapter;
    private GlobalEmptyView emptyView;
    private RecyclerView recyclerView;

    @Override
    public void initComponents() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter = new MicroAppLishAdapter());
        emptyView = findViewById(R.id.emptyView);
        performAppListRequest();
    }

    private void performAppListRequest() {
        showProgressDialog(getString(R.string.tips_list_loading));
        HttpServiceManager.queryMicroAppList(this);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_look_microapp;
    }


    @Override
    public void onHttpRequestSucceed(MicroAppListResult result, OriginalCall call) {
        hideProgressDialog();
        if (result.isNotEmpty()) {
            adapter.addAll(result.dataList);
        }
        emptyView.toggle(adapter);
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }


}
