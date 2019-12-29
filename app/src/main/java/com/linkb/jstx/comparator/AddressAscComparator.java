
package com.linkb.jstx.comparator;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.linkb.jstx.util.AppTools;

import java.io.Serializable;
import java.util.Comparator;

public class AddressAscComparator implements Comparator<PoiInfo>, Serializable {


    private LatLng current;

    public AddressAscComparator(LatLng current)

    {
        this.current = current;
    }

    @Override
    public int compare(PoiInfo arg0, PoiInfo arg1) {
        if (arg0 == null) {
            return 0;
        }
        if (arg1 == null) {
            return 1;
        }
        double distance0 = AppTools.getDistance(current.longitude, current.latitude, arg0.location.longitude, arg0.location.latitude);
        double distance1 = AppTools.getDistance(current.longitude, current.latitude, arg1.location.longitude, arg1.location.latitude);
        return Double.compare(distance0,distance1);
    }

}
