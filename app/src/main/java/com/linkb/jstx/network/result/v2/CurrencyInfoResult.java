package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

public class CurrencyInfoResult extends BaseResult {
    public DataBean data;

    public static class DataBean {
        public int id;
        public String account;
        public float balance;
        public int currencyId;
        public String currencyName;
        public String currencyIcon;
        public String addTime;
        public float lockBalance;

    }

}
