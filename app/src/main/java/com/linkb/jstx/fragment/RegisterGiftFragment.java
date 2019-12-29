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

import com.linkb.R;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.LoginResult;
import com.linkb.jstx.network.result.ReceivedRedPacketResult;
import com.linkb.jstx.util.ConvertUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterGiftFragment extends BaseDialogFragment {

    private LoginResult.DataBean mDataBean;

    @BindView(R.id.textView121)
    TextView moneyTv;
    @BindView(R.id.textView122)
    TextView moneyUnitTv;
    @BindView(R.id.textView123)
    TextView moneyTipsTv;


    public static RegisterGiftFragment getInstance(LoginResult.DataBean dataBean) {
        RegisterGiftFragment fragment = new RegisterGiftFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(LoginResult.DataBean.class.getName(), dataBean);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gift_register, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            mDataBean = (LoginResult.DataBean) bundle.getSerializable(LoginResult.DataBean.class.getName());
            moneyTv.setText(ConvertUtils.doubleToString(mDataBean.getReceiveMoney()));
            moneyUnitTv.setText("DPC");
            moneyTipsTv.setText(getResources().getString(R.string.register_gift_tips, mDataBean.getReceiveMoney()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @OnClick(R.id.button6)
    public void receivedGift(){
        HttpServiceManager.receiveRedPacket(mDataBean.getCurrencyId(), mDataBean.getFromAccount(), mDataBean.getRedFlag(), mListenner);
    }

    @OnClick(R.id.imageView38)
    public void closeFragment(){
        dismiss();
    }

    private HttpRequestListener<ReceivedRedPacketResult> mListenner = new HttpRequestListener<ReceivedRedPacketResult>() {
        @Override
        public void onHttpRequestSucceed(ReceivedRedPacketResult result, OriginalCall call) {
            RegisterGiftFragment.this.dismiss();
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };
}
