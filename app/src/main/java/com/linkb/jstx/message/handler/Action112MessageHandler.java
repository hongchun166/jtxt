
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.MessageRepository;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.database.GroupMemberRepository;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.message.builder.Action112Builder;
import com.linkb.jstx.model.Group;
import com.google.gson.Gson;


public class Action112MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        Action112Builder builder = new Gson().fromJson(message.getContent(), Action112Builder.class);
        String account = builder.account;
        long groupId = builder.id;
        GroupMemberRepository.delete(groupId, account);
        Group group = GroupRepository.queryById(groupId);
        if (group.founder.equals(Global.getCurrentAccount())) {
            MessageRepository.updateSender(message.getId(), Constant.SYSTEM);
            return true;
        }

        MessageRepository.deleteById(message.getId());
        return true;
    }

}
