package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

import java.io.Serializable;

public class SendRedPacketResultV2 extends BaseResult implements Serializable {

    /**
     * code : 200
     * message : 成功
     * data : {"id":15,"sendAccount":"18274839631","type":1,"receiver":"18684758953","money":1,"sendNumber":1,"surplusNumber":1,"version":0,"sendTime":"2020-04-04T07:52:15.431+0000","endTime":"2020-04-05T07:52:15.431+0000","currencyId":1,"state":0,"remark":""}
     */
    private DataBean data;



    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Serializable {
        /**
         * id : 15   红包ID
         * sendAccount : 18274839631   发生人
         * type : 1 1，普通个人 2，群普通红包  3群运气红包
         * receiver : 18684758953    接收账号
         * money : 1  金额
         * sendNumber : 1  个数
         * surplusNumber : 1  剩余个数
         * version : 0
         * sendTime : 2020-04-04T07:52:15.431+0000
         * endTime : 2020-04-05T07:52:15.431+0000
         * currencyId : 1   币种ID
         * state : 0  0，可领取  1已经过期 2已经领完
         * remark :   描述
         */

        private long id;
        private String sendAccount;
        private int type;
        private String receiver;
        private double money;
        private int sendNumber;
        private int surplusNumber;
        private int version;
        private String sendTime;
        private String endTime;
        private long currencyId;
        private int state;
        private String remark;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getSendAccount() {
            return sendAccount;
        }

        public void setSendAccount(String sendAccount) {
            this.sendAccount = sendAccount;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public int getSendNumber() {
            return sendNumber;
        }

        public void setSendNumber(int sendNumber) {
            this.sendNumber = sendNumber;
        }

        public int getSurplusNumber() {
            return surplusNumber;
        }

        public void setSurplusNumber(int surplusNumber) {
            this.surplusNumber = surplusNumber;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public long getCurrencyId() {
            return currencyId;
        }

        public void setCurrencyId(long currencyId) {
            this.currencyId = currencyId;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }
    }
}
