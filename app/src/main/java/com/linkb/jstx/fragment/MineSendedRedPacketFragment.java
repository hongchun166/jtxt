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
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.SendedRedPacketListResult;
import com.linkb.jstx.network.result.v2.RedpackgeListSndHistoryResult;
import com.linkb.jstx.util.ConvertUtils;
import com.linkb.jstx.util.FileURLBuilder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MineSendedRedPacketFragment extends LazyLoadFragment implements HttpRequestListener<RedpackgeListSndHistoryResult> {

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

    @BindView(R.id.textView104)
    TextView viewDataSelete;


    private List<RedpackgeListSndHistoryResult.DataBean.SendListBean> mList = new ArrayList<>();
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
        User user=Global.getCurrentUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mAdapter = new SendedRedPacketAdapter(mList, getContext()));
        HttpServiceManagerV2.redpackgeListSendHistroy(user.getAccount(),this);
        viewDataSelete.setVisibility(View.GONE);
    }

    @Override
    public void onHttpRequestSucceed(RedpackgeListSndHistoryResult result, OriginalCall call) {
        if (result.isSuccess()){
            RedpackgeListSndHistoryResult.DataBean dataBean=result.getData();
            List<RedpackgeListSndHistoryResult.DataBean.SendListBean> sendList=dataBean.getSendList();

            avatar.load(FileURLBuilder.getUserIconUrl(Global.getCurrentAccount()), R.mipmap.lianxiren, 999);

            sendedTotalMoneyTv.setText(ConvertUtils.doubleToString(dataBean.getSumMoney()));
            if (sendList.size() > 0) {
                sendedMoneyCurrencyMarkTv.setText(sendList.get(0).getCurrencyName());
            }else {
                sendedMoneyCurrencyMarkTv.setText("KKC");
            }
            if (getActivity() != null) sendedTotalRedCountTv.setText(getResources().getString(R.string.send_red_packer_count, sendList.size()));


//            sendedTotalMoneyTv.setText(ConvertUtils.doubleToString(result.getData().getSendRedTotal()));
//            if (result.getData().getRedList().size() > 0) sendedMoneyCurrencyMarkTv.setText(result.getData().getRedList().get(0).getCurrencyName());
//            if (getActivity() != null) sendedTotalRedCountTv.setText(getResources().getString(R.string.send_red_packer_count, result.getData().getRedList().size()));

            mList.clear();
            mList.addAll(sendList);
            mAdapter.notifyDataSetChanged();
        }else {
            showToastView(result.message);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }
}
