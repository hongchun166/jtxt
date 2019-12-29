
package com.linkb.jstx.network.result;

import com.linkb.jstx.model.MicroApp;

import java.util.ArrayList;


public class MicroAppListResult extends BaseResult {
    public ArrayList<MicroApp> dataList;

    public boolean isNotEmpty() {
        return dataList != null && !dataList.isEmpty();
    }
}
