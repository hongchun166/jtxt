
package com.linkb.jstx.activity.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.dialog.MicroServerMenuWindow;
import com.linkb.jstx.listener.OnTouchDownListenter;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.activity.base.CIMMonitorActivity;
import com.linkb.jstx.activity.contact.MicroServerDetailedActivity;
import com.linkb.jstx.adapter.MicroServerWindowAdapter;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.ChatRecordListView;
import com.linkb.jstx.component.CustomSwipeRefreshLayout;
import com.linkb.jstx.component.MicroServerInputPanelView;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.database.MicroServerMenuRepository;
import com.linkb.jstx.database.MicroServerRepository;
import com.linkb.jstx.listener.OnInputPanelEventListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MicroServerMenu;
import com.linkb.jstx.network.MicroServerMenuHander;
import com.linkb.jstx.network.model.MicroServerTextMessage;
import com.linkb.R;
import com.linkb.jstx.util.MessageUtil;
import com.google.gson.Gson;

import java.util.List;
public class MicroServerWindowActivity extends CIMMonitorActivity implements OnInputPanelEventListener, OnTouchDownListenter, SwipeRefreshLayout.OnRefreshListener, MicroServerMenuWindow.OnMenuClickListener {
    private final String[] INCLUDE_TYPES = new String[]{Constant.MessageAction.ACTION_200, Constant.MessageAction.ACTION_201};
    private User self;
    private int currentPage = Constant.DEF_PAGE_INDEX;
    private MicroServerWindowAdapter adapter;
    private ChatRecordListView chatListView;
    private MicroServer microServer;
    private MicroServerInputPanelView windowInputPanelView;
    private View progressView;
    private CustomSwipeRefreshLayout swipeRefreshLayout;
    private MenuInvokedReceiver menuInvokedReceiver;

    @Override
    public void initComponents() {

        microServer = (MicroServer) this.getIntent().getSerializableExtra(MicroServer.NAME);
        setToolbarTitle(microServer.name);
        self = Global.getCurrentUser();
        chatListView = findViewById(R.id.chat_list);
        progressView = findViewById(R.id.menu_progress_view);
        chatListView.setOnTouchDownListenter(this);
        windowInputPanelView = findViewById(R.id.inputPanel);
        windowInputPanelView.buildMenus(MicroServerMenuRepository.queryPublicMenuList(microServer.account));
        windowInputPanelView.setOnMenuClickListener(this);
        windowInputPanelView.setOnInputPanelEventListener(this);
        self = Global.getCurrentUser();
        adapter = new MicroServerWindowAdapter(microServer);
        chatListView.setAdapter(adapter);
        loadChatRecord();

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        menuInvokedReceiver = new MenuInvokedReceiver();
        LvxinApplication.registerLocalReceiver(menuInvokedReceiver, menuInvokedReceiver.getIntentFilter());

    }


    @Override
    public void onResume() {
        super.onResume();
        microServer = MicroServerRepository.queryById(microServer.account);
        if (microServer == null) {
            this.finish();
        }
    }


    private void loadChatRecord() {
        List<Message> data = MessageRepository.queryMessage(microServer.account, INCLUDE_TYPES, currentPage);
        if (data != null && !data.isEmpty()) {
            adapter.addAllMessage(data);
            if (currentPage == Constant.DEF_PAGE_INDEX) {
                chatListView.scrollToBottom();
            } else {
                chatListView.scrollToPosition(data.size() - 1);
            }


        } else {
            currentPage = currentPage == Constant.DEF_PAGE_INDEX ? currentPage : currentPage - 1;
        }
    }

    @Override
    public void onMessageReceived(com.farsunset.cim.sdk.android.model.Message message) {
        Message msg = MessageUtil.transform(message);
        if (Constant.MessageAction.ACTION_201.equals(msg.action) && msg.sender.equals(microServer.account)) {
            adapter.addMessage(MessageUtil.transform(message));
            chatListView.scrollToBottom();
            if (adapter.getItemCount() == 1 && currentPage == 0) {
                currentPage = Constant.DEF_PAGE_INDEX;
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        LvxinApplication.unregisterLocalReceiver(menuInvokedReceiver);

        if (microServer==null){
            return;
        }
        ChatItem chatItem = new ChatItem(microServer,adapter.getLastMessage());
        Intent intent = new Intent();
        intent.setAction(chatItem.message == null ? Constant.Action.ACTION_RECENT_DELETE_CHAT : Constant.Action.ACTION_RECENT_REFRESH_CHAT);
        intent.putExtra(ChatItem.NAME, chatItem);
        LvxinApplication.sendLocalBroadcast(intent);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_microserver_window;
    }

    @Override
    public int getToolbarTitle() {
        return R.string.common_back;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_icon, menu);
        menu.findItem(R.id.menu_icon).setIcon(getMenuIcon());
        return super.onCreateOptionsMenu(menu);
    }


    private int getMenuIcon() {
        return R.drawable.icon_menu_microserver;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon) {
            Intent intent = new Intent();
            intent.setClass(this, MicroServerDetailedActivity.class);
            intent.putExtra(MicroServer.NAME, microServer);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onMenuClicked(MicroServerMenu menu) {
        if (menu.isWebMenu()) {
            Intent intent = new Intent(this, MMWebViewActivity.class);
            intent.setData(Uri.parse(menu.content));
            startActivity(intent);
        }
        if (menu.isApiMenu()) {
            progressView.setVisibility(View.VISIBLE);
            MicroServerMenuHander.execute(microServer, menu);
        }
        if (menu.isTextMenu()) {
            Message  lastMessage = new Message();
            lastMessage.content = menu.content;
            lastMessage.sender = microServer.account;
            lastMessage.receiver = self.account;
            lastMessage.format = Constant.MessageFormat.FORMAT_TEXT;
            lastMessage.action = Constant.MessageAction.ACTION_201;
            lastMessage.state = Message.STATUS_READ;
            lastMessage.id = System.currentTimeMillis();
            lastMessage.timestamp = System.currentTimeMillis();
            MicroServerTextMessage textMsg = new MicroServerTextMessage();
            textMsg.content = menu.content;
            lastMessage.content = (new Gson().toJson(textMsg));
            MessageRepository.add(lastMessage);
            adapter.addMessage(lastMessage);
            chatListView.scrollToBottom();
        }
    }


    @Override
    public void onSendButtonClicked(String content) {
        Message message = new Message();
        message.id = System.currentTimeMillis();
        message.content = content;
        message.sender = self.account;
        message.receiver = microServer.account;
        message.format = Constant.MessageFormat.FORMAT_TEXT;
        message.action = Constant.MessageAction.ACTION_200;
        message.timestamp = System.currentTimeMillis();
        message.state = Constant.MessageStatus.STATUS_NO_SEND;
        adapter.addMessage(message);
        chatListView.scrollToBottom();
        if (adapter.getItemCount() == 1 && currentPage == 0) {
            currentPage = Constant.DEF_PAGE_INDEX;
        }
    }

    @Override
    public void onMessageInsertAt() {

    }

    @Override
    public void onMessageTextDelete(String txt) {

    }

    @Override
    public void onKeyboardStateChanged(boolean visable) {

    }

    @Override
    public void onPanelStateChanged(boolean switchToPanel) {

    }

    @Override
    public void onTouchDown() {
        windowInputPanelView.resetInputPanel();
    }

    @Override
    public void onRefresh() {
        ++currentPage;
        loadChatRecord();
        swipeRefreshLayout.onRefreshCompleted();
    }


    public class MenuInvokedReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            progressView.setVisibility(View.GONE);
        }

        IntentFilter getIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(Constant.Action.ACTION_MICROSERVER_MENU_INVOKED);
            return filter;
        }
    }
}
