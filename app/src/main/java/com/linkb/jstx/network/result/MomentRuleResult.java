
package com.linkb.jstx.network.result;

import com.linkb.jstx.model.MomentRule;

import java.util.ArrayList;


public class MomentRuleResult extends BaseResult {
    public ArrayList<MomentRule> dataList;

    public boolean isNotEmpty() {
        return dataList != null && !dataList.isEmpty();
    }
}
