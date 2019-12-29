
package com.linkb.jstx.network.result;

import com.linkb.jstx.model.Message;

import java.util.ArrayList;


public class MessageListResult extends BaseResult {
    public ArrayList<Message> dataList;

    public boolean isNotEmpty() {
        return dataList != null && !dataList.isEmpty();
    }
}
