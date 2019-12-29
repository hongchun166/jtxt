
package com.linkb.jstx.network.result;

import com.linkb.jstx.model.MicroServer;

import java.util.List;


public class MicroServerListResult extends BaseResult {
    public List<MicroServer> dataList;

    public boolean isNotEmpty() {
        return dataList != null && !dataList.isEmpty();
    }
}
