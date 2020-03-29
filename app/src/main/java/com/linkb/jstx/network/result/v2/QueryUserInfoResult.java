package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

import java.util.List;

public class QueryUserInfoResult extends BaseResult {

    /**
     * {"code":200,"message":"成功",
     * "data":[
     * {"account":"18274839631","name":"黑狐","telephone":"18274839631","email":"ghjjwdv","code":"Kov10021","gender":"1","motto":"好哦了喔，哦哦","isLoginFlag":"0","state":"0","regTime":1577855996,"tradePassword":"4579a0a67759cd28a5a8176691604757","inviteCode":"MHFZNEK","referrerNumber":0,"jianjieReferrerNumber":0,"lockBalanceFreed":0.0000,"lot":0.0,"lat":0.0,"disabled":false}]}
     */
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        private String account;
        private String name;
        private String telephone;
        private String email;
        private String code;
        private String gender;
        private String motto;
        private String isLoginFlag;
        private String state;
        private long regTime;
        private String tradePassword;
        private String inviteCode;
        private int referrerNumber;
        private int jianjieReferrerNumber;
        private float lockBalanceFreed;
        private double lot;
        private double lat;
        private boolean disabled;
        public String area;
        public String tag;
        public String industry;
        public String marrriage;
        public String position;



        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getMotto() {
            return motto;
        }

        public void setMotto(String motto) {
            this.motto = motto;
        }

        public String getIsLoginFlag() {
            return isLoginFlag;
        }

        public void setIsLoginFlag(String isLoginFlag) {
            this.isLoginFlag = isLoginFlag;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public long getRegTime() {
            return regTime;
        }

        public void setRegTime(long regTime) {
            this.regTime = regTime;
        }

        public String getTradePassword() {
            return tradePassword;
        }

        public void setTradePassword(String tradePassword) {
            this.tradePassword = tradePassword;
        }

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public int getReferrerNumber() {
            return referrerNumber;
        }

        public void setReferrerNumber(int referrerNumber) {
            this.referrerNumber = referrerNumber;
        }

        public int getJianjieReferrerNumber() {
            return jianjieReferrerNumber;
        }

        public void setJianjieReferrerNumber(int jianjieReferrerNumber) {
            this.jianjieReferrerNumber = jianjieReferrerNumber;
        }

        public float getLockBalanceFreed() {
            return lockBalanceFreed;
        }

        public void setLockBalanceFreed(float lockBalanceFreed) {
            this.lockBalanceFreed = lockBalanceFreed;
        }

        public double getLot() {
            return lot;
        }

        public void setLot(double lot) {
            this.lot = lot;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }
    }

}
