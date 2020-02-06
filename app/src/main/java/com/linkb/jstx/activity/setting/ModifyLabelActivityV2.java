package com.linkb.jstx.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.ModifyLabelAdapterV2;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.v2.ListIndustryResult;
import com.linkb.jstx.network.result.v2.ListTagsResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyLabelActivityV2 extends BaseActivity {
    static final int ACT_TYPE_Select=1;
    static final int ACT_TYPE_Update=2;
    @BindView(R.id.lv_labels)
    ListView lvLabels;

    private ModifyLabelAdapterV2 adapterV2;


    private String labelString;
    private int labelItem;
    int actType;

    public static void navToActBySelectTag(Context context,int requestCode,String label){
        Intent intent = new Intent(context, ModifyLabelActivityV2.class);
        intent.putExtra("labelItem", label);
        intent.putExtra("actType", ACT_TYPE_Select);
        ((Activity)context).startActivityForResult(intent, requestCode);
    }
    public static void navToActByUpdateTag(Context context,int requestCode,String label){
        Intent intent = new Intent(context, ModifyLabelActivityV2.class);
        intent.putExtra("labelItem", label);
        intent.putExtra("actType", ACT_TYPE_Update);
        ((Activity)context).startActivityForResult(intent, requestCode);
    }
    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        labelString = getIntent().hasExtra("labelItem") ? getIntent().getStringExtra("labelItem") : "";
        actType= getIntent().hasExtra("actType") ? getIntent().getIntExtra("actType",ACT_TYPE_Update) : ACT_TYPE_Update;
        httpLoadTagsList();
    }



    @Override
    protected int getContentLayout() {
        return R.layout.activity_modify_label_v2;
    }

    @OnClick(R.id.back_btn)
    public void onBack() {
        finish();
    }

    private void refreshData(final List<ListTagsResult.DataBean> datas) {
        labelItem = TextUtils.isEmpty(labelString) ? 0 : datas.indexOf(labelString);
        adapterV2 = new ModifyLabelAdapterV2(this, datas, labelItem);
        lvLabels.setAdapter(adapterV2);
        lvLabels.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                adapterV2.setSelectedItem(i);
                adapterV2.notifyDataSetChanged();
                ListTagsResult.DataBean dataBean=datas.get(i);
                if(actType==ACT_TYPE_Select){
                    finshActByIntent(dataBean.getName());
                }else {
                    httpUpdateLabel(dataBean);
                }
            }
        });
    }
    public void finshActByIntent(String labelName){
        Intent intent = new Intent();
        intent.putExtra("labelItem", labelName);
        setResult(RESULT_OK, intent);
        finish();
    }
    private void httpLoadTagsList(){
        String account= Global.getCurrentUser().account;
        HttpServiceManagerV2.getLisTags(account,new HttpRequestListener<ListTagsResult>() {
            @Override
            public void onHttpRequestSucceed(ListTagsResult result, OriginalCall call) {
                if(result.isSuccess()){
                    refreshData(result.getData());
                }else {
                    refreshData(loadNativeList());
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
            }
        });
    }
    private void httpUpdateLabel(ListTagsResult.DataBean industryBean){
        final String labelName=industryBean.getName();
        User userTemp=new User();
        userTemp.tag=labelName;
        HttpServiceManagerV2.updateUserInfo(userTemp, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
                if(result.isSuccess()){
                    finshActByIntent(labelName);
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }

    private List<ListTagsResult.DataBean> loadNativeList() {
        List<ListTagsResult.DataBean> datas=new ArrayList<>();
//        datas.add(new ListTagsResult.DataBean("不限"));
//        datas.add(new ListTagsResult.DataBean("金融"));
//        datas.add(new ListTagsResult.DataBean("互联网"));
//        datas.add(new ListTagsResult.DataBean("VC"));
//        datas.add(new ListTagsResult.DataBean("PE"));
//        datas.add(new ListTagsResult.DataBean("直销"));
//        datas.add(new ListTagsResult.DataBean("保险"));
//        datas.add(new ListTagsResult.DataBean("区块链"));
//        datas.add(new ListTagsResult.DataBean("比特币"));
//        datas.add(new ListTagsResult.DataBean("投资方"));
//        datas.add(new ListTagsResult.DataBean("项目方"));
//        datas.add(new ListTagsResult.DataBean("创业者"));
        return datas;
    }
}
