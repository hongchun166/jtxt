
package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.TagMemberAdapter;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.TagRepository;
import com.linkb.jstx.dialog.InputTagNameDialog;
import com.linkb.jstx.listener.OnInputCompleteClickListener;
import com.linkb.jstx.model.Tag;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.model.Friend;
import com.linkb.R;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.FriendListResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class TagDetailActivity extends BaseActivity implements OnInputCompleteClickListener {

    private List<String> addAccountList = new LinkedList<>();
    private List<String> delAccountList = new LinkedList<>();
    private Tag mTag;
    private TagMemberAdapter memberAdapter;
    private InputTagNameDialog tagNameDialog;

    @Override
    public void initComponents() {

        mTag = (Tag) this.getIntent().getSerializableExtra(Tag.class.getName());
        setToolbarTitle(mTag.name);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5, GridLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        memberAdapter = new TagMemberAdapter();
        recyclerView.setAdapter(memberAdapter);
        registerForContextMenu(recyclerView);

        tagNameDialog = new InputTagNameDialog(this);
        tagNameDialog.setOnInputCompleteClickListener(this);

        loadMemberList();
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tag_detailed, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.bar_menu_modify){
            tagNameDialog.setName(mTag.name);
            tagNameDialog.show();
        }
        if (item.getItemId() == R.id.bar_menu_delete){
            HttpServiceManager.deleteTag(mTag.id);
            TagRepository.delete(mTag.id);

            Intent intent = new Intent();
            intent.putExtra("remove",mTag);
            setResult(RESULT_OK,intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {

        if (!addAccountList.isEmpty()){
            mTag.addAccountList(addAccountList);
            HttpServiceManager.addTagMember(mTag.id,addAccountList);
        }
        if (!delAccountList.isEmpty()){
            mTag.delAccountList(delAccountList);
            HttpServiceManager.removeTagMember(mTag.id,delAccountList);
        }
        TagRepository.update(mTag);
        Intent intent = new Intent();
        intent.putExtra("modify",mTag);
        setResult(RESULT_OK,intent);
        finish();
    }



    // 添加上下文菜单
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(1, Integer.MAX_VALUE, 1, getString(R.string.common_delete));
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    // 响应上下文菜单
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId() == Integer.MAX_VALUE) {
            memberAdapter.removeSelected();
            delAccountList.add(memberAdapter.getTarget().getFriendAccount());
            addAccountList.remove(memberAdapter.getTarget().getFriendAccount());
        }
        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ContactSelectorActivity.RESULT_CODE && resultCode == RESULT_OK) {
            ArrayList<FriendListResult.FriendShip> dataList = (ArrayList<FriendListResult.FriendShip>) data.getSerializableExtra("data");
            memberAdapter.addAll(dataList);
            for (FriendListResult.FriendShip friend:dataList){
                addAccountList.add(friend.getFriendAccount());
                delAccountList.remove(friend.getFriendAccount());
            }
        }
    }

    private void loadMemberList(){
        if (mTag.getAccountCount() == 0){
            return;
        }
//        List<Friend> memberList = FriendRepository.queryFriendList(mTag.getAccountList(),true);
        HttpServiceManager.getFriendsList(mListener);

    }

    @Override
    public void onInputCompleted(String text) {
        HttpServiceManager.updateTag(mTag.id,text);
        mTag.name =  text;
        setToolbarTitle(text);
        TagRepository.update(mTag);
    }

    private HttpRequestListener<FriendListResult> mListener = new HttpRequestListener<FriendListResult>() {
        @Override
        public void onHttpRequestSucceed(FriendListResult result, OriginalCall call) {
            if (result.isSuccess() && result.isNotEmpty()){
                if (result.getDataList().isEmpty()) {
                    memberAdapter.addAll(result.getDataList());
                }
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

}
