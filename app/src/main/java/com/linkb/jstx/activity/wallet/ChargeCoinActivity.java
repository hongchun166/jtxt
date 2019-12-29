package com.linkb.jstx.activity.wallet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.stmt.query.In;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.QueryAssetsResult;
import com.linkb.jstx.util.ClipboardUtils;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.ZXingUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 充币
* */
public class ChargeCoinActivity extends BaseActivity implements HttpRequestListener<BaseDataResult> {

    @BindView(R.id.imageView2)
    ImageView qrcodeImage;
    @BindView(R.id.imageView4)
    ImageView copyImageView;
    @BindView(R.id.textView10)
    TextView chargeAddressTv;
    @BindView(R.id.copy_fly)
    View copyFly;

    public static final String CURRENCY_ID = "CurrencyID";
    private int CurrencyID;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        Intent intent = getIntent();
        CurrencyID =  intent.getIntExtra(CURRENCY_ID, 0 );
        if (CurrencyID > 0){
            showProgressDialog("");
            HttpServiceManager.getChargeCoinAddress(CurrencyID, this);
        }
    }

    private void convertQrCode(String url){
        Bitmap bitmap = ZXingUtils.createQRImage(url,
                ConvertUtils.dp2px(150), ConvertUtils.dp2px(150));
        qrcodeImage.setImageBitmap(bitmap);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_charge_coin;
    }

    @Override
    public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
        hideProgressDialog();
        if (result.isSuccess()){
            if (TextUtils.isEmpty(result.getData())){
                copyFly.setVisibility(View.INVISIBLE);
                convertQrCode("");
                chargeAddressTv.setText("");
            }else {
                copyFly.setVisibility(View.VISIBLE);
                convertQrCode(result.getData());
                chargeAddressTv.setText(result.getData());
            }
        }else {
            copyFly.setVisibility(View.INVISIBLE);
            convertQrCode(result.message);
            chargeAddressTv.setText("");
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }

    @OnClick(R.id.copy_fly)
    public void copyAddress(){
        ClipboardUtils.copy(this, chargeAddressTv.getText().toString());
    }

    @OnClick(R.id.back_btn)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.bill_fly)
    public void onGotoBill(){
        startActivity(new Intent(this, BillActivity.class));
    }
}
