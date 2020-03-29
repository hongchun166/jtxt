package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.CIMMonitorActivity;
import com.linkb.jstx.activity.base.CIMMonitorActivityWithoutImmersion;
import com.linkb.jstx.adapter.ContactsSendCardsAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.comparator.FriendShipNameAscComparator;
import com.linkb.jstx.component.CharSelectorBar;
import com.linkb.jstx.database.StarMarkRepository;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.listener.OnTouchMoveCharListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.HttpServiceManagerV2;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.FriendListResultV2;
import com.linkb.jstx.util.CharacterParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/** 发送名片选择联系人
* */
public class SendCardsSelectContactActivity extends CIMMonitorActivity implements OnTouchMoveCharListener, View.OnClickListener, HttpRequestListener<FriendListResultV2>, OnItemClickedListener<Object> {

    public static final int SELECT_CONTACTS_CARDS_REQUEST = 0x1111;
    public static final String SELECT_CONTACTS_CARDS_REQUEST_KEY_ACCOUNT = "SELECT_CONTACTS_CARDS_REQUEST_KEY_ACCOUNT";
    public static final String SELECT_CONTACTS_CARDS_REQUEST_KEY_NAME = "SELECT_CONTACTS_CARDS_REQUEST_KEY_NAME";

    @BindView(R.id.contact_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private LinearLayoutManager layoutManager;
    private ContactsSendCardsAdapter adapter;

    private List<Object> mContentList = new ArrayList<>();
    private List<String> mStarMarkList = new ArrayList<>();
    private int mContactsSize;

    /** 如果是从好友对话框过来，需要选择的时候过滤掉好友
    * */
    private Friend mFriend;
    private List<FriendListResultV2.FriendShip> mFilterList = new ArrayList<>();

    @Override
    protected void initComponents() {
        mFriend = (Friend) getIntent().getSerializableExtra(Friend.class.getSimpleName());

        ButterKnife.bind(this);
        CharSelectorBar sideBar = (CharSelectorBar) findViewById(R.id.sidrbar);
        sideBar.setTextView((TextView) findViewById(R.id.dialog));
        sideBar.setOnTouchMoveCharListener(this);
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadFriendDate(false);
                HttpServiceManager.loadAllBaseData();
            }
        });

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter = new ContactsSendCardsAdapter(this, mStarMarkList, this));

        loadFriendDate(true);
    }

    /** 加载数据
     * */
    private void loadFriendDate(boolean showLoading) {
        if (showLoading ) showProgressDialog("");
        HttpServiceManagerV2.listMyFriendV2(Global.getCurrentUser().account,this);
        mStarMarkList.clear();
        mStarMarkList.addAll(StarMarkRepository.queryAccountList());
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_friend_list_select;
    }


    @Override
    public void onClick(View view) {
        
    }

    @Override
    public void onCharChanged(char s) {
        // 该字母首次出现的位置
        int position = adapter.getPositionForSection(s);
        if (position != -1 ) {
            layoutManager.scrollToPositionWithOffset(position,0);
            layoutManager.setStackFromEnd(true);
        }
    }

    @Override
    public void onHttpRequestSucceed(FriendListResultV2 result, OriginalCall call) {
        refreshLayout.finishRefresh();
        if (result.isSuccess() && result.isNotEmpty()){
            onFilterFriend(result.getDataList());
            mContactsSize = mFilterList.size();
            new GetPingYinTask().execute(mFilterList);
        }else {
//            showToastView(result.message);
        }
    }

    private void onFilterFriend(List<FriendListResultV2.FriendShip> filterList) {
        for (FriendListResultV2.FriendShip friend : filterList) {
            if (mFriend != null && mFriend.account.equals(friend.getFriendAccount())) {
                continue;
            }
            mFilterList.add(friend);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {

    }

    @OnClick(R.id.back_btn)
    public void onBack() {
        finish();
    }

    @Override
    public void onItemClicked(Object obj, View view) {
        if (obj instanceof FriendListResultV2.FriendShip){
            FriendListResultV2.FriendShip friendShip = (FriendListResultV2.FriendShip) obj;
            Intent intent = getIntent();
            intent.putExtra(SELECT_CONTACTS_CARDS_REQUEST_KEY_ACCOUNT, friendShip.getFriendAccount());
            intent.putExtra(SELECT_CONTACTS_CARDS_REQUEST_KEY_NAME, friendShip.getName());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private class GetPingYinTask extends AsyncTask<List<FriendListResultV2.FriendShip>, Void, List<Object>> {

        @Override
        protected List<Object> doInBackground(List<FriendListResultV2.FriendShip>... lists) {
            List<FriendListResultV2.FriendShip> topList = new ArrayList<>();
            List<Object> contentList = new ArrayList<>();
            for (FriendListResultV2.FriendShip friend : lists[0]) {
                // 汉字转换成拼音
                if (friend.getName() != null){
                    friend.setFristPinyin(CharacterParser.getFirstPinYinChar(friend.getName()));

                    if (mStarMarkList.contains(friend.getFriendAccount())) {
                        friend = friend.clone();
                        friend.setFristPinyin(String.valueOf(CharSelectorBar.STAR));
                        topList.add(friend);
                    }
                }
            }

            lists[0].addAll(topList);
            Collections.sort(lists[0], new FriendShipNameAscComparator());

            for (FriendListResultV2.FriendShip friend : lists[0]) {
                Character name = friend.getFristPinyin().charAt(0);
                if (!contentList.contains(name))
                {
                    contentList.add(name);
                }
                contentList.add(friend);
            }
            return contentList;
        }

        @Override
        protected void onPostExecute(List<Object> objects) {
            hideProgressDialog();
            adapter.notifyDataSetChanged(mContactsSize, objects);
        }
    }
}
