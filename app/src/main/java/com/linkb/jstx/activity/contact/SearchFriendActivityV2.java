package com.linkb.jstx.activity.contact;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.setting.ModifyIndustryActivityV2;
import com.linkb.jstx.activity.setting.ModifyLabelActivityV2;
import com.linkb.jstx.adapter.SearchFriendAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.network.result.FriendQueryResult;
import com.linkb.jstx.util.InputSoftUtils;

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


    private SearchFriendAdapter mAdapter;
    private List<FriendQueryResult.DataListBean> mSearchList = new ArrayList<>();
    private FriendQueryResult.DataListBean mDataBean;
    private int searchType = 0;  //0找人  1找群
    public static final int REQUEST_INDUSTRY_CODE = 1001;
    public static final int REQUEST_LABE_CODE = 1002;
    private String industr;
    private String label;

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
}
