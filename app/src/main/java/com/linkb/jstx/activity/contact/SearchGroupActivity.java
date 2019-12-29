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

import com.j256.ormlite.stmt.query.In;
import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.chat.GroupChatActivity;
import com.linkb.jstx.adapter.SearchGroupAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.GroupQueryResult;
import com.linkb.jstx.util.InputSoftUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 搜索群组功能
* */
public class SearchGroupActivity extends BaseActivity implements HttpRequestListener<GroupQueryResult>, SearchGroupAdapter.OnSearchGroupClickedListener {

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

    private SearchGroupAdapter mAdapter;
    private List<GroupQueryResult.DataListBean> mSearchList = new ArrayList<>();
    private GroupQueryResult.DataListBean mDataBean;
    private User mUser;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);
        mUser = Global.getCurrentUser();

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
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchFriendRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchGroupAdapter(this, mSearchList, this);
        searchFriendRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.search_content_cly)
    public void searchGroup(){
        String keyword = contentTv.getText().toString();
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_login)));
        HttpServiceManager.queryGroup(keyword, this);

    }

    @OnClick(R.id.back_btn)
    public void onBackBtn() {
        finish();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_search_group;
    }

    @Override
    public void onHttpRequestSucceed(GroupQueryResult result, OriginalCall call) {
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

    private HttpRequestListener<BaseDataResult> applyJoinGroupListener = new HttpRequestListener<BaseDataResult>() {
        @Override
        public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
            hideProgressDialog();
            if (result.isSuccess()){
                searchEdt.setText("");
                showToastView(getResources().getString(R.string.apply_join_group_tips));
            }else {
//                showToastView(result.message);
            }
////                Intent intent = new Intent(SearchGroupActivity.this, PersonInfoActivity.class);
////                Friend friend = Friend.searchResultToFriend(mDataBean);
////                intent.putExtra(Friend.class.getName(), friend);
////                startActivity(intent);
////
////                LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_RELOAD_CONTACTS));
////            }else {
////                showToastView(result.message);
////            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };

    @Override
    public void onJoinGroup(GroupQueryResult.DataListBean dataBean) {
        mDataBean = dataBean;
        showProgressDialog("");
        HttpServiceManager.applyJoinGroup(dataBean.getId(),  applyJoinGroupListener);
    }

    @Override
    public void onGroupChat(GroupQueryResult.DataListBean dataBean) {
        Intent intent = new Intent();
        intent.setClass(this, GroupChatActivity.class);
        intent.putExtra(Constant.CHAT_OTHRES_ID, dataBean.getId());
        intent.putExtra(Constant.CHAT_OTHRES_NAME, dataBean.getName());
        startActivity(intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        InputSoftUtils.hideSoftInput(this);
    }
}
