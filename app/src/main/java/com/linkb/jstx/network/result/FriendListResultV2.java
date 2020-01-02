
package com.linkb.jstx.network.result;

import java.util.List;

public class FriendListResultV2 extends FriendListResult {

    private List<FriendShip> data;

    @Override
    public List<FriendShip> getDataList() {
        return getData();
    }

    public List<FriendShip> getData() {
        return data;
    }

    public void setData(List<FriendShip> data) {
        this.data = data;
    }
}
