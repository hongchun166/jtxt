
package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.adapter.RencentContactsAdapter;
import com.linkb.jstx.adapter.SelectedMessageSourceAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.RightPaddingDecoration;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.database.MicroServerRepository;
import com.linkb.jstx.dialog.SharedMessageDialog;
import com.linkb.jstx.listener.OnContactHandleListener;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.listener.OnItemClickedListener;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.model.Receiver;
import com.linkb.jstx.network.result.MessageForwardResult;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class MessageForwardActivity extends BaseActivity implements OnContactHandleListener, TextWatcher, OnDialogButtonClickListener, HttpRequestListener<MessageForwardResult>,OnItemClickedListener<MessageSource> {

    private static final String[] INCLUDED_MESSAGE_TYPES = new String[]{Constant.MessageAction.ACTION_0, Constant.MessageAction.ACTION_1, Constant.MessageAction.ACTION_3, Constant.MessageAction.ACTION_GrpRedPack};
    private RencentContactsAdapter adapter;
    private User self;
    SharedMessageDialog sharedDialog;
    Message message;
    private ArrayList<MessageSource> reciverList = new ArrayList<>();
    private ListView memberListView;
    private Button button;
    private List<MessageSource> tempList = new ArrayList<>();
    private View emptyView;
    private View headerView;

    private RecyclerView selectedListView;
    private SelectedMessageSourceAdapter selectedAdapter;

    @Override
    public void initComponents() {

        self = Global.getCurrentUser();


        message = getMessage();
        message.sender = self.account;

        memberListView = findViewById(R.id.listView);
        headerView = LayoutInflater.from(this).inflate(R.layout.layout_recent_contacts_header, null);
        memberListView.addHeaderView(headerView, null, false);
        adapter = new RencentContactsAdapter(this);
        memberListView.setAdapter(adapter);
        findViewById(R.id.bar_create_chat).setOnClickListener(this);
        adapter.addAll(MessageRepository.getRecentContacts(INCLUDED_MESSAGE_TYPES));
        adapter.setOnContactHandleListener(this);
        EditText keyword = findViewById(R.id.keyword);
        keyword.addTextChangedListener(this);
        emptyView = findViewById(R.id.emptyView);
        emptyView.setOnClickListener(this);

        selectedListView = findViewById(R.id.selectedListView);
        selectedListView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        selectedListView.addItemDecoration(new RightPaddingDecoration(AppTools.dip2px(5)));
        selectedListView.setItemAnimator(new DefaultItemAnimator());
        selectedListView.setAdapter(selectedAdapter = new SelectedMessageSourceAdapter());
        selectedAdapter.setOnItemClickedListener(this);


        sharedDialog = new SharedMessageDialog(this);
        sharedDialog.setOnDialogButtonClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == button) {
            if (button.getTag().equals(R.string.common_send)) {
                reciverList.clear();
                reciverList.addAll(adapter.getSelectedList());
                sharedDialog.setMessage(message);
                sharedDialog.show(adapter.getSelectedList());
            } else if (button.getTag().equals(R.string.common_multiselect)) {
                adapter.setMultiSelect();
                button.setTag(R.string.common_singleselect);
                button.setText(R.string.common_singleselect);
            } else {
                adapter.setSinglSelect();
                button.setTag(R.string.common_multiselect);
                button.setText(R.string.common_multiselect);
            }
        }
        if (v.getId() == R.id.icon) {
            MessageSource target = (MessageSource) v.getTag(R.id.target);
            AppTools.setCheckBoxState(memberListView.findViewWithTag(target),false);
            adapter.getSelectedList().remove(target);
            onContactCanceled((MessageSource) v.getTag(R.id.target));
        }
        if (v.getId() == R.id.bar_create_chat) {
            startActivityForResult(new Intent(this, ContactSelectorActivity.class), ContactSelectorActivity.RESULT_CODE);
        }
    }

    @Override
    public void onHttpRequestSucceed(MessageForwardResult result, OriginalCall call) {
        hideProgressDialog();
        int count = result.dataList.size();
        for (int i = 0; i < count; i++) {
            message.receiver = reciverList.get(i).getId();
            message.id = result.dataList.get(i);
            message.action = reciverList.get(i).getSourceType();
            message.timestamp = System.currentTimeMillis();
            message.state = Constant.MessageStatus.STATUS_SEND;
            MessageRepository.add(message);

            ChatItem chatItem = new ChatItem(reciverList.get(i),message);
            Intent intent = new Intent(Constant.Action.ACTION_RECENT_APPEND_CHAT);
            intent.putExtra(ChatItem.NAME, chatItem);
            LvxinApplication.sendLocalBroadcast(intent);
        }
        showToastView(R.string.tip_message_forward_completed);
        finish();
    }


    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        if (call != null && call.equals(URLConstant.MESSAGE_FORWARD_URL)){
            hideProgressDialog();
        }
    }

    Message getMessage() {
        message = (Message) this.getIntent().getSerializableExtra(Message.NAME);
        return message;
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_message_forward;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.title_message_forward;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_button, menu);
        button = menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
        button.setOnClickListener(this);
        button.setText(R.string.common_multiselect);
        button.setTag(R.string.common_multiselect);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onContactClicked(MessageSource source) {

        reciverList.clear();
        reciverList.add(source);
        sharedDialog.setMessage(message);
        sharedDialog.show(source);
    }

    @Override
    public boolean onContactSelected(MessageSource source) {

        selectedAdapter.add(source);
        button.setEnabled(true);
        selectedListView.smoothScrollToPosition(selectedAdapter.getItemCount());

        button.setTag(R.string.common_send);
        button.setText(R.string.common_send);

        return true;
    }

    @Override
    public void onContactCanceled(MessageSource source) {

        selectedAdapter.remove(source);

        if (adapter.getSelectedList().isEmpty()) {
            button.setTag(R.string.common_singleselect);
            button.setText(R.string.common_singleselect);
        } else {
            button.setTag(R.string.common_send);
            button.setText(R.string.common_send);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        if (!TextUtils.isEmpty(text.toString().trim())) {
            tempList.clear();
            tempList.addAll(FriendRepository.queryLike(text.toString()));
            if (message.format.equals(Constant.MessageFormat.FORMAT_TEXT)) {
                tempList.addAll(MicroServerRepository.queryLike(text.toString()));
            }

            tempList.addAll(GroupRepository.queryLike(text.toString()));
            if (tempList.isEmpty()) {
                emptyView.setVisibility(View.VISIBLE);
            } else {
                emptyView.setVisibility(View.GONE);
                adapter.addAll(tempList, true);
                headerView.findViewById(R.id.bar_rencent_chat).setVisibility(View.GONE);
            }

        } else {
            headerView.findViewById(R.id.bar_rencent_chat).setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
            adapter.addAll(MessageRepository.getRecentContacts(INCLUDED_MESSAGE_TYPES));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ContactSelectorActivity.RESULT_CODE && resultCode == RESULT_OK) {

            ArrayList<Friend> dataList = (ArrayList<Friend>) data.getSerializableExtra("data");
            dataList.removeAll(adapter.getSelectedList());
            adapter.getSelectedList().addAll(dataList);
            adapter.notifyDataSetChanged();
            for (Friend friend : dataList) {
                onContactSelected(friend);
            }
        }
    }

    @Override
    public void onLeftButtonClicked() {
        sharedDialog.dismiss();
    }

    String getReceiver() {
        ArrayList<Receiver> list = new ArrayList<>(reciverList.size());
        for (MessageSource source : reciverList) {
            Receiver receiver = new Receiver();
            receiver.id = source.getId();
            receiver.type = source.getSourceType();
            list.add(receiver);
        }
        return new Gson().toJson(list);
    }
    @Override
    public void onRightButtonClicked() {
        sharedDialog.dismiss();
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_send)));
        message.receiver = getReceiver();
        HttpServiceManager.forwardText(message,this);
    }

    @Override
    public void onItemClicked(MessageSource source, View view) {

        AppTools.setCheckBoxState( memberListView.findViewWithTag(source),false);
        adapter.getSelectedList().remove(source);
        onContactCanceled(source);
    }
}
