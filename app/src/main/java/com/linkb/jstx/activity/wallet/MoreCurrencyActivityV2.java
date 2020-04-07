package com.linkb.jstx.activity.wallet;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.wallet.MoreCurrencyAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.CurrencyListResult;
import com.linkb.jstx.network.result.v2.ListMyCurrencyResult;
import com.linkb.jstx.network.result.v2.RedpackgeListCurrenCyResult;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoreCurrencyActivityV2 extends BaseActivity implements HttpRequestListener<RedpackgeListCurrenCyResult>, MoreCurrencyAdapter.CurrencySelectListener {

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
        User seld=Global.getCurrentUser();
        HttpServiceManagerV2.redpackgeListCurrenCy(seld.getAccount(),this);
    }

    @Override
    public void onHttpRequestSucceed(RedpackgeListCurrenCyResult result, OriginalCall call) {
        refreshLayout.finishRefresh();
        if (result.isSuccess()){
            List<CurrencyListResult.DataListBean> beanList=new ArrayList<>();
            if(result.getData()!=null){
                for (RedpackgeListCurrenCyResult.DataBean datum : result.getData()) {
                    CurrencyListResult.DataListBean dataListBean=new CurrencyListResult.DataListBean();
                    dataListBean.setCurrencyIcon(datum.getCurrencyIcon());
                    dataListBean.setCurrencyName(datum.getCurrencyName());
                    dataListBean.setAmount(0);
                    dataListBean.setCurrencyMark(datum.getCurrencyName());
                    dataListBean.setId(datum.getId());
                    beanList.add(dataListBean);
                }
            }
            mAdapter.replaceAll(beanList);
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
