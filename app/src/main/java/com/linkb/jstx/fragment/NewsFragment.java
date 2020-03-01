package com.linkb.jstx.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.R;
import com.linkb.jstx.adapter.trend.NewsAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.NewsDataResult;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
* 新闻快讯
* */
public class NewsFragment extends LazyLoadFragment implements HttpRequestListener<NewsDataResult> {

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    private NewsAdapter mAdapter;
    /*
    * 数据的页数
    * */
    private int mPage = 0;

    private List<NewsDataResult.DataListBean> mNewsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mPage = 0 ;
                loadNewsData(mPage);
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter( mAdapter = new NewsAdapter(mNewsList, getContext()));
        mAdapter.setOnItemClickCallback((view, bean) -> httpGetRedBag(String.valueOf(bean.getId())));
    }


    @Override
    public void requestData() {
        loadNewsData(mPage);
    }

    private void loadNewsData(int page){
        HttpServiceManager.getNewsOrExpressList(page, 1, this);
    }

    @Override
    public void onHttpRequestSucceed(NewsDataResult result, OriginalCall call) {
        refreshLayout.finishRefresh();
        if (result.isSuccess()){
            mAdapter.addAll(result.getDataList());
        }else {
            showToastView(result.message);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }

    public void httpGetRedBag(String id){
        String account= Global.getCurrentUser().account;
        HttpServiceManagerV2.getRedBag(account, id, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {

            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }

}
