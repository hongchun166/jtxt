package com.linkb.jstx.activity.wallet;

import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.dialog.CommonInputDialog;
import com.linkb.jstx.fragment.PasswordInputFragment;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.CoinTransferResult;
import com.linkb.jstx.network.result.QueryAssetsResult;
import com.linkb.jstx.network.result.QueryExchangeRateResult;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.FileURLBuilder;
import com.linkb.video.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CoinTransferActivity extends BaseActivity implements CommonInputDialog.InputGetListener, PasswordInputFragment.VerifyApplyPasswordListener{

    @BindView(R.id.imageView29)
    WebImageView avatarImage;

    @BindView(R.id.textView107)
    TextView nickNameTv;

    @BindView(R.id.editText7)
    EditText coinTransferAmountEdt;

    @BindView(R.id.textView164)
    TextView coinToCNYTv;

    @BindView(R.id.textView161)
    TextView coinTv;

    @BindView(R.id.textView163)
    TextView transferCoinTv;

    private CommonInputDialog mCommonInputDialog;

    private PasswordInputFragment mPasswordInputFragment;

    /** 我的USDT币财产信息
    * */
    private QueryAssetsResult.DataListBean USDTCoinBalanceData;
    /** USDT币当前货币信息
    * */
    private QueryExchangeRateResult.DataListBean USDTCoinData;
    private static final int USDT_COIN_ID = 22;
    private static final String USDT_COIN_CODE = "usdt"; //dpc

    /** 转账说明
    * */
    private String transferStatement = "";

    private User mSelf;

    private Friend mFriend;


    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        mFriend = (Friend) getIntent().getSerializableExtra(Friend.class.getSimpleName());
        if (mFriend != null){
            avatarImage.load(FileURLBuilder.getUserIconUrl(mFriend.account), R.mipmap.lianxiren,999);
            nickNameTv.setText(mFriend.name);
        }
        mSelf = Global.getCurrentUser();

        coinTransferAmountEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)){
                    coinToCNYTv.setText("");
                }else if (USDTCoinData != null){
                    double input = Double.valueOf(charSequence.toString());
                    double price = USDTCoinData.getLast();
                    String value = ConvertUtils.doubleToString(input * price);
                    coinToCNYTv.setText(getString(R.string.coin_transfer_RMB_value, value));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        coinToCNYTv.setText("");
        //获取财产信息
        HttpServiceManager.queryAssetsBalance(queryAssetsListener);
        //获取汇率信息
        HttpServiceManager.queryCoinCurrentExchangeRate(queryCoinRateListener);
    }

    private HttpRequestListener<QueryAssetsResult> queryAssetsListener = new HttpRequestListener<QueryAssetsResult>() {
        @Override
        public void onHttpRequestSucceed(QueryAssetsResult result, OriginalCall call) {
            if (result.isSuccess()){
                for (QueryAssetsResult.DataListBean datalistBean : result.getDataList()) {
                    if (datalistBean.getCurrency().getId() == USDT_COIN_ID){
                        USDTCoinBalanceData = datalistBean;
                        refreshUI();
                    }
                }
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    private HttpRequestListener<QueryExchangeRateResult> queryCoinRateListener = new HttpRequestListener<QueryExchangeRateResult>() {
        @Override
        public void onHttpRequestSucceed(QueryExchangeRateResult result, OriginalCall call) {
            if (result.isSuccess()){
                for (QueryExchangeRateResult.DataListBean dateListBean : result.getDataList()) {
                    if (dateListBean.getCurrencyCode().equals(USDT_COIN_CODE)){
                        USDTCoinData = dateListBean;
                    }
                }
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    private void refreshUI(){
//        coinToCNYTv.setText(getString(R.string.coin_transfer_RMB_value, USDTCoinData.getPriceStr()));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_transfer;
    }

    @OnClick(R.id.button13)
    public void onTransfer() {
//        if (USDTCoinBalanceData == null || USDTCoinBalanceData.getAmount() <= 0){
//            ToastUtils.s(CoinTransferActivity.this, getString(R.string.coin_transfer_empty_error));
//            return;
//        }
//        if (Double.valueOf(coinTransferAmountEdt.getText().toString()) > (USDTCoinBalanceData.getAmount() - USDTCoinBalanceData.getFrozenAmount())){
//            ToastUtils.s(CoinTransferActivity.this, getString(R.string.coin_transfer_not_enough_error));
//            return;
//        }
        if (mPasswordInputFragment == null){
            mPasswordInputFragment = PasswordInputFragment.getInstance(coinTransferAmountEdt.getText().toString() ,USDT_COIN_CODE, getString(R.string.coin_transfer));
            mPasswordInputFragment.setListener(this);
        }
        mPasswordInputFragment.show(this.getSupportFragmentManager(), "PasswordInputFragment");
    }

    @OnClick(R.id.back_btn)
    public void onBack(){finish();}

    @OnClick(R.id.textView163)
    public void onTransferStatement(){
        if (mCommonInputDialog == null){
            mCommonInputDialog = new CommonInputDialog(this);
        }
        mCommonInputDialog.setOnDialogButtonClickListener(this);
        mCommonInputDialog.show();
    }

    @Override
    public void onGetInputContent(String content) {
        transferStatement = content;
        transferCoinTv.setText(getString(R.string.coin_transfer_statement_content, content));
    }

    @Override
    public void cancel() {

    }

    @Override
    public void onVerifySuccess(String password) {
        showProgressDialog("");
        HttpServiceManager.transferCoin(mSelf.account, String.valueOf(USDTCoinBalanceData.getCurrency().getId()),
                coinTransferAmountEdt.getText().toString(), transferStatement, "5", password, coinTransferHttpRequestListener);
    }

    @Override
    public void onVerifyFailed() {

    }

    private HttpRequestListener<CoinTransferResult> coinTransferHttpRequestListener = new HttpRequestListener<CoinTransferResult>() {
        @Override
        public void onHttpRequestSucceed(CoinTransferResult result, OriginalCall call) {
            hideProgressDialog();
            if (result.isSuccess()){
                Intent intent = getIntent();
                intent.putExtra(CoinTransferResult.DataBean.class.getName(), result.getData());
                setResult(RESULT_OK, intent);
                finish();
            }else {

                ToastUtils.s(CoinTransferActivity.this, result.message);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };
}
