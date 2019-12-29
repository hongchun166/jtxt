
package com.linkb.jstx.activity.contact;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.linkb.jstx.adapter.MicroServerLookViewAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.listener.OnLoadRecyclerViewListener;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.component.LoadMoreRecyclerView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.MicroServerListResult;
import com.linkb.R;

public class LookMicroServerActivity extends BaseActivity implements OnItemClickedListener, OnLoadRecyclerViewListener, HttpRequestListener<MicroServerListResult> {

    private MicroServerLookViewAdapter adapter;
    private GlobalEmptyView emptyView;
    private int currentPage = 0;
    private LoadMoreRecyclerView recyclerView;

    @Override
    public void initComponents() {

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MicroServerLookViewAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnLoadEventListener(this);
        recyclerView.setFooterView(adapter.getFooterView());
        adapter.setOnItemClickedListener(this);
        emptyView = findViewById(R.id.emptyView);

        performLookListRequest();
    }

    private void performLookListRequest() {
        showProgressDialog(getString(R.string.tips_list_loading));
        HttpServiceManager.queryAllMicroServer(this);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_loadmore_recyclerview;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.title_look_microserver;
    }

    @Override
    public void onItemClicked(Object obj, View view) {

        Intent intent = new Intent(this, MicroServerDetailedActivity.class);
        intent.putExtra(MicroServer.NAME, (MicroServer) obj);
        this.startActivity(intent);
    }

    @Override
    public void onHttpRequestSucceed(MicroServerListResult result, OriginalCall call) {
        if (result.isNotEmpty()) {
            adapter.addAll(result.dataList);
        }
        if (!result.isNotEmpty() && currentPage > Constant.DEF_PAGE_INDEX) {
            currentPage--;
        }
        recyclerView.showMoreComplete(result.page);
        switchEmptyView();

        hideProgressDialog();
    }

    private void switchEmptyView() {
        if (adapter.isEmpty()) {
            emptyView.setVisibility(View.VISIBLE);
        } else {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }

    @Override
    public void onGetNextPage() {
        currentPage++;
        performLookListRequest();
    }

    @Override
    public void onListViewStartScroll() {

    }
}
