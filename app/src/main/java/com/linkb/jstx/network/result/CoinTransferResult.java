package com.linkb.jstx.network.result;

import java.io.Serializable;

/** 转账成功的回调
* */
public class CoinTransferResult extends BaseResult implements Serializable {

    private static final long serialVersionUID = 2320881249012480L;


    /**
     * code : 200
     * data : {"redType":5,"userAccount":"15802674030","name":"泉","currencyMark":"USDT","remark":"恭喜发财","endMoney":990,"currencyId":22,"redFlag":"blink1559290871758199","sendMoney":10}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        private static final long serialVersionUID = 2320563249012480L;
        /**
         * redType : 5
         * userAccount : 15802674030
         * name : 泉
         * currencyMark : USDT
         * remark : 恭喜发财
         * endMoney : 990
         * currencyId : 22
         * redFlag : blink1559290871758199
         * sendMoney : 10
         */

        private int redType;
        private String userAccount;
        private String name;
        private String currencyMark;
        private String remark;
        private double endMoney;
        private int currencyId;
        private String redFlag;
        private double sendMoney;

        public int getRedType() {
            return redType;
        }

        public void setRedType(int redType) {
            this.redType = redType;
        }

        public String getUserAccount() {
            return userAccount;
        }

        public void setUserAccount(String userAccount) {
            this.userAccount = userAccount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCurrencyMark() {
            return currencyMark;
        }

        public void setCurrencyMark(String currencyMark) {
            this.currencyMark = currencyMark;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public double getEndMoney() {
            return endMoney;
        }

        public void setEndMoney(double endMoney) {
            this.endMoney = endMoney;
        }

        public int getCurrencyId() {
            return currencyId;
        }

        public void setCurrencyId(int currencyId) {
            this.currencyId = currencyId;
        }

        public String getRedFlag() {
            return redFlag;
        }

        public void setRedFlag(String redFlag) {
            this.redFlag = redFlag;
        }

        public double getSendMoney() {
            return sendMoney;
        }

        public void setSendMoney(double sendMoney) {
            this.sendMoney = sendMoney;
        }
    }
}
