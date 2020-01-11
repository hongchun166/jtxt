package com.linkb.jstx.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.model.world_area.CountryBean;
import com.linkb.jstx.model.world_area.CountryCodeBean;
import com.linkb.jstx.model.world_area.WorlAreaOpt;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SelectCountryCodeActivity extends BaseActivity {
    @BindView(R.id.lv_friend_data)
    RecyclerView recyclerView;
    Unbinder unbinder;
    CountryCodeAdapt mAdapter;
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(unbinder!=null){
            unbinder.unbind();;
            unbinder=null;
        }
    }

    @Override
    protected void initComponents() {
        unbinder = ButterKnife.bind(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new CountryCodeAdapt(this);
        recyclerView.setAdapter(mAdapter);
        loadData();
    }
    @OnClick(R.id.back_btn)
    public void onEvenBack() {
        finish();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_country_code_list;
    }

    public void loadData(){
        WorlAreaOpt worlAreaOpt=new WorlAreaOpt();
        mAdapter.addDataAll(worlAreaOpt.qureyCountryCodeList(this));
    }
    public void confirmCountryCodeBeanBy(CountryCodeBean countryCodeBean){
        Intent intent=new Intent();
        intent.putExtra("CountryCode",countryCodeBean.getCode());
        setResult(Activity.RESULT_OK,intent);
        finish();
    }

    class CountryCodeAdapt extends RecyclerView.Adapter<CountryCodeHodler>  {
        Context context;
        List<CountryCodeBean> mContentList;

        public CountryCodeAdapt(Context context) {
            this.context = context;
            mContentList = new ArrayList<>();
        }

        public void addDataAll(List<CountryCodeBean> data) {
            mContentList.clear();
            mContentList.addAll(data);
            super.notifyDataSetChanged();
        }

        private Context getFmContent() {
            return context;
        }

        @NonNull
        @Override
        public CountryCodeHodler onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
            View view = LayoutInflater.from(getFmContent()).inflate(R.layout.item_country_code, viewGroup, false);
            CountryCodeHodler hodler = new CountryCodeHodler(view);
            return hodler;
        }

        @Override
        public void onBindViewHolder(CountryCodeHodler contactsHolder, int position) {
            CountryCodeBean friend = (CountryCodeBean) mContentList.get(position);
            contactsHolder.countryCodeBean = friend;
            contactsHolder.viewCountryName.setText(friend.getCn());
            contactsHolder.viewCountryCode.setText(friend.getCode());
            contactsHolder.itemView.setTag(friend);
        }

        @Override
        public int getItemCount() {
            return mContentList.size();
        }
    }

    class CountryCodeHodler extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView viewCountryName;
        public TextView viewCountryCode;
        public View viewItemRoot;
        CountryCodeBean countryCodeBean;

        public CountryCodeHodler(View itemView) {
            super(itemView);
            viewItemRoot=itemView.findViewById(R.id.viewItemRoot);
            viewCountryName = itemView.findViewById(R.id.viewCountryName);
            viewCountryCode = itemView.findViewById(R.id.viewCountryCode);
            viewItemRoot.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            confirmCountryCodeBeanBy(countryCodeBean);
        }
    }
}
