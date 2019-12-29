
package com.linkb.jstx.comparator;

import java.util.Comparator;

public class TimeDescComparator implements Comparator<Long>  {
    @Override
    public int compare(Long lhs, Long rhs) {
        return Long.compare(rhs,lhs);
    }

}
