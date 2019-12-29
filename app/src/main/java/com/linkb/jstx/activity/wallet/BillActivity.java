package com.linkb.jstx.activity.wallet;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.wallet.WithdrawBillAdapter;
import com.linkb.jstx.fragment.BillChangeDialogFragment;
import com.linkb.jstx.fragment.SexChangeDialogFragment;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.WithdrawBillResult;
import com.linkb.jstx.util.ConvertUtils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 账单
* */
public class BillActivity extends BaseActivity implements HttpRequestListener<WithdrawBillResult>, BillChangeDialogFragment.OnBottomDialogSelectListener {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    @BindView(R.id.bill_type_selected_btn)
    TextView billTypeTv;
    @BindView(R.id.empty_view)
    View emptyView;

    private WithdrawBillAdapter mAdapter;
    private List<WithdrawBillResult.DataBean> mList = new ArrayList<>();

    private static final int SELECT_TIME_REQUEST = 0x10;
    private String startTime, endTime;

    /** 账单类型: 0表示转账账单. 1表示红包账单
    * */
    private int billType = 0;

    private static final int COIN_EXCHAGE_BILL = 0;
    private static final int RED_PACKET_BILL = 1;

    private BillChangeDialogFragment mBillChangeDialogFragment;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        //默认查询最近一个月的账单
//        SimpleDateFormat formatter   =   new   SimpleDateFormat   ("yyyy-MM-dd");
//        long currentTime = System.currentTimeMillis();
//        long lastMonthTime = currentTime - 60 * 60* 24 *30;
//        Date curDate =  new Date(currentTime);
//        Date lastMonthDate = new Date(lastMonthTime);
//        endTime = formatter.format(curDate);
//        startTime = formatter.format(lastMonthDate);
//        Log.d("BillActivity", endTime);
//        Log.d("BillActivity", startTime);
//        queryBill(startTime, endTime);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        Date date = calendar.getTime();
        calendar.add(Calendar.MONTH, -1);
        Date lastMonthDate = calendar.getTime();
        startTime = format.format(lastMonthDate);
        endTime = format.format(date);
        queryBill(startTime, endTime);

        Log.d("BillActivity", endTime);
        Log.d("BillActivity", startTime);

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                queryBill(startTime, endTime);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mAdapter = new WithdrawBillAdapter(mList, this));
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_bill;
    }

    @OnClick(R.id.query_type_btn)
    public void queryTypeSelect(){
        Intent intent = new Intent(this, TimeSelectActivity.class);
        startActivityForResult(intent,SELECT_TIME_REQUEST );
    }

    @OnClick(R.id.back_btn)
    public void onBackBTn(){
        finish();
    }

    @OnClick(R.id.bill_type_selected_btn)
    public void changeBillType(){
        if (mBillChangeDialogFragment == null){
            mBillChangeDialogFragment = new BillChangeDialogFragment();
            mBillChangeDialogFragment.setListener(this);
        }
        mBillChangeDialogFragment.show(getSupportFragmentManager(), "BillChangeDialogFragment");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_TIME_REQUEST){
            startTime = data.getStringExtra("beginTime");
            endTime = data.getStringExtra("endTime");
            queryBill(startTime,endTime);
        }
    }

    private void queryBill(String startTime, String endTime){
        HttpServiceManager.getWithdrawBill(startTime, endTime, this);
    }

    @Override
    public void onHttpRequestSucceed(WithdrawBillResult result, OriginalCall call) {
        refreshLayout.finishRefresh();
        if (result.isSuccess()){
            mAdapter.replaceAll(result.getData());
            emptyView.setVisibility(result.getData().size() > 0 ? View.INVISIBLE: View.VISIBLE);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        refreshLayout.finishRefresh();
    }

    @Override
    public void selectFirstItem() {
        billType = COIN_EXCHAGE_BILL;
        billTypeTv.setText(getResources().getString(R.string.withdraw_coin_record));
        queryBill(startTime, endTime);
        mBillChangeDialogFragment.dismiss();
    }

    @Override
    public void selectSecondItem() {
        billType = RED_PACKET_BILL;
        billTypeTv.setText(getResources().getString(R.string.withdraw_coin_record));
        Intent intent = new Intent(BillActivity.this, RedPacketRecordActivity.class);
        startActivity(intent);
        mBillChangeDialogFragment.dismiss();
    }
}
