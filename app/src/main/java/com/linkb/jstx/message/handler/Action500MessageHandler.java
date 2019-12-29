
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.database.MessageRepository;


public class Action500MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        if (message.getSender().equals(Global.getCurrentAccount())) {
            MessageRepository.deleteById(message.getId());
            return false;
        }
        return true;
    }

}
