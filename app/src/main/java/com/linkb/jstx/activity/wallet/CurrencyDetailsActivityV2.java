package com.linkb.jstx.activity.wallet;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.wallet.BillListAdapterV2;
import com.linkb.jstx.adapter.wallet.CurrencyDetailsAdapterV2;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.v2.CurrencyInfoBean;
import com.linkb.jstx.network.result.v2.CurrencyInfoResult;
import com.linkb.jstx.network.result.v2.ListMyBalanceFlowResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CurrencyDetailsActivityV2 extends BaseActivity {
    @BindView(R.id.ll_record)
    LinearLayout llRecord;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.image_up)
    ImageView imageUp;
    @BindView(R.id.tv_title)
    TextView tvTile;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_transfer)
    TextView tvTransfer;
    @BindView(R.id.tv_collection)
    TextView tvCollection;
    @BindView(R.id.lv_records)
    ListView listView;
    @BindView(R.id.tv_code)
    TextView tvCode;

    private String currencyName;
    private  int currencyId;
    private int maxMargin = 1;
    private boolean isAnimation = true;
    private List<ListMyBalanceFlowResult.DataBean> mData = new ArrayList<>();
//    private CurrencyDetailsAdapterV2 adapterV2;
    BillListAdapterV2 mAdapter;

    public static void navToAct(Context context,String currencyId,String currencyName){
        Intent intent = new Intent(context, CurrencyDetailsActivityV2.class);
        intent.putExtra("currencyId", currencyId);
        intent.putExtra("currencyName", currencyName);
        context.startActivity(intent);
    }

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        ViewTreeObserver vto = llTop.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                maxMargin = llTop.getHeight();
            }
        });
        initView();
        initData();
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_currency_details_v2;
    }

    /**
     * 初始化界面
     */
    private void initView() {
        updateType(0);
        tvCode.setText("dhj1df234dfg5567ddf");
        mAdapter = new BillListAdapterV2(mData, this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListMyBalanceFlowResult.DataBean bean = mData.get(i);
//                showToastView(bean.getRemark() + "被点击");
            }
        });
    }


    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
         currencyId = Integer.valueOf(intent.getStringExtra("currencyId"));
        currencyName = intent.getStringExtra("currencyName");
        tvTile.setText(currencyName);
        httpGetMyCurrencyById();
        httpListMyBalanceFlow();
    }


    /**
     * 网络Ok修改数据
     *
     * @param result 币种详情请求结果
     */
    private void updateData(CurrencyInfoResult result) {
        if (result == null) return;
        if (!result.isSuccess()) return;
        tvAmount.setText(String.valueOf(result.data.lockBalance));
//        records.clear();
//        //无数据暂用假数据
//        for (int i = 0; i < 100; i++) {
//            CurrencyInfoBean rec = new CurrencyInfoBean();
//            rec.amount = String.valueOf(i * 100.3 + i);
//            rec.icon = "";
//            rec.name = "测试佚名" + i;
//            rec.status = i % 2;
//            rec.time = "2020-01-22 12:25";
//            records.add(rec);
//        }
//        mAdapter.setData(records);
    }

    @OnClick(R.id.back_btn)
    public void back() {
        finish();
    }

    @OnClick(R.id.tv_all)
    public void allData() {
        updateType(0);
    }

    @OnClick(R.id.tv_transfer)
    public void transferData() {
        updateType(1);
    }

    @OnClick(R.id.tv_collection)
    public void collectionData() {
        updateType(2);
    }

    @OnClick(R.id.ll_transfer)
    public void transfer() {
        //需求不明后续添加
        showToastView("转账");

    }

    @OnClick(R.id.ll_collection)
    public void collection() {
        //需求不明后续添加
        showToastView("收款");
    }

    @OnClick(R.id.image_copy)
    public void copy() {
        String code = tvCode.getText().toString();
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("Label", code);
        cm.setPrimaryClip(mClipData);
        showToastView("复制成功");
    }

    /**
     * 改变数据类型
     *
     * @param type 0为全部,1为转账,2为收款
     */
    private void updateType(int type) {
        tvAll.setSelected(type == 0);
        tvTransfer.setSelected(type == 1);
        tvCollection.setSelected(type == 2);
    }

    /**
     * 点击初始化动画进行展开跟隐藏
     */
    @OnClick(R.id.image_up)
    public void initAnimation() {
        if (!isAnimation) return;
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) llRecord.getLayoutParams();
        final int startMargin = params.topMargin;
        int endMargin = startMargin == 0 ? -maxMargin : 0;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(startMargin, endMargin);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int margin = (int) valueAnimator.getAnimatedValue();
                params.topMargin = (int) valueAnimator.getAnimatedValue();
                llRecord.setLayoutParams(params);
                float rot = (margin != 0 && margin != maxMargin) ? 180 - 180 * margin / maxMargin : startMargin == 0 ? 180 : startMargin != maxMargin ? 180 - 180 * margin / maxMargin : 0;
                imageUp.setRotation(rot);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimation = true;
            }
        });
        valueAnimator.start();
        isAnimation = false;
    }

    private void httpGetMyCurrencyById(){
        User user = Global.getCurrentUser();
        HttpServiceManagerV2.getMyCurrencyById(user == null ? "" : user.account, String.valueOf(currencyId),
                new HttpRequestListener<CurrencyInfoResult>() {
                    @Override
                    public void onHttpRequestSucceed(CurrencyInfoResult result, OriginalCall call) {
                        updateData(result);
                    }
                    @Override
                    public void onHttpRequestFailure(Exception e, OriginalCall call) {

                    }
                });
    }
    private void httpListMyBalanceFlow(){
        User user = Global.getCurrentUser();
        HttpServiceManagerV2.listMyBalanceFlow(user == null ? "" : user.account, "", String.valueOf(currencyId), new HttpRequestListener<ListMyBalanceFlowResult>() {
            @Override
            public void onHttpRequestSucceed(ListMyBalanceFlowResult result, OriginalCall call) {
                if (result.isSuccess()) {
                    mData = result.getData();
                    mAdapter.setData(mData);
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }
}
