
package com.linkb.jstx.network.result;

import com.linkb.jstx.model.Moment;

import java.util.ArrayList;


public class MomentListResult extends BaseResult {
    public ArrayList<Moment> dataList;

    public boolean isNotEmpty() {
        return dataList != null && !dataList.isEmpty();
    }

    public boolean isEmpty() {
        return dataList == null || dataList.isEmpty();
    }
}
