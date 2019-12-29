package com.linkb.jstx.network.result;

import java.util.List;

public class FriendQueryResult extends BaseResult {


    /**
     * code : 200
     * dataList : [{"account":"17602060695","name":"平生二号","telephone":"17602060695","code":"blink435963","gender":"1","isLoginFlag":"1","state":"0","regTime":1552293657,"tradePassword":"4579a0a67759cd28a5a8176691604757","disabled":false},{"account":"17602060696","name":"平生二号","code":"blink393768","gender":"1","isLoginFlag":"1","state":"0","regTime":1551860660,"tradePassword":"4579a0a67759cd28a5a8176691604757","disabled":false},{"account":"17602060697","name":"平生","code":"blink869426","gender":"1","isLoginFlag":"1","state":"0","regTime":1551860586,"tradePassword":"4579a0a67759cd28a5a8176691604757","disabled":false}]
     */

    private List<DataListBean> dataList;

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean {
        /**
         * account : 17602060695
         * name : 平生二号
         * telephone : 17602060695
         * code : blink435963
         * gender : 1
         * isLoginFlag : 1
         * state : 0
         * regTime : 1552293657
         * tradePassword : 4579a0a67759cd28a5a8176691604757
         * disabled : false
         */

        private String account;
        private String name;
        private String telephone;
        private String code;
        private String gender;
        private String isLoginFlag;
        private String state;
        private int regTime;
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

        public String getTelephone() {
            return telephone;
        }

        public void setTelephone(String telephone) {
            this.telephone = telephone;
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

        public int getRegTime() {
            return regTime;
        }

        public void setRegTime(int regTime) {
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
