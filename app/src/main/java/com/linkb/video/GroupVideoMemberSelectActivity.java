package com.linkb.video;

import android.content.Intent;
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
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.GroupMember;
import com.linkb.video.adapter.GroupVideoMemberAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupVideoMemberSelectActivity extends BaseActivity implements GroupVideoMemberAdapter.GroupMemberSelectedListener, OnItemClickedListener<GroupMember> {

    public static final String SELECT_VIDEO_MEMBER = "select_video_member";

    @BindView(R.id.recyclerView1)
    RecyclerView selectedGroupManagerRecyclerView;
    @BindView(R.id.recyclerView2) RecyclerView groupMemberRecyclerView;
    @BindView(R.id.editText)
    EditText searchEditText;

    private GroupManagerSelectedAdapter mGroupManagerSelectedAdapter;
    private GroupVideoMemberAdapter mGroupMenberAdapter;

    /** 选中的人
     * */
    private List<GroupMember> selectedtGroupMemberList = new ArrayList<>();

    /**  非管理员和创始人员的群成员
     * */
    private List<GroupMember> commonMemberList = new ArrayList<>();

    private Group mGroup ;

    private User mSelf;


    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        mGroup = (Group) this.getIntent().getSerializableExtra("group");

        mSelf = Global.getCurrentUser();

        selectedGroupManagerRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        mGroupManagerSelectedAdapter = new GroupManagerSelectedAdapter();
        mGroupManagerSelectedAdapter.setOnItemClickedListener(this);
        selectedGroupManagerRecyclerView.setAdapter(mGroupManagerSelectedAdapter);

        groupMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mGroupMenberAdapter = new GroupVideoMemberAdapter();
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

    private void loadMemberList(){
        mGroupMenberAdapter.addAll(getManagerList());
    }

    /** 过滤自己
     * */
    private List<GroupMember> getManagerList(){
        commonMemberList.clear();
        for (GroupMember groupMember : mGroup.memberList) {
            if (!(groupMember.account).equals(mSelf.account )){
                commonMemberList.add(groupMember);
            }
        }
        return commonMemberList;
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

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_group_video_connect;
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

    }

    @OnClick(R.id.back_btn)
    public void onBack(){
        finish();
    }

    @OnClick(R.id.start_btn)
    public void onSelectComplete(){
        if (selectedtGroupMemberList.size() > 8){
            showToastView(getString(R.string.video_connect_selecte_limit_tips));
            return;
        }

        String accout = "";
        if (selectedtGroupMemberList.size() > 1){
            for (GroupMember groupMember : selectedtGroupMemberList) {
                accout += groupMember.account + ",";
            }
        }else if (selectedtGroupMemberList.size() > 0){
            accout = selectedtGroupMemberList.get(0).account;
        }

        Intent intent = getIntent();
        intent.putExtra(SELECT_VIDEO_MEMBER, accout);
        setResult(RESULT_OK, intent);
        finish();
    }
}
