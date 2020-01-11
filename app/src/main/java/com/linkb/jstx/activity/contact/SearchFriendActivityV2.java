package com.linkb.jstx.activity.contact;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.SelectCountryActivity;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.setting.ModifyIndustryActivityV2;
import com.linkb.jstx.activity.setting.ModifyLabelActivityV2;
import com.linkb.jstx.activity.setting.ProfileEditActivityV2;
import com.linkb.jstx.adapter.SearchFriendAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.world_area.CountryBean;
import com.linkb.jstx.model.world_area.WorlAreaOpt;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.network.result.FriendQueryResult;
import com.linkb.jstx.network.result.RegionResult;
import com.linkb.jstx.util.InputSoftUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchFriendActivityV2 extends BaseActivity implements HttpRequestListener<FriendQueryResult>, SearchFriendAdapter.OnSearchFriendClickedListener {

    @BindView(R.id.search_edit_text)
    EditText searchEdt;
    @BindView(R.id.tv_find_people)
    TextView tvFindPeople;
    @BindView(R.id.tv_find_group)
    TextView tvFindGroup;
    @BindView(R.id.tv_label)
    TextView tvLabel;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.tv_region)
    TextView tv_region;

    private SearchFriendAdapter mAdapter;
    private List<FriendQueryResult.DataListBean> mSearchList = new ArrayList<>();
    private FriendQueryResult.DataListBean mDataBean;
    private int searchType = 0;  //0找人  1找群
    public static final int REQUEST_INDUSTRY_CODE = 1001;
    public static final int REQUEST_LABE_CODE = 1002;
    private String industr;
    private String label;
    protected OptionsPickerView pvCustomOptions;
    private List<String> provinces = new ArrayList<>();
    private List<List<String>> citys = new ArrayList<>();
    WorlAreaOpt worlAreaOpt;
    CountryBean countryBean;
    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        tvFindPeople.setSelected(true);
        mAdapter = new SearchFriendAdapter(this, mSearchList, this);
    }

    @OnClick(R.id.next_button)
    public void search() {
        String friendName = searchEdt.getText().toString();
        if (TextUtils.isEmpty(friendName)) {
            showToastView(R.string.please_search_content);
            return;
        }
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_login)));
        HttpServiceManager.queryFriend(friendName, this);
    }

    @OnClick(R.id.back_btn)
    public void onBackBtn() {
        finish();
    }

    @OnClick(R.id.tv_find_people)
    public void findPhone() {
        updateType(0);
    }

    @OnClick(R.id.tv_find_group)
    public void findGroup() {
        updateType(1);
    }

    @OnClick(R.id.ll_industry)
    public void findIndustry() {
        Intent intent = new Intent(this, ModifyIndustryActivityV2.class);
        startActivityForResult(intent, REQUEST_INDUSTRY_CODE);
    }

    @OnClick(R.id.ll_label)
    public void findLabel() {
        Intent intent = new Intent(this, ModifyLabelActivityV2.class);
        String label = tvLabel.getText().toString().trim();
        intent.putExtra("labelItem", label);
        startActivityForResult(intent, REQUEST_LABE_CODE);
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_search_friend;
    }

    @Override
    public void onHttpRequestSucceed(FriendQueryResult result, OriginalCall call) {
        System.out.println("当前数据------");
        hideProgressDialog();
        if (result.isSuccess()) {
//            emptyView.setVisibility(View.INVISIBLE);
//            searchContentCly.setVisibility(View.VISIBLE);
            mSearchList.clear();
            mSearchList.addAll(result.getDataList());
            mAdapter.notifyDataSetChanged();
//            LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
////            finish();
        } else {
//            showToastView(result.message);
//            emptyView.setVisibility(View.VISIBLE);
//            searchContentCly.setVisibility(View.INVISIBLE);
            mSearchList.clear();
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }

    @Override
    public void onAddFirend(FriendQueryResult.DataListBean dataBean) {
        mDataBean = dataBean;
        Friend friend = new Friend();
        friend.name = dataBean.getName();
        friend.account = dataBean.getAccount();
        Intent intent = new Intent(this, ApplyFriendActivityV2.class);
        intent.putExtra(Friend.class.getName(), friend);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==0x15 && Activity.RESULT_OK==resultCode){
            CountryBean countryBean= (CountryBean) data.getSerializableExtra("CountryBean");
            changeRegion(countryBean);
            return;
        }
        if (requestCode == REQUEST_INDUSTRY_CODE && resultCode == Activity.RESULT_OK) {
            industr = data.getStringExtra("111");

        }
        if (requestCode == REQUEST_LABE_CODE && resultCode == 200) {
            label = data == null ? "" : data.getStringExtra("labelItem");
            tvLabel.setText(label);
        }
    }

    @Override
    public void finish() {
        super.finish();
        InputSoftUtils.hideSoftInput(this);
    }

    private void updateType(int type) {
        searchType = type;
        tvFindGroup.setSelected(searchType == 1);
        tvFindPeople.setSelected(searchType != 1);
    }

    private void showRegionDialog(){
        final Context context=this;
        if(countryBean==null){
            countryBean=new CountryBean();
            countryBean.setId("44");//中国
            countryBean.setCname("中国");
            countryBean.setName("China");
        }
        if (pvCustomOptions == null) {
            pvCustomOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    String region = "" + provinces.get(options1) + "-" + citys.get(options1).get(options2);
                    tv_region.setText(region);
                }
            }).setLayoutRes(R.layout.view_region_options_layout, new CustomListener() {
                @Override
                public void customLayout(View v) {
                    final TextView viewCancel = (TextView) v.findViewById(R.id.viewCancel);
                    final TextView viewFinish = (TextView) v.findViewById(R.id.viewFinish);
                    TextView viewCountry = (TextView) v.findViewById(R.id.viewCountry);
                    viewCountry.setText(countryBean.getCname());
                    viewFinish.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pvCustomOptions.returnData();
                            pvCustomOptions.dismiss();
                        }
                    });
                    viewCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            pvCustomOptions.dismiss();
                        }
                    });
                    viewCountry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(
                                    new Intent(context, SelectCountryActivity.class)
                                    ,0x15);
                        }
                    });
                }
            })
                    .isDialog(false)
                    .setSubmitText(getString(R.string.finish))
                    .setSubmitColor(ContextCompat.getColor(context, R.color.color_2e76e5))
                    .setCancelText(getString(R.string.common_cancel))
                    .setCancelColor(ContextCompat.getColor(context, R.color.divider_color_gray_999999))
                    .setOutSideCancelable(false)
                    .build();
            pvCustomOptions.setSelectOptions(0, 1, 1);
            Dialog mDialog = pvCustomOptions.getDialog();
            if (mDialog != null) {
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        Gravity.BOTTOM);
                params.leftMargin = 0;
                params.rightMargin = 0;
                pvCustomOptions.getDialogContainerLayout().setLayoutParams(params);
                Window dialogWindow = mDialog.getWindow();
                if (dialogWindow != null) {
                    dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                    dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                    dialogWindow.setDimAmount(0.1f);
                }
            }
        }
        if(provinces == null || provinces.size() == 0 || citys == null || citys.size() == 0){
            if(countryBean.getId().equals("44")){
                getJson("citycode.json");
            }else {
                List<List<String>> provincesDouList = worlAreaOpt.qureyProvinceList(countryBean.getId());
                provinces = provincesDouList.get(1);
                citys = worlAreaOpt.qureyCityList(provincesDouList.get(0));
                if(provinces.size()==1 && provinces.get(0).equals("")){
                    provinces.clear();
                    provinces.add(countryBean.getCname());
                }
            }
        }
        pvCustomOptions.setPicker(provinces, citys);
        pvCustomOptions.show();
    }
    private void changeRegion(CountryBean countryBean){
        this.countryBean=countryBean;
        if(pvCustomOptions!=null){
            pvCustomOptions.dismiss();
            pvCustomOptions=null;
        }
        if(provinces!=null)provinces.clear();
        if(citys!=null)citys.clear();
        showRegionDialog();
    }
    /**
     * 读取assets本地json
     *
     * @param fileName 文件名称
     */
    public void getJson(final String fileName) {

        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = this.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
            Gson gson = new Gson();
            RegionResult regionResult = gson.fromJson(stringBuilder.toString(), RegionResult.class);
            provinces = new ArrayList<>();
            citys = new ArrayList<>();
            for (int i = 0; i < regionResult.province.size(); i++) {
                provinces.add(regionResult.province.get(i).a);
                List<String> marr = new ArrayList<>();
                for (int j = 0; j < regionResult.province.get(i).city.size(); j++) {
                    marr.add(regionResult.province.get(i).city.get(j).a);
                }
                citys.add(marr);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
