package com.linkb.jstx.network.result;

import java.io.Serializable;

public class SendRedPacketResult extends BaseResult implements Serializable {

    private static final long serialVersionUID = 2320183881249012480L;


    /**
     * code : 200
     * data : {"userAccount":"15874986188","name":"海斌个","remark":"恭喜发财，大吉大利","endMoney":9890,"redFlag":"blink403802"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {

        private static final long serialVersionUID = 2320188249012480L;

        /**
         * userAccount : 15874986188
         * currencyMark:
         * name : 海斌个
         * remark : 恭喜发财，大吉大利
         * endMoney : 9890
         * sendMoney : 100
         * redFlag : blink403802
         * currencyId : 18
         * redCount : 1
         * redType: 1
         */
        private int currencyId;
        private String currencyMark;
        private String userAccount;
        private String name;
        private String remark;
        private String endMoney;
        private String sendMoney;
        private String redFlag;
        /** 红包个数
         * */
        private int redCount;
        /** 红包类型
        * */
        private int redType;

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

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getEndMoney() {
            return endMoney;
        }

        public void setEndMoney(String endMoney) {
            this.endMoney = endMoney;
        }

        public String getSendMoney() {
            return sendMoney;
        }

        public void setSendMoney(String sendMoney) {
            this.sendMoney = sendMoney;
        }

        public String getRedFlag() {
            return redFlag;
        }

        public void setRedFlag(String redFlag) {
            this.redFlag = redFlag;
        }

        public int getCurrencyId() {
            return currencyId;
        }

        public void setCurrencyId(int currencyId) {
            this.currencyId = currencyId;
        }

        public String getCurrencyMark() {
            return currencyMark;
        }

        public void setCurrencyMark(String currencyMark) {
            this.currencyMark = currencyMark;
        }

        public int getRedCount() {
            return redCount;
        }

        public void setRedCount(int redCount) {
            this.redCount = redCount;
        }

        public int getRedType() {
            return redType;
        }

        public void setRedType(int redType) {
            this.redType = redType;
        }
    }
}
