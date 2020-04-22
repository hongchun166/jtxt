
package com.linkb.jstx.network;

import android.content.Intent;
import android.text.TextUtils;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.model.MicroServerMenu;
import com.linkb.jstx.network.http.HttpRequestLauncher;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.model.MicroServerMenuEvent;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.MicroServerResponse;
import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.network.http.HttpRequestBody;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * 公众号菜单处理
 */
public class MicroServerMenuHander {


    public static void execute(final MicroServer account, com.linkb.jstx.model.Message message) {
        final MicroServerMenuEvent event = new MicroServerMenuEvent();
        event.eventType = MicroServerMenuEvent.EVENT_FORMAT_TEXT;
        event.text = message.content;
        event.account = Global.getCurrentUser().account;

        dispatcherEvent(account, event, message);
    }

    public static void execute(final MicroServer account, MicroServerMenu menu) {
        final MicroServerMenuEvent event = new MicroServerMenuEvent();
        event.eventType = MicroServerMenuEvent.EVENT_ACTION_MENU;
        event.account = Global.getCurrentAccount();
        event.menuCode = menu.code;

        dispatcherEvent(account, event, menu);
    }


    private static void dispatcherEvent(final MicroServer account, final MicroServerMenuEvent event, final Serializable target) {

        //如果没有配置API地址，直接返回
        if (TextUtils.isEmpty(account.url)) {
            performMenuApiCallback(Constant.MessageStatus.STATUS_SEND, target);
            return;
        }
        HttpRequestBody requestBody = new HttpRequestBody(account.url, MicroServerResponse.class);
        requestBody.setContentType(HttpRequestBody.JSON_MEDIATYPE);
        requestBody.setContent(new Gson().toJson(event));
        requestBody.setRunWithOtherThread();
        HttpRequestLauncher.execute(requestBody, new HttpRequestListener() {
            @Override
            public void onHttpRequestSucceed(BaseResult result, OriginalCall call) {

                sendMessageBroadcast(event.account, account.account, (MicroServerResponse) result);
                performMenuApiCallback(Constant.MessageStatus.STATUS_SEND, target);
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {
                performMenuApiCallback(Constant.MessageStatus.STATUS_SEND_FAILURE, target);

            }
        });

    }

    private static void sendMessageBroadcast(String selfAccount, String pubAccount, MicroServerResponse apiResult) {
        final Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setReceiver(selfAccount);
        message.setSender(pubAccount);
        message.setAction(Constant.MessageAction.ACTION_201);
        message.setTimestamp(System.currentTimeMillis());
        message.setContent(apiResult.toString());
        message.setFormat(apiResult.contentType);

        Intent intent = new Intent(CIMConstant.IntentAction.ACTION_MESSAGE_RECEIVED);
        intent.putExtra(com.farsunset.cim.sdk.android.model.Message.class.getName(), message);
        intent.putExtra(Constant.NEED_RECEIPT, false);
        LvxinApplication.sendGlobalBroadcastPackageName(intent);

    }

    private static void performMenuApiCallback(String status, Object target) {
        if (target instanceof MicroServerMenu){
            LvxinApplication.sendLocalBroadcast(new Intent(Constant.Action.ACTION_MICROSERVER_MENU_INVOKED));
        }

        /**
         * 通知聊天窗口刷新状态
         */
        if (target instanceof com.linkb.jstx.model.Message){
            com.linkb.jstx.model.Message message = (com.linkb.jstx.model.Message) target;
            message.state = status;
            MessageRepository.updateStatus(message.id, message.state);

            Intent intent = new Intent(Constant.Action.ACTION_WINDOW_REFRESH_MESSAGE);
            intent.putExtra(ChatItem.NAME, new ChatItem(null,message));
            LvxinApplication.sendLocalBroadcast(intent);
        }

    }

}
