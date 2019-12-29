
package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.SearchResultContactsAdapter;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.database.MicroServerRepository;
import com.linkb.jstx.listener.OnContactHandleListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.GroupResult;
import com.linkb.jstx.network.result.MicroServerResult;
import com.linkb.jstx.util.AppTools;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements  OnClickListener, OnContactHandleListener, HttpRequestListener, SearchView.OnQueryTextListener {

    private SearchResultContactsAdapter resuleAdapter;
    private TextView labelGroup;
    private TextView labelMicroServer;
    private ListView memberListView;
    private List<MessageSource> contactsList = new ArrayList<>();
    private String keyword;
    @Override
    public void initComponents() {

        memberListView = findViewById(R.id.listView);
        View headerView = LayoutInflater.from(this).inflate(R.layout.layout_search_result_header, null);
        memberListView.addHeaderView(headerView);

        resuleAdapter = new SearchResultContactsAdapter(this);
        resuleAdapter.setOnContactHandleListener(this);
        memberListView.setAdapter(resuleAdapter);

        headerView.findViewById(R.id.item_group_id).setOnClickListener(this);
        headerView.findViewById(R.id.item_micro_server).setOnClickListener(this);

        labelGroup = findViewById(R.id.label_group_id);
        labelMicroServer = findViewById(R.id.label_micro_server);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchViewMenu = menu.findItem(R.id.menu_search_view);
        SearchView searchView  = (SearchView) searchViewMenu.getActionView();
        AppTools.setCursorDrawable(searchView.findViewById(R.id.search_src_text),R.drawable.white_edit_cursor);
        searchView.setQueryHint(getString(R.string.tip_search_hint));
        searchView.setIconified(false);
        searchView.setOnQueryTextListener(this);
        searchViewMenu.expandActionView();
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.item_group_id:
                performSearchGroupRequest();
                break;
            case R.id.item_micro_server:
                performSearchMicroServerRequest();
                break;
        }
    }

    private void performSearchGroupRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_query)));
        HttpServiceManager.searchGroup(keyword,this);
    }

    private void performSearchMicroServerRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_query)));
        HttpServiceManager.searchMicroServer(keyword,this);
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {
        if (result instanceof GroupResult){
            onHttpRequestSucceed((GroupResult)result);
        }
        if (result instanceof MicroServerResult){
            onHttpRequestSucceed((MicroServerResult)result);
        }
    }
    private void onHttpRequestSucceed(GroupResult result) {
        hideProgressDialog();
        if (result.data != null) {
            startContactsActivity(result.data);
            return;
        }
        showToastView(R.string.tip_search_noresult);
    }

    private void onHttpRequestSucceed(MicroServerResult result) {
        hideProgressDialog();
        if (result.data != null) {
            startContactsActivity(result.data);
            return;
        }
        showToastView(R.string.tip_search_noresult);
    }


    private void startContactsActivity(MessageSource source) {
        Intent intent = null;
        if (source instanceof Friend) {
            intent = new Intent(SearchActivity.this, PersonInfoActivity.class);
            intent.putExtra(Friend.class.getName(), source);
            startActivity(intent);
            return;
        }
        if (source instanceof Group) {
            intent = new Intent(SearchActivity.this, GroupDetailActivity.class);
            intent.putExtra("group", source);
            startActivity(intent);
            return;
        }
        if (source instanceof MicroServer) {
            intent = new Intent(SearchActivity.this, MicroServerDetailedActivity.class);
            intent.putExtra(MicroServer.NAME, source);
            startActivity(intent);
        }
    }



    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
        showToastView(R.string.tip_search_noresult);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_search;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_add;
    }

    @Override
    public void onContactClicked(MessageSource source) {
        startContactsActivity(source);
    }

    @Override
    public boolean onContactSelected(MessageSource source) {

        return true;
    }

    @Override
    public void onContactCanceled(MessageSource source) {

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        keyword = s.trim();
        if (TextUtils.isEmpty(keyword)) {
            memberListView.setVisibility(View.GONE);
        } else {

            memberListView.setVisibility(View.VISIBLE);
            SpannableStringBuilder text = new SpannableStringBuilder(getString(R.string.label_search_group, keyword));
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#45C01A")), 5, 5 + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            labelGroup.setText(text);

            text.clear();
            text.append(getString(R.string.label_search_microserver, keyword));
            text.setSpan(new ForegroundColorSpan(Color.parseColor("#45C01A")), 5, 5 + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            labelMicroServer.setText(text);

            contactsList.clear();
            contactsList.addAll(FriendRepository.queryLike(keyword));
            contactsList.addAll(MicroServerRepository.queryLike(keyword));
            contactsList.addAll(GroupRepository.queryLike(keyword));
            resuleAdapter.addAll(contactsList);
        }
        return false;
    }
}
