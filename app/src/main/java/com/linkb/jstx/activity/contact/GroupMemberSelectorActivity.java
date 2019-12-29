
package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.linkb.jstx.app.Global;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.GroupMemberRepository;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.MessageSource;
import com.linkb.R;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.util.AppTools;

import java.util.List;

public class GroupMemberSelectorActivity extends ContactSelectorActivity implements SearchView.OnQueryTextListener {

    private List<String> memberList;
    @Override
    public void initComponents() {
        Long groupId = getIntent().getLongExtra("groupId",0L);
        memberList = GroupMemberRepository.queryMemberAccountList(groupId);
        super.initCommonComponents();
        HttpServiceManager.getFriendsList(mListener);
    }

    @Override
    protected boolean isCharSelectorEnable() {
        return true;
    }

    @Override
    public boolean isSingleMode() {
        return true;
    }

//    @Override
//    public List<Friend>  loadContactsList() {
//        memberList.remove(Global.getCurrentAccount());
//        return FriendRepository.queryFriendList(memberList,true);
//    }

    @Override
    public void onConfirmMenuClicked() {

    }

    @Override
    public void onContactClicked(MessageSource source) {

    }

    @Override
    public boolean onContactSelected(MessageSource source) {
        Intent intent = new Intent();
        intent.putExtra(Friend.NAME,source);
        setResult(RESULT_OK,intent);
        finish();
        return true;
    }

//    @Override
//    protected void onKeywordChanged(String keyword){
//        if (!TextUtils.isEmpty(keyword)) {
//            List<Friend> tempList = FriendRepository.queryLikeList(memberList,true,keyword);
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
        return R.string.title_select_at_member;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchViewMenu = menu.findItem(R.id.menu_search_view);
        SearchView searchView  = (SearchView) searchViewMenu.getActionView();
        AppTools.setCursorDrawable(searchView.findViewById(R.id.search_src_text),R.drawable.white_edit_cursor);
        searchView.setQueryHint(getString(R.string.common_search));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    public int getContentLayout() {
        return R.layout.activity_member_single_selecotr;
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
//        onKeywordChanged(s);
        return false;
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
