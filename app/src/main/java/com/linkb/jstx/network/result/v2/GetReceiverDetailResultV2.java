package com.linkb.jstx.network.result.v2;

import android.support.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.util.TimeUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Keep
public class GetReceiverDetailResultV2 extends BaseResult {

    /**
     * code : 200
     * data : {"redpackgeReceivers":[],"redpackge":{"id":17,"sendAccount":"18274839631","type":1,"receiver":"18684758953","money":1.2,"sendNumber":1,"surplusNumber":1,"version":0,"sendTime":"2020-04-04T08:14:49.188+0000","endTime":"2020-04-05T08:14:49.188+0000","currencyId":1,"state":0,"remark":""}}
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
         * redpackgeReceivers : []
         * redpackge : {"id":17,"sendAccount":"18274839631","type":1,"receiver":"18684758953","money":1.2,"sendNumber":1,"surplusNumber":1,"version":0,"sendTime":"2020-04-04T08:14:49.188+0000","endTime":"2020-04-05T08:14:49.188+0000","currencyId":1,"state":0,"remark":""}
         */

        private RedpackgeBean redpackge;
        private List<RedpackgeReceiversBean> redpackgeReceivers;

        public RedpackgeBean getRedpackge() {
            return redpackge;
        }

        public void setRedpackge(RedpackgeBean redpackge) {
            this.redpackge = redpackge;
        }

        public List<RedpackgeReceiversBean> getRedpackgeReceivers() {
            return redpackgeReceivers;
        }

        public void setRedpackgeReceivers(List<RedpackgeReceiversBean> redpackgeReceivers) {
            this.redpackgeReceivers = redpackgeReceivers;
        }

        public static class RedpackgeReceiversBean {
            private long id;
            private long redpackgeId;
            private String receiverAccount;
            private String receiverMoney;
            private String receiverTime;

            public long getId() {
                return id;
            }

            public void setId(long id) {
                this.id = id;
            }

            public long getRedpackgeId() {
                return redpackgeId;
            }

            public void setRedpackgeId(long redpackgeId) {
                this.redpackgeId = redpackgeId;
            }

            public String getReceiverAccount() {
                return receiverAccount;
            }

            public void setReceiverAccount(String receiverAccount) {
                this.receiverAccount = receiverAccount;
            }

            public String getReceiverMoney() {
                return receiverMoney;
            }

            public void setReceiverMoney(String receiverMoney) {
                this.receiverMoney = receiverMoney;
            }

            public String getReceiverTime() {
                return receiverTime;
            }

            public void setReceiverTime(String receiverTime) {
                this.receiverTime = receiverTime;
            }
            public String getTimeFinalStr(){
                return TimeUtils.timeToStr(receiverTime);
            }
        }

        public static class RedpackgeBean {
            /**
             * id : 17
             * sendAccount : 18274839631
             * type : 1
             * receiver : 18684758953
             * money : 1.2
             * sendNumber : 1
             * surplusNumber : 1
             * version : 0
             * sendTime : 2020-04-04T08:14:49.188+0000
             * endTime : 2020-04-05T08:14:49.188+0000
             * currencyId : 1
             * state : 0  0，可领取  1已经过期 2已经领完
             * remark :
             */

            private int id;
            private String sendAccount;
            private int type;
            private String receiver;
            private double money;
            private int sendNumber;
            private int surplusNumber;
            private int version;
            private String sendTime;
            private String endTime;
            private int currencyId;
            private String currencyName;
            private int state;
            private String remark;
            private String userName;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getSendAccount() {
                return sendAccount;
            }

            public String getCurrencyName() {
                return currencyName;
            }

            public void setCurrencyName(String currencyName) {
                this.currencyName = currencyName;
            }

            public void setSendAccount(String sendAccount) {
                this.sendAccount = sendAccount;
            }

            public String getUserName() {
               return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
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

            public int getCurrencyId() {
                return currencyId;
            }

            public void setCurrencyId(int currencyId) {
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
}
