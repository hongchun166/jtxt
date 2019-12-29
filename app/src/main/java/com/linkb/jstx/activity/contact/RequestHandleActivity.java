
package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.base.BaseActivity;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.message.request.RequestHandler;
import com.linkb.jstx.message.request.RequestHandlerFactory;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;

/**
 * 处理请求好友 ， 请求 入群
 */
public class RequestHandleActivity extends BaseActivity {

    private RequestHandler messageHandler;

    private MessageSource source; //谁请求

    @Override
    public void initComponents() {


        findViewById(R.id.ignoreButton).setOnClickListener(this);
        findViewById(R.id.agreeButton).setOnClickListener(this);
        findViewById(R.id.refuseButton).setOnClickListener(this);

        Message message = (Message) this.getIntent().getExtras().getSerializable("message");
        messageHandler = RequestHandlerFactory.getFactory().getMessageHandler(message.action);
        messageHandler.initialized(this, message);
        source = messageHandler.decodeMessageSource();
        setToolbarTitle(messageHandler.getTitle());
        ((TextView) findViewById(R.id.messsage)).setText(messageHandler.getMessage());
        ((TextView) findViewById(R.id.description)).setText(messageHandler.getDescription());

        ((TextView) findViewById(R.id.name)).setText(source.getName());
        WebImageView icon = findViewById(R.id.icon);
        icon.load(source.getWebIcon(), source.getDefaultIconRID(), 999);
        icon.setOnClickListener(this);

        Intent intent = new Intent();
        intent.putExtra("id", message.id);
        setResult(RESULT_OK, intent);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.icon:
                if (source instanceof Friend) {
                    Intent intent = new Intent(this, PersonInfoActivity.class);
                    intent.putExtra(Friend.class.getName(), source);
                    startActivity(intent);
                }
                break;

            case R.id.agreeButton:
                messageHandler.handleAgree();
                break;

            case R.id.ignoreButton:
                messageHandler.handleIgnore();
                break;

            case R.id.refuseButton:
                messageHandler.handleRefuse();
                break;

        }
    }


    @Override
    public int getContentLayout() {

        return R.layout.activity_request_handle;
    }


}
