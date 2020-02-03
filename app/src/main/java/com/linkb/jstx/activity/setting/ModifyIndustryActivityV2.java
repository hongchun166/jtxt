package com.linkb.jstx.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;
import com.linkb.jstx.network.result.v2.ListIndustryResult;
import com.linkb.jstx.network.result.v2.ListTagsResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyIndustryActivityV2 extends BaseActivity {


    @BindView(R.id.tv_title)
    TextView titleTv;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;


    private User user;

    IndustryAdapt mAdapter;



    public static void nacToAct(Context context,int requestCode){
        Intent intent=new Intent(context,ModifyIndustryActivityV2.class);
        if(context instanceof Activity){
            ((Activity)context).startActivityForResult(intent,requestCode);
        }else {
            context.startActivity(intent);
        }
    }

    @Override
    public void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        user = Global.getCurrentUser();

        String curIndustryStr=user.industry;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new IndustryAdapt(this);
        recyclerView.setAdapter(mAdapter);
        httpLoadIndustryList();
    }

    @OnClick(R.id.back_btn)
    public void onBack() {
        finish();
    }
    @Override
    public int getContentLayout() {
        return R.layout.activity_update_industry_v2;
    }

    private void resultData(List<ListIndustryResult.DataBean> industryBeanList){
        String curIndustryStr=user.industry;
        ListIndustryResult.DataBean curIndus=null;
        if(!TextUtils.isEmpty(curIndustryStr)){
            for (ListIndustryResult.DataBean industryBean : industryBeanList) {
                if(industryBean.getIndustryName().equals(curIndustryStr)){
                    curIndus=industryBean;
                    break;
                }
            }
            if(curIndus==null){
                curIndus=industryBeanList.get(industryBeanList.size()-1);
//                curIndus=new IndustryBean(curIndustryStr,String.valueOf(Integer.parseInt(industryBeanList.get(0).code)-1));
//                industryBeanList.add(curIndus);
            }
        }else {
            curIndus=industryBeanList.get(0);
        }
        selectMap.put(curIndus.getIndustryCode(),curIndus);
        mAdapter.addDataAll(industryBeanList);
    }

    private List<ListIndustryResult.DataBean > loadNativeIndustryList(){
     List<ListIndustryResult.DataBean> list=new ArrayList<>();

//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_buxiang),"10"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_jishuaji),"20"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_shengc),"30"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_yiliao),"40"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_jingrong),"50"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_shangye),"60"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_wenhua),"70"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_yule),"80"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_lvsi),"90"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_jiaoyu),"100"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_gongwuyuan),"110"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_xuesheng),"120"));
//        list.add(new ListIndustryResult.DataBean(getString(R.string.item_industry_qita),"130"));
        return list;
    }

    private void httpLoadIndustryList(){
        String account=user.account;
        HttpServiceManagerV2.getListIndustry(account,new HttpRequestListener<ListIndustryResult>() {
            @Override
            public void onHttpRequestSucceed(ListIndustryResult result, OriginalCall call) {
                if(result.isSuccess() && result.getData()!=null){
                    resultData(result.getData());
                }else {
                    resultData(loadNativeIndustryList());
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }

    private void httpUpdateIndustry(ListIndustryResult.DataBean industryBean){
        final String curIndustrySt=industryBean.getIndustryName();
        User user=new User();
        user.industry=curIndustrySt;
        HttpServiceManagerV2.updateUserInfo(user, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
                if(result.isSuccess()){
                    Intent intent=new Intent();
                    intent.putExtra("curIndustrySt",curIndustrySt);
                    setResult(RESULT_OK,intent);
                    finish();
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }

    HashMap<String,ListIndustryResult.DataBean> selectMap=new HashMap();
    class IndustryAdapt extends RecyclerView.Adapter<IndustryHodler>  {
        Context context;
        List<ListIndustryResult.DataBean> beanList;

        public IndustryAdapt(Context context) {
            this.context = context;
            beanList = new ArrayList<>();
        }

        public void addDataAll(List<ListIndustryResult.DataBean> data) {
            beanList.clear();
            beanList.addAll(data);
            super.notifyDataSetChanged();
        }

        private Context getFmContent() {
            return context;
        }
        @NonNull
        @Override
        public IndustryHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            View view = LayoutInflater.from(getFmContent()).inflate(R.layout.item_industry_v2, viewGroup, false);
            IndustryHodler hodler = new IndustryHodler(view);
            return hodler;
        }
        @Override
        public void onBindViewHolder(IndustryHodler hodler, int position) {
            ListIndustryResult.DataBean bean=  beanList.get(position);
            hodler.curBean=bean;

            hodler.viewTVName.setText(bean.getIndustryName());
            if(selectMap.containsKey(bean.getIndustryCode())){
                hodler.viewIVSelectState.setBackgroundResource(R.mipmap.ic_industry_blue_hook);
            }else {
                hodler.viewIVSelectState.setBackgroundResource(android.R.color.transparent);
            }
        }

        @Override
        public int getItemCount() {
            return beanList.size();
        }
    }

    class IndustryHodler extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView viewTVName;
        public ImageView viewIVSelectState;
        ListIndustryResult.DataBean curBean;
        public IndustryHodler(View itemView) {
            super(itemView);
            viewTVName = itemView.findViewById(R.id.viewTVName);
            viewIVSelectState = itemView.findViewById(R.id.viewIVSelectState);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            httpUpdateIndustry(curBean);
        }
    }
}