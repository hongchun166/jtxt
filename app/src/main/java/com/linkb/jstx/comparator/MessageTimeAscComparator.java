
package com.linkb.jstx.comparator;


import com.linkb.jstx.model.Message;

import java.io.Serializable;
import java.util.Comparator;

public class MessageTimeAscComparator implements Comparator<Message>, Serializable {

    @Override
    public int compare(Message arg0, Message arg1) {
        return Long.compare(arg0.timestamp,arg1.timestamp);
    }


}
