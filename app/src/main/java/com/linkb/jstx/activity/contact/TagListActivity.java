
package com.linkb.jstx.activity.contact;


import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.linkb.jstx.dialog.InputTagNameDialog;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.TagListAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.database.TagRepository;
import com.linkb.jstx.listener.OnInputCompleteClickListener;
import com.linkb.jstx.model.Tag;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.CommonResult;
import com.linkb.R;


public class TagListActivity extends BaseActivity implements OnItemClickedListener<Tag>,OnInputCompleteClickListener, HttpRequestListener<CommonResult> {

    private TagListAdapter adapter;
    private InputTagNameDialog tagNameDialog;
    @Override
    public void initComponents() {
        RecyclerView listView = findViewById(R.id.recyclerView);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TagListAdapter(this);
        listView.setAdapter(adapter);
        adapter.addAll(TagRepository.queryList());
        tagNameDialog = new InputTagNameDialog(this);
        tagNameDialog.setOnInputCompleteClickListener(this);

        findViewById(R.id.emptyView).setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_menu_add);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            tagNameDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_tag_list;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Tag removeTag = (Tag) data.getSerializableExtra("remove");
            Tag modifyTag = (Tag) data.getSerializableExtra("modify");
            adapter.remove(removeTag);
            adapter.update(modifyTag);
            findViewById(R.id.emptyView).setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
        }
    }


    @Override
    public void onItemClicked(Tag tag, View view) {
        Intent intent = new Intent(this, TagDetailActivity.class);
        intent.putExtra(Tag.class.getName(),tag);
        startActivityForResult(intent,123);
    }

    @Override
    public void onInputCompleted(String text) {
        tagNameDialog.setTag(text);
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_save)));
        HttpServiceManager.createTag(text,this);
    }

    @Override
    public void onHttpRequestSucceed(CommonResult result, OriginalCall call) {
        Tag tag = new Tag();
        tag.id = result.data;
        tag.name = tagNameDialog.getTag().toString();
        tag.source = Global.getCurrentAccount();
        TagRepository.add(tag);
        adapter.add(tag);
        findViewById(R.id.emptyView).setVisibility(View.GONE);
        hideProgressDialog();
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }
}
