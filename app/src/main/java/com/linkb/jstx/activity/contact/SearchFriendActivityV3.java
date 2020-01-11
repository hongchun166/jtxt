package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
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

public class SearchFriendActivityV3 extends BaseActivity implements HttpRequestListener<FriendQueryResult>, SearchFriendAdapter.OnSearchFriendClickedListener {

    @BindView(R.id.search_edit_text)
    EditText searchEdt;
    @BindView(R.id.search_content_cly)
    View searchContentCly;
    @BindView(R.id.content_textView)
    TextView contentTv;
    @BindView(R.id.empty_view)
    View emptyView;
    @BindView(R.id.search_friend_recycler_view)
    RecyclerView searchFriendRecyclerView;

    private SearchFriendAdapter mAdapter;
    private List<FriendQueryResult.DataListBean> mSearchList = new ArrayList<>();
    private FriendQueryResult.DataListBean mDataBean;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        searchEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                emptyView.setVisibility(View.INVISIBLE);
                if (!TextUtils.isEmpty(charSequence)){
                    searchContentCly.setVisibility(View.VISIBLE);
                    contentTv.setText(charSequence);
                }else {
                    searchContentCly.setVisibility(View.INVISIBLE);
                    contentTv.setText("");
                    mSearchList.clear();
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchFriendRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchFriendAdapter(this, mSearchList, this);
        searchFriendRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.search_content_cly)
    public void searchFriend(){
        String friendName = contentTv.getText().toString();
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_login)));
        HttpServiceManager.queryFriend(friendName, this);
    }

    @OnClick(R.id.back_btn)
    public void onBackBtn() {
        finish();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_search_friend_v3;
    }

    @Override
    public void onHttpRequestSucceed(FriendQueryResult result, OriginalCall call) {
        hideProgressDialog();
        if (result.isSuccess()){
            emptyView.setVisibility(View.INVISIBLE);
            searchContentCly.setVisibility(View.VISIBLE);
            mSearchList.clear();
            mSearchList.addAll(result.getDataList());
            mAdapter.notifyDataSetChanged();
//            LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
////            finish();
        }else {
//            showToastView(result.message);
            emptyView.setVisibility(View.VISIBLE);
            searchContentCly.setVisibility(View.INVISIBLE);
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
        Intent intent=new Intent(this,ApplyFriendActivityV2.class);
        intent.putExtra(Friend.class.getName(),friend);
        startActivity(intent);
    }

    private HttpRequestListener<BaseResult> addFriendRequest = new HttpRequestListener<BaseResult>() {
        @Override
        public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
            hideProgressDialog();
            if (result.isSuccess()){
                Intent intent = new Intent(SearchFriendActivityV3.this, PersonInfoActivity.class);
                Friend friend = Friend.searchResultToFriend(mDataBean);
                intent.putExtra(Friend.class.getName(), friend);
                startActivity(intent);

                LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
            }else {
                showToastView(result.message);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };

    @Override
    public void finish() {
        super.finish();
        InputSoftUtils.hideSoftInput(this);
    }
}
