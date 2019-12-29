package com.linkb.jstx.activity.contact;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.GroupManagerSelectedAdapter;
import com.linkb.jstx.adapter.GroupMenberAdapter;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.GroupMember;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseDataResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 群管理员选择页面
* */
public class GroupManagerSelectActivity extends BaseActivity implements GroupMenberAdapter.GroupMemberSelectedListener, OnItemClickedListener<GroupMember> , HttpRequestListener<BaseDataResult> {

    @BindView(R.id.recyclerView1) RecyclerView selectedGroupManagerRecyclerView;
    @BindView(R.id.recyclerView2) RecyclerView groupMemberRecyclerView;

    @BindView(R.id.editText) EditText searchEditText;

    private GroupManagerSelectedAdapter mGroupManagerSelectedAdapter;
    private GroupMenberAdapter mGroupMenberAdapter;

    /** 选中的人
    * */
    private List<GroupMember> selectedtGroupMemberList = new ArrayList<>();

    /**  非管理员和创始人员的群成员
    * */
    private List<GroupMember> commonMemberList = new ArrayList<>();

    private Group mGroup ;

    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        mGroup = (Group) this.getIntent().getSerializableExtra("group");

        selectedGroupManagerRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mGroupManagerSelectedAdapter = new GroupManagerSelectedAdapter();
        mGroupManagerSelectedAdapter.setOnItemClickedListener(this);
        selectedGroupManagerRecyclerView.setAdapter(mGroupManagerSelectedAdapter);
        
        groupMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mGroupMenberAdapter = new GroupMenberAdapter();
        mGroupMenberAdapter.setGroupMemberSelectedListener(this);
        groupMemberRecyclerView.setAdapter(mGroupMenberAdapter);
        

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!TextUtils.isEmpty(charSequence)){
                    searchGroupMember(charSequence.toString());
                }else {
                    loadMemberList();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        loadMemberList();
    }

    /** 搜索群成员
    * */
    private void searchGroupMember(String searchKey){
        List<GroupMember> searchMemberList = new ArrayList<>();
        for (GroupMember groupMember : commonMemberList) {
            if (groupMember.name.contains(searchKey)){
                searchMemberList.add(groupMember);
            }
        }
        mGroupMenberAdapter.addAll(searchMemberList);
    }

    private void loadMemberList(){
        mGroupMenberAdapter.addAll(getManagerList());
    }

    /** 过滤管理员和创始人
    * */
    private List<GroupMember> getManagerList(){
        commonMemberList.clear();
        for (GroupMember groupMember : mGroup.memberList) {
            if (groupMember.host.equals(GroupMember.RULE_NORMAL )){
                commonMemberList.add(groupMember);
            }
        }
        return commonMemberList;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_group_manager;
    }

    @OnClick(R.id.back_btn)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.confirm_btn)
    public void onConfirm(){
        String accout = "";
        for (GroupMember groupMember : selectedtGroupMemberList) {
            accout += groupMember.account + ",";
        }
        HttpServiceManager.setGroupManager(mGroup.id, accout, "2", this) ;
    }


    @Override
    public void onGroupMemberSelected(GroupMember groupMember) {
        selectedtGroupMemberList.add(groupMember);
        mGroupManagerSelectedAdapter.add(groupMember);
    }

    @Override
    public void onGroupMemberCancel(GroupMember groupMember) {
        selectedtGroupMemberList.remove(groupMember);
        mGroupManagerSelectedAdapter.remove(groupMember);
    }

    @Override
    public void onItemClicked(GroupMember obj, View view) {
        // TODO: 2019/4/3
    }

    @Override
    public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
        if (result.isSuccess()){
            showToastView(getResources().getString(R.string.label_set_group_manager_success));
            setResult(RESULT_OK, getIntent());
            finish();
        }else {
            showToastView(result.message);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }
}
