package com.linkb.jstx.network.result;

import java.util.List;

public class RedPacketReceivedMemberResult extends BaseResult {


    /**
     * code : 200
     * data : [{"account":"18964088166","username":"呵呵开心","currencyName":"ETH","createTime":1450217211,"money":100},{"createTime":1550217253,"account":"17602060697","username":"平生","currencyName":"ETH","money":100}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * account : 18964088166
         * username : 呵呵开心
         * currencyName : ETH
         * createTime : 1450217211
         * money : 100
         */

        private String account;
        private String username;
        private String currencyName;
        private long createTime;
        private double money;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }
    }
}
