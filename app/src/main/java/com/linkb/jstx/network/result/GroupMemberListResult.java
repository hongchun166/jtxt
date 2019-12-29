
package com.linkb.jstx.network.result;

import com.linkb.jstx.model.GroupMember;

import java.util.ArrayList;


public class GroupMemberListResult extends BaseResult {
    public ArrayList<GroupMember> dataList;

    public boolean isNotEmpty() {
        return dataList != null && !dataList.isEmpty();
    }
}
