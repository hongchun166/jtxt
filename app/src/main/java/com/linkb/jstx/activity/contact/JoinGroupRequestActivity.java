
package com.linkb.jstx.activity.contact;

import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.message.builder.Action102Builder;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.SendMessageResult;
import com.linkb.R;

/**
 * 请求入群
 */
public class JoinGroupRequestActivity extends BaseActivity implements HttpRequestListener<SendMessageResult> {


    private Group group;
    private Message message = new Message();

    @Override
    public void initComponents() {

        group = (Group) this.getIntent().getExtras().getSerializable("group");
    }

    @Override
    public void onClick(View v) {
        sendAllyRequest();
    }


    private void sendAllyRequest() {
        //验证请求消息
        String token = ((EditText) findViewById(R.id.token)).getText().toString();
        User source = Global.getCurrentUser();
        message.action = Constant.MessageAction.ACTION_102;
        //接收者为 群创建者account
        message.receiver = group.founder;
        message.content = new Action102Builder().buildJsonString(source, group, token);
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_handle)));
        HttpServiceManager.sendOnly(message,this);

    }


    @Override
    public void onHttpRequestSucceed(SendMessageResult result, OriginalCall call) {
        hideProgressDialog();
        showToastView(R.string.tip_send_request_complete);
        finish();
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
        hideProgressDialog();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_ally_request;
    }


    @Override
    public int getToolbarTitle() {
        return R.string.title_contacts_verify;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.single_button, menu);
        Button button = menu.findItem(R.id.menu_button).getActionView().findViewById(R.id.button);
        button.setOnClickListener(this);
        return super.onCreateOptionsMenu(menu);
    }
}
