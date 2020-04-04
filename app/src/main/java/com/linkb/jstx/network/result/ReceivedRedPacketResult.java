package com.linkb.jstx.network.result;

import android.text.TextUtils;

import java.io.Serializable;

public class ReceivedRedPacketResult extends BaseResult implements Serializable {

    private static final long serialVersionUID = 23183881249012480L;


    /**
     * code : 200
     * data : {"type":0,"endMoney":867.44395553,"receiveMoney":99}
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
         * type : 0
         * endMoney : 867.44395553
         * receiveMoney : 99.0
         */

        private int type;
        private double endMoney;
        private double receiveMoney;
        private double value;

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public double getEndMoney() {
            return endMoney;
        }

        public void setEndMoney(double endMoney) {
            this.endMoney = endMoney;
        }

        public double getReceiveMoney() {
            return receiveMoney;
        }

        public void setReceiveMoney(double receiveMoney) {
            this.receiveMoney = receiveMoney;
        }
    }
}
