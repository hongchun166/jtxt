package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

public class AccountBalanceResult extends BaseResult {

    private DataBean data;

    public static class DataBean{
       private double balance;

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }
}
