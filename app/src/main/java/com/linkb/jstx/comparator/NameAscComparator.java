
package com.linkb.jstx.comparator;

import com.linkb.jstx.model.Friend;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;

public class NameAscComparator implements Comparator<Friend>, Serializable {

    private final String START ="☆";
    private final String UNKNOW ="#";

    @Override
    public int compare(Friend o1, Friend o2) {
        if (Objects.equals(UNKNOW,o2.fristPinyin)) {
            return -1;
        }
        if (Objects.equals(UNKNOW,o1.fristPinyin)) {
            return 1;
        }

        if (Objects.equals(START,o2.fristPinyin)) {
            return 1;
        }
        if (Objects.equals(START,o1.fristPinyin)) {
            return -1;
        }

        return o1.fristPinyin.compareToIgnoreCase(o2.fristPinyin);
    }
}
