
package com.linkb.jstx.comparator;

import com.linkb.jstx.network.result.FriendListResult;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class FriendShipNameAscComparator implements Comparator<FriendListResult.FriendShip>, Serializable {

    private final String START ="☆";
    private final String UNKNOW ="#";

    @Override
    public int compare(FriendListResult.FriendShip o1, FriendListResult.FriendShip o2) {
        if (Objects.equals(UNKNOW,o2.getFristPinyin())) {
            return -1;
        }
        if (Objects.equals(UNKNOW,o1.getFristPinyin())) {
            return 1;
        }

        if (Objects.equals(START,o2.getFristPinyin())) {
            return 1;
        }
        if (Objects.equals(START,o1.getFristPinyin())) {
            return -1;
        }

        return o1.getFristPinyin().compareToIgnoreCase(o2.getFristPinyin());
    }
}
