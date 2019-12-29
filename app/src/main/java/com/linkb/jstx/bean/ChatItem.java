
package com.linkb.jstx.bean;

import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;

import java.io.Serializable;

/**
 * 对话实体
 */
public class ChatItem implements Serializable {

    /**
     *
     */
    public static final String NAME = ChatItem.class.getSimpleName();
    private static final long serialVersionUID = 1L;

    public Message message;

    public MessageSource source;

    public ChatItem() {
    }

    private ChatItem(Message msg) {
        this.message = msg;
    }

    public ChatItem(MessageSource source) {
        this.source = source;
    }

    public ChatItem(Message msg, MessageSource source) {
        this.message = msg;
        this.source = source;
    }
    public ChatItem(MessageSource source, Message msg) {
        this(msg,source);
    }
    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ChatItem) {
            ChatItem item = (ChatItem) o;
            if (source != null && item.source != null) {
                return source.equals(item.source);
            }
        }
        return false;
    }
    public Class<? extends MessageSource> getSourceClass() {
        return source.getClass();
    }

    public long getLastTime() {
        return message == null ? 0 : message.timestamp;
    }
}
