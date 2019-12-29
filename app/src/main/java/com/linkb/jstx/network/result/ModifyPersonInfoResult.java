package com.linkb.jstx.network.result;

import com.google.gson.annotations.SerializedName;

public class ModifyPersonInfoResult extends BaseResult {


    /**
     * code : 200
     * data : {"account":"17602060697","name":"平生二号","email":"1","code":"blink382015","gender":"1","isLoginFlag":"1","state":"0","regTime":1551866832,"tradePassword":"4579a0a67759cd28a5a8176691604757","disabled":false}
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
         * account : 17602060697
         * name : 平生二号
         * email : 1
         * code : blink382015
         * gender : 1
         * isLoginFlag : 1
         * state : 0
         * regTime : 1551866832
         * tradePassword : 4579a0a67759cd28a5a8176691604757
         * disabled : false
         */

        private String account;
        private String name;
        private String email;
        @SerializedName("code")
        private String codeX;
        private String gender;
        private String isLoginFlag;
        private String state;
        private long regTime;
        private String tradePassword;
        private boolean disabled;

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

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getCodeX() {
            return codeX;
        }

        public void setCodeX(String codeX) {
            this.codeX = codeX;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
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

        public boolean isDisabled() {
            return disabled;
        }

        public void setDisabled(boolean disabled) {
            this.disabled = disabled;
        }
    }
}
