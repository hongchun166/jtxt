package com.linkb.jstx.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.adapter.wallet.ReceivedRedPacketAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.ReceivedRedPacketListResult;
import com.linkb.jstx.network.result.v2.RedpackgeListRcvHistroyResult;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineReceivedRedPacketFragment extends LazyLoadFragment implements HttpRequestListener<RedpackgeListRcvHistroyResult> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.textView95)
    TextView receivedTotalMoneyTv;
    @BindView(R.id.textView96)
    TextView receivedTotalMoneyCurrencyTv;
    @BindView(R.id.textView97)
    TextView receivedTotalMoneyCountTv;
    @BindView(R.id.textView98)
    TextView receivedTotalMoneyCountTvHint;

    @BindView(R.id.imageView5)
    WebImageView avatar;

    @BindView(R.id.textView104)
    TextView viewDataSelete;
    @BindView(R.id.textView99)
    TextView viewShouQiZuiJiaCount;
    @BindView(R.id.textView100)
    TextView viewShouQiZuiJiaHint;
    @BindView(R.id.viewReceiveRedCount)
    TextView viewReceiveRedCount;

    private ReceivedRedPacketAdapter mAdapter;

    private List<RedpackgeListRcvHistroyResult.DataBean.ReceivListBean> mList = new ArrayList<>();

    public static MineReceivedRedPacketFragment getInstance(){
        return new MineReceivedRedPacketFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_received_red_packet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void requestData() {
        User user=Global.getCurrentUser();

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new ReceivedRedPacketAdapter(mList));
        HttpServiceManagerV2.redpackgeListReceivHistroy(user.getAccount(),this);

        viewShouQiZuiJiaCount.setVisibility(View.GONE);
        viewShouQiZuiJiaHint.setVisibility(View.GONE);
        viewDataSelete.setVisibility(View.GONE);
        receivedTotalMoneyCountTv.setVisibility(View.GONE);
        receivedTotalMoneyCountTvHint.setVisibility(View.GONE);

    }

    @Override
    public void onHttpRequestSucceed(RedpackgeListRcvHistroyResult result, OriginalCall call) {
        if (result.isSuccess()){
            RedpackgeListRcvHistroyResult.DataBean dataBean=result.getData();
            List<RedpackgeListRcvHistroyResult.DataBean.ReceivListBean> receivList=dataBean.getReceivList();

            avatar.load(FileURLBuilder.getUserIconUrl(Global.getCurrentAccount()), R.mipmap.lianxiren, 999);
            receivedTotalMoneyTv.setText(ConvertUtils.doubleToString(dataBean.getSumMoney()));
            if (receivList.size() > 0){
                receivedTotalMoneyCurrencyTv.setText(receivList.get(0).getCurrencyName());
            }else {
                receivedTotalMoneyCurrencyTv.setText("KKC");
            }
            viewReceiveRedCount.setText(getString(R.string.receive_red_packer_count,(dataBean.getSumNumber()==null?0:dataBean.getSumNumber().intValue())));

//            receivedTotalMoneyTv.setText(ConvertUtils.doubleToString(result.getData().getSendReceiveTotal()));
//            if (dataBeanList.size() > 0) receivedTotalMoneyCurrencyTv.setText(dataBean.get(0).getCurrencyName());
//            receivedTotalMoneyCountTv.setText(String.valueOf(result.getData().getRedList().size()));

            mList.clear();
            mList.addAll(receivList);
            mAdapter.notifyDataSetChanged();
        }else {
            showToastView(result.message);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }

}
