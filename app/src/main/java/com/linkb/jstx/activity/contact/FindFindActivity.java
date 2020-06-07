package com.linkb.jstx.activity.contact;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.SelectCountryActivity;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.contact.contracts.RegionDigOpt;
import com.linkb.jstx.activity.setting.ModifyIndustryActivityV2;
import com.linkb.jstx.activity.setting.ModifyLabelActivityV2;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.model.intent.SearchUserParam;
import com.linkb.jstx.model.world_area.CountryBean;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.v2.ListTagsResult;
import com.linkb.jstx.util.InputSoftUtils;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 找一找
 *
 */
public class FindFindActivity extends BaseActivity {
    public  final int REQUEST_INDUSTRY_CODE = 1001;//行业
    public  final int REQUEST_LABE_CODE = 1002;//标签
    public  final int REQUEST_CountryBean_CODE = 1003;////国家

    @BindView(R.id.viewFindUserRoot)
    LinearLayout viewFindUserRoot;
    @BindView(R.id.viewFindGroupRoot)
    RelativeLayout viewFindGroupRoot;

    @BindView(R.id.viewSearchUserInput)
    EditText viewSearchUserInput;
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
    @BindView(R.id.viewSexCondition)
    TextView viewSexCondition;
    @BindView(R.id.viewMarriedCondition)
    TextView viewMarriedCondition;

    @BindView(R.id.viewSearchGroupInput)
    EditText viewSearchGroupInput;

    @BindView(R.id.viewTagFlowLayout)
    TagFlowLayout viewTagFlowLayout;
    @BindView(R.id.viewSearchHotItemRoot)
    RelativeLayout viewSearchHotItemRoot;
    @BindView(R.id.vieLatelySearchRecyclerView)
    RecyclerView vieLatelySearchRecyclerView;
    @BindView(R.id.nextButtonGroup)
    Button nextButtonGroup;


    private int searchType = 0;  //0找人  1找群



    RegionDigOpt regionDigOpt;

    List<ListTagsResult.DataBean> hotSearchList=new ArrayList<>();//热门搜索
    List<String> latelySearchByList=new ArrayList<>();//最近搜索
    LatelySearchAdapt latelySearchAdapt;//最近搜索适配器
    TagAdapter tagAdapterHotSearchGroup;
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(regionDigOpt!=null){
            regionDigOpt.release();
            regionDigOpt=null;
        }
    }
    @Override
    protected int getContentLayout() {
        return R.layout.activity_search_find_find;
    }
    private Context getContext(){
        return this;
    }
    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        ImmersionBar.with(this)
                .keyboardEnable(true)
                .statusBarDarkFont(true)
                .init();
        updateType(0);
        tvFindPeople.setSelected(true);
        regionDigOpt=new RegionDigOpt();
        regionDigOpt.loadData(this);
        httpHotSearchByGroup();
        viewTagFlowLayout.setAdapter(tagAdapterHotSearchGroup=new TagAdapter<ListTagsResult.DataBean>(hotSearchList) {
            @Override
            public View getView(FlowLayout parent, int position, ListTagsResult.DataBean bean) {
                TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_group_hot_search,parent, false);
                tv.setText(bean.getName());
                return tv;
            }
        });
        initEven();
    }

    private void initEven(){
        viewSearchHotItemRoot.setVisibility(View.GONE);
        viewSearchGroupInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    viewSearchHotItemRoot.setVisibility(View.VISIBLE);
                }
            }
        });
        viewSearchGroupInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //点击搜索的时候隐藏软键盘
                    InputSoftUtils.hideSoftInput(FindFindActivity.this);
                    // 在这里写搜索的操作,一般都是网络请求数据
                    httpSearchGroup();
                    return true;
                }
                return false;
            }
        });
        regionDigOpt.setOnRegionDigOptCallback(new RegionDigOpt.OnRegionDigOptCallback() {
            @Override
            public void onRegionDigCountryClick() {
                startActivityForResult(new Intent(FindFindActivity.this, SelectCountryActivity.class),REQUEST_CountryBean_CODE);
            }
            @Override
            public void onRegionSelectSuc(String region) {
                if(tv_region!=null)tv_region.setText(region);
            }
        });
        viewTagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                ListTagsResult.DataBean str=hotSearchList.get(position);
                inputGroupStrToSearch(str.getName());
                return false;
            }
        });
        viewSearchGroupInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                String inputText=viewSearchGroupInput.getText().toString();
                updateLatelySearchByGroup(inputText);
            }
        });
    }

    private void addSearLog(String str){
        if(!latelySearchByList.contains(str)){
            latelySearchByList.add(str);
        }
    }
    private void removeSearchLog(String str){
        latelySearchAdapt.removeData(str);
    }
    private void updateLatelySearchByGroup(String inputStr){
        if(latelySearchByList.size()==0){
            latelySearchByList.add("金融");
            latelySearchByList.add("比特币");
            latelySearchByList.add("互联网");
        }
        List<String> findLatelyList=new ArrayList<>();
        if(!TextUtils.isEmpty(inputStr)){
            for (String str : latelySearchByList) {
                if(str.contains(inputStr)){
                    findLatelyList.add(str);
                }
            }
        }
        if(latelySearchAdapt==null){
            latelySearchAdapt=new LatelySearchAdapt();
            vieLatelySearchRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            vieLatelySearchRecyclerView.setAdapter(latelySearchAdapt);
        }
        latelySearchAdapt.setData(findLatelyList);
    }
    private void inputGroupStrToSearch(String inputStr){
        if(inputStr==null)return;
        viewSearchGroupInput.setText(inputStr);
        httpSearchGroup();
    }
    @OnClick(R.id.next_button)
    public void search() {
        httpSearchFrient();
    }
    @OnClick(R.id.nextButtonGroup)
    public void searchGroup() {
        httpSearchGroup();
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
        ModifyIndustryActivityV2.navToActBySelectTag(this,REQUEST_INDUSTRY_CODE,"");
    }
    @OnClick(R.id.ll_region_item)
    public void findRegion() {
        if(regionDigOpt!=null) {
            regionDigOpt.changeRegion(null,false);
            regionDigOpt.showRegionDialog(this);
        }
    }
    @OnClick(R.id.ll_label)
    public void findLabel() {
        String label = tvLabel.getText().toString().trim();
        ModifyLabelActivityV2.navToActBySelectTag(this,REQUEST_LABE_CODE,label);
    }
    @OnClick(R.id.viewSexCondition)
    public void sexCondition() {
        showSexConditionDig();
    }
    @OnClick(R.id.viewMarriedCondition)
    public void marriedCondition() {
        showMarriedConditionDig();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CountryBean_CODE && Activity.RESULT_OK==resultCode){
            CountryBean countryBean= (CountryBean) data.getSerializableExtra("CountryBean");
            if(regionDigOpt!=null)regionDigOpt.changeRegion(countryBean);
        }else if(requestCode == REQUEST_INDUSTRY_CODE && resultCode == Activity.RESULT_OK){
            String industr = data.getStringExtra("curIndustrySt");
            tvIndustry.setText(industr);
        }else if(requestCode == REQUEST_LABE_CODE && resultCode == RESULT_OK){
            String  label = data == null ? "" : data.getStringExtra("labelItem");
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
        viewFindUserRoot.setVisibility(searchType != 1?View.VISIBLE:View.GONE);
        viewFindGroupRoot.setVisibility(searchType == 1?View.VISIBLE:View.GONE);
    }
    private void showMarriedConditionDig(){
        final  List<String> otherConditionMarriage=new ArrayList<>();
        otherConditionMarriage.add(getString(R.string.no_limit));
        otherConditionMarriage.add(getString(R.string.marriage2));
        otherConditionMarriage.add(getString(R.string.unmarried2));

        OptionsPickerView pvCustomOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String sex=otherConditionMarriage.get(options1);
//                String marriage=otherConditionMarriage.get(options2);
                viewMarriedCondition.setText(sex);
            }
        }).isDialog(false)
                .setSubmitText(getString(R.string.finish))
                .setSubmitColor(ContextCompat.getColor(getContext(), R.color.color_2e76e5))
                .setCancelText(getString(R.string.common_cancel))
                .setCancelColor(ContextCompat.getColor(getContext(), R.color.divider_color_gray_999999))
                .setOutSideCancelable(false)
                .build();;

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
        pvCustomOptions.setNPicker(otherConditionMarriage, null,null);
        pvCustomOptions.show();
    }
    private void showSexConditionDig(){
        final  List<String> otherConditionSex=new ArrayList<>();
        otherConditionSex.add(getString(R.string.no_limit));
        otherConditionSex.add(getString(R.string.common_man));
        otherConditionSex.add(getString(R.string.common_female));
        OptionsPickerView pvCustomOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String sex=otherConditionSex.get(options1);
//                String marriage=otherConditionMarriage.get(options2);
                viewSexCondition.setText(sex);
            }
        }).isDialog(false)
                .setSubmitText(getString(R.string.finish))
                .setSubmitColor(ContextCompat.getColor(getContext(), R.color.color_2e76e5))
                .setCancelText(getString(R.string.common_cancel))
                .setCancelColor(ContextCompat.getColor(getContext(), R.color.divider_color_gray_999999))
                .setOutSideCancelable(false)
                .build();;

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
        pvCustomOptions.setNPicker(otherConditionSex, null,null);
        pvCustomOptions.show();
    }
    private void refreshDataHotSearchByGroup(List<ListTagsResult.DataBean> dataBeans){
        hotSearchList.clear();
        hotSearchList.addAll(dataBeans);
        tagAdapterHotSearchGroup.notifyDataChanged();
    }
    private void httpSearchFrient(){
        String friendName = viewSearchUserInput.getText().toString();
        SearchUserParam searchUserParam=new SearchUserParam();
        searchUserParam.setInputStr(friendName);
        searchUserParam.setIndustry(tvIndustry.getText().toString());
        searchUserParam.setLabel(tvLabel.getText().toString());
        searchUserParam.setRegion(tv_region.getText().toString());
        {
            String man=getString(R.string.common_man);
            String female=getString(R.string.common_female);
            String inputStrSex=viewSexCondition.getText().toString();
            if(!TextUtils.isEmpty(inputStrSex)){
                searchUserParam.setGender(man.equals(inputStrSex)?"1":(female.equals(inputStrSex))?"0":"");
            }
        }
        {
            String marriage2=getString(R.string.marriage2);
            String unmarried2=getString(R.string.unmarried2);
            String marriageInput=viewMarriedCondition.getText().toString();
            if(!TextUtils.isEmpty(marriageInput)){
                searchUserParam.setMarriedStr(marriage2.equals(marriageInput)?"非单身":(unmarried2.equals(marriageInput))?"单身":"");
            }
        }
        SearchUserListActivity.navToSearchUser(this,searchUserParam);
    }
    private void httpSearchGroup(){
        String groupKey = viewSearchGroupInput.getText().toString();
        addSearLog(groupKey);
        SearchUserParam searchUserParam=new SearchUserParam();
        searchUserParam.setInputStr(groupKey);
        SearchUserListActivity.navToSearchGroup(this,searchUserParam);
    }
    private void httpHotSearchByGroup(){
        String account=Global.getCurrentUser().account;
        HttpServiceManagerV2.getLisTags(account,new HttpRequestListener<ListTagsResult>() {
            @Override
            public void onHttpRequestSucceed(ListTagsResult result, OriginalCall call) {
                if(result.isSuccess()){
                    refreshDataHotSearchByGroup(result.getData());
                }else {
                    List<ListTagsResult.DataBean> list=new ArrayList<>();
                    list.add(new ListTagsResult.DataBean("1","金融"));
                    list.add(new ListTagsResult.DataBean("2","区块链"));
                    list.add(new ListTagsResult.DataBean("3","直销"));
                    list.add(new ListTagsResult.DataBean("4","PE"));
                    list.add(new ListTagsResult.DataBean("5","比特币"));
                    list.add(new ListTagsResult.DataBean("5","互联网"));
                    refreshDataHotSearchByGroup(list);
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
            }
        });
    }

   public  class LatelySearchAdapt extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
        List<String> mData=new ArrayList<>();
        public void setData(List<String> data){
            mData.clear();
            mData.addAll(data);
            notifyDataSetChanged();
        }
        public void removeData(String str){
            if(mData.contains(str)){
                mData.remove(str);
                notifyDataSetChanged();
            }
        }
       @NonNull
       @Override
       public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
           View view = LayoutInflater.from(getContext()).inflate(R.layout.item_lately_sear,viewGroup, false);
           LatelySearchAdaptHodler hodler=new LatelySearchAdaptHodler(view);
           return hodler;
       }

       @Override
       public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
           LatelySearchAdaptHodler hodler= (LatelySearchAdaptHodler) viewHolder;
           String bean=mData.get(position);
           hodler.bean=bean;
           hodler.viewSearchStr.setText(bean);
       }

       @Override
       public int getItemCount() {
           return mData.size();
       }
   }
   public class LatelySearchAdaptHodler extends RecyclerView.ViewHolder implements View.OnClickListener{
        View viewSearchRoot;
        TextView  viewSearchStr;
        ImageView viewIVX;
        String bean;
       public LatelySearchAdaptHodler(@NonNull View itemView) {
           super(itemView);
           viewSearchRoot=itemView.findViewById(R.id.viewSearchRoot);
           viewSearchStr=itemView.findViewById(R.id.viewSearchStr);
           viewIVX=itemView.findViewById(R.id.viewIVX);
           viewIVX.setOnClickListener(this);
           viewSearchRoot.setOnClickListener(this);
       }

       @Override
       public void onClick(View v) {
           if(v.getId()==R.id.viewIVX){
                removeSearchLog(bean);
           }else if(v.getId()==R.id.viewSearchRoot){
               inputGroupStrToSearch(bean);
           }
       }
   }
}
