
package com.linkb.jstx.activity.contact;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.model.GroupMember;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.GroupMemberListResult;
import com.linkb.jstx.adapter.GroupMemberListViewAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.database.GroupMemberRepository;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.R;

public class GroupMemberListActivity extends BaseActivity implements OnDialogButtonClickListener, HttpRequestListener {

    private GroupMemberListViewAdapter adapter;
    private Group group;
    private User self;
    private CustomDialog customDialog;
    private Button button;
    private MenuItem mClearMenu;

    private boolean enableGroupManager = false;

    @Override
    public void initComponents() {
        group = (Group) this.getIntent().getSerializableExtra("group");

        RecyclerView memberListView = findViewById(R.id.recyclerView);
        memberListView.setLayoutManager(new LinearLayoutManager(this));
        memberListView.setItemAnimator(new DefaultItemAnimator());
        adapter = new GroupMemberListViewAdapter(this, group.founder, group.memberAble == 0);
        memberListView.setAdapter(adapter);
        adapter.addAll(group.memberList);
        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setIcon(R.drawable.icon_dialog_hint);
        customDialog.setMessage((R.string.tip_kickout_group));

        self = Global.getCurrentUser();

        enableGroupManager = checkManagerPermission();

        performLoadMembersRequest();
    }

    /** 判断是否是管理员
     * */
    private Boolean checkManagerPermission(){
        boolean result = false;
        for (GroupMember groupMember : group.memberList) {
            if (groupMember.account.equals(self.account) && groupMember.host .equals(GroupMember.RULE_MANAGER)){
                result = true;
            }
        }

        if (group.founder.equals(self.account) ){
            result  = true;
        }
        return result;
    }

    private void performLoadMembersRequest() {
        if (adapter.getItemCount() == 0) {
            HttpServiceManager.queryGroupMember(group.id,this);
        }
    }

    private void performRemoveMembersRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        HttpServiceManager.removeGroupMember(group.id,adapter.getSelected().account,this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (enableGroupManager) {
            getMenuInflater().inflate(R.menu.menu_mgr_member, menu);
            mClearMenu = menu.findItem(R.id.menu_clear);
            button = menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
            button.setOnClickListener(this);
            button.setText(R.string.common_mangment);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {

        if (adapter.getMangmentMode()) {
            adapter.setMangmentMode(false);
            button.setText(R.string.common_mangment);
        } else {
            adapter.setMangmentMode(true);
            button.setText(R.string.common_cancel);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_clear && adapter.getSelected() != null) {
            customDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


   @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        if (result instanceof GroupMemberListResult){
           GroupMemberRepository.saveAll(((GroupMemberListResult)result).dataList);
           adapter.addAll(GroupMemberRepository.queryMemberList(group.id));
           return;
        }

        if (call.equalsDelete(URLConstant.GROUP_MEMBER_BATCH_URL)) {
            hideProgressDialog();
            GroupMember member = adapter.getSelected();
            adapter.remove(member);
            button.setText(R.string.common_cancel);
            button.setBackgroundResource(R.drawable.toolbar_button);
            GroupMemberRepository.delete(group.id, member.account);
            showToastView(R.string.tip_group_kickout_complete);

            setResult(RESULT_OK, getIntent());
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }


    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {

        customDialog.dismiss();
        performRemoveMembersRequest();

    }

    @Override
    public int getContentLayout() {

        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {

        return R.string.label_group_members;
    }


    public void onMemberSelected() {
        mClearMenu.setVisible(true);
        button.setVisibility(View.GONE);
    }

    @Override
    public void finish() {

        super.finish();
    }
}
