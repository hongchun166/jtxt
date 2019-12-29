
package com.linkb.jstx.message.handler;

import android.content.Context;
import android.content.Intent;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.GlideImageRepository;
import com.linkb.jstx.database.MicroServerRepository;
import com.linkb.jstx.model.MicroServer;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.util.FileURLBuilder;


public class Action205MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        MessageRepository.deleteById(message.getId());
        String account = message.getSender();
        GlideImageRepository.save(FileURLBuilder.getServerLogoUrl(account), String.valueOf(System.currentTimeMillis()));
        MicroServer source = MicroServerRepository.queryById(account);
        ChatItem chatItem = new ChatItem(source);
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_LOGO);
        intent.putExtra(ChatItem.NAME, chatItem);
        LvxinApplication.sendLocalBroadcast(intent);
        return false;
    }

}
