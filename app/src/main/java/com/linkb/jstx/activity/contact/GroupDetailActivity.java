
package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.CIMMonitorActivity;
import com.linkb.jstx.adapter.GroupMemberAdapter;
import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.GroupMemberRepository;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.GroupMember;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.GroupMemberListResult;
import com.linkb.jstx.network.result.QueryGroupInfoResult;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupDetailActivity extends CIMMonitorActivity implements OnDialogButtonClickListener,HttpRequestListener {

    @BindView(R.id.switch1) Switch ignoreMessageCheckbox;
    @BindView(R.id.hide_message_cly) View ignoreMessageView;

    @BindView(R.id.groupBannedCheckbox) Switch groupBannedSwitch;
    @BindView(R.id.groupBannedBar) View groupBannedView;

    @BindView(R.id.switch3) Switch groupSetUnableCheckInfoSwitch;
    @BindView(R.id.set_check_info_cly) View groupSetUnableCheckInfoView;

    @BindView(R.id.imageView19) ImageView qeCodeScaleImage;

    @BindView(R.id.group_name_text_view) TextView mGroupName;

    @BindView(R.id.imageView17) WebImageView groupLogo;

    @BindView(R.id.textView129) TextView mGroupProfile;

    @BindView(R.id.recyclerView3) RecyclerView groupMemberRecyclerView;
    @BindView(R.id.recyclerView2) RecyclerView groupManagerRecyclerView;

    @BindView(R.id.textView131) TextView mGroupManagerNumberTv;
    @BindView(R.id.textView133) TextView mGroupMemberNumberTv;

    @BindView(R.id.dismiss_group_btn) Button mDismissBtn;

    @BindView(R.id.modify_group_info_view) View modifyGroupInfoView;

    @BindView(R.id.imageView22) ImageView groupSummaryModifybtn;

    @BindView(R.id.textView135) TextView mGroupNotice;

    @BindView(R.id.group_id) TextView mGroupIdTv;

    private Group group;

    private CustomDialog customDialog;
    private User self;
    private GroupMemberAdapter memberAdapter;
    private GroupMemberAdapter managerMemberAdapter;
    private static final LinkedList<String> MEMBER_CHANGED_ACTION = new LinkedList();

    /** 管理员列表
    * */
    private List<GroupMember> groupManagerList = new ArrayList<>();

    private static int EDIT_GROUP_NOTICE_REQUEST = 0x11;
    private static int SET_GROUP_MANAGER = 0x12;
    private static int SET_GROUP_INFO = 123;
    private static int DELECT_GROUP_MANAGER_REQUEST = 0x13;
    private static int MODIFY_GROUP_SUMMARY = 0x14;
    private static int MODIFY_GROUP_INFO_REQUEST = 0x15;
    private static int MANAGER_GROUP_MEMBER_REQUEST = 0x16;

    static {
        MEMBER_CHANGED_ACTION.add(Constant.MessageAction.ACTION_112);
        MEMBER_CHANGED_ACTION.add(Constant.MessageAction.ACTION_113);
    }

    /** 二维码生成源字符串
    * */
    private String mGroupQrCodeStr;

    private boolean enableGroupManager = false;

    /** 屏蔽群消息功能过滤
    * */
    private boolean ignoreFliter = false;
    /** 全员禁言功能过滤
     * */
    private boolean bannedFliter = false;
    /** 不可互加好友功能过滤
     * */
    private boolean unCheckInfoFliter = false;

    @Override
    public void initComponents() {
        ButterKnife.bind(this);

        group = (Group) this.getIntent().getSerializableExtra("group");
        self = Global.getCurrentUser();

        enableGroupManager = checkManagerPermission();

        mDismissBtn.setText(group.founder.equals(self.account) ? R.string.label_group_dismiss : R.string.common_quit);
        changeGroupPermissionUI(enableGroupManager);
        initDismissDialog(group.founder.equals(self.account));

        if (group!= null)mGroupIdTv.setText(String.valueOf(group.id));

        groupBannedSwitch.setChecked(group.banned == 1);
        groupBannedSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!bannedFliter){
                    setGroupBanned(b ? 1 : 0);
                }else {
                    bannedFliter = false;
                }
            }
        });
        groupSetUnableCheckInfoSwitch.setChecked(group.memberAble == 1);
        groupSetUnableCheckInfoSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!unCheckInfoFliter){
                    setUnableCkeckInfo(b ? 1 : 0);
                }else {
                    unCheckInfoFliter = false;
                }
            }
        });

        groupLogo.load(FileURLBuilder.getGroupIconUrl(group.id), R.drawable.logo_group_normal, 999);
        mGroupName.setText(group.name);

        mGroupNotice.setText(TextUtils.isEmpty(group.summary) ? getResources().getString(R.string.group_summary_default) : group.summary);

        mGroupProfile.setText(TextUtils.isEmpty(group.category) ? getResources().getString(R.string.label_group_profile, getResources().getString(R.string.label_group_summary_default)) :
                getResources().getString(R.string.label_group_profile, group.category));

        ignoreMessageCheckbox.setChecked(ClientConfig.isIgnoredGroupMessage(group.id));
        ignoreMessageCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!ignoreFliter)ClientConfig.saveIgnoredGroupMessage(group.id, b);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false);
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 6, GridLayoutManager.VERTICAL, false);
        groupManagerRecyclerView.setNestedScrollingEnabled(false);
        groupMemberRecyclerView.setNestedScrollingEnabled(false);
        groupManagerRecyclerView.setLayoutManager(gridLayoutManager2);
        groupMemberRecyclerView.setLayoutManager(gridLayoutManager);
        groupManagerRecyclerView.setItemAnimator(new DefaultItemAnimator());
        groupMemberRecyclerView.setItemAnimator(new DefaultItemAnimator());
        managerMemberAdapter = new GroupMemberAdapter(this, group.memberAble == 0, mInviteGroupManagerListener);
        memberAdapter = new GroupMemberAdapter(this, group.memberAble == 0, mInviteJoinGroupListener);
        groupManagerRecyclerView.setAdapter(managerMemberAdapter);
        groupMemberRecyclerView.setAdapter(memberAdapter);

        loadMemberList();

        qeCodeScaleImage.setColorFilter(Color.GRAY, PorterDuff.Mode.LIGHTEN);

    }

    private View.OnClickListener mInviteJoinGroupListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            Intent inviteIntent = new Intent(GroupDetailActivity.this, InviteGroupMemberActivity.class);
            inviteIntent.putExtra("group", group);
            startActivity(inviteIntent);
        }
    };

    private  View.OnClickListener mInviteGroupManagerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (group.founder.equals(self.account)){
                Intent inviteIntent = new Intent(GroupDetailActivity.this, GroupManagerSelectActivity.class);
                inviteIntent.putExtra("group", group);
                startActivityForResult(inviteIntent, SET_GROUP_MANAGER);
            }else {
                showToastView(getResources().getString(R.string.label_set_group_manager_tips));
            }
        }
    };

    private void changeGroupPermissionUI(boolean enableGroupManager){
        groupBannedView.setVisibility(enableGroupManager ? View.VISIBLE : View.GONE);
        groupSetUnableCheckInfoView.setVisibility(enableGroupManager ? View.VISIBLE : View.GONE);
        modifyGroupInfoView.setVisibility(enableGroupManager ? View.VISIBLE : View.GONE);
        groupSummaryModifybtn.setVisibility(enableGroupManager ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (GroupRepository.queryById(group.id) != null) {
            getMenuInflater().inflate(R.menu.group_detailed, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }


    /** 弹出框初始化
    * */
    private void initDismissDialog(boolean enableManager) {
        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);

        if (group.founder != null){
            if (enableManager) {
                customDialog.setIcon(R.drawable.icon_dialog_destory);
                customDialog.setMessage((R.string.tip_dissolve_group));
            } else {
                customDialog.setIcon(R.drawable.icon_dialog_exit);
                customDialog.setMessage((R.string.tip_quit_group));
            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.bar_menu_delete){
            customDialog.show();
        }

        if (item.getItemId() == R.id.bar_menu_invite){
            Intent inviteIntent = new Intent(this, InviteGroupMemberActivity.class);
            inviteIntent.putExtra("group", group);
            startActivity(inviteIntent);
        }

        if (item.getItemId() == R.id.bar_menu_modify_name){
            Intent intent = new Intent(this, ModifyGroupActivity.class);
            intent.putExtra(Group.class.getName(), group);
            intent.setAction("name");
            startActivityForResult(intent,SET_GROUP_INFO);
        }

        if (item.getItemId() == R.id.bar_menu_modify_summary){
            Intent intent = new Intent(this, ModifyGroupActivity.class);
            intent.putExtra(Group.class.getName(), group);
            intent.setAction("summary");
            startActivityForResult(intent,SET_GROUP_INFO);
        }

        if (item.getItemId() == R.id.bar_menu_modify_logo){
            Intent intent = new Intent(this, ModifyGroupActivity.class);
            intent.putExtra(Group.class.getName(), group);
            intent.setAction("logo");
            startActivityForResult(intent,SET_GROUP_INFO);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {
        if (MEMBER_CHANGED_ACTION.contains(message.getAction()) && memberAdapter.getItemCount() < Constant.MAX_GRID_MEMBER) {
            loadMemberList();
        }
    }

    private void loadMemberList(){
//        List<GroupMember> memberList = GroupMemberRepository.queryMemberList(group.id);
        getManagerList();
        mGroupManagerNumberTv.setText(getResources().getString(R.string.label_group_manager_number, groupManagerList.size()));
        memberAdapter.addAll(group.memberList);
        managerMemberAdapter.addAll(groupManagerList);
        mGroupMemberNumberTv.setText(getString(R.string.label_group_manager_number, group.memberList.size()));
    }

    private void gotoMemberListActivity(){
        Intent memberIntent = new Intent(this, GroupMemberListActivity.class);
        memberIntent.putExtra("group", group);
        startActivityForResult(memberIntent, MANAGER_GROUP_MEMBER_REQUEST);
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

    private List<GroupMember> getManagerList(){
        groupManagerList.clear();
        for (GroupMember groupMember : group.memberList) {
            if (groupMember.host.equals(GroupMember.RULE_FOUNDER )|| groupMember.host .equals(GroupMember.RULE_MANAGER)){
                groupManagerList.add(groupMember);
            }
        }
        return groupManagerList;
    }


    @OnClick(R.id.imageView19)
    public void gotoQrCode(){
        Intent intent = new Intent(this, GroupQrCodeActivity.class);
        intent.putExtra("qrcode", Constant.QrCodeFormater.GROUP_QR_CODE + Constant.QrCodeFormater.QR_CODE_SPLIT + String.valueOf(group.id));
        startActivity(intent);
    }

    @OnClick(R.id.textView133)
    public void checkGroupMenber() {
        gotoMemberListActivity();
    }

    @OnClick(R.id.back_btn)
    public void onBack() {finish();}

    @OnClick(R.id.textView131)
    public void onDeleteGroupManager(){
        if (group.founder.equals(self.account)){
            if (groupManagerList.size() > 1){
                Intent memberIntent = new Intent(this, DeleteGroupManagerActivity.class);
                memberIntent.putExtra("group", group);
                startActivityForResult(memberIntent, DELECT_GROUP_MANAGER_REQUEST);
            }else {
                showToastView(getResources().getString(R.string.label_group_manager_empty_tips));
            }
        }
    }

    @OnClick(R.id.imageView22)
    public void setmGroupSummary(){
        Intent memberIntent = new Intent(this, GroupSummaryEditActivity.class);
        memberIntent.putExtra("group", group);
        startActivityForResult(memberIntent, MODIFY_GROUP_SUMMARY);
    }

    @OnClick(R.id.dismiss_group_btn)
    public void onExitGroup(){
        customDialog.show();
    }

    @OnClick(R.id.modify_group_info_view)
    public void onModifyGroupInfo(){
        Intent memberIntent = new Intent(this, GroupInfoEditActivity.class);
        memberIntent.putExtra("group", group);
        startActivityForResult(memberIntent, MODIFY_GROUP_INFO_REQUEST);
    }

    private void joinGroup(){
        Intent intent = new Intent(this, JoinGroupRequestActivity.class);
        intent.putExtra("group", group);
        startActivity(intent);
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        customDialog.dismiss();
        if (group.founder.equals(self.account)) {
            performDisdandRequest();
        } else {
            performQuitGroupRequest();
        }
    }

    private void performDisdandRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_dissolve)));
        HttpServiceManager.disbandGroup(group.id, this);
    }

    private void performQuitGroupRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_quit)));
        HttpServiceManager.quitGroup(group.id, this);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_group_detail;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_group_detail;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK && requestCode == SET_GROUP_INFO){
            group = (Group) data.getSerializableExtra(Group.class.getName());
            groupLogo.load(FileURLBuilder.getGroupIconUrl(group.id), R.drawable.logo_group_normal, 999);
            mGroupName.setText(group.name);
            ((TextView) findViewById(R.id.summary)).setText(group.summary);

            Intent changentent = new Intent(Constant.Action.ACTION_GROUP_UPDATE);
            changentent.putExtra(Group.NAME, group);
            LvxinApplication.sendLocalBroadcast(changentent);
        }

        if (resultCode == RESULT_OK &&  requestCode == SET_GROUP_MANAGER){
            getGroupInfo();
        }

        if (resultCode == RESULT_OK && requestCode == DELECT_GROUP_MANAGER_REQUEST){
            getGroupInfo();
        }

        if (resultCode == RESULT_OK && requestCode == MODIFY_GROUP_SUMMARY){
            group = (Group) data.getSerializableExtra(Group.class.getName());
            mGroupNotice.setText(TextUtils.isEmpty(group.summary) ? getResources().getString(R.string.group_summary_default) : group.summary);
        }

        if (resultCode == RESULT_OK && requestCode == MODIFY_GROUP_INFO_REQUEST){
            group = (Group) data.getSerializableExtra(Group.class.getName());
            mGroupProfile.setText(TextUtils.isEmpty(group.category) ? getResources().getString(R.string.label_group_profile, getResources().getString(R.string.label_group_summary_default)) :
                    getResources().getString(R.string.label_group_profile, group.category));

            groupLogo.load(FileURLBuilder.getGroupIconUrl(group.id), R.drawable.logo_group_normal, 999);
            mGroupName.setText(group.name);

            Intent changentent = new Intent(Constant.Action.ACTION_GROUP_UPDATE);
            changentent.putExtra(Group.NAME, group);
            LvxinApplication.sendLocalBroadcast(changentent);
        }

        if (resultCode == RESULT_OK && requestCode == MANAGER_GROUP_MEMBER_REQUEST){
            getGroupInfo();
        }
    }


    /** 获取群资料
     * */
    private void getGroupInfo() {
        HttpServiceManager.queryGroupInfo(group.id, new HttpRequestListener<QueryGroupInfoResult>() {
            @Override
            public void onHttpRequestSucceed(QueryGroupInfoResult result, OriginalCall call) {
                if (result.isSuccess()){
                    if (GroupRepository.queryById(group.id) != null){
                        GroupRepository.update(result.getData());
                    }else {
                        GroupRepository.add(result.getData());
                    }
                    group = result.getData();
                    loadMemberList();

                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        if (result instanceof GroupMemberListResult){
            GroupMemberRepository.saveAll(((GroupMemberListResult)result).dataList);
            memberAdapter.addAll(group.memberList);
            return;
        }
        if (result.isSuccess() && (call.equals(URLConstant.GROUP_DISBAND_URL) || call.equalsDelete(URLConstant.GROUP_QUIT_URL))) {
            hideProgressDialog();
            if (call.equalsDelete(URLConstant.GROUP_DISBAND_URL)) {
                showToastView(R.string.tip_group_dissolve_complete);
            } else {
                showToastView(R.string.tip_group_quit_complete);
            }

            GroupRepository.deleteById(group.id);
            GroupMemberRepository.delete(group.id, self.account);
            MessageRepository.deleteBySenderOrReceiver(group.id);

            Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            intent.putExtra(ChatItem.NAME, new ChatItem(group));
            LvxinApplication.sendLocalBroadcast(intent);

            Intent deleteIntent = new Intent(Constant.Action.ACTION_GROUP_DELETE);
            deleteIntent.putExtra(Group.NAME, group);
            LvxinApplication.sendLocalBroadcast(deleteIntent);

            finish();
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }

    /**设置禁言
    * */
    private void setGroupBanned(int banned) {
        HttpServiceManager.setGroupBanned(group.id, banned, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {

                if (result.isSuccess()){
                    if (groupBannedSwitch.isChecked()){
                        groupBannedSwitch.setChecked(true);
                    }else {
                        groupBannedSwitch.setChecked(false);
                    }
                }else {
                    bannedFliter = true;
                    if (groupBannedSwitch.isChecked()){
                        groupBannedSwitch.setChecked(false);
                    }else {
                        groupBannedSwitch.setChecked(true);
                    }
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                bannedFliter = true;
                if (groupBannedSwitch.isChecked()){
                    groupBannedSwitch.setChecked(false);
                }else {
                    groupBannedSwitch.setChecked(true);
                }
            }
        });
    }

    /** 设置不可查看资料，不可互加好友
    * */
    private void setUnableCkeckInfo(final int unableCkeckInfo){
        HttpServiceManager.setGroupCheckMenberInfo(group.id, unableCkeckInfo, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
                if (result.isSuccess()){
                    group.setMemberAble(unableCkeckInfo);
                    GroupRepository.update(group);

                    Intent bannedIntent = new Intent(Constant.Action.ACTION_GROUP_UNABLE_CHECK_INFO);
                    if (groupSetUnableCheckInfoSwitch.isChecked()){
                        groupSetUnableCheckInfoSwitch.setChecked(true);
                        memberAdapter.setmEnableCheckMenberInfo(false);
                        bannedIntent.putExtra("enableCheckInfo", false);
                    }else {
                        groupSetUnableCheckInfoSwitch.setChecked(false);
                        memberAdapter.setmEnableCheckMenberInfo(true);
                        bannedIntent.putExtra("enableCheckInfo", true);
                    }

                    LvxinApplication.sendLocalBroadcast(bannedIntent);
                }else {
                    unCheckInfoFliter = true;
                    if (groupSetUnableCheckInfoSwitch.isChecked()){
                        groupSetUnableCheckInfoSwitch.setChecked(false);
                    }else {
                        groupSetUnableCheckInfoSwitch.setChecked(true);
                    }
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                unCheckInfoFliter = true;
                if (groupSetUnableCheckInfoSwitch.isChecked()){
                    groupSetUnableCheckInfoSwitch.setChecked(false);
                }else {
                    groupSetUnableCheckInfoSwitch.setChecked(true);
                }
            }
        });
    }

}
