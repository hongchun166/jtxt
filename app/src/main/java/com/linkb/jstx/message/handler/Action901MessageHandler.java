
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.database.MessageRepository;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.FriendRepository;

public class Action901MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        String account = message.getContent();
        FriendRepository.modifyOnlineStatus(account, User.ON_LINE);
        MessageRepository.deleteById(message.getId());
        return false;
    }

}
