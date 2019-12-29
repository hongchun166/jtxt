
package com.linkb.jstx.activity.contact;

import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;

import com.linkb.jstx.message.builder.Action105Builder;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.GroupMemberRepository;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.R;
import com.linkb.jstx.network.result.FriendListResult;

import java.util.ArrayList;
import java.util.List;

public class InviteGroupMemberActivity extends ContactSelectorActivity implements HttpRequestListener<BaseResult> {

    private Group group;
    private List<String> memberList;

    @Override
    public void initComponents() {
        group = (Group) this.getIntent().getSerializableExtra("group");
        memberList = GroupMemberRepository.queryMemberAccountList(group.id);
        super.initComponents();

        HttpServiceManager.getFriendsList(mListener);
    }
//
//    @Override
//    public List<Friend>  loadContactsList() {
//        HttpServiceManager.getFriendsList(this);
//
//
//        return FriendRepository.queryFriendList(memberList,false);
//    }

    @Override
    public void onConfirmMenuClicked() {

        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        String account = TextUtils.join(",", getAccountList().toArray());
        String content = new Action105Builder().buildJsonString(Global.getCurrentUser(), group);
        HttpServiceManager.inviteGroupMember(account,content, group.id, this);
    }


    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        hideProgressDialog();
        if (result.isSuccess() && call.equals(URLConstant.GROUP_MEMBER_INVITE_URL)) {
            showToastView(R.string.tip_group_invite_complete);
            finish();
        }else {
            showToastView(result.message);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }


    private ArrayList<String> getAccountList() {
        ArrayList<String> list = new ArrayList<>(adapter.getSelectedList().size());
        for (FriendListResult.FriendShip friend : adapter.getSelectedList()) {
            list.add(friend.getFriendAccount());
        }
        return list;
    }

    @Override
    public boolean onContactSelected(MessageSource source) {
        super.onContactSelected(source);
        button.setText(getString(R.string.label_group_invite_count, adapter.getSelectedList().size()));
        return true;
    }

    @Override
    public void onContactCanceled(MessageSource source) {
        super.onContactCanceled(source);
        button.setText(getString(R.string.label_group_invite_count, adapter.getSelectedList().size()));
    }

//    @Override
//    protected void onKeywordChanged(String keyword){
//        if (!TextUtils.isEmpty(keyword)) {
//            List<Friend> tempList = FriendRepository.queryLikeList(memberList,false,keyword);
//            if (tempList.isEmpty()) {
//                emptyView.setVisibility(View.VISIBLE);
//            } else {
//                emptyView.setVisibility(View.GONE);
//                adapter.notifyDataSetChanged(tempList);
//            }
//        } else {
//            emptyView.setVisibility(View.GONE);
//            adapter.notifyDataSetChanged(loadContactsList());
//        }
//    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_group_invite;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean flag = super.onCreateOptionsMenu(menu);
        button.setText(getString(R.string.label_group_invite_count, 0));
        button.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        return flag;
    }

    private HttpRequestListener<FriendListResult> mListener = new HttpRequestListener<FriendListResult>() {
        @Override
        public void onHttpRequestSucceed(FriendListResult result, OriginalCall call) {
            if (result.isSuccess() && result.isNotEmpty()){
                if (result.getDataList().isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
                adapter.notifyDataSetChanged(result.getDataList());
            }
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };
}
