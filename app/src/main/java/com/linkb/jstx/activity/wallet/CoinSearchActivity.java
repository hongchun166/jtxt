package com.linkb.jstx.activity.wallet;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.wallet.CoinSearchAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.CoinSearchResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 钱包地址搜索
* */
public class CoinSearchActivity extends BaseActivity implements CoinSearchAdapter.SearchCoinClickListener{

    @BindView(R.id.search_edit_text)
    EditText searchEdt;
    @BindView(R.id.search_content_cly)
    View searchContentCly;
    @BindView(R.id.content_textView)
    TextView contentTv;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.search_recycler_view)
    RecyclerView searchRecyclerView;

    private List<CoinSearchResult.DataBean> mList = new ArrayList<>();
    private CoinSearchAdapter mAdapter;
    private User mSelf;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        mSelf = Global.getCurrentUser();

        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emptyView.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(charSequence)){
                    searchContentCly.setVisibility(View.VISIBLE);
                    contentTv.setText(charSequence);
                }else {
                    searchContentCly.setVisibility(View.INVISIBLE);
                    contentTv.setText("");
                    mList.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CoinSearchAdapter(mList,this,  this);
        searchRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_seach_coin;
    }

    @OnClick(R.id.search_content_cly)
    public void searchCoin(){
        String coinAddress = contentTv.getText().toString();
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.search)));
        HttpServiceManager.queryCoin(mSelf.account, coinAddress, searchListener);
    }

    @OnClick(R.id.cancel_btn_tv)
    public void onBack(){
        finish();
    }

    @Override
    public void onAddCoin(CoinSearchResult.DataBean dataBean) {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_add)));
        HttpServiceManager.addCoin(mSelf.account, dataBean.getAddress(), dataBean.getEthToken(), addCoinListener);
    }

    private HttpRequestListener<CoinSearchResult> searchListener = new HttpRequestListener<CoinSearchResult>() {
        @Override
        public void onHttpRequestSucceed(CoinSearchResult result, OriginalCall call) {
            hideProgressDialog();
            if (result.isSuccess() && result.getData() != null){
                emptyView.setVisibility(View.INVISIBLE);
                searchContentCly.setVisibility(View.VISIBLE);
                mList.clear();
                mList.add(result.getData());
                mAdapter.notifyDataSetChanged();
            }else {
                emptyView.setVisibility(View.VISIBLE);
                searchContentCly.setVisibility(View.INVISIBLE);
                mList.clear();
                mAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };

    private HttpRequestListener<BaseResult> addCoinListener = new HttpRequestListener<BaseResult>() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
            hideProgressDialog();
            if (result.isSuccess()) {
                showToastView(getString(R.string.add_coin_success));
                setResult(RESULT_OK, getIntent());
            }else {
                showToastView(getString(R.string.add_coin_failed));
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };
}
