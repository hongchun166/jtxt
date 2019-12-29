
package com.linkb.jstx.activity.chat;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.linkb.jstx.activity.base.CIMMonitorActivity;
import com.linkb.jstx.activity.contact.RequestHandleActivity;
import com.linkb.jstx.adapter.SystemMessageListAdapter;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.component.DashlineItemDivider;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.dialog.CustomDialog;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.SystemMessage;
import com.linkb.jstx.util.MessageUtil;
import com.linkb.R;

import java.util.List;

public class SystemMessageActivity extends CIMMonitorActivity implements OnDialogButtonClickListener {

    private RecyclerView messageListView;
    private SystemMessageListAdapter adapter;
    private CustomDialog customDialog;

    @Override
    public void initComponents() {

        getWindow().setBackgroundDrawableResource(android.R.color.white);
        messageListView = findViewById(R.id.recyclerView);
        messageListView.setLayoutManager(new LinearLayoutManager(this));
        messageListView.setItemAnimator(new DefaultItemAnimator());
        messageListView.addItemDecoration(new DashlineItemDivider());
        adapter = new SystemMessageListAdapter();
        messageListView.setAdapter(adapter);

        loadMessageRecord();

        customDialog = new CustomDialog(this);
        customDialog.setOnDialogButtonClickListener(this);
        customDialog.setIcon((R.drawable.icon_dialog_delete));
        customDialog.setMessage(getString(R.string.tip_delete_system_message));
        customDialog.setButtonsText(R.string.common_cancel, R.string.common_confirm);

        MessageRepository.batchReadMessage(SystemMessage.MESSAGE_ACTION_ARRAY);

    }


    private void loadMessageRecord() {

        List<Message> datalist = MessageRepository.queryIncludedSystemMessageList(SystemMessage.MESSAGE_ACTION_ARRAY);

        if (datalist != null && !datalist.isEmpty()) {
            adapter.addAllMessage(datalist);
            messageListView.smoothScrollToPosition(0);
        }

    }

    public void onHandleButtonClick(Message message) {
        Intent intent = new Intent(this, RequestHandleActivity.class);
        intent.putExtra("message", message);
        this.startActivityForResult(intent, 6789);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 6789) {
            Message target = MessageRepository.queryById(data.getLongExtra("id",0));
            adapter.onItemChanged(target);
        }

    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {
        Message msg = MessageUtil.transform(message);
        if (msg.sender.equals(Constant.SYSTEM) && !msg.isNoNeedShow()) {
            adapter.addMessage(msg);
            messageListView.smoothScrollToPosition(0);
            MessageRepository.updateStatus(msg.id, Message.STATUS_READ);
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_common_recyclerview;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_sysmessage;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            customDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(R.drawable.icon_menu_delete);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onLeftButtonClicked() {
        customDialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        MessageRepository.deleteIncludedSystemMessageList(SystemMessage.MESSAGE_ACTION_ARRAY);
        adapter.notifyDataSetChanged();
        customDialog.dismiss();
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
        intent.putExtra(ChatItem.NAME, new ChatItem(null, new SystemMessage(Constant.MessageAction.ACTION_2)));
        LvxinApplication.sendLocalBroadcast(intent);
        finish();
    }

    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        intent.putExtra(ChatItem.NAME, new ChatItem(adapter.getLastMessage(), new SystemMessage(Constant.MessageAction.ACTION_2)));
        LvxinApplication.sendLocalBroadcast(intent);
    }

}
