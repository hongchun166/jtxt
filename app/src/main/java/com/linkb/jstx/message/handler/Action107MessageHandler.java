
package com.linkb.jstx.message.handler;

import android.content.Context;
import android.content.Intent;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.database.GroupMemberRepository;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.message.builder.Action107Builder;
import com.linkb.jstx.model.Group;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.database.GroupRepository;
import com.google.gson.Gson;


public class Action107MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        Action107Builder builder = new Gson().fromJson(message.getContent(), Action107Builder.class);

        String account = Global.getCurrentAccount();
        GroupMemberRepository.delete(builder.id, builder.accountList);
        if (builder.accountList.contains(account)) {
            GroupMemberRepository.delete(builder.id, account);
            GroupRepository.deleteById(builder.id);

            Intent intent = new Intent(Constant.Action.ACTION_RECENT_DELETE_CHAT);
            intent.putExtra(ChatItem.NAME, new ChatItem(new Group(builder.id)));
            LvxinApplication.sendLocalBroadcast(intent);

            MessageRepository.updateSender(message.getId(), Constant.SYSTEM);

            MessageRepository.deleteBySenderOrReceiver(String.valueOf(builder.id));
            return true;
        }

        MessageRepository.deleteById(message.getId());
        return false;
    }

}
