package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.GroupMenberSingleSelectAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.GroupMember;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** @群成员选择
* */
public class GroupMemberAtSelectActivity extends BaseActivity implements GroupMenberSingleSelectAdapter.GroupMemberSingleSelectedListener{
    @BindView(R.id.recyclerView2)
    RecyclerView groupMemberRecyclerView;
    @BindView(R.id.editText)
    EditText searchEditText;

    private GroupMenberSingleSelectAdapter mGroupMenberAdapter;

    /** 不包含本人的群员列表
    * */
    private List<GroupMember> groupMemberList = new ArrayList<>();

    private Group mGroup ;
    private User self;

    @Override
    protected void initComponents() {

        ButterKnife.bind(this);
        mGroup = (Group) this.getIntent().getSerializableExtra("group");
        self = Global.getCurrentUser();

        groupMemberRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mGroupMenberAdapter = new GroupMenberSingleSelectAdapter();
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

    /** 搜索群成员
     * */
    private void searchGroupMember(String searchKey){
        List<GroupMember> searchMemberList = new ArrayList<>();
        for (GroupMember groupMember : groupMemberList) {
            if (groupMember.name.contains(searchKey)){
                searchMemberList.add(groupMember);
            }
        }
        mGroupMenberAdapter.addAll(searchMemberList);
    }

    /** 过滤自己
     * */
    private List<GroupMember> getManagerList(){
        groupMemberList.clear();
        for (GroupMember groupMember : mGroup.memberList) {
            if (!groupMember.account.equals(self.account)){
                groupMemberList.add(groupMember);
            }
        }
        return groupMemberList;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_select_group_menber;
    }

    @Override
    public void onGroupMemberSelected(GroupMember groupMember) {
        Intent intent = getIntent();
        intent.putExtra(GroupMember.class.getName(), groupMember);
        setResult(RESULT_OK, intent);
        finish();
    }

    @OnClick(R.id.back_btn)
    public void onBack(){
        finish();
    }

}
