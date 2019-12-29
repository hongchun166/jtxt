package com.linkb.jstx.network.result;

/** 查询转账状态
* */
public class QueryCoinTransferStatusResult extends BaseResult {

    public static final int COIN_TRANSFER_AVAILABLE = 0;
    public static final int COIN_TRANSFER_NO_EXITED = 1;
    public static final int COIN_TRANSFER_TIME_OUT = 2;
    public static final int COIN_TRANSFER_RECEIVEDED_BY = 3;
    public static final int COIN_TRANSFER_MIME = 4;
    public static final String REDPACKET_STATUS = "CoinTransferStatus";

    /**
     * code : 200
     * data : 0
     */

    /** 转账状态， 0 ： 可领取， 1： 不存在， 2：已过期，自动退回 3： 对方已经领取
    * */
    private int data;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
