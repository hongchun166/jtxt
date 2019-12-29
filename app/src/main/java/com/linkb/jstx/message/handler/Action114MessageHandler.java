
package com.linkb.jstx.message.handler;


import android.content.Context;

import com.linkb.jstx.database.MessageRepository;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.model.Group;

public class Action114MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        MessageRepository.deleteById(message.getId());
        Group group = GroupRepository.queryById(message.getSender());
        if (group!=null){
            group.name = message.getContent();
            GroupRepository.update(group);
        }
        return false;

    }

}
