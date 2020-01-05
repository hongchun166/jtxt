package com.linkb.jstx.network.result;

import com.baidu.mapapi.search.core.CityInfo;

import java.util.List;

public class RegionResult {
    public List<ProvinceInfo> province; //省

    public class ProvinceInfo {
//        public String i;  // 编号
//        public String n;  //名称带省
        public String a;  //名称不带省
//        public String y;  //大写全拼
//        public String b;  //大写首字母
//        public String z;  //简写编号
        public List<MyCityInfo> city;  //市
    }

    public class MyCityInfo {
//        public String i;  //
//        public String n;  //
        public String a;  //
//        public String y;  //
//        public String b;  //
//        public String z;  //
        public List<MyAreaInfo> c;  //县
    }

    public class MyAreaInfo {
//        public String i;  //
//        public String n;  //
        public String a;  //
//        public String y;  //
//        public String b;  //
//        public String z;  //
    }
}
