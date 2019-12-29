package com.linkb.jstx.activity.wallet;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.wallet.CoinExchangeListAdapter;
import com.linkb.jstx.adapter.wallet.ExchangeRateAdapter;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.util.ConvertUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 兑汇页面
* */
public class ExChangeActivity extends BaseActivity {

    private ExchangeRateAdapter mAdapter;
    private List<String> mList = new ArrayList<>();
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                initData(false);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new ExchangeRateAdapter(this, mList));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_exchange;
    }

    @OnClick(R.id.back_btn)
    public void onBackTv(){
        finish();
    }
}
