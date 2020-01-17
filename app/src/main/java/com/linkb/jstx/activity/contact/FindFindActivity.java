package com.linkb.jstx.activity.contact;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
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
import com.linkb.jstx.model.intent.SearchUserParam;
import com.linkb.jstx.model.world_area.CountryBean;
import com.linkb.jstx.util.InputSoftUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 找一找
 * {@link SearchFriendActivityV2}
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
    @BindView(R.id.viewOtherCondition)
    TextView viewOtherCondition;
    @BindView(R.id.viewSearchGroupInput)
    EditText viewSearchGroupInput;

    private int searchType = 0;  //0找人  1找群


    private String industr;
    private String label;
    RegionDigOpt regionDigOpt;

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
    }
    @OnClick(R.id.next_button)
    public void search() {
        httpSearchFrient();
    }
    @OnClick(R.id.viewBtnSearchGroup)
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
        Intent intent = new Intent(this, ModifyIndustryActivityV2.class);
        startActivityForResult(intent, REQUEST_INDUSTRY_CODE);
    }
    @OnClick(R.id.ll_region_item)
    public void findRegion() {
        if(regionDigOpt!=null) {
            regionDigOpt.changeRegion(null);
            regionDigOpt.showRegionDialog(this);
        }
    }
    @OnClick(R.id.ll_label)
    public void findLabel() {
        Intent intent = new Intent(this, ModifyLabelActivityV2.class);
        String label = tvLabel.getText().toString().trim();
        intent.putExtra("labelItem", label);
        startActivityForResult(intent, REQUEST_LABE_CODE);
    }
    @OnClick(R.id.viewOtherCondition)
    public void otherCondition() {
        showOtherConditionDig();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CountryBean_CODE && Activity.RESULT_OK==resultCode){
            CountryBean countryBean= (CountryBean) data.getSerializableExtra("CountryBean");
            if(regionDigOpt!=null)regionDigOpt.changeRegion(countryBean);
        }else if(requestCode == REQUEST_INDUSTRY_CODE && resultCode == Activity.RESULT_OK){
            industr = data.getStringExtra("curIndustrySt");
            tvIndustry.setText(industr);
        }else if(requestCode == REQUEST_LABE_CODE && resultCode == 200){
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
        viewFindUserRoot.setVisibility(searchType != 1?View.VISIBLE:View.GONE);
        viewFindGroupRoot.setVisibility(searchType == 1?View.VISIBLE:View.GONE);
    }

    private void showOtherConditionDig(){
        final  List<String> otherConditionSex=new ArrayList<>();
        otherConditionSex.add(getString(R.string.no_limit));
        otherConditionSex.add(getString(R.string.common_man));
        otherConditionSex.add(getString(R.string.common_female));

        final  List<String> otherConditionMarriage=new ArrayList<>();
        otherConditionMarriage.add(getString(R.string.no_limit));
        otherConditionMarriage.add(getString(R.string.marriage2));
        otherConditionMarriage.add(getString(R.string.unmarried2));

        OptionsPickerView pvCustomOptions = new OptionsPickerBuilder(getContext(), new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                String sex=otherConditionSex.get(options1);
                String marriage=otherConditionMarriage.get(options2);
                viewOtherCondition.setText(sex+"/"+marriage);
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
        pvCustomOptions.setNPicker(otherConditionSex, otherConditionMarriage,null);
        pvCustomOptions.show();
    }

    private void httpSearchFrient(){
        String friendName = viewSearchUserInput.getText().toString();
        SearchUserParam searchUserParam=new SearchUserParam();
        searchUserParam.setInputStr(friendName);
        SearchUserListActivity.navToSearchUser(this,searchUserParam);
    }
    private void httpSearchGroup(){
        String groupKey = viewSearchGroupInput.getText().toString();
        SearchUserParam searchUserParam=new SearchUserParam();
        searchUserParam.setInputStr(groupKey);
        SearchUserListActivity.navToSearchGroup(this,searchUserParam);
    }

}