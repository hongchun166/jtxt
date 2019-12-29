package com.linkb.jstx.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.CIMMonitorFragment;
import com.linkb.jstx.adapter.ContactsListViewAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.comparator.FriendShipNameAscComparator;
import com.linkb.jstx.comparator.NameAscComparator;
import com.linkb.jstx.component.CharSelectorBar;
import com.linkb.jstx.database.StarMarkRepository;
import com.linkb.jstx.listener.OnTouchMoveCharListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.util.CharacterParser;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/** BLINK好友列表
* */
public class FriendListFragment extends CIMMonitorFragment implements OnTouchMoveCharListener , View.OnClickListener, HttpRequestListener<FriendListResult> {


    @BindView(R.id.contact_recyclerView) RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;
    private LinearLayoutManager layoutManager;
    private ContactsListViewAdapter adapter;
    private FriendChangedReceiver friendChangedReceiver;

    private List<Object> mContentList = new ArrayList<>();
    private List<String> mStarMarkList = new ArrayList<>();
    private int mContactsSize;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

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

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter = new ContactsListViewAdapter(getContext(), mStarMarkList));
//        adapter.notifyDataSetChanged(FriendRepository.queryFriendList());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        friendChangedReceiver = new FriendChangedReceiver();
        LvxinApplication.registerLocalReceiver(friendChangedReceiver, friendChangedReceiver.getIntentFilter());


    }
    @Override
    public void onDetach() {
        super.onDetach();
        LvxinApplication.unregisterLocalReceiver(friendChangedReceiver);
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

    public void onTabLongClicked() {
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void onHttpRequestSucceed(FriendListResult result, OriginalCall call) {
        refreshLayout.finishRefresh();
        if (result.isSuccess() && result.isNotEmpty()){
            mContactsSize = result.getDataList().size();
           new GetPingYingTask().execute(result.getDataList());
        }else {
            hideProgressDialog();
//            showToastView(result.message);
        }
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
        refreshLayout.finishRefresh();
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void requestData() {
        loadFriendDate(true);
        HttpServiceManager.loadAllBaseData();
    }

    /** 加载数据
    * */
    private void loadFriendDate(boolean showLoading) {
        if (showLoading ) showProgressDialog("");
        HttpServiceManager.getFriendsList(this);
        mStarMarkList.clear();
        mStarMarkList.addAll(StarMarkRepository.queryAccountList());
    }

    private class GetPingYingTask extends AsyncTask<List<FriendListResult.FriendShip>, Void, List<Object>> {

        @Override
        protected List<Object> doInBackground(List<FriendListResult.FriendShip>... lists) {
            List<FriendListResult.FriendShip> topList = new ArrayList<>();
            List<Object> contentList = new ArrayList<>();
            for (FriendListResult.FriendShip friend : lists[0]) {
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

            for (FriendListResult.FriendShip friend : lists[0]) {
                Character name = friend.getFristPinyin().charAt(0);
                if (!contentList.contains(name))
                {
                    contentList.add(name);
                }
                contentList.add(friend);
            }
            FriendListResult.FriendShip friendShip=new FriendListResult.FriendShip();
            friendShip.setName("新好友");
            friendShip.setId("-123");
            contentList.add(0,friendShip);
            return contentList;
        }

        @Override
        protected void onPostExecute(List<Object> objects) {
            hideProgressDialog();
            adapter.notifyDataSetChanged(mContactsSize, objects);
        }
    }

    public class FriendChangedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadFriendDate(false);

//            adapter.notifyDataSetChanged(FriendRepository.queryFriendList());
//            /**
//             *如果用户是首次登录进来，先获取通讯录之后再拉取离线消息
//             */
            if (!Global.getAlreadyLogin(Global.getCurrentAccount()))
            {
                HttpServiceManager.queryOfflineMessage();
                Global.saveAlreadyLogin(Global.getCurrentAccount());
            }
        }

        IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_RELOAD_CONTACTS);
            return filter;
        }

    }
}
