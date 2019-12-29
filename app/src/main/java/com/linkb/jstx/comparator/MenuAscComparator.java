
package com.linkb.jstx.comparator;

import com.linkb.jstx.model.MicroServerMenu;

import java.io.Serializable;
import java.util.Comparator;

public class MenuAscComparator implements Comparator<MicroServerMenu>, Serializable {

    @Override
    public int compare(MicroServerMenu arg0, MicroServerMenu arg1) {
        return Integer.compare(arg0.sort,arg1.sort);
    }

}
