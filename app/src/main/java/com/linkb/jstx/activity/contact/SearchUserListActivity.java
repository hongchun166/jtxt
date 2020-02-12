package com.linkb.jstx.activity.contact;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.chat.GroupChatActivity;
import com.linkb.jstx.adapter.FindPersonsAdapter;
import com.linkb.jstx.adapter.SearchGroupAdapterV2;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.intent.SearchUserParam;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.v2.CheckInGroupResult;
import com.linkb.jstx.network.result.v2.FindGroupsResult;
import com.linkb.jstx.network.result.v2.FindPersonsResult;
import com.linkb.jstx.util.InputSoftUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 找个找，
 * 搜索好友，搜索群组
 */
public class SearchUserListActivity extends BaseActivity {

    private static final int SEARCH_TYPE_User=0;
    private static final int SEARCH_TYPE_Group=1;
    @BindView(R.id.viewSearchInput)
    TextView viewSearchInput;
    @BindView(R.id.viewCancel)
    TextView viewCancel;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.viewEmpty)
    LinearLayout viewEmpty;
    Unbinder unbinder;

    SearchUserParam searchUserParam;
    private List<FindPersonsResult.DataBean.ContentBean> mSearchUserList = new ArrayList<>();
    private FindPersonsAdapter mAdapterUser;

    private SearchGroupAdapterV2 mAdapterGroup;
    private List<FindGroupsResult.DataBean.ContentBean> mSearchGroupList = new ArrayList<>();


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
            StringBuffer stringBuffer=new StringBuffer();
            if(!TextUtils.isEmpty(searchUserParam.getInputStr()))stringBuffer.append(searchUserParam.getInputStr()).append(" ");
            if(!TextUtils.isEmpty(searchUserParam.getRegion()))stringBuffer.append(searchUserParam.getRegion()).append(" ");;
            if(!TextUtils.isEmpty(searchUserParam.getIndustry()))stringBuffer.append(searchUserParam.getIndustry()).append(" ");;
            if(!TextUtils.isEmpty(searchUserParam.getLabel()))stringBuffer.append(searchUserParam.getLabel()).append(" ");;
            if(!TextUtils.isEmpty(searchUserParam.getGender())){
                if("0".equals(searchUserParam.getGender())){
                    stringBuffer.append(getString(R.string.common_female)).append(" ");
                }else if("1".equals(searchUserParam.getGender())){
                    stringBuffer.append(getString(R.string.common_man)).append(" ");
                }
            }
            viewSearchInput.setText(stringBuffer.toString());
            initSearchUserView();
            httpSearchUserList(searchUserParam);
        }else if(searchUserParam.getSearchType()==SEARCH_TYPE_Group){
            viewSearchInput.setText(searchUserParam.getInputStr());
            initSearchGroupView();
            httpSearchGroupList(searchUserParam);
        }
    }

    @OnClick(R.id.viewCancel)
    public void onBackBtn() {
        finish();
    }

    public SearchUserParam getSearchUserParam() {
        return searchUserParam;
    }

    private void initSearchUserView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapterUser = new FindPersonsAdapter(this, mSearchUserList, new FindPersonsAdapter.OnSearchFriendClickedListener() {
            @Override
            public void onAddFirend(FindPersonsResult.DataBean.ContentBean dataBean) {
                showProgressDialog("");
                httpCheckFriend(dataBean);
            }
        });
        recyclerView.setAdapter(mAdapterUser);
    }
    private void initSearchGroupView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapterGroup = new SearchGroupAdapterV2(this, mSearchGroupList, new SearchGroupAdapterV2.OnSearchGroupClickedListener() {
            @Override
            public void onClickJoinGroup(FindGroupsResult.DataBean.ContentBean dataBean) {
                if (GroupRepository.queryById(dataBean.getId()) != null){
                    Intent intent = new Intent();
                    intent.setClass(getContext(), GroupChatActivity.class);
                    intent.putExtra(Constant.CHAT_OTHRES_ID, dataBean.getId());
                    intent.putExtra(Constant.CHAT_OTHRES_NAME, dataBean.getName());
                    startActivity(intent);
                    finish();
                }else {
                    httpCheckInGroup(dataBean);
                }
            }
            @Override
            public void onClickItemGroup(FindGroupsResult.DataBean.ContentBean dataBean) {
                if (GroupRepository.queryById(dataBean.getId()) != null){
                    Intent intent = new Intent();
                    intent.setClass(getContext(), GroupChatActivity.class);
                    intent.putExtra(Constant.CHAT_OTHRES_ID, dataBean.getId());
                    intent.putExtra(Constant.CHAT_OTHRES_NAME, dataBean.getName());
                    startActivity(intent);
                    finish();
                }else {
                    GroupDetailDescActivity.navToAct(getContext(),dataBean.toGroupB());
                }
            }
        });
        recyclerView.setAdapter(mAdapterGroup);
        recyclerView.setAdapter(mAdapterGroup);
    }

    public void navToApplyFriendAct(FindPersonsResult.DataBean.ContentBean dataBean){
        Friend friend = new Friend();
        friend.name = dataBean.getName();
        friend.account = dataBean.getAccount();
        Intent intent=new Intent(getContext(),ApplyFriendActivityV2.class);
        intent.putExtra(Friend.class.getName(),friend);
        startActivity(intent);
    }

    private void httpSearchUserList(SearchUserParam searchUserParam){

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.label_login)));
//        HttpServiceManager.queryFriend(searchUserParam.getInputStr(), new HttpRequestListener<FriendQueryResult>(){
        HttpServiceManagerV2.findPersons(searchUserParam.getInputStr(),searchUserParam.getRegion(),
                searchUserParam.getIndustry(),searchUserParam.getLabel(),searchUserParam.getGender(), new HttpRequestListener<FindPersonsResult>(){
                    @Override
                    public void onHttpRequestSucceed(FindPersonsResult result, OriginalCall call) {
                        hideProgressDialog();
                        boolean isDataListEmpty=true;
                        if(result.isSuccess()){
                            isDataListEmpty=result.isDataListEmpty();
                            if(!isDataListEmpty){
                                isDataListEmpty=false;
                                viewEmpty.setVisibility(View.INVISIBLE);
                                mSearchUserList.clear();
                                mSearchUserList.addAll(result.getData().getContent());
                                mAdapterUser.notifyDataSetChanged();
                            }
                        }
                        if(isDataListEmpty){
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
//        HttpServiceManager.queryGroup(searchUserParam.getInputStr(), new HttpRequestListener<GroupQueryResult>());
        HttpServiceManagerV2.findGroups(searchUserParam.getInputStr(), new HttpRequestListener<FindGroupsResult>(){
            @Override
            public void onHttpRequestSucceed(FindGroupsResult result, OriginalCall call) {
                hideProgressDialog();
                boolean isDataEmpty=true;
                if(result.isSuccess() ){
                    List<FindGroupsResult.DataBean.ContentBean> dataList=result.getData().getContent();
                    if(dataList!=null && !dataList.isEmpty()){
                        isDataEmpty=false;
                        viewEmpty.setVisibility(View.INVISIBLE);
                        mSearchGroupList.clear();
                        mSearchGroupList.addAll(dataList);
                        mAdapterGroup.notifyDataSetChanged();
                    }
                }
                if(isDataEmpty){
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

    private void httpCheckInGroup(final FindGroupsResult.DataBean.ContentBean contentBean){
        showProgressDialog("");
        User user=Global.getCurrentUser();
        final  Context context=SearchUserListActivity.this;

        final Group group=contentBean.toGroupB();
        HttpServiceManagerV2.checkInGroup(user.account, group.getId(), new HttpRequestListener<CheckInGroupResult>() {
            @Override
            public void onHttpRequestSucceed(CheckInGroupResult result, OriginalCall call) {
                hideProgressDialog();
                if(result.isSuccess()){
                    if(result.isData()){
                        showToastView(getResources().getString(R.string.apply_join_group_error_tips));
                    }else {
                        ApplyGroupActivityV2.navToAct(context,group);
                    }
                }else {
                    ApplyGroupActivityV2.navToAct(context,group);
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
                ApplyGroupActivityV2.navToAct(context,group);
            }
        });

    }
    private void httpCheckFriend(final FindPersonsResult.DataBean.ContentBean contentBean){
        showProgressDialog("");
        User user=Global.getCurrentUser();
        final  Context context=SearchUserListActivity.this;
        HttpServiceManagerV2.checkFriend(user.account, contentBean.getAccount(), new HttpRequestListener<CheckInGroupResult>() {
            @Override
            public void onHttpRequestSucceed(CheckInGroupResult result, OriginalCall call) {
                hideProgressDialog();
                if(result.isSuccess()){
                    if(result.isData()){
                        showToastView(getResources().getString(R.string.add_friend_success_tips));
                    }else {
                        navToApplyFriendAct(contentBean);
                    }
                }else {
                    navToApplyFriendAct(contentBean);
                }
            }
            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                hideProgressDialog();
                navToApplyFriendAct(contentBean);
            }
        });

    }
}
