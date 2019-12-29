
package com.linkb.jstx.activity.contact;


import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.chat.MicroServerWindowActivity;
import com.linkb.jstx.adapter.MicroServerListViewAdapter;
import com.linkb.jstx.component.GlobalEmptyView;
import com.linkb.jstx.database.MicroServerRepository;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.R;

public class MicroServerListActivity extends BaseActivity implements OnItemClickedListener {

    private MicroServerListViewAdapter adapter;
    private GlobalEmptyView emptyView;

    @Override
    public void initComponents() {


        RecyclerView listview = findViewById(R.id.recyclerView);
        listview.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MicroServerListViewAdapter();
        listview.setAdapter(adapter);
        adapter.setOnItemClickedListener(this);

        emptyView = findViewById(R.id.emptyView);

        emptyView.setTips(R.string.tips_no_microserver);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_contacts_public;
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.addAll(MicroServerRepository.queryList());
        emptyView.toggle(adapter);
    }


    @Override
    public void onItemClicked(Object obj, View view) {
        Intent intent = new Intent(this, MicroServerWindowActivity.class);
        intent.putExtra(MicroServer.NAME, (MicroServer) obj);
        this.startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_menu_look);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            startActivity(new Intent(this, LookMicroServerActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
