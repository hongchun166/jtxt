
package com.linkb.jstx.message.handler;


import android.content.Context;

import com.linkb.jstx.database.GlideImageRepository;
import com.linkb.jstx.database.MessageRepository;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.util.FileURLBuilder;

public class Action300MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        MessageRepository.deleteById(message.getId());
        String code = message.getSender();
        GlideImageRepository.save(FileURLBuilder.getAppIconUrl(code), String.valueOf(System.currentTimeMillis()));
        return false;
    }

}
