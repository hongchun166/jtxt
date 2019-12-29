
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.database.MicroServerMenuRepository;
import com.linkb.jstx.model.MicroServerMenu;
import com.farsunset.cim.sdk.android.model.Message;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;


public class Action204MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        List<MicroServerMenu> menuList = new Gson().fromJson(message.getContent(), new TypeToken<List<MicroServerMenu>>() {}.getType());
        MicroServerMenuRepository.deleteByAccount(message.getSender());
        MicroServerMenuRepository.savePublicMenus(menuList);
        MessageRepository.deleteById(message.getId());
        return false;
    }

}
