package com.linkb.jstx.adapter.wallet;

import com.bigkoo.pickerview.adapter.ArrayWheelAdapter;
import com.contrarywind.adapter.WheelAdapter;
import com.linkb.jstx.network.result.RegionResult;

import java.util.List;

public class RegionProvinceAdapterV2 implements WheelAdapter<String> {

    public List<RegionResult.ProvinceInfo> provinces;

    public RegionProvinceAdapterV2(List<RegionResult.ProvinceInfo> list) {
        this.provinces = list;
    }

    @Override
    public int getItemsCount() {
        return provinces == null ? 0 : provinces.size();
    }

    @Override
    public String getItem(int index) {
        return provinces.get(index).a;
    }

    @Override
    public int indexOf(String o) {
        int b = 0;
        for (int i = 0; i < provinces.size(); i++) {
            if (provinces.get(i).a.equals(o)) {
                b = i;
                break;
            }
        }
        System.out.println("发的发生范德萨开发技术"+b);
        return b;
    }
}
