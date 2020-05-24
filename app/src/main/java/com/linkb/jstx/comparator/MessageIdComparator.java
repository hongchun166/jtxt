package com.linkb.jstx.comparator;

import com.linkb.jstx.model.Message;

import java.util.Comparator;

public class MessageIdComparator implements Comparator<Message> {
    @Override
    public int compare(Message arg0, Message arg1) {
        return Long.compare(arg0.id,arg1.id);
    }
}
