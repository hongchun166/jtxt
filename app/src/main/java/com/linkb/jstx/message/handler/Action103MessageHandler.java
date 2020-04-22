
package com.linkb.jstx.message.handler;

import android.content.Context;
import android.content.Intent;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.message.parser.MessageParserFactory;
import com.farsunset.cim.sdk.android.constant.CIMConstant;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.model.Group;
import com.linkb.R;


public class Action103MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        Group group  = (Group) MessageParserFactory.getFactory().parserMessageBody(message);
        GroupRepository.add(group);


        com.farsunset.cim.sdk.android.model.Message msg = new com.farsunset.cim.sdk.android.model.Message();
        msg.setTimestamp(System.currentTimeMillis());
        msg.setId(System.currentTimeMillis());
        msg.setAction(Constant.MessageAction.ACTION_3);
        msg.setSender(String.valueOf(group.id));
        msg.setReceiver(message.getReceiver());
        msg.setFormat(Constant.MessageFormat.FORMAT_TEXT);
        msg.setContent(context.getString(R.string.tip_group_hello_message));
        msg.setExtra(group.founder);

        Intent intent = new Intent(CIMConstant.IntentAction.ACTION_MESSAGE_RECEIVED);
        intent.putExtra(com.farsunset.cim.sdk.android.model.Message.class.getName(), msg);
        intent.putExtra(Constant.NEED_RECEIPT, true);
        LvxinApplication.sendGlobalBroadcastPackageName(intent);

        MessageRepository.updateSender(message.getId(), Constant.SYSTEM);

        return true;
    }

}
