package com.linkb.jstx.activity.trend;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.trend.GameAdapter;
import com.linkb.jstx.network.result.GameResult;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 游戏列表
* */
public class GameListActivity extends BaseActivity implements GameAdapter.GameItemClickListener{

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.emptyView)
    View emptyView;

    private GameAdapter mAdapter;
    private List<GameResult.DataBean> mGameList = new ArrayList<>();

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadDate();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new GameAdapter(this, this));
        loadDate();
    }
    private void loadDate(){
        refreshLayout.finishRefresh();

        GameResult.DataBean dataBean = new GameResult.DataBean();
        dataBean.setId(0);
        dataBean.setName(getResources().getString(R.string.huoshen_online));
        dataBean.setDesription(getResources().getString(R.string.blockchain));
        dataBean.setType(getResources().getString(R.string.chess));
        mGameList.clear();
        mGameList.add(dataBean);
        mAdapter.addAll(mGameList);
        emptyView.setVisibility(mGameList.size() > 0 ? View.INVISIBLE: View.VISIBLE);
    }


    @Override
    protected int getContentLayout() {
        return  R.layout.activity_game_list;
    }

    @OnClick(R.id.back_btn)
    public void onBack(){ finish();}

    @Override
    public void checkGameDetail() {
        startActivity(new Intent(GameListActivity.this, GameDetailActivity.class));
    }

    @Override
    public void downloadGame() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(BuildConfig.GAME_DOWNLOAD_URL));
        startActivity(intent);
    }
}
