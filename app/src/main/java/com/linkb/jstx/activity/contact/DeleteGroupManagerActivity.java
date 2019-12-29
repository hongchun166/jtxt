package com.linkb.jstx.activity.contact;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.GroupMenberAdapter;
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

/** 删除群管理员
* */
public class DeleteGroupManagerActivity extends BaseActivity implements GroupMenberAdapter.GroupMemberSelectedListener, HttpRequestListener<BaseDataResult> {

    @BindView(R.id.recyclerView)
    RecyclerView groupManagerRecyclerView;

    @BindView(R.id.textView139)
    TextView groupManagerTotalNumberTv;

    private Group mGroup ;

    private GroupMenberAdapter mGroupManagerSelectedAdapter;

    /** 选中的人
     * */
    private List<GroupMember> selectedtGroupMemberList = new ArrayList<>();

    /**  群管理员
     * */
    private List<GroupMember> groupManagerList = new ArrayList<>();


    @Override
    protected void initComponents() {
        ButterKnife.bind(this);

        mGroup = (Group) this.getIntent().getSerializableExtra("group");
        groupManagerRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mGroupManagerSelectedAdapter = new GroupMenberAdapter();
        mGroupManagerSelectedAdapter.setGroupMemberSelectedListener(this);
        groupManagerRecyclerView.setAdapter(mGroupManagerSelectedAdapter);

        loadMemberList();
    }

    private void loadMemberList(){
        mGroupManagerSelectedAdapter.addAll(getManagerList());
    }

    /** 过滤管理员和创始人
     * */
    private List<GroupMember> getManagerList(){
        groupManagerList.clear();
        for (GroupMember groupMember : mGroup.memberList) {
            if (groupMember.host.equals(GroupMember.RULE_MANAGER )){
                groupManagerList.add(groupMember);
            }
        }
        groupManagerTotalNumberTv.setText(getResources().getString(R.string.label_group_manager_total_number, groupManagerList.size()));
        return groupManagerList;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_delect_group_manager;
    }

    @Override
    public void onGroupMemberSelected(GroupMember groupMember) {
        selectedtGroupMemberList.add(groupMember);
    }

    @Override
    public void onGroupMemberCancel(GroupMember groupMember) {
        selectedtGroupMemberList.remove(groupMember);
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
        HttpServiceManager.setGroupManager(mGroup.id, accout, "0", this) ;
    }

    @Override
    public void onHttpRequestSucceed(BaseDataResult result, OriginalCall call) {
        if (result.isSuccess()){
            showToastView(getResources().getString(R.string.label_delect_group_manager_success));
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
