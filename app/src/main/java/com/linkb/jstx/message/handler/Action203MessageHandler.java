
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.database.MicroServerRepository;
import com.linkb.jstx.model.MicroServer;
import com.farsunset.cim.sdk.android.model.Message;
import com.google.gson.Gson;


public class Action203MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        MicroServer microServer = new Gson().fromJson(message.getContent(), MicroServer.class);
        MicroServerRepository.delete(microServer.account);
        MicroServerRepository.add(microServer);
        MessageRepository.deleteById(message.getId());
        return false;
    }

}
