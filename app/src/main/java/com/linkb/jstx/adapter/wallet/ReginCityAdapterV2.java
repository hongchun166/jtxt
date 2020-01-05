package com.linkb.jstx.adapter.wallet;

import com.contrarywind.adapter.WheelAdapter;
import com.linkb.jstx.network.result.RegionResult;

import java.util.List;

public class ReginCityAdapterV2 implements WheelAdapter<String> {
    public List<RegionResult.MyCityInfo> citys;

    public ReginCityAdapterV2(List<RegionResult.MyCityInfo> list) {
        this.citys = list;
    }

    @Override
    public int getItemsCount() {
        return citys == null ? 0 : citys.size();
    }

    @Override
    public String getItem(int index) {
        return citys.get(index).a;
    }

    @Override
    public int indexOf(String o) {
        int b = 0;
        for (int i = 0; i < citys.size(); i++) {
            if (citys.get(i).a.equals(o)) {
                b = i;
                break;
            }
        }
        return b;
    }
}
