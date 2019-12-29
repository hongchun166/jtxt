package com.linkb.jstx.activity.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.wallet.MoreCurrencyAdapter;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.CurrencyListResult;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreCurrencyActivity extends BaseActivity implements HttpRequestListener<CurrencyListResult>, MoreCurrencyAdapter.CurrencySelectListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    public static final String CURRENCY_KEY = "CurrencyId";

    private List<CurrencyListResult.DataListBean> mList = new ArrayList<>();

    private MoreCurrencyAdapter mAdapter;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initDate();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MoreCurrencyAdapter(mList, this);
        mAdapter.setListener(this);
        recyclerView.setAdapter(mAdapter);

        initDate();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_more_currency;
    }

    private void initDate() {
        HttpServiceManager.queryCurrencyList(this);
    }

    @Override
    public void onHttpRequestSucceed(CurrencyListResult result, OriginalCall call) {
        refreshLayout.finishRefresh();
        if (result.isSuccess()){
            mAdapter.replaceAll(result.getDataList());
        }else {

        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        refreshLayout.finishRefresh();
    }

    @Override
    public void onCurrencySelected(CurrencyListResult.DataListBean dataListBean) {
        Intent intent = getIntent();
        intent.putExtra(CurrencyListResult.DataListBean .class.getName(), dataListBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.back_btn)
    public void onBack(){
        finish();
    }


}
