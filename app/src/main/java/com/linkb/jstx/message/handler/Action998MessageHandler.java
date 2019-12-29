
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.farsunset.cim.sdk.android.model.Message;

public class Action998MessageHandler implements CustomMessageHandler {

    @Override
    public boolean handle(Context context, Message message) {

//        HttpServiceManager.loadUserDatabase();
        MessageRepository.deleteById(message.getId());
        return false;
    }

}
