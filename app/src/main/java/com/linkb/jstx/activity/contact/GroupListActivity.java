
package com.linkb.jstx.activity.contact;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.adapter.GroupListViewAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.model.Group;
import com.linkb.R;

import java.util.Objects;

public class GroupListActivity extends BaseActivity {
    private GlobalEmptyView emptyView;
    private GroupListViewAdapter adapter;
    private GroupChangeReceiver groupChangeReceiver;
    @Override
    public void initComponents() {
        RecyclerView recyclerView = findViewById(R.id.groupList);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroupListViewAdapter();
        recyclerView.setAdapter(adapter);
        emptyView = findViewById(R.id.emptyView);
        emptyView.setTips(R.string.tips_empty_group);

        String account = Global.getCurrentAccount();
        adapter.clearAll();
        adapter.addAll(GroupRepository.queryCreatedList(account));
        adapter.addAll(GroupRepository.queryJoinList(account));
        emptyView.toggle(adapter);

        groupChangeReceiver = new GroupChangeReceiver();
        LvxinApplication.registerLocalReceiver(groupChangeReceiver,groupChangeReceiver.getIntentFilter());

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        LvxinApplication.unregisterLocalReceiver(groupChangeReceiver);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_grouplist;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_mygroups;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(getDrawable(R.mipmap.add_icon));
        return super.onCreateOptionsMenu(menu);


//        getMenuInflater().inflate(R.menu.single_button, menu);
//        Button button = menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
//        button.setBackground(null);
//        button.setTextColor(getResources().getColor(R.color.tex_color_white));
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(view.getContext(), CreateGroupActivity.class));
//            }
//        });
//        button.setText(R.string.common_create);
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            startActivity(new Intent(this, CreateGroupActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    public class GroupChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Group group = (Group) intent.getSerializableExtra(Group.NAME);
            if (Objects.equals(intent.getAction(), Constant.Action.ACTION_GROUP_DELETE)){
                adapter.remove(group);
            }
            if (Objects.equals(intent.getAction(),Constant.Action.ACTION_GROUP_ADD)){
                adapter.add(group);
            }
            if (Objects.equals(intent.getAction(),Constant.Action.ACTION_GROUP_UPDATE)){
                adapter.update(group);
            }
            emptyView.toggle(adapter);
        }
        IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_GROUP_DELETE);
            filter.addAction(Constant.Action.ACTION_GROUP_ADD);
            filter.addAction(Constant.Action.ACTION_GROUP_UPDATE);
            return filter;
        }

    }

}
