
package com.linkb.jstx.network.result;


import com.linkb.jstx.bean.User;

import java.io.Serializable;

public class LoginResult extends BaseResult implements Serializable {
    private static final long serialVersionUID = 2320183881249012480L;

    /**
     * code : 200
     * data : {"isLoginFlag":"1","fromAccount":"system","currencyId":27,"redFlag":"blink1551752975585495","receiveMoney":45.02355,"user":{"account":"17602060697","name":"平生四号","telephone":"17602060697","email":"1","code":"blink382015","gender":"0","grade":0,"isLoginFlag":"1","state":"0","regTime":1551866832,"tradePassword":"4579a0a67759cd28a5a8176691604757","disabled":false}}
     * token : 50308e113b88d606b318275635d05974
     */

    private DataBean data;
    private String token;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static class DataBean implements Serializable {
        private static final long serialVersionUID = 2320183249012480L;
        /**
         * isLoginFlag : 1
         * fromAccount : system
         * currencyId : 27
         * redFlag : blink1551752975585495
         * receiveMoney : 45.02355
         * user : {"account":"17602060697","name":"平生四号","telephone":"17602060697","email":"1","code":"blink382015","gender":"0","grade":0,"isLoginFlag":"1","state":"0","regTime":1551866832,"tradePassword":"4579a0a67759cd28a5a8176691604757","disabled":false}
         */

        private String isLoginFlag;
        private String fromAccount;
        private int currencyId;
        private String redFlag;
        private int receiveMoney;
        private User user;

        public String getIsLoginFlag() {
            return isLoginFlag;
        }

        public void setIsLoginFlag(String isLoginFlag) {
            this.isLoginFlag = isLoginFlag;
        }

        public String getFromAccount() {
            return fromAccount;
        }

        public void setFromAccount(String fromAccount) {
            this.fromAccount = fromAccount;
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

        public int getReceiveMoney() {
            return receiveMoney;
        }

        public void setReceiveMoney(int receiveMoney) {
            this.receiveMoney = receiveMoney;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }


    }
}
