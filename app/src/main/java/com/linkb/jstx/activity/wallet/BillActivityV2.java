package com.linkb.jstx.activity.wallet;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.wallet.BillListAdapterV2;
import com.linkb.jstx.adapter.wallet.WithdrawBillAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.dialog.BillChangeDialogV2;
import com.linkb.jstx.fragment.BillChangeDialogFragment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.WithdrawBillResult;
import com.linkb.jstx.network.result.v2.ListMyBalanceFlowResult;
import com.linkb.jstx.util.TimeUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BillActivityV2 extends BaseActivity implements HttpRequestListener<ListMyBalanceFlowResult>, BillChangeDialogV2.OnBillChangeListener {

    @BindView(R.id.recyclerView)
    ListView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.bill_type_selected_btn)
    TextView billTypeTv;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.query_type_btn)
    TextView query_type_btn;

    private BillListAdapterV2 mAdapter;
    private List<ListMyBalanceFlowResult.DataBean> mList = new ArrayList<>();

    private static final int SELECT_TIME_REQUEST = 0x10;

    /**
     * 账单类型: 0表示全部. 1表示红包账单 2提现  3充值
     */
    private int billType = 0;

    private static final int ALL_BILL = 0;
    private static final int RED_PACKET_BILL = 1;
    private static final int WITHDRAWAL_BILL = 2;
    private static final int TOP_UP_BILL = 3;
    private BillChangeDialogV2 dialogV2;


    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        Date date = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        Date lastMonthDate = calendar.getTime();
        queryBill();
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                queryBill();
            }
        });

        mAdapter = new BillListAdapterV2(mList, this);
        recyclerView.setAdapter(mAdapter);
        query_type_btn.setVisibility(View.GONE);
        query_type_btn.setEnabled(false);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill;
    }

    @OnClick(R.id.query_type_btn)
    public void queryTypeSelect() {
        Intent intent = new Intent(this, TimeSelectActivity.class);
        startActivityForResult(intent, SELECT_TIME_REQUEST);
    }

    @OnClick(R.id.back_btn)
    public void onBackBTn() {
        finish();
    }

    @OnClick(R.id.bill_type_selected_btn)
    public void changeBillType() {
        if (dialogV2 == null) {
            dialogV2 = new BillChangeDialogV2(this);
        }
        dialogV2.setType(billType);
        dialogV2.setOnBillChangeListener(this);
        dialogV2.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_TIME_REQUEST) {
            String startTime = data.getStringExtra("beginTime");
            String  endTime = data.getStringExtra("endTime");
            queryBill();
        }
    }

    private void queryBill() {
        User user=Global.getCurrentUser();
        String cid="";
        if(billType!=0){
            cid=String.valueOf(billType);
        }
        HttpServiceManagerV2.listMyBalanceFlow(user.account ,"",cid,this);
    }

    @Override
    public void onHttpRequestSucceed(ListMyBalanceFlowResult result, OriginalCall call) {
        refreshLayout.finishRefresh();
        if (result.isSuccess()) {
//            mList = result.getData();
            mList.clear();
            mList.addAll(result.getData());

            Collections.sort(mList, new Comparator<ListMyBalanceFlowResult.DataBean>() {
                @Override
                public int compare(ListMyBalanceFlowResult.DataBean o1, ListMyBalanceFlowResult.DataBean o2) {
                    long o1Time=TimeUtils.string2Millis(o1.getAddTimeFinal());
                    long o2Time=TimeUtils.string2Millis(o2.getAddTimeFinal());
                    return o1Time>o2Time?-1:0;
                }
            });
            mAdapter.setData(mList);
            emptyView.setVisibility(result.getData().size() > 0 ? View.INVISIBLE : View.VISIBLE);
            temporary();
        }
    }

    private void temporary() {
//        mList.clear();
//        for (int i = 0; i < 100; i++) {
//            WithdrawBillResult.DataBean dataBean = new WithdrawBillResult.DataBean();
//            if (i % 2 == 0) {
//                dataBean.setRed_type(0);
//            } else {
//                dataBean.setRed_type(1);
//            }
//            switch (i % 4) {
//                case 0:
//                    dataBean.setBillType(0);
//                    break;
//                case 1:
//                    dataBean.setBillType(1);
//                    break;
//                case 2:
//                    dataBean.setBillType(2);
//                    break;
//                case 3:
//                    dataBean.setBillType(3);
//                    break;
//            }
//            dataBean.setEvent("提币支出—钱包提现");
//            dataBean.setAdd_date(1032256335);
//            dataBean.setMoney(i * 1.6);
//            mList.add(dataBean);
//        }
//        mAdapter.setData(mList);
//        emptyView.setVisibility(View.GONE);
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        refreshLayout.finishRefresh();
    }


    @Override
    public void onAll() {
        billType = ALL_BILL;
        billTypeTv.setText(getResources().getString(R.string.all_coin_record));
        queryBill();
    }

    @Override
    public void onRed() {
        billType = RED_PACKET_BILL;
        billTypeTv.setText(getResources().getString(R.string.red_coin_record));
        Intent intent = new Intent(BillActivityV2.this, RedPacketRecordActivity.class);
        startActivity(intent);
//        billTypeTv.setText(getResources().getString(R.string.withdraw_coin_record));
//        queryBill(startTime, endTime);

    }

    @Override
    public void onWithdrawal() {
        billType = WITHDRAWAL_BILL;
        billTypeTv.setText(getResources().getString(R.string.withdraw_coin_record));
        queryBill();
    }

    @Override
    public void onTopUp() {
        billType = TOP_UP_BILL;
        billTypeTv.setText(getResources().getString(R.string.top_up_coin_record));
        queryBill();
    }
}
