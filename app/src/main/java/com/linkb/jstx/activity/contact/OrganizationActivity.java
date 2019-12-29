
package com.linkb.jstx.activity.contact;


import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.OrganizationListAdapter;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.OrganizationRepository;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.model.Organization;

import java.util.ArrayList;
import java.util.List;

public class OrganizationActivity extends BaseActivity implements OnItemClickedListener {

    private OrganizationListAdapter adapter;
    private List<MessageSource> dataList = new ArrayList<>();
    private Organization current;

    @Override
    public void initComponents() {
        RecyclerView listView = findViewById(R.id.recyclerView);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setLayoutManager(new LinearLayoutManager(this));
        findViewById(R.id.backbutton).setOnClickListener(this);
        adapter = new OrganizationListAdapter(this);
        listView.setAdapter(adapter);
        dataList.addAll(OrganizationRepository.queryRootList());
        dataList.addAll(FriendRepository.queryRootFriendList());
        adapter.addAll(dataList);
    }


    @Override
    public void onClick(View v) {
        finish();
    }

    private void goBack(Organization org) {
        if (current != null) {
            if (org.parentCode == null) {
                setTitle(R.string.label_org_list);
                dataList.clear();
                dataList.addAll(OrganizationRepository.queryRootList());
                dataList.addAll(FriendRepository.queryRootFriendList());
                adapter.addAll(dataList);
                current = null;
                return;
            }
            current = OrganizationRepository.queryOne(org.parentCode);
            if (current != null) {
                setToolbarTitle(current.getName());
                dataList.clear();
                dataList.addAll(OrganizationRepository.queryList(current.code));
                dataList.addAll(FriendRepository.queryFriendList(current.code));
                adapter.addAll(dataList);
            }
        }else {
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        goBack(current);
    }

    private void gotoEnter(Organization org) {
        setToolbarTitle(org.getName());
        dataList.clear();
        dataList.addAll(OrganizationRepository.queryList(org.code));
        dataList.addAll(FriendRepository.queryFriendList(org.code));
        adapter.addAll(dataList);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_organization;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_org_list;
    }


    @Override
    public void onItemClicked(Object obj, View view) {
        if (obj instanceof Organization) {
            current = (Organization) obj;
            gotoEnter((Organization) obj);

        } else {
            Intent intent = new Intent(this, PersonInfoActivity.class);
            intent.putExtra(Friend.class.getName(), (Friend) obj);
            startActivity(intent);
        }
    }
}
