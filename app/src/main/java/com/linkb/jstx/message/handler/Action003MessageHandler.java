
package com.linkb.jstx.message.handler;

import android.content.Context;
import android.text.TextUtils;

import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.database.MessageRepository;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.message.extra.MessageExtra;

import java.util.List;


public class Action003MessageHandler extends Action000MessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        long groupId = Long.parseLong(message.getSender());

        //判断用户是否屏蔽该群消息
        if (ClientConfig.isIgnoredGroupMessage(groupId)) {
            MessageRepository.updateStatus(message.getId(), com.linkb.jstx.model.Message.STATUS_READ);
            ClientConfig.addNewGroupMessageCount(groupId);
        }
        if (!TextUtils.isEmpty(message.getExtra())){
            if (message.getExtra().contains(MessageExtra.TYPE_AT)){
                List<String> accounts = MessageExtra.getAtAccountList(message.getExtra());
                if (accounts.contains(Global.getCurrentAccount())){
                    ClientConfig.saveHasAtMeGroupMessage(groupId);
                    ClientConfig.saveAtMeGroupMessageId(message.getId());
                }
            }
        }
        if (GroupRepository.queryById(message.getSender()) == null) {
            MessageRepository.deleteById(message.getId());
            return false;
        }
        beforehandLoadFiles(message);
        return true;
    }

}
