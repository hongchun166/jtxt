
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.message.builder.Action102Builder;
import com.google.gson.Gson;

import java.util.List;


public class Action102MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        Action102Builder builder = new Gson().fromJson(message.getContent(), Action102Builder.class);
        long groupId = builder.id;
        List<com.linkb.jstx.model.Message> list = MessageRepository.queryMessageByAction(Constant.MessageAction.ACTION_102);
        for (com.linkb.jstx.model.Message msg : list) {
            if (groupId == (new Gson().fromJson(msg.content, Action102Builder.class).id)
                    &&  msg.id !=(message.getId())) {
                MessageRepository.deleteById(msg.id);
            }
        }
        MessageRepository.updateSender(message.getId(), Constant.SYSTEM);
        return true;
    }

}
