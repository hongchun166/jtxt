package com.linkb.jstx.network.result;

import java.util.List;

public class ReceivedRedPacketListResult extends BaseResult {


    /**
     * code : 200
     * data : {"redList":[{"username":"海斌哥","currencyName":"ETH","createTime":1450217211,"money":100},{"createTime":1550832412,"username":"海斌哥","money":88,"currencyName":"ETH"},{"username":"呵呵开心","createTime":1550914257,"currencyName":"ETH","money":67.15736557},{"username":"呵呵开心","money":26.28658996,"createTime":1550914424,"currencyName":"ETH"},{"money":99,"createTime":1550914851,"username":"海斌哥","currencyName":"ETH"},{"username":"海斌哥","currencyName":"ETH","createTime":1550916346,"money":6.95271556},{"username":"呵呵开心","createTime":1551083745,"money":1,"currencyName":"ETH"},{"username":"呵呵开心","money":13.79959711,"currencyName":"ETH","createTime":1551087272},{"username":"呵呵开心","createTime":1551087293,"currencyName":"ETH","money":100},{"username":"呵呵开心","createTime":1551087308,"currencyName":"ETH","money":100},{"username":"呵呵开心","money":96.93741008,"createTime":1551088894,"currencyName":"ETH"}],"sendReceiveTotal":699.13367828}
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
         * redList : [{"username":"海斌哥","currencyName":"ETH","createTime":1450217211,"money":100},{"createTime":1550832412,"username":"海斌哥","money":88,"currencyName":"ETH"},{"username":"呵呵开心","createTime":1550914257,"currencyName":"ETH","money":67.15736557},{"username":"呵呵开心","money":26.28658996,"createTime":1550914424,"currencyName":"ETH"},{"money":99,"createTime":1550914851,"username":"海斌哥","currencyName":"ETH"},{"username":"海斌哥","currencyName":"ETH","createTime":1550916346,"money":6.95271556},{"username":"呵呵开心","createTime":1551083745,"money":1,"currencyName":"ETH"},{"username":"呵呵开心","money":13.79959711,"currencyName":"ETH","createTime":1551087272},{"username":"呵呵开心","createTime":1551087293,"currencyName":"ETH","money":100},{"username":"呵呵开心","createTime":1551087308,"currencyName":"ETH","money":100},{"username":"呵呵开心","money":96.93741008,"createTime":1551088894,"currencyName":"ETH"}]
         * sendReceiveTotal : 699.13367828
         */

        private double sendReceiveTotal;
        private List<RedListBean> redList;

        public double getSendReceiveTotal() {
            return sendReceiveTotal;
        }

        public void setSendReceiveTotal(double sendReceiveTotal) {
            this.sendReceiveTotal = sendReceiveTotal;
        }

        public List<RedListBean> getRedList() {
            return redList;
        }

        public void setRedList(List<RedListBean> redList) {
            this.redList = redList;
        }

        public static class RedListBean {
            /**
             * username : 海斌哥
             * currencyName : ETH
             * createTime : 1450217211
             * money : 100
             */

            private String username;
            private String currencyName;
            private long createTime;
            private double money;

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
}
