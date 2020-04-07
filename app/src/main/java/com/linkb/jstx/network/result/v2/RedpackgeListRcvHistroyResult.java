package com.linkb.jstx.network.result.v2;

import android.text.TextUtils;

import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

public class RedpackgeListRcvHistroyResult extends BaseResult {

    /**
     * code : 200
     * data : {"sumMoney":19,"sumNumber":19,"receivList":[{"id":1,"receiverAccount":"18684758953","receiverMoney":2,"redpackgeId":6,"receiverTime":"2020-03-27T09:02:45.000+0000"},{"id":2,"receiverAccount":"18684758953","receiverMoney":1,"redpackgeId":6,"receiverTime":"2020-03-27T09:03:25.000+0000"}]}
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
         * sumMoney : 19
         * sumNumber : 19
         * receivList : [{"id":1,"receiverAccount":"18684758953","receiverMoney":2,"redpackgeId":6,"receiverTime":"2020-03-27T09:02:45.000+0000"},{"id":2,"receiverAccount":"18684758953","receiverMoney":1,"redpackgeId":6,"receiverTime":"2020-03-27T09:03:25.000+0000"}]
         */

        private double sumMoney;
        private Integer sumNumber;
        private List<ReceivListBean> receivList;

        public double getSumMoney() {
            return sumMoney;
        }

        public void setSumMoney(double sumMoney) {
            this.sumMoney = sumMoney;
        }

        public Integer getSumNumber() {
            return sumNumber;
        }

        public void setSumNumber(Integer sumNumber) {
            this.sumNumber = sumNumber;
        }

        public List<ReceivListBean> getReceivList() {
            if(receivList==null){
                return new ArrayList<>();
            }
            return receivList;
        }

        public void setReceivList(List<ReceivListBean> receivList) {
            this.receivList = receivList;
        }

        public static class ReceivListBean {
            /**
             * id : 1
             * receiverAccount : 18684758953
             * receiverMoney : 2
             * redpackgeId : 6
             * receiverTime : 2020-03-27T09:02:45.000+0000
             */

            private int id;
            private String receiverAccount;
            private double receiverMoney;
            private int redpackgeId;
            private String receiverTime;
            private int currencyId;
           private String currencyName;

            public int getCurrencyId() {
                return currencyId;
            }

            public void setCurrencyId(int currencyId) {
                this.currencyId = currencyId;
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

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getReceiverAccount() {
                return receiverAccount;
            }

            public void setReceiverAccount(String receiverAccount) {
                this.receiverAccount = receiverAccount;
            }

            public double getReceiverMoney() {
                return receiverMoney;
            }

            public void setReceiverMoney(double receiverMoney) {
                this.receiverMoney = receiverMoney;
            }

            public int getRedpackgeId() {
                return redpackgeId;
            }

            public void setRedpackgeId(int redpackgeId) {
                this.redpackgeId = redpackgeId;
            }

            public String getReceiverTime() {
                return receiverTime;
            }

            public void setReceiverTime(String receiverTime) {
                this.receiverTime = receiverTime;
            }
            public String getReceiveTimerFinalStr(){
                return TimeUtils.timeToStr(receiverTime);
            }
        }
    }
}
