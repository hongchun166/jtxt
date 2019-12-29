package com.linkb.jstx.network.result;

/** 转账确认的回调
* */
public class CoinReceiveResult extends BaseResult {


    /**
     * code : 200
     * data : {"endMoney":"1000","receiveMoney":"10"}
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
         * endMoney : 1000
         * receiveMoney : 10
         */

        private String endMoney;
        private String receiveMoney;

        public String getEndMoney() {
            return endMoney;
        }

        public void setEndMoney(String endMoney) {
            this.endMoney = endMoney;
        }

        public String getReceiveMoney() {
            return receiveMoney;
        }

        public void setReceiveMoney(String receiveMoney) {
            this.receiveMoney = receiveMoney;
        }
    }
}
