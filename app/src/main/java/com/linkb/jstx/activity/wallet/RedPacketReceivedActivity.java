package com.linkb.jstx.activity.wallet;

import android.content.Intent;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.wallet.RedPacketReceiveDetailAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.QueryRedPacketStatusResult;
import com.linkb.jstx.network.result.v2.GetReceiverDetailResultV2;
import com.linkb.jstx.network.result.v2.RedpackgeGetInfoResult;
import com.linkb.jstx.util.FileURLBuilder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**  红包领取详情页面
* */
public class RedPacketReceivedActivity extends BaseActivity implements HttpRequestListener<GetReceiverDetailResultV2> {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.title_tv)
    TextView title;
    @BindView(R.id.textView67)
    TextView remarkTv;
    @BindView(R.id.textView68)
    TextView totalRedPacketTv;
    @BindView(R.id.textView70)
    TextView redPacketCurrencyTv;
    @BindView(R.id.red_packet_sender_avatar)
    WebImageView avatar;
    @BindView(R.id.check_balance_lly)
    View checkBalanceView;
    @BindView(R.id.red_packet_tips_lly)
    View redPacketsTipsLly;

    @BindView(R.id.red_packet_received_tips)
    TextView red_packet_received_tips;


    private RedPacketReceiveDetailAdapter mAdapter;
    private List<GetReceiverDetailResultV2.DataBean.RedpackgeReceiversBean> mList = new ArrayList<>();


    /**    收到的红包实体
    * */
    private RedpackgeGetInfoResult.DataBean mDataBean;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);


        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefresh();
                    }
                }, 1000);
//                initData(false);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        mDataBean = (RedpackgeGetInfoResult.DataBean) getIntent().getSerializableExtra(RedpackgeGetInfoResult.DataBean.class.getName());
//      int  mRedPacketStatus = getIntent().getIntExtra(QueryRedPacketStatusResult.REDPACKET_STATUS, 0);
        recyclerView.setAdapter(mAdapter = new RedPacketReceiveDetailAdapter(this, mList, getRedPackgeBeanInfo().getSendNumberNum()));

        initDate();
    }
    private RedpackgeGetInfoResult.DataBean getRedPackgeBeanInfo(){
        return mDataBean;
    }
    private void initDate(){
        avatar.load(FileURLBuilder.getUserIconUrl(getRedPackgeBeanInfo().getSendAccount()), R.mipmap.lianxiren, 999);
        title.setText(getResources().getString(R.string.red_packet_sender, getRedPackgeBeanInfo().getUserName()));
        remarkTv.setText(getRedPackgeBeanInfo().getRemark());
        totalRedPacketTv.setText(String.valueOf(getRedPackgeBeanInfo().getMoney()));
        String cName= TextUtils.isEmpty(getRedPackgeBeanInfo().getCurrencyName())?"KKC":getRedPackgeBeanInfo().getCurrencyName();
        redPacketCurrencyTv.setText(cName);
        mAdapter.setDangWei(cName);

        int mRedPacketStatus = TextUtils.isEmpty(getRedPackgeBeanInfo().getState())?0:Integer.valueOf(getRedPackgeBeanInfo().getState());

        updateViewByState(getRedPackgeBeanInfo().getType(),getRedPackgeBeanInfo().getGetState(),mRedPacketStatus);

        HttpServiceManagerV2.redpackgeGetReceiverDetail(String.valueOf(getRedPackgeBeanInfo().getId()),  this);
    }
    public void updateRedPackagerByRedpackgeBean(GetReceiverDetailResultV2.DataBean.RedpackgeBean bean){
        if(bean==null){
            return;
        }
        title.setText(getResources().getString(R.string.red_packet_sender, bean.getUserName()));
        remarkTv.setText(bean.getRemark());
        totalRedPacketTv.setText(String.valueOf(bean.getMoney()));
        String cName= TextUtils.isEmpty(bean.getCurrencyName())?"KKC":bean.getCurrencyName();
        redPacketCurrencyTv.setText(cName);
        mAdapter.setDangWei(cName);
    }

    private void updateViewByState(String type,int getState,int mRedPacketStatus ){
        System.out.println("==updateViewByState=="+type+","+getState+","+mRedPacketStatus);
        if(getState==1){//自己已经领取
            refreshLayout.setVisibility(View.VISIBLE);
            checkBalanceView.setVisibility(View.GONE);
            redPacketsTipsLly.setVisibility(View.GONE);
        }else if(mRedPacketStatus==QueryRedPacketStatusResult.RED_PACKET_Receiveed){//红包已领完
            refreshLayout.setVisibility(View.VISIBLE);
            checkBalanceView.setVisibility(View.GONE);
            redPacketsTipsLly.setVisibility(View.VISIBLE);
            red_packet_received_tips.setText(R.string.red_packet_empty);
        }else if(mRedPacketStatus==QueryRedPacketStatusResult.RED_PACKET_TimeOut){//红包已经过期
            refreshLayout.setVisibility(View.GONE);
            checkBalanceView.setVisibility(View.GONE);
            redPacketsTipsLly.setVisibility(View.VISIBLE);
            red_packet_received_tips.setText(R.string.red_packet_timeOut);//红包已过期
        }else if(type.equals(String.valueOf(Constant.RedPacketType.COMMON_RED_PACKET))){
            refreshLayout.setVisibility(View.GONE);
            checkBalanceView.setVisibility(View.GONE);
            redPacketsTipsLly.setVisibility(View.VISIBLE);
        }else {
            refreshLayout.setVisibility(View.VISIBLE);
            checkBalanceView.setVisibility(View.GONE);
            redPacketsTipsLly.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_red_packet_open_detail;
    }

    @OnClick(R.id.received_red_packet_fly)
    public void onReceivedRedPacket(){
        Intent intent = new Intent(this, RedPacketRecordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.back_btn)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.click_check_balance_tv)
    public void onCheckBalance(){
        startActivity(new Intent(this, WalletActivity.class));
    }

    @Override
    public void onHttpRequestSucceed(GetReceiverDetailResultV2 result, OriginalCall call) {
        if (result.isSuccess()){
            if(result.getData()!=null){
                List<GetReceiverDetailResultV2.DataBean.RedpackgeReceiversBean> list= result.getData().getRedpackgeReceivers();
                mAdapter.replaceAll(list);
                if(list.size()>0){
                    redPacketsTipsLly.setVisibility(View.GONE);
                }
                if(result.getData()!=null){
                    GetReceiverDetailResultV2.DataBean.RedpackgeBean redpackgeBean=result.getData().getRedpackge();
                    updateRedPackagerByRedpackgeBean(redpackgeBean);
                }
            }

        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }

}
