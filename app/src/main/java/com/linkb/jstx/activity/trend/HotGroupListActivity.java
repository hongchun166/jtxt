package com.linkb.jstx.activity.trend;

import android.support.v7.widget.RecyclerView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.GroupResult;


/** 热门群列表页面
* */
public class HotGroupListActivity extends BaseActivity implements HttpRequestListener<GroupResult> {

    private GlobalEmptyView emptyView;
    private RecyclerView recyclerView;

    @Override
    protected void initComponents() {
        initData();
    }

    private void initData() {
        showProgressDialog(getString(R.string.tips_list_loading));
        HttpServiceManager.getAllGroup(this);
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    public void onHttpRequestSucceed(GroupResult result, OriginalCall call) {
        hideProgressDialog();

    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }
}
