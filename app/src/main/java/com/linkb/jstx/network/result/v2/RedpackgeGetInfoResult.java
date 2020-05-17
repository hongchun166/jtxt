package com.linkb.jstx.network.result.v2;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.linkb.jstx.network.result.BaseResult;

import java.io.Serializable;

public class RedpackgeGetInfoResult extends BaseResult {


    /**
     * code : 200
     * data : {"receiver":"valueNull","remark":"恭厚厚","surplusNumber":"3","type":"3","userName":"黑狐","version":"0","sendTime":"Wed Apr 08 22:04:55 CST 2020","getState":1,"currencyName":"KKC","money":"1.02","sendAccount":"18274839631","sendNumber":"3","endTime":"Thu Apr 09 22:04:55 CST 2020","id":"67","state":"0","currencyId":"1","class":"class com.dby.jstx.model.Redpackge"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean  implements Serializable {
        /**
         * receiver : valueNull
         * remark : 恭厚厚
         * surplusNumber : 3
         *
         * {@link com.linkb.jstx.app.Constant.RedPacketType}
         * type : 3
         *
         * userName : 黑狐
         * version : 0
         * sendTime : Wed Apr 08 22:04:55 CST 2020
         * getState : 1         红包对人状态，0 未领取， 1、已领取
         * currencyName : KKC
         * money : 1.02
         * sendAccount : 18274839631
         * sendNumber : 3
         * endTime : Thu Apr 09 22:04:55 CST 2020
         * id : 67
         *
         * state : 0    0, "可领取" ,1, "超时",2, "已领完"
         *
         * currencyId : 1
         * class : class com.dby.jstx.model.Redpackge
         */

        private String receiver;
        private String remark;
        private String surplusNumber;
        private String type;
        private String userName;
        private String version;
        private String sendTime;
        private int getState;
        private String currencyName;
        private String money;
        private String sendAccount;
        private String sendNumber;
        private String endTime;
        private String id;
        private String state;
        private String currencyId;


        public String getReceiver() {
            return receiver;
        }

        public void setReceiver(String receiver) {
            this.receiver = receiver;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getSurplusNumber() {
            return surplusNumber;
        }

        public void setSurplusNumber(String surplusNumber) {
            this.surplusNumber = surplusNumber;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getSendTime() {
            return sendTime;
        }

        public void setSendTime(String sendTime) {
            this.sendTime = sendTime;
        }

        public int getGetState() {
            return getState;
        }
        public boolean hasSelfReceiveed(){
            return getGetState()==1;
        }
        public void setGetState(int getState) {
            this.getState = getState;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getSendAccount() {
            return sendAccount;
        }

        public void setSendAccount(String sendAccount) {
            this.sendAccount = sendAccount;
        }

        public String getSendNumber() {
            return sendNumber;
        }
        public int getSendNumberNum() {
            if(TextUtils.isEmpty(sendNumber)){
                return 0;
            }
            int num=0;
            try{
                num=Integer.valueOf(sendNumber);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
            return num;
        }
        public void setSendNumber(String sendNumber) {
            this.sendNumber = sendNumber;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCurrencyId() {
            return currencyId;
        }

        public void setCurrencyId(String currencyId) {
            this.currencyId = currencyId;
        }

    }
}
