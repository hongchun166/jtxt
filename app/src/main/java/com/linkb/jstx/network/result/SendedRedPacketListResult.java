package com.linkb.jstx.network.result;

import java.util.List;

public class SendedRedPacketListResult extends BaseResult {


    /**
     * code : 200
     * data : {"redList":[{"username":"呵呵开心","redType":0,"eventRemark":"发出红包","redResiCount":0,"redCount":2,"createTime":1550914061,"status":4,"currencyName":"ETH","money":100},{"username":"呵呵开心","redType":0,"eventRemark":"发出红包","redResiCount":0,"redCount":2,"status":4,"currencyName":"ETH","createTime":1550914410,"money":100},{"username":"呵呵开心","redType":0,"eventRemark":"发出红包","redResiCount":0,"createTime":1551083730,"money":1,"redCount":1,"status":4,"currencyName":"ETH"},{"username":"呵呵开心","eventRemark":"发出红包","redType":1,"redResiCount":0,"createTime":1551087121,"redCount":2,"status":4,"currencyName":"ETH","money":100},{"redType":2,"username":"呵呵开心","eventRemark":"发出红包","redResiCount":0,"createTime":1551087198,"redCount":2,"money":200,"status":4,"currencyName":"ETH"},{"username":"呵呵开心","redType":3,"createTime":1551087205,"eventRemark":"发出红包","redResiCount":0,"redCount":2,"currencyName":"ETH","status":3,"money":100},{"username":"呵呵开心","eventRemark":"发出红包","redType":1,"redResiCount":0,"redCount":2,"currencyName":"ETH","status":3,"createTime":1551087480,"money":100},{"username":"呵呵开心","redType":3,"createTime":1551088884,"eventRemark":"发出红包","redResiCount":0,"redCount":2,"currencyName":"ETH","status":3,"money":100},{"username":"呵呵开心","createTime":1551168210,"eventRemark":"发出红包","redType":1,"redResiCount":0,"redCount":2,"currencyName":"ETH","status":3,"money":100}],"sendRedTotal":901}
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
         * redList : [{"username":"呵呵开心","redType":0,"eventRemark":"发出红包","redResiCount":0,"redCount":2,"createTime":1550914061,"status":4,"currencyName":"ETH","money":100},{"username":"呵呵开心","redType":0,"eventRemark":"发出红包","redResiCount":0,"redCount":2,"status":4,"currencyName":"ETH","createTime":1550914410,"money":100},{"username":"呵呵开心","redType":0,"eventRemark":"发出红包","redResiCount":0,"createTime":1551083730,"money":1,"redCount":1,"status":4,"currencyName":"ETH"},{"username":"呵呵开心","eventRemark":"发出红包","redType":1,"redResiCount":0,"createTime":1551087121,"redCount":2,"status":4,"currencyName":"ETH","money":100},{"redType":2,"username":"呵呵开心","eventRemark":"发出红包","redResiCount":0,"createTime":1551087198,"redCount":2,"money":200,"status":4,"currencyName":"ETH"},{"username":"呵呵开心","redType":3,"createTime":1551087205,"eventRemark":"发出红包","redResiCount":0,"redCount":2,"currencyName":"ETH","status":3,"money":100},{"username":"呵呵开心","eventRemark":"发出红包","redType":1,"redResiCount":0,"redCount":2,"currencyName":"ETH","status":3,"createTime":1551087480,"money":100},{"username":"呵呵开心","redType":3,"createTime":1551088884,"eventRemark":"发出红包","redResiCount":0,"redCount":2,"currencyName":"ETH","status":3,"money":100},{"username":"呵呵开心","createTime":1551168210,"eventRemark":"发出红包","redType":1,"redResiCount":0,"redCount":2,"currencyName":"ETH","status":3,"money":100}]
         * sendRedTotal : 901
         */

        private double sendRedTotal;
        private List<RedListBean> redList;

        public double getSendRedTotal() {
            return sendRedTotal;
        }

        public void setSendRedTotal(double sendRedTotal) {
            this.sendRedTotal = sendRedTotal;
        }

        public List<RedListBean> getRedList() {
            return redList;
        }

        public void setRedList(List<RedListBean> redList) {
            this.redList = redList;
        }

        public static class RedListBean {
            /**
             * username : 呵呵开心
             * redType : 0
             * eventRemark : 发出红包
             * redResiCount : 0
             * redCount : 2
             * createTime : 1550914061
             * status : 4
             * currencyName : ETH
             * money : 100
             */

            private String username;
            private int redType;
            private String eventRemark;
            private int redResiCount;
            private int redCount;
            private long createTime;
            private int status;
            private String currencyName;
            private double money;

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public int getRedType() {
                return redType;
            }

            public void setRedType(int redType) {
                this.redType = redType;
            }

            public String getEventRemark() {
                return eventRemark;
            }

            public void setEventRemark(String eventRemark) {
                this.eventRemark = eventRemark;
            }

            public int getRedResiCount() {
                return redResiCount;
            }

            public void setRedResiCount(int redResiCount) {
                this.redResiCount = redResiCount;
            }

            public int getRedCount() {
                return redCount;
            }

            public void setRedCount(int redCount) {
                this.redCount = redCount;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public int getStatus() {
                return status;
            }

            public void setStatus(int status) {
                this.status = status;
            }

            public String getCurrencyName() {
                return currencyName;
            }

            public void setCurrencyName(String currencyName) {
                this.currencyName = currencyName;
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
