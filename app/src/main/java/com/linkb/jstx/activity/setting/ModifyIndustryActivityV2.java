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
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.ModifyPersonInfoResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyIndustryActivityV2 extends BaseActivity implements HttpRequestListener<ModifyPersonInfoResult> {
    static class IndustryBean{
        public String name;
        public String code;
        public IndustryBean(String name,String code) {
            this.name = name;
            this.code=code;
        }
    }

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

        List<IndustryBean> industryBeanList=loadIndustryList();
        IndustryBean curIndus=null;
        if(!TextUtils.isEmpty(curIndustryStr)){
            for (IndustryBean industryBean : industryBeanList) {
                if(industryBean.name.equals(curIndustryStr)){
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
        selectMap.put(curIndus.code,curIndus);
        mAdapter.addDataAll(industryBeanList);
    }

    @OnClick(R.id.back_btn)
    public void onBack() {
        finish();
    }
    @Override
    public int getContentLayout() {
        return R.layout.activity_update_industry_v2;
    }

    @Override
    public void onHttpRequestSucceed(ModifyPersonInfoResult result, OriginalCall call) {
        if (result.isSuccess()) {

            showToastView(R.string.tip_save_complete);
            setResult(RESULT_OK, getIntent());
            finish();
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }

    private List<IndustryBean> loadIndustryList(){
     List<IndustryBean> list=new ArrayList<>();

        list.add(new IndustryBean(getString(R.string.item_industry_buxiang),"10"));
        list.add(new IndustryBean(getString(R.string.item_industry_jishuaji),"20"));
        list.add(new IndustryBean(getString(R.string.item_industry_shengc),"30"));
        list.add(new IndustryBean(getString(R.string.item_industry_yiliao),"40"));
        list.add(new IndustryBean(getString(R.string.item_industry_jingrong),"50"));
        list.add(new IndustryBean(getString(R.string.item_industry_shangye),"60"));
        list.add(new IndustryBean(getString(R.string.item_industry_wenhua),"70"));
        list.add(new IndustryBean(getString(R.string.item_industry_yule),"80"));
        list.add(new IndustryBean(getString(R.string.item_industry_lvsi),"90"));
        list.add(new IndustryBean(getString(R.string.item_industry_jiaoyu),"100"));
        list.add(new IndustryBean(getString(R.string.item_industry_gongwuyuan),"110"));
        list.add(new IndustryBean(getString(R.string.item_industry_xuesheng),"120"));
        list.add(new IndustryBean(getString(R.string.item_industry_qita),"130"));
        return list;
    }

    private void updateIndustryToServer(IndustryBean industryBean){
//        ArrayList<IndustryBean> sele=new ArrayList<>();
//        sele.addAll(selectMap.values());
        String curIndustrySt=industryBean.name;
//        if(sele.size()>0){
//            curIndustrySt=sele.get(sele.size()-1).name;
//        }
        user.industry = curIndustrySt;
        Global.modifyAccount(user);
        setResult(RESULT_OK);
        finish();
    }

    HashMap<String,IndustryBean> selectMap=new HashMap();
    class IndustryAdapt extends RecyclerView.Adapter<IndustryHodler>  {
        Context context;
        List<IndustryBean> beanList;

        public IndustryAdapt(Context context) {
            this.context = context;
            beanList = new ArrayList<>();
        }

        public void addDataAll(List<IndustryBean> data) {
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
            IndustryBean bean=  beanList.get(position);
            hodler.curBean=bean;

            hodler.viewTVName.setText(bean.name);
            if(selectMap.containsKey(bean.code)){
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
        IndustryBean curBean;
        public IndustryHodler(View itemView) {
            super(itemView);
            viewTVName = itemView.findViewById(R.id.viewTVName);
            viewIVSelectState = itemView.findViewById(R.id.viewIVSelectState);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
//            selectMap.clear();
//            selectMap.put(curBean.code,curBean);
//            mAdapter.notifyDataSetChanged();
            updateIndustryToServer(curBean);
        }
    }
}