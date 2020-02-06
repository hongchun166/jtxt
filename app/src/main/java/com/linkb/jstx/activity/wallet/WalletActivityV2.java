package com.linkb.jstx.activity.wallet;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
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
import com.linkb.jstx.adapter.wallet.MyCurrencyListAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.v2.AccountBalanceResult;
import com.linkb.jstx.network.result.v2.ListMyCurrencyResult;
import com.linkb.jstx.util.ConvertUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletActivityV2 extends BaseActivity{
    private static final int ADD_COIN_REQUEST_CODE = 0x11;

    @BindView(R.id.emptyView) GlobalEmptyView emptyView;


    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.refreshLayout) RefreshLayout refreshLayout;
    @BindView(R.id.money_visible_fly) View moneyHideFly;
    @BindView(R.id.money_visible_img) ImageView moneyHideImg;
    @BindView(R.id.total_assets_btc_tv) TextView total_assets_btc_tv;
    @BindView(R.id.hide_currency_ckb) CheckBox hideCurrencyCkb;

    @BindView(R.id.exchange_tv) TextView exchange_tv;
    @BindView(R.id.financial_btn) TextView financial_btn;
    @BindView(R.id.bill_tv) TextView bill_tv;

    private MyCurrencyListAdapter mAdapter;
    private List<ListMyCurrencyResult.DataBean> mList = new ArrayList<>();

    private boolean enableMoneyVisible = true;
    private String totalAssetsBtc = "0.00";
    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        hideCurrencyCkb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    List<ListMyCurrencyResult.DataBean> list = new ArrayList<>();
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(i).getLockBalance() > 0){
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
       final Context context=this;
        recyclerView.setAdapter(mAdapter = new MyCurrencyListAdapter(this, mList, new MyCurrencyListAdapter.OnItemClick() {
            @Override
            public void onItemClick(ListMyCurrencyResult.DataBean dataBean) {
                CurrencyDetailsActivityV2.navToAct(context,String.valueOf(dataBean.getCurrencyId()),dataBean.getCurrencyName());
            }
        }));

        int imgSize=66;
        {
            Drawable drawable=getResources().getDrawable(R.mipmap.ic_duihui_v2);
            drawable.setBounds(0,0,imgSize,imgSize);
            exchange_tv.setCompoundDrawables(null,drawable,null,null);
        }
        {
            Drawable drawable=getResources().getDrawable(R.mipmap.ic_licai_v2);
            drawable.setBounds(0,0,imgSize,imgSize);
            financial_btn.setCompoundDrawables(null,drawable,null,null);
        }
        {
            Drawable drawable=getResources().getDrawable(R.mipmap.ic_zhangdan_v2);
            drawable.setBounds(0,0,imgSize,imgSize);
            bill_tv.setCompoundDrawables(null,drawable,null,null);
        }

        initDate();
    }
    @Override
    protected int getContentLayout() {
        return R.layout.activity_wallet_v2;
    }

    private void initDate(){
        showProgressDialog("");
        User user=Global.getCurrentUser();
        HttpServiceManagerV2.getAccountBalance(user.account, new HttpRequestListener<AccountBalanceResult>() {
            @Override
            public void onHttpRequestSucceed(AccountBalanceResult result, OriginalCall call) {
                if(result.isSuccess()){
                    total_assets_btc_tv.setText(String.valueOf(result.getData().getBalance()));
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
            }
        });
        HttpServiceManagerV2.listMyCurrency(user.account, "", new HttpRequestListener<ListMyCurrencyResult>() {
            @Override
            public void onHttpRequestSucceed(ListMyCurrencyResult result, OriginalCall call) {
                hideProgressDialog();
                refreshLayout.finishRefresh();
                if (result.isSuccess()){
                    List<ListMyCurrencyResult.DataBean> dataBeanList=result.getData();
                    mList.clear();
                    mList.addAll(dataBeanList);
                    mAdapter.replaceAll(dataBeanList);
                }else {
                    showToastView(result.message);
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
                refreshLayout.finishRefresh();
            }
        });
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
        startActivity(new Intent(WalletActivityV2.this, BillActivityV2.class));
    }

    @OnClick(R.id.exchange_tv)
    public void gotoExchange(){
        startActivity(new Intent(WalletActivityV2.this, ExChangeActivity.class));
    }

    @OnClick(R.id.imageView_image_back)
    public void onBack(){
        finish();
    }

    private void changeMoneyVisibleState() {
        total_assets_btc_tv.setText(enableMoneyVisible ? String.valueOf(totalAssetsBtc) : "******");
    }

    @OnClick(R.id.financial_btn)
    public void onFinancial(){
        startActivity(new Intent(WalletActivityV2.this, FinancialActivity.class));
    }

    @OnClick(R.id.search_btn_tv)
    public void onSearchCoin() {
        startActivityForResult(new Intent(WalletActivityV2.this, CoinSearchActivity.class), ADD_COIN_REQUEST_CODE);
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
