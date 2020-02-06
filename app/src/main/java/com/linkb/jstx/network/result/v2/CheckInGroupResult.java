package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

public class CheckInGroupResult  extends BaseResult {

    /**
     * code : 200
     * message : 成功
     * data : false
     */

    private boolean data;


    public boolean isData() {
        return data;
    }

    public void setData(boolean data) {
        this.data = data;
    }
}
