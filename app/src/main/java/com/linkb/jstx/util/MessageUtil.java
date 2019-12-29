
package com.linkb.jstx.util;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.model.Message;

public class MessageUtil {


    private MessageUtil() {
    }

    public static Message transform(com.farsunset.cim.sdk.android.model.Message msg) {
        Message m = new Message();
        m.content = msg.getContent();
        m.title = msg.getTitle();
        m.format = msg.getFormat();
        m.receiver = msg.getReceiver();
        m.sender = msg.getSender();
        m.action = msg.getAction();
        m.extra = msg.getExtra();
        m.id = msg.getId();
        m.timestamp = msg.getTimestamp();

        if (m.action == Constant.MessageAction.ACTION_113){
            m.state = Message.STATUS_NOT_READ;
        }else {
            m.state = Message.STATUS_NOT_READ;
        }

        return m;
    }

    public static com.farsunset.cim.sdk.android.model.Message transform(Message msg) {
        com.farsunset.cim.sdk.android.model.Message m = new com.farsunset.cim.sdk.android.model.Message();
        m.setContent(msg.content);
        m.setTitle(msg.title);
        m.setFormat(msg.format);
        m.setReceiver(msg.receiver);
        m.setSender(msg.sender);
        m.setAction(msg.action);
        m.setExtra(msg.extra);
        m.setId(msg.id);
        m.setTimestamp(msg.timestamp);
        return m;
    }

    public static Message clone(Message msg) {
        Message m = new Message();
        m.content = msg.content;
        m.title = msg.title;
        m.format = msg.format;
        m.receiver = msg.receiver;
        m.sender = msg.sender;
        m.action = msg.action;
        m.id = msg.id;
        m.extra = msg.extra;
        m.timestamp = msg.timestamp;
        m.state = msg.state;
        return m;
    }

}
