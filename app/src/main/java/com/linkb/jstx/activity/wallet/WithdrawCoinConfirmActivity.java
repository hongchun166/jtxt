package com.linkb.jstx.activity.wallet;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.BuildConfig;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.component.countdownbutton.CountDownButton;
import com.linkb.jstx.fragment.PasswordInputFragment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.WithDrawResult;
import com.linkb.jstx.util.ConvertUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**提币确认
* */
public class WithdrawCoinConfirmActivity extends BaseActivity implements PasswordInputFragment.VerifyApplyPasswordListener, HttpRequestListener<BaseResult> {


    @BindView(R.id.input_editText)
    TextView withdrawAmountTv;
    @BindView(R.id.textView19)
    TextView actualArrivalAmountTv;

    @BindView(R.id.textView20)
    TextView actualArrivalCurrenctTv;

    @BindView(R.id.textView22)
    TextView processingFeeTv;

    @BindView(R.id.textView23)
    TextView processingFeeCurrency;

    @BindView(R.id.textView14)
    TextView actualArrivalAddressTv;

    @BindView(R.id.countbtn)
    CountDownButton countDownButton;

    @BindView(R.id.editText3)
    EditText messageVerifyCode;

    @BindView(R.id.withdraw_confirm_button)
    Button confirmBtn;

    @BindView(R.id.imageView6)
    WebImageView coinIconImage;

    private PasswordInputFragment mPasswordInputFragment;

    private int currencyId;
    private String coinIcon;
    private String currencyMark;
    private String remark;
    private String money;
    private String arriveAddress;
    /** 手续费是按照比例计算
     * */
    private double processingFeed;
    private double actualArrivalMoney;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        messageVerifyCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmBtn.setEnabled((i + i2) == 6);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        initData();
    }

    private void initData(){
        coinIcon = getIntent().getStringExtra("currencyIcon");
        currencyId = getIntent().getIntExtra("currencyId", 18);
        currencyMark = getIntent().getStringExtra("currencyMark");
        remark = getIntent().getStringExtra("remark");
        money = getIntent().getStringExtra("money");
        arriveAddress = getIntent().getStringExtra("arriveAddress");
        processingFeed = getIntent().getDoubleExtra("processingFeed", 0d);
        coinIconImage.load(BuildConfig.API_HOST + coinIcon, R.mipmap.btc);
        actualArrivalMoney = Double.valueOf(money) - processingFeed;
        withdrawAmountTv.setText(money);
        actualArrivalAmountTv.setText(ConvertUtils.doubleToString(actualArrivalMoney));
        actualArrivalCurrenctTv.setText(currencyMark);
        processingFeeTv.setText(ConvertUtils.doubleToString(processingFeed));
        processingFeeCurrency.setText(currencyMark);
        actualArrivalAddressTv.setText(arriveAddress);


    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_withdraw_coin_confirm;
    }

    @OnClick(R.id.countbtn)
    public void onCountDown(){
        countDownButton.startCount();
        HttpServiceManager.getApplyVerifyCode(this);
    }

    @OnClick(R.id.withdraw_confirm_button)
    public void onConfirmSubmit(){
        if (mPasswordInputFragment == null){
            mPasswordInputFragment = PasswordInputFragment.getInstance(withdrawAmountTv.getText().toString(), currencyMark, getString(R.string.withdraw_coin));
            mPasswordInputFragment.setListener(this);
        }
        mPasswordInputFragment.show(this.getSupportFragmentManager(), "PasswordInputFragment");
    }


    @OnClick(R.id.bill_fly)
    public void onBillQeury(){
        Intent intent = new Intent(this, BillActivity.class);
        startActivity(intent);
    }

    @Override
    public void onVerifySuccess(String password) {
        showProgressDialog("");
        HttpServiceManager.withdrawCoin(Global.getCurrentUser().account, currencyId, remark, money,
                arriveAddress, messageVerifyCode.getText().toString(), this);
    }

    @Override
    public void onVerifyFailed() {

    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        hideProgressDialog();
        if (result instanceof BaseDataResult){
            BaseDataResult baseDataResult = (BaseDataResult) result;
            if (!result.isSuccess()){
                showToastView(result.message);
            }else {
//                messageVerifyCode.setText(baseDataResult.getData());
//                if (baseDataResult.getData().length() > 0){
//                    messageVerifyCode.setSelection(baseDataResult.getData().length());
//                }
            }
        }else if (result instanceof WithDrawResult){
            if (result.isSuccess()){
                startActivity(new Intent(this, WithDrawFinishActivity.class));
                setResult(RESULT_OK, getIntent());
                finish();
            }else {
                showToastView(result.message);
            }
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }


}
