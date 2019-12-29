
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.MessageRepository;
import com.farsunset.cim.sdk.android.model.Message;

//收到好友阅读消息
public class Action108MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        long mid = Long.parseLong(message.getContent());
        MessageRepository.updateStatus(mid, Constant.MessageStatus.STATUS_OTHERS_READ);
        MessageRepository.deleteById(message.getId());
        return true;
    }

}
