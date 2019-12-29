package com.linkb.jstx.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.adapter.wallet.ReceivedRedPacketAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.ReceivedRedPacketListResult;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineReceivedRedPacketFragment extends LazyLoadFragment implements HttpRequestListener<ReceivedRedPacketListResult> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.textView95)
    TextView receivedTotalMoneyTv;
    @BindView(R.id.textView96)
    TextView receivedTotalMoneyCurrencyTv;
    @BindView(R.id.textView97)
    TextView receivedTotalMoneyCountTv;
    @BindView(R.id.imageView5)
    WebImageView avatar;

    private ReceivedRedPacketAdapter mAdapter;

    private List<ReceivedRedPacketListResult.DataBean.RedListBean> mList = new ArrayList<>();

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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new ReceivedRedPacketAdapter(mList));
        HttpServiceManager.queryRedPacketReceivedList(this);
    }

    @Override
    public void onHttpRequestSucceed(ReceivedRedPacketListResult result, OriginalCall call) {
        if (result.isSuccess()){
            avatar.load(FileURLBuilder.getUserIconUrl(Global.getCurrentAccount()), R.mipmap.lianxiren, 999);
            receivedTotalMoneyTv.setText(ConvertUtils.doubleToString(result.getData().getSendReceiveTotal()));
            if (result.getData().getRedList().size() > 0) receivedTotalMoneyCurrencyTv.setText(result.getData().getRedList().get(0).getCurrencyName());
            receivedTotalMoneyCountTv.setText(String.valueOf(result.getData().getRedList().size()));

            mList.clear();
            mList.addAll(result.getData().getRedList());
            mAdapter.notifyDataSetChanged();
        }else {
            showToastView(result.message);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }

}
