
package com.linkb.jstx.message.handler;

import android.content.Context;
import android.content.Intent;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.MessageRepository;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.message.builder.Action111Builder;
import com.linkb.jstx.model.Friend;
import com.google.gson.Gson;

public class Action111MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        MessageRepository.deleteById(message.getId());
        if (message.getSender().equals(message.getReceiver())) {
            return false;
        }

        Action111Builder builder = new Gson().fromJson(message.getContent(), Action111Builder.class);
        String account = builder.account;
        Friend friend = new Friend();
        friend.name = builder.name;
        friend.account = account;

        FriendRepository.modifyNameAndMotto(account, builder.name, builder.motto);

        ChatItem chatItem = new ChatItem(friend);
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_LOGO);
        intent.putExtra(ChatItem.NAME, chatItem);
        LvxinApplication.sendLocalBroadcast(intent);
        MessageRepository.deleteById(message.getId());
        return false;
    }

}
