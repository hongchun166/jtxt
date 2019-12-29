
package com.linkb.jstx.message.handler;

import android.content.Context;
import android.content.Intent;

import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.message.builder.Action104Builder;
import com.linkb.jstx.model.Group;
import com.google.gson.Gson;


public class Action104MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        Action104Builder builder = new Gson().fromJson(message.getContent(), Action104Builder.class);
        if (GroupRepository.queryById(builder.id) == null) {
            MessageRepository.deleteById(message.getId());
            return false;
        }
        //删除群消息记录，删除群列表
        GroupRepository.deleteById(builder.id);
        MessageRepository.deleteBySenderOrReceiver(builder.id);

        Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
        intent.putExtra(ChatItem.NAME, new ChatItem(new Group(builder.id)));
        LvxinApplication.sendLocalBroadcast(intent);

        MessageRepository.updateSender(message.getId(), Constant.SYSTEM);

        return true;
    }

}
