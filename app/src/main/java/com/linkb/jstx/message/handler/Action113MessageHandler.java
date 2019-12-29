
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.model.GroupMember;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.database.GroupMemberRepository;
import com.linkb.jstx.message.builder.Action113Builder;
import com.google.gson.Gson;


public class Action113MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        Action113Builder builder = new Gson().fromJson(message.getContent(), Action113Builder.class);
//        GroupMember member = new GroupMember();
//        member.account = builder.account;
//        member.groupId = builder.id;
//        member.name = builder.name;
//        member.id = System.currentTimeMillis();
//        member.host = GroupMember.RULE_NORMAL;
//        GroupMemberRepository.delete(member.groupId, member.account);
//        GroupMemberRepository.saveMember(member);
//        MessageRepository.deleteById(message.getId());
        return true;
    }
}
