
package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.ContactSelectorAdapter;
import com.linkb.jstx.adapter.SelectedMessageSourceAdapter;
import com.linkb.jstx.component.RightPaddingDecoration;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.listener.OnContactHandleListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.component.CharSelectorBar;
import com.linkb.jstx.listener.OnTouchMoveCharListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.MessageSource;
import com.linkb.R;

import java.util.ArrayList;
import java.util.List;

public class ContactSelectorActivity extends BaseActivity implements OnContactHandleListener, TextWatcher, OnTouchMoveCharListener, OnItemClickedListener<MessageSource> {

    public static final int RESULT_CODE = 8155;
    protected ContactSelectorAdapter adapter;
    protected Button button;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private EditText keyword;
    private RecyclerView selectedListView;
    private SelectedMessageSourceAdapter selectedAdapter;
    protected View emptyView;

    @Override
    public void initComponents() {

        initCommonComponents();

        selectedListView = findViewById(R.id.selectedListView);
        selectedListView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        selectedListView.addItemDecoration(new RightPaddingDecoration(AppTools.dip2px(5)));
        selectedListView.setItemAnimator(new DefaultItemAnimator());
        selectedListView.setAdapter(selectedAdapter = new SelectedMessageSourceAdapter());
        selectedAdapter.setOnItemClickedListener(this);

        keyword = findViewById(R.id.keyword);
        keyword.addTextChangedListener(this);

    }

    public void initCommonComponents() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager( layoutManager = new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter = new ContactSelectorAdapter());
        emptyView = findViewById(R.id.emptyView);
        emptyView.setOnClickListener(this);
        CharSelectorBar sideBar = findViewById(R.id.sidrbar);
        sideBar.setTextView((TextView) findViewById(R.id.dialog));
        sideBar.setOnTouchMoveCharListener(this);

        adapter.setCharVisable(isCharSelectorEnable());
        adapter.setSingleMode(isSingleMode());
        adapter.setOnContactHandleListener(this);
//        adapter.notifyDataSetChanged(loadContactsList());
    }
//    List<Friend> loadContactsList() {
//        if (getIntent().getStringArrayListExtra("excludeList") == null){
//            return FriendRepository.queryFriendList();
//        }
//        ArrayList<String> excludeList = getIntent().getStringArrayListExtra("excludeList");
//        return FriendRepository.queryFriendList(excludeList,false);
//    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            onConfirmMenuClicked();
        }
    }

    boolean isCharSelectorEnable() {
        return true;
    }

    boolean isSingleMode() {
        return false;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_contact_selecotr;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.title_choice_contact;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_button, menu);
        button = menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
        button.setOnClickListener(this);
        button.setText(R.string.common_confirm);
        button.setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onContactClicked(MessageSource source) {

    }

    @Override
    public boolean onContactSelected(MessageSource source) {

        selectedAdapter.add(source);
        button.setEnabled(true);
        selectedListView.smoothScrollToPosition(selectedAdapter.getItemCount());
        return true;
    }

    @Override
    public void onContactCanceled(MessageSource source) {

        selectedAdapter.remove(source);
        if (adapter.getSelectedList().isEmpty()) {
            button.setEnabled(false);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
//        onKeywordChanged(text.toString().trim());
    }

//    void onKeywordChanged(String keyword){
//        if (!TextUtils.isEmpty(keyword)) {
//            List<FriendListResult.FriendShip> tempList = FriendRepository.queryLike(keyword);
//            if (tempList.isEmpty()) {
//                emptyView.setVisibility(View.VISIBLE);
//            } else {
//                emptyView.setVisibility(View.GONE);
//                adapter.notifyDataSetChanged(tempList);
//            }
//        } else {
//            emptyView.setVisibility(View.GONE);
//            adapter.notifyDataSetChanged(FriendRepository.queryFriendList());
//        }
//    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void onCharChanged(char s) {
        // 该字母首次出现的位置
        int position = adapter.getPositionForSection(s);
        if (position != -1) {
            layoutManager.scrollToPositionWithOffset(position,0);
            layoutManager.setStackFromEnd(true);
        }
    }

    void onConfirmMenuClicked() {
        Intent intent = new Intent();
        intent.putExtra("data", adapter.getSelectedList());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onItemClicked(MessageSource source, View view) {
        AppTools.setCheckBoxState(recyclerView.findViewWithTag(source),false);
        adapter.getSelectedList().remove(source);
        onContactCanceled(source);
    }
}
