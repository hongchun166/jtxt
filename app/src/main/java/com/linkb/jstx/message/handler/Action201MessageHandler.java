
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.network.model.MicroServerLinkMessage;
import com.linkb.jstx.network.model.MicroServerTextMessage;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.network.model.MomentLink;
import com.google.gson.Gson;


public class Action201MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        try {
            if (Constant.MessageFormat.FORMAT_LINK.equals(message.getFormat())) {
                new Gson().fromJson(message.getContent(), MomentLink.class);
            }
            if (Constant.MessageFormat.FORMAT_LINKLIST.equals(message.getFormat())) {
                new Gson().fromJson(message.getContent(), MicroServerLinkMessage.class);
            }
            if (Constant.MessageFormat.FORMAT_TEXT.equals(message.getFormat())) {
                new Gson().fromJson(message.getContent(), MicroServerTextMessage.class);
            }
        } catch (Exception e) {
            MessageRepository.deleteById(message.getId());
            return false;
        }

        return true;
    }

}
