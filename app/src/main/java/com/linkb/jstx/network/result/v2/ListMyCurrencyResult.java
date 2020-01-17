package com.linkb.jstx.network.result.v2;

import com.google.gson.annotations.SerializedName;
import com.linkb.jstx.network.result.BaseResult;

import java.util.List;

public class ListMyCurrencyResult extends BaseResult {

    /**
     * code : 200
     * data : [{"id":6,"account":"18684758953","balance":0,"currencyId":1,"currencyName":"KKC","currencyIcon":"图标","addTime":"2020-01-03T07:06:58.000+0000","lockBalance":300}]
     */

    private List<DataBean> data;
    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 6
         * account : 18684758953
         * balance : 0.0
         * currencyId : 1
         * currencyName : KKC
         * currencyIcon : 图标
         * addTime : 2020-01-03T07:06:58.000+0000
         * lockBalance : 300.0
         */

        private int id;
        private String account;
        private double balance;
        private int currencyId;
        private String currencyName;
        private String currencyIcon;
        private String addTime;
        private double lockBalance;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }

        public int getCurrencyId() {
            return currencyId;
        }

        public void setCurrencyId(int currencyId) {
            this.currencyId = currencyId;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName;
        }

        public String getCurrencyIcon() {
            return currencyIcon;
        }

        public void setCurrencyIcon(String currencyIcon) {
            this.currencyIcon = currencyIcon;
        }

        public String getAddTime() {
            return addTime;
        }

        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public double getLockBalance() {
            return lockBalance;
        }

        public void setLockBalance(double lockBalance) {
            this.lockBalance = lockBalance;
        }
    }
}
