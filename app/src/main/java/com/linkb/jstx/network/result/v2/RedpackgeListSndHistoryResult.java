package com.linkb.jstx.network.result.v2;

import android.text.TextUtils;

import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class RedpackgeListSndHistoryResult extends BaseResult {

    /**
     * code : 200
     * data : {"sendList":[{"id":2,"sendAccount":"18684758953","type":2,"receiver":"18684758953","money":10,"sendNumber":10,"surplusNumber":10,"version":0,"sendTime":"2020-03-27T07:12:15.000+0000","endTime":"2020-03-28T07:12:15.000+0000","currencyId":1,"state":0,"remark":""}],"sumMoney":91,"sumNumber":10}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * sendList : [{"id":2,"sendAccount":"18684758953","type":2,"receiver":"18684758953","money":10,"sendNumber":10,"surplusNumber":10,"version":0,"sendTime":"2020-03-27T07:12:15.000+0000","endTime":"2020-03-28T07:12:15.000+0000","currencyId":1,"state":0,"remark":""}]
         * sumMoney : 91
         * sumNumber : 10
         */

        private double sumMoney;
        private int sumNumber;
        private List<SendListBean> sendList;

        public double getSumMoney() {
            return sumMoney;
        }

        public void setSumMoney(double sumMoney) {
            this.sumMoney = sumMoney;
        }

        public int getSumNumber() {
            return sumNumber;
        }

        public void setSumNumber(int sumNumber) {
            this.sumNumber = sumNumber;
        }

        public List<SendListBean> getSendList() {
            if(sendList==null){
                return new ArrayList<>();
            }
            return sendList;
        }

        public void setSendList(List<SendListBean> sendList) {
            this.sendList = sendList;
        }

        public static class SendListBean {
            /**
             * id : 2
             * sendAccount : 18684758953
             * type : 2
             * receiver : 18684758953
             * money : 10
             * sendNumber : 10
             * surplusNumber : 10
             * version : 0
             * sendTime : 2020-03-27T07:12:15.000+0000
             * endTime : 2020-03-28T07:12:15.000+0000
             * currencyId : 1
             * state : 0
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

            public int getId() {
                return id;
            }

            public String getCurrencyName() {
                if(TextUtils.isEmpty(currencyName)){
                    return "KKC";
                }
                return currencyName;
            }

            public void setCurrencyName(String currencyName) {
                this.currencyName = currencyName;
            }

            public void setId(int id) {
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
            public String getSendTimeFinalStr(){
                return TimeUtils.timeToStr(sendTime);
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
