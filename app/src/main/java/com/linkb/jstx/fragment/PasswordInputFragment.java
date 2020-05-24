package com.linkb.jstx.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jungly.gridpasswordview.GridPasswordView;
import com.linkb.R;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.util.KeyBoardUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordInputFragment extends BaseDialogFragment implements HttpRequestListener {

    @BindView(R.id.password_input_view)
    GridPasswordView gridPasswordView;
    @BindView(R.id.textView82)
    TextView passwordErrorTips;
    @BindView(R.id.textView77)
    TextView moneyTv;
    @BindView(R.id.textView78)
    TextView currencyTv;
    @BindView(R.id.textView76)
    TextView tradeTypeTv;

    private String mRedPacketMoney;

    private String mCurrencyMark;

    private String mTradeType;

    private VerifyApplyPasswordListener mListener;

    private String applyPassword = "";

    public static PasswordInputFragment getInstance(String redPacketMoney, String currencyMark, String tradeType) {
        PasswordInputFragment fragment = new PasswordInputFragment();
        Bundle bundle = new Bundle();
        bundle.putString("redPacketMoney", redPacketMoney);
        bundle.putString("currencyMark", currencyMark);
        bundle.putString("tradeType", tradeType);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static PasswordInputFragment getInstance(String redPacketMoney, String currencyMark) {
        PasswordInputFragment fragment = new PasswordInputFragment();
        Bundle bundle = new Bundle();
        bundle.putString("redPacketMoney", redPacketMoney);
        bundle.putString("currencyMark", currencyMark);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_input, null);
        ButterKnife.bind(this, view);

        gridPasswordView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {
                gotoVerifyPassword(psw);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            mRedPacketMoney = bundle.getString("redPacketMoney");
            mCurrencyMark = bundle.getString("currencyMark");
            mTradeType = bundle.getString("tradeType");
            moneyTv.setText(mRedPacketMoney);
            currencyTv.setText(mCurrencyMark);
            tradeTypeTv.setText(mTradeType);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        KeyBoardUtil.onPopSoftInput(gridPasswordView);

        gridPasswordView.postDelayed(()->{
            gridPasswordView.callOnClick();
        },200);


    }

    public void setListener(VerifyApplyPasswordListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void gotoVerifyPassword(String password) {
        applyPassword = password;
        showProgressDialog(getResources().getString(R.string.verify_password_ing));
//        HttpServiceManager.verifyApplyPassword(password, this);
        HttpServiceManagerV2.validateTradePassword(password, this);
    }

    @OnClick(R.id.dismiss_btn)
    public void fragmentDismiss(){
        this.dismiss();
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        gridPasswordView.clearPassword();
        hideProgressDialog();
        if (result.isSuccess()){
            this.dismiss();
            passwordErrorTips.setVisibility(View.GONE);
            if (mListener != null) mListener.onVerifySuccess(applyPassword);
        }else {
            Global.setTradePassword("");
            passwordErrorTips.setVisibility(View.VISIBLE);
            if (mListener != null) mListener.onVerifyFailed();
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        gridPasswordView.clearPassword();
        passwordErrorTips.setVisibility(View.VISIBLE);
        hideProgressDialog();
    }

    public void setRedPacketMoney(String mRedPacketMoney) {
        this.mRedPacketMoney = mRedPacketMoney;
        moneyTv.setText(mRedPacketMoney);
    }

    public void setCurrencyMark(String mCurrencyMark) {
        this.mCurrencyMark = mCurrencyMark;
        currencyTv.setText(mCurrencyMark);
    }

    @OnClick(R.id.imageView25)
    public void onCloseDialog(){
        this.dismiss();
    }

    public interface VerifyApplyPasswordListener{
        void onVerifySuccess(String password);
        void onVerifyFailed();
    }
}
