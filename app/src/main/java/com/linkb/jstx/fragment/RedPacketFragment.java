package com.linkb.jstx.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.wallet.RedPacketReceivedActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.ReceivedRedPacketResult;
import com.linkb.jstx.network.result.SendRedPacketResult;
import com.linkb.jstx.util.ConvertUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.linkb.jstx.network.result.QueryRedPacketStatusResult.REDPACKET_STATUS;
import static com.linkb.jstx.network.result.QueryRedPacketStatusResult.RED_PACKET_AVAILABLE;
import static com.linkb.jstx.network.result.QueryRedPacketStatusResult.RED_PACKET_RECEIVEDED;
import static com.linkb.jstx.network.result.QueryRedPacketStatusResult.RED_PACKET_RECEIVEDED_EMPTY;
import static com.linkb.jstx.network.result.QueryRedPacketStatusResult.RED_PACKET_TIME_OUT;

public class RedPacketFragment extends BaseDialogFragment implements HttpRequestListener<ReceivedRedPacketResult> {

    @BindView(R.id.open_btn_frameLayout)
    View openBtnView;
    @BindView(R.id.textView66)
    TextView redPacketSender;
    @BindView(R.id.textView65)
    TextView redPacketRemark;
    @BindView(R.id.textView85)
    TextView checkDetailBtn;


    private SendRedPacketResult.DataBean mDataBean;
    /** 红包状态， 0 ： 可领取， 1： 不存在， 2：已过期， 3： 已领完 ， 4：已领取, 5:自己发的红包
    * */
    private int mRedPacketStatus;

    public static RedPacketFragment getInstance(SendRedPacketResult.DataBean mDataBean, int redPacketStatus) {
        RedPacketFragment fragment = new RedPacketFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(SendRedPacketResult.DataBean.class.getName(), mDataBean);
        bundle.putInt(REDPACKET_STATUS, redPacketStatus);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ConvertUtils.dp2px(240);
        params.height = ConvertUtils.dp2px(360);
        getDialog().getWindow().setAttributes( params);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_open_red_packet, null);
        ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.open_btn_frameLayout)
    public void onOpenRedPaclet(){
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(openBtnView, "rotationY", 0f, 720f);
        objectAnimator.setDuration(2000);
        objectAnimator.start();
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                HttpServiceManager.receiveRedPacket(mDataBean.getCurrencyId(), mDataBean.getUserAccount(), mDataBean.getRedFlag(), RedPacketFragment.this);
            }
        });
    }

    @OnClick(R.id.textView85)
    public void checkRedPacketDetail(){
        //红包领取成功
        Intent intent = new Intent(getActivity(), RedPacketReceivedActivity.class);
        intent.putExtra(SendRedPacketResult.DataBean.class.getName(), mDataBean);
        intent.putExtra(SendRedPacketResult.DataBean.class.getName(), mDataBean);
        startActivity(intent);
        RedPacketFragment.this.dismiss();
    }

    @OnClick(R.id.imageView28)
    public void fragmentDismiss(){
        this.dismiss();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null){
            mDataBean = (SendRedPacketResult.DataBean) bundle.getSerializable(SendRedPacketResult.DataBean.class.getName());
            mRedPacketStatus = bundle.getInt(REDPACKET_STATUS);
            redPacketSender.setText(getResources().getString(R.string.red_packet_sender, mDataBean.getName()));
            updateRedPacketStatus(mRedPacketStatus);
        }

    }

    private void updateRedPacketStatus(int redPacketStatus){
        switch (redPacketStatus){
            case RED_PACKET_AVAILABLE:
                redPacketRemark.setText(mDataBean.getRemark());
                openBtnView.setVisibility(View.VISIBLE);
                checkDetailBtn.setVisibility(View.GONE);
                break;
            case RED_PACKET_TIME_OUT:
                redPacketRemark.setText(getResources().getString(R.string.red_packet_time_out));
                openBtnView.setVisibility(View.GONE);
                checkDetailBtn.setVisibility(View.GONE);
                break;
            case RED_PACKET_RECEIVEDED_EMPTY:
                redPacketRemark.setText(getResources().getString(R.string.red_packet_empty));
                openBtnView.setVisibility(View.GONE);
                checkDetailBtn.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //设置背景透明
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onHttpRequestSucceed(ReceivedRedPacketResult result, OriginalCall call) {
        if (result.isSuccess()){
            //红包领取成功
            Intent intent = new Intent(getActivity(), RedPacketReceivedActivity.class);
            intent.putExtra(SendRedPacketResult.DataBean.class.getName(), mDataBean);
            startActivity(intent);
            RedPacketFragment.this.dismiss();
        }else {
            showToastView(getResources().getString(R.string.received_red_packet_error));
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }


}