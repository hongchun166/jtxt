
package com.linkb.jstx.activity.contact;


import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.activity.chat.MMWebViewActivity;
import com.linkb.jstx.activity.chat.MicroServerWindowActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.database.MicroServerMenuRepository;
import com.linkb.jstx.database.MicroServerRepository;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.result.MicroServerMenuListResult;
import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.model.MicroServerTextMessage;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

public class MicroServerDetailedActivity extends BaseActivity implements HttpRequestListener {

    private MicroServer microServer;
    private User self;

    @Override
    public void initComponents() {

        microServer = (MicroServer) this.getIntent().getSerializableExtra(MicroServer.NAME);
        self = Global.getCurrentUser();
        WebImageView icon = findViewById(R.id.icon);
        icon.setPopuShowAble();
        ((TextView) findViewById(R.id.microserver)).setText(getString(R.string.label_microserver, microServer.account));
        ((TextView) findViewById(R.id.name)).setText(microServer.name);
        ((TextView) findViewById(R.id.summary)).setText(microServer.description);
        icon.load(FileURLBuilder.getServerLogoUrl(microServer.account), microServer.getDefaultIconRID(), 999);
        findViewById(R.id.item_homepage).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.subscribeButton:
                performSubscribeRequest();
                break;
            case R.id.enterButton:
                Intent intent1 = new Intent(this, MicroServerWindowActivity.class);
                intent1.putExtra(MicroServer.NAME, microServer);
                startActivity(intent1);
                break;
            case R.id.item_homepage:
                if (!TextUtils.isEmpty(microServer.website)) {
                    MMWebViewActivity.createNavToParam(Uri.parse(microServer.website)).start(this);
                }
                break;
        }
    }



    private void onHttpRequestSucceed(MicroServerMenuListResult result) {
        hideProgressDialog();
        if (!TextUtils.isEmpty(microServer.greet)) {
            com.farsunset.cim.sdk.android.model.Message msg = new com.farsunset.cim.sdk.android.model.Message();
            msg.setTimestamp(System.currentTimeMillis());
            msg.setId(System.currentTimeMillis());
            msg.setAction(Constant.MessageAction.ACTION_201);
            msg.setSender(microServer.account);
            msg.setReceiver(self.account);
            msg.setFormat(Constant.MessageFormat.FORMAT_TEXT);
            MicroServerTextMessage textMsg = new MicroServerTextMessage();
            textMsg.content = microServer.greet;
            msg.setContent(new Gson().toJson(textMsg));

            Intent intent = new Intent(CIMConstant.IntentAction.ACTION_MESSAGE_RECEIVED);
            intent.putExtra(com.farsunset.cim.sdk.android.model.Message.class.getName(), msg);
            intent.putExtra(Constant.NEED_RECEIPT, false);
            LvxinApplication.sendGlobalBroadcast(intent);

        }

        Intent intent1 = new Intent(this, MicroServerWindowActivity.class);
        intent1.putExtra(MicroServer.NAME, microServer);
        startActivity(intent1);

        MicroServerRepository.add(microServer);
        MicroServerMenuRepository.savePublicMenus(result.dataList);

        finish();
    }

    @Override
    public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {

        if (result instanceof MicroServerMenuListResult){
            onHttpRequestSucceed((MicroServerMenuListResult) result);
            return;
        }

        if (result.isSuccess() && call.equalsGet(URLConstant.SUBSCRIBE_OPERATION_URL)) {
            HttpServiceManager.queryMicroServerMenuList(microServer.account,this);
        }

        if (result.isSuccess() && call.equalsDelete(URLConstant.SUBSCRIBE_OPERATION_URL)) {
            MessageRepository.deleteByActions(microServer.account, new String[]{Constant.MessageAction.ACTION_200, Constant.MessageAction.ACTION_201});
            MicroServerRepository.delete(microServer.account);
            MicroServerMenuRepository.deleteByAccount(microServer.account);

            Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            intent.putExtra(ChatItem.NAME, new ChatItem(microServer));
            LvxinApplication.sendLocalBroadcast(intent);
            hideProgressDialog();
            finish();
        }
    }


    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        if (call.equals(URLConstant.SUBSCRIBE_OPERATION_URL)
            ||call.equals(URLConstant.SUBSCRIBE_OPERATION_URL)
            ||call.equals(URLConstant.MICROSERVER_MENU_URL)){
            hideProgressDialog();
        }
    }

    @Override
    public int getContentLayout() {

        return R.layout.activity_microserver_detail;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        MicroServer target = MicroServerRepository.queryById(microServer.account);
        if (target == null) {
            findViewById(R.id.subscribeButton).setVisibility(View.VISIBLE);
            findViewById(R.id.subscribeButton).setOnClickListener(this);
            findViewById(R.id.enterButton).setVisibility(View.GONE);
            setToolbarTitle(microServer.name);
        } else {
            findViewById(R.id.subscribeButton).setVisibility(View.GONE);
            findViewById(R.id.enterButton).setOnClickListener(this);
            findViewById(R.id.enterButton).setVisibility(View.VISIBLE);
            getMenuInflater().inflate(R.menu.microserver_detailed, menu);
            setToolbarTitle(target.name);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.bar_menu_unsubscribe) {
            performUnsubscribeRequest();
        }

        return super.onOptionsItemSelected(item);

    }

    private void performUnsubscribeRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        HttpServiceManager.unsubscriber(microServer.account,this);
    }

    private void performSubscribeRequest() {
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        HttpServiceManager.subscriber(microServer.account,this);

    }


}
