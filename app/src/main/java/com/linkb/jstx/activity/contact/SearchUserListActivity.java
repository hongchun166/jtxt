package com.linkb.jstx.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.chat.GroupChatActivity;
import com.linkb.jstx.adapter.SearchFriendAdapter;
import com.linkb.jstx.adapter.SearchGroupAdapter;
import com.linkb.jstx.adapter.SearchGroupAdapterV2;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.intent.SearchUserParam;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;
import com.linkb.jstx.network.result.FriendQueryResult;
import com.linkb.jstx.network.result.GroupQueryResult;
import com.linkb.jstx.util.InputSoftUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SearchUserListActivity extends BaseActivity {

    private static final int SEARCH_TYPE_User=0;
    private static final int SEARCH_TYPE_Group=1;
    @BindView(R.id.viewTitle)
    TextView viewTitle;
    @BindView(R.id.viewBack)
    View viewBack;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.viewEmpty)
    LinearLayout viewEmpty;
    Unbinder unbinder;

    SearchUserParam searchUserParam;
    private List<FriendQueryResult.DataListBean> mSearchUserList = new ArrayList<>();
    private SearchFriendAdapter mAdapterUser;

    private SearchGroupAdapterV2 mAdapterGroup;
    private List<GroupQueryResult.DataListBean> mSearchGroupList = new ArrayList<>();


    public static void navToSearchUser(Context context, SearchUserParam searchUserParam){
        Intent intent=new Intent(context,SearchUserListActivity.class);
        intent.putExtra("SearchType",SEARCH_TYPE_User);
        intent.putExtra("SearchParam",searchUserParam);
        context.startActivity(intent);
    }
    public static void navToSearchGroup(Context context, SearchUserParam searchUserParam){
        Intent intent=new Intent(context,SearchUserListActivity.class);
        intent.putExtra("SearchType",SEARCH_TYPE_Group);
        intent.putExtra("SearchParam",searchUserParam);
        context.startActivity(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(unbinder!=null){
            unbinder.unbind();
            unbinder=null;
        }
        InputSoftUtils.hideSoftInput(this);
    }


    @Override
    protected int getContentLayout() {
        return R.layout.activity_search_user_list;
    }
    private Context getContext(){
        return this;
    }
    @Override
    protected void initComponents() {
        unbinder=ButterKnife.bind(this);
        Bundle bundle=getIntent().getExtras();
        searchUserParam= (SearchUserParam) bundle.getSerializable("SearchParam");
        searchUserParam.setSearchType(bundle.getInt("SearchType",SEARCH_TYPE_User));
        if(searchUserParam.getSearchType()==SEARCH_TYPE_User){
            viewTitle.setText(R.string.search_user2);
            initSearchUserView();
            httpSearchUserList(searchUserParam);
        }else if(searchUserParam.getSearchType()==SEARCH_TYPE_Group){
            viewTitle.setText(R.string.search_group2);
            initSearchGroupView();
            httpSearchGroupList(searchUserParam);
        }
    }

    @OnClick(R.id.viewBack)
    public void onBackBtn() {
        finish();
    }

    public SearchUserParam getSearchUserParam() {
        return searchUserParam;
    }

    private void initSearchUserView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapterUser = new SearchFriendAdapter(this, mSearchUserList, new SearchFriendAdapter.OnSearchFriendClickedListener() {
            @Override
            public void onAddFirend(FriendQueryResult.DataListBean dataBean) {
                Friend friend = new Friend();
                friend.name = dataBean.getName();
                friend.account = dataBean.getAccount();
                Intent intent=new Intent(getContext(),ApplyFriendActivityV2.class);
                intent.putExtra(Friend.class.getName(),friend);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(mAdapterUser);
    }
    private void initSearchGroupView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapterGroup = new SearchGroupAdapterV2(this, mSearchGroupList, new SearchGroupAdapterV2.OnSearchGroupClickedListener() {
            @Override
            public void onJoinGroup(GroupQueryResult.DataListBean dataBean) {
                showProgressDialog("");
                HttpServiceManager.applyJoinGroup(dataBean.getId(),  applyJoinGroupListener);
            }
            @Override
            public void onGroupChat(GroupQueryResult.DataListBean dataBean) {
                Intent intent = new Intent();
                intent.setClass(getContext(), GroupChatActivity.class);
                intent.putExtra(Constant.CHAT_OTHRES_ID, dataBean.getId());
                intent.putExtra(Constant.CHAT_OTHRES_NAME, dataBean.getName());
                startActivity(intent);
                finish();
            }
        });
        recyclerView.setAdapter(mAdapterGroup);
        recyclerView.setAdapter(mAdapterGroup);
    }


    private void httpSearchUserList(SearchUserParam searchUserParam){

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_login)));
        HttpServiceManager.queryFriend(searchUserParam.getInputStr(), new HttpRequestListener<FriendQueryResult>(){
            @Override
            public void onHttpRequestSucceed(FriendQueryResult result, OriginalCall call) {
                hideProgressDialog();
                if (result.isSuccess() && result.getData()!=null && !result.getData().isEmpty()){
                    viewEmpty.setVisibility(View.INVISIBLE);
                    mSearchUserList.clear();
                    mSearchUserList.addAll(result.getData());
                    mAdapterUser.notifyDataSetChanged();
                }else {
                    viewEmpty.setVisibility(View.VISIBLE);
                    mSearchUserList.clear();
                    mAdapterUser.notifyDataSetChanged();
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
                viewEmpty.setVisibility(View.VISIBLE);
            }
        });
    }
    private void httpSearchGroupList(SearchUserParam searchUserParam){
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_login)));
        HttpServiceManager.queryGroup(searchUserParam.getInputStr(), new HttpRequestListener<GroupQueryResult>(){
            @Override
            public void onHttpRequestSucceed(GroupQueryResult result, OriginalCall call) {
                hideProgressDialog();
                if (result.isSuccess() && result.getDataList()!=null && !result.getDataList().isEmpty()){
                    viewEmpty.setVisibility(View.INVISIBLE);
                    mSearchGroupList.clear();
                    mSearchGroupList.addAll(result.getDataList());
                    mAdapterGroup.notifyDataSetChanged();
                }else {
                    viewEmpty.setVisibility(View.VISIBLE);
                    mSearchGroupList.clear();
                    mAdapterGroup.notifyDataSetChanged();
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
                viewEmpty.setVisibility(View.VISIBLE);
            }
        });
    }

    private HttpRequestListener<BaseDataResult> applyJoinGroupListener = new HttpRequestListener<BaseDataResult>() {
        @Override
        public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
            hideProgressDialog();
            if (result.isSuccess()){
                showToastView(getResources().getString(R.string.apply_join_group_tips));
            }
        }
        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {
            hideProgressDialog();
        }
    };
}
