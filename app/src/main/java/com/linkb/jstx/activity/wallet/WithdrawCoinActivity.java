package com.linkb.jstx.activity.wallet;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.activity.QrcodeScanActivity;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.QueryAssetsResult;
import com.linkb.jstx.network.result.QueryWithdrawAmountResult;
import com.linkb.jstx.util.ConvertUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**提币
* */
public class WithdrawCoinActivity extends BaseActivity  implements EasyPermissions.PermissionCallbacks{

    private static final int REQUEST  = 0x10;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;
    private static final int SCAN_QR_CODE_REQUEST  = 0x11;

    @BindView(R.id.imageView6)
    WebImageView coinIconImage;

    @BindView(R.id.input_editText)
    EditText inputEditText;

    @BindView(R.id.textView16)
    TextView availableBalanceTv;

    @BindView(R.id.textView17)
    TextView availableBalanceCurrencyTv;

    @BindView(R.id.textView19)
    TextView actualArrivalAmountTv;

    @BindView(R.id.textView20)
    TextView actualArrivalCurrenctTv;

    @BindView(R.id.textView22)
    TextView processingFeeTv;

    @BindView(R.id.textView23)
    TextView processingFeeCurrency;

    @BindView(R.id.textView25)
    TextView singleMinTv;

    @BindView(R.id.textView30)
    TextView singleMostTv;

    @BindView(R.id.textView27)
    TextView singleMinUnitTv;

    @BindView(R.id.textView31)
    TextView singleMostUnitTv;

    @BindView(R.id.editText2)
    EditText withdrawAddressEdt;

    @BindView(R.id.editText3)
    EditText ramarkEdt;

    @BindView(R.id.withdraw_confirm_button)
    Button confirmBtn;

    private QueryAssetsResult.DataListBean dataListBean;
    /** 手续费是按照比例计算
    * */
    private double processingFeed = 0.00d;
    private double processingFeedData = 0.00d;
    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmBtn.setEnabled(charSequence.length() > 0);

                if (!TextUtils.isEmpty(charSequence.toString())){
                    processingFeedData = Double.valueOf(charSequence.toString()) * processingFeed;
                    processingFeeTv.setText(ConvertUtils.doubleToString(processingFeedData));
                    double doubleStr = Double.valueOf(charSequence.toString()) - processingFeedData;
                    actualArrivalAmountTv.setText(ConvertUtils.doubleToString(doubleStr));
                }else {
                    processingFeeTv.setText("");
                    actualArrivalAmountTv.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        initData();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_withdraw_coin;
    }

    @OnClick(R.id.withdraw_confirm_button)
    public void gotoConfirm(){
        if (Double.valueOf(inputEditText.getText().toString()) > dataListBean.getAmount()){
            showToastView(R.string.withdraw_amount_too_large);
            return;
        }
        if (TextUtils.isEmpty(withdrawAddressEdt.getText().toString())){
            showToastView(R.string.withdraw_address_empty_error);
            return;
        }

        Intent intent = new Intent(this, WithdrawCoinConfirmActivity.class);
        intent.putExtra("currencyId", dataListBean.getCurrency().getId());
        intent.putExtra("currencyMark", dataListBean.getCurrency().getCurrencyMark());
        intent.putExtra("remark", ramarkEdt.getText().toString());
        intent.putExtra("money", inputEditText.getText().toString());
        intent.putExtra("processingFeed", processingFeedData);
        intent.putExtra("arriveAddress", withdrawAddressEdt.getText().toString());
        intent.putExtra("currencyIcon", dataListBean.getCurrency().getCurrencyIcon());
        startActivityForResult(intent, REQUEST);
    }

    private void initData(){
        dataListBean = (QueryAssetsResult.DataListBean) getIntent().getSerializableExtra(QueryAssetsResult.DataListBean.class.getName());
        singleMinUnitTv.setText(dataListBean.getCurrency().getCurrencyMark());
        singleMostUnitTv.setText(dataListBean.getCurrency().getCurrencyMark());
        coinIconImage.load(BuildConfig.API_HOST + dataListBean.getCurrency().getCurrencyIcon(), R.mipmap.btc);
        HttpServiceManager.queryWithdrawAmount(dataListBean.getCurrency().getId(), queryAvailableAmountListener);
    }


    @OnClick(R.id.back_btn)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.bill_fly)
    public void onBillQeury(){
        Intent intent = new Intent(this, BillActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.imageView43)
    public void onScanAddress(){
        //扫描二维码获得地址
        requestCodeQRCodePermissions();
    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQRCodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "扫描二维码需要打开相机和散光灯的权限", REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }else {
            Intent intent = new Intent(this, QrcodeScanActivity.class);
            intent.putExtra(Constant.QrCodeType.QR_CODE_TYPE, Constant.QrCodeType.WALLET_QRCODE);
            startActivityForResult(intent, SCAN_QR_CODE_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST){
            setResult(RESULT_OK, getIntent());
            finish();
        }
        if (resultCode == RESULT_OK && requestCode == SCAN_QR_CODE_REQUEST){
            String walletAdress = data.getStringExtra(Constant.QrCodeType.QR_CODE_CONTENT);
            withdrawAddressEdt.setText(walletAdress);
        }
    }

    private HttpRequestListener<QueryWithdrawAmountResult>  queryAvailableAmountListener = new HttpRequestListener<QueryWithdrawAmountResult>() {
        @Override
        public void onHttpRequestSucceed(QueryWithdrawAmountResult result, OriginalCall call) {
            if (result.isSuccess()){
                processingFeed = result.getData().getRateAmount();
                availableBalanceTv.setText(ConvertUtils.doubleToString(result.getData().getUseAmount()));
                availableBalanceCurrencyTv.setText(dataListBean.getCurrency().getCurrencyMark());

                processingFeeTv.setText(ConvertUtils.doubleToString(processingFeed));
                processingFeeCurrency.setText(dataListBean.getCurrency().getCurrencyMark());

                actualArrivalAmountTv.setText(ConvertUtils.doubleToString(result.getData().getActulAmount()));
                actualArrivalCurrenctTv.setText(dataListBean.getCurrency().getCurrencyMark());

                singleMinTv.setText(ConvertUtils.doubleToString(result.getData().getExtractMin()));
                singleMostTv.setText(ConvertUtils.doubleToString(result.getData().getExtractMax()));
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Intent intent = new Intent(this, QrcodeScanActivity.class);
        intent.putExtra(Constant.QrCodeType.QR_CODE_TYPE, Constant.QrCodeType.WALLET_QRCODE);
        startActivityForResult(intent, SCAN_QR_CODE_REQUEST);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
    }
}
