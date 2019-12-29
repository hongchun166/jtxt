
package com.linkb.jstx.network.result;

import com.linkb.jstx.model.MicroServerMenu;

import java.util.ArrayList;


public class MicroServerMenuListResult extends BaseResult {
    public ArrayList<MicroServerMenu> dataList;

    public boolean isNotEmpty() {
        return dataList != null && !dataList.isEmpty();
    }
}
