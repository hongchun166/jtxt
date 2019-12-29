
package com.linkb.jstx.network.result;

import java.util.ArrayList;


public class MessageForwardResult extends BaseResult {
    public ArrayList<Long> dataList;

    public boolean isNotEmpty() {
        return dataList != null && !dataList.isEmpty();
    }
}
