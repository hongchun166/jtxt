package com.linkb.jstx.network.result;

import java.io.Serializable;
import java.util.List;

public class CurrencyListResult extends BaseResult implements Serializable {

    private static final long serialVersionUID = 2320183901249012480L;


    /**
     * code : 200
     * dataList : [{"amount":0,"currencyName":"dpc币","currencyMark":"DPC","id":27,"currencyIcon":"/upload/currency/dpc.png"},{"amount":0,"currencyName":"泰达币","currencyMark":"USDT","id":22,"currencyIcon":"/upload/currency/usdt.png"},{"amount":0,"currencyName":"狗狗币","currencyMark":"DOGE","id":21,"currencyIcon":"/upload/currency/doge.png"},{"amount":0,"currencyName":"莱特币","currencyMark":"LTC","id":20,"currencyIcon":"/upload/currency/ltc.png"},{"amount":1000,"currencyName":"比特币","currencyMark":"BTC","id":19,"currencyIcon":"/upload/currency/btc.png"},{"amount":8515.10476733,"currencyName":"以太坊币","currencyMark":"ETH","id":18,"currencyIcon":"/upload/currency/eth.png"}]
     */

    private List<DataListBean> dataList;

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean implements Serializable {

        private static final long serialVersionUID = 23201831249012480L;
        /**
         * amount : 0
         * currencyName : dpc币
         * currencyMark : DPC
         * id : 27
         * currencyIcon : /upload/currency/dpc.png
         */

        private double amount;
        private String currencyName;
        private String currencyMark;
        private int id;
        private String currencyIcon;

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName;
        }

        public String getCurrencyMark() {
            return currencyMark;
        }

        public void setCurrencyMark(String currencyMark) {
            this.currencyMark = currencyMark;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCurrencyIcon() {
            return currencyIcon;
        }

        public void setCurrencyIcon(String currencyIcon) {
            this.currencyIcon = currencyIcon;
        }
    }
}
