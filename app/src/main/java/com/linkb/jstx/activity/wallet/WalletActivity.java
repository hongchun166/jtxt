package com.linkb.jstx.activity.wallet;

import android.content.Intent;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.wallet.CoinExchangeListAdapter;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.QueryAssetsResult;
import com.linkb.jstx.network.result.QueryExchangeRateResult;
import com.linkb.jstx.util.ConvertUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletActivity extends BaseActivity implements HttpRequestListener<BaseResult> {
    private static final int ADD_COIN_REQUEST_CODE = 0x11;

    @BindView(R.id.emptyView) GlobalEmptyView emptyView;
    private CoinExchangeListAdapter mAdapter;
    private List<QueryAssetsResult.DataListBean> mList = new ArrayList<>();
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.refreshLayout) RefreshLayout refreshLayout;
    @BindView(R.id.money_visible_fly) View moneyHideFly;
    @BindView(R.id.money_visible_img) ImageView moneyHideImg;
    @BindView(R.id.total_assets_btc_tv) TextView total_assets_btc_tv;
    @BindView(R.id.total_assets_cny_tv) TextView total_assets_cny_tv;
    @BindView(R.id.hide_currency_ckb) CheckBox hideCurrencyCkb;

    private boolean enableMoneyVisible = true;
    private String totalAssetsBtc = "0.0000";
    private String totalAssetsCny = "0.0000";

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        hideCurrencyCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    List<QueryAssetsResult.DataListBean> list = new ArrayList<>();
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).getAmount() > 0){
                            list.add(mList.get(i));
                        }else {
                            continue;
                        }
                    }
                    mAdapter.replaceAll(list);
                }else {
                    mAdapter.replaceAll(mList);
                }
            }
        });

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initDate();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        VerticalSpaceItemDecoration itemDecor = new VerticalSpaceItemDecoration(ConvertUtils.dp2px(8));
        recyclerView.addItemDecoration(itemDecor);
        recyclerView.setAdapter(mAdapter = new CoinExchangeListAdapter(this, mList));

        initDate();
    }

//    private void initData(boolean refresh){
//
//        mAdapter.replaceAll(mList);
//    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_wallet;
    }

    private void initDate(){
        showProgressDialog("");
        HttpServiceManager.queryAssetsBalance(this);
    }

    /*获取当前实时汇率*/
    private void getCurrentExchangeRate() {
        HttpServiceManager.queryCoinCurrentExchangeRate(this);
    }

    @OnClick(R.id.money_visible_fly)
    public void hideMoney(){
        if (enableMoneyVisible) {
            moneyHideImg.setImageResource(R.mipmap.close);
            enableMoneyVisible = false;
        }else {
            moneyHideImg.setImageResource(R.mipmap.open);
            enableMoneyVisible = true;
        }
        changeMoneyVisibleState();
    }

    @OnClick(R.id.bill_tv)
    public void gotoBill(){
        startActivity(new Intent(WalletActivity.this, BillActivity.class));
    }

    @OnClick(R.id.exchange_tv)
    public void gotoExchange(){
        startActivity(new Intent(WalletActivity.this, ExChangeActivity.class));
    }

    @OnClick(R.id.imageView_image_back)
    public void onBack(){
        finish();
    }

    private void changeMoneyVisibleState() {
        total_assets_btc_tv.setText(enableMoneyVisible ? String.valueOf(totalAssetsBtc) : "******");
        total_assets_cny_tv.setText(enableMoneyVisible ? String.valueOf(totalAssetsCny) : "******");
    }

    @OnClick(R.id.financial_btn)
    public void onFinancial(){
        startActivity(new Intent(WalletActivity.this, FinancialActivity.class));
    }

    @OnClick(R.id.search_btn_tv)
    public void onSearchCoin() {
        startActivityForResult(new Intent(WalletActivity.this, CoinSearchActivity.class), ADD_COIN_REQUEST_CODE);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        hideProgressDialog();
        refreshLayout.finishRefresh();
        if (result.isSuccess()){
            if (result instanceof QueryAssetsResult){
                QueryAssetsResult result1 = (QueryAssetsResult) result;
                totalAssetsBtc = ConvertUtils.doubleToString(result1.getData().getTotalBTC());
                totalAssetsCny = ConvertUtils.doubleToString(result1.getData().getTotalCNY());
                total_assets_btc_tv.setText(totalAssetsBtc);
                total_assets_cny_tv.setText(totalAssetsCny);
                mList.clear();
                mList.addAll(result1.getDataList());
                mAdapter.replaceAll(result1.getDataList());
            }else if (result instanceof QueryExchangeRateResult){
                QueryExchangeRateResult result2 = (QueryExchangeRateResult) result;
            }

        }else {
            showToastView(result.message);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
        refreshLayout.finishRefresh();
    }

    public class VerticalSpaceItemDecoration extends RecyclerView.ItemDecoration {

        private final int verticalSpaceHeight;

        public VerticalSpaceItemDecoration(int verticalSpaceHeight) {
            this.verticalSpaceHeight = verticalSpaceHeight;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.bottom = verticalSpaceHeight;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0x10){
            initDate();
        }

        if (resultCode == RESULT_OK && requestCode == ADD_COIN_REQUEST_CODE){
            initDate();
        }
    }
}
