
package com.linkb.jstx.message.handler;


import android.content.Context;
import android.content.Intent;

import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.database.GlideImageRepository;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.util.FileURLBuilder;

public class Action116MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        MessageRepository.deleteById(message.getId());
        Group group = GroupRepository.queryById(message.getSender());
        if (group!=null){

            GroupRepository.update(group);
            GlideImageRepository.save(FileURLBuilder.getGroupIconUrl(group.id), String.valueOf(System.currentTimeMillis()));
            Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_LOGO);
            intent.putExtra(ChatItem.NAME, new ChatItem(group));
            LvxinApplication.sendLocalBroadcast(intent);

        }

        return false;

    }

}
