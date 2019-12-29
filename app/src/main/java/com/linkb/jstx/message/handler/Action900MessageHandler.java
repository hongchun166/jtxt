
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.MessageRepository;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.bean.User;


public class Action900MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        String account = message.getContent();
        FriendRepository.modifyOnlineStatus(account, User.OFF_LINE);
        MessageRepository.deleteById(message.getId());
        return false;
    }

}
