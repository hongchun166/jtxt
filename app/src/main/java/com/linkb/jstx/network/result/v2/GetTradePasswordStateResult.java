package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

public class GetTradePasswordStateResult extends BaseResult {


    /**
     * code : 200
     * data : {"tradePasswordState":1}
     *
     * 1、已经设置
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
         * tradePasswordState : 1
         */

        private String tradePasswordState;

        public boolean hasSetPswSuc(){
            return "1".equals(tradePasswordState);
        }
        public String getTradePasswordState() {
            return tradePasswordState;
        }

        public void setTradePasswordState(String tradePasswordState) {
            this.tradePasswordState = tradePasswordState;
        }

    }
}
