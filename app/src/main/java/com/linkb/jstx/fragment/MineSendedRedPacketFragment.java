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
import com.linkb.jstx.adapter.wallet.SendedRedPacketAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.SendedRedPacketListResult;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.FileURLBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineSendedRedPacketFragment extends LazyLoadFragment implements HttpRequestListener<SendedRedPacketListResult> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.textView95)
    TextView sendedTotalMoneyTv;
    @BindView(R.id.textView96)
    TextView sendedMoneyCurrencyMarkTv;
    @BindView(R.id.textView103)
    TextView sendedTotalRedCountTv;
    @BindView(R.id.imageView5)
    WebImageView avatar;

    private List<SendedRedPacketListResult.DataBean.RedListBean > mList = new ArrayList<>();
    private SendedRedPacketAdapter mAdapter;

    public static MineSendedRedPacketFragment getInstance(){
        return new MineSendedRedPacketFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine_sended_red_packet, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void requestData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new SendedRedPacketAdapter(mList, getContext()));
        HttpServiceManager.querySendedPacketList(this);
    }

    @Override
    public void onHttpRequestSucceed(SendedRedPacketListResult result, OriginalCall call) {
        if (result.isSuccess()){
            avatar.load(FileURLBuilder.getUserIconUrl(Global.getCurrentAccount()), R.mipmap.lianxiren, 999);
            sendedTotalMoneyTv.setText(ConvertUtils.doubleToString(result.getData().getSendRedTotal()));
            if (result.getData().getRedList().size() > 0) sendedMoneyCurrencyMarkTv.setText(result.getData().getRedList().get(0).getCurrencyName());
            if (getActivity() != null) sendedTotalRedCountTv.setText(getResources().getString(R.string.send_red_packer_count, result.getData().getRedList().size()));

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
