package com.linkb.jstx.network.result;

public class QueryRedPacketStatusResult extends BaseResult {

    public static final int RED_PACKET_AVAILABLE = 0;
    public static final int RED_PACKET_NO_EXITED = 1;
    public static final int RED_PACKET_TIME_OUT = 2;
    public static final int RED_PACKET_RECEIVEDED_EMPTY = 3;
    public static final int RED_PACKET_RECEIVEDED = 4;
    public static final int RED_PACKET_MIME = 5;
    public static final int RED_PACKET_RECEIVEDED_BY = 8;
    public static final String REDPACKET_STATUS = "RedPacketStatus";

    /**
     * code : 200
     * data : 0
     */

    /** 红包状态， 0 ： 可领取， 1： 不存在， 2：已过期， 3： 已领完 ， 4：已领取, 5: 普通红包本人不可领取, 8:对方已经领取
    * */
    private int data;

    public int getData() {
        return data;
    }

    public void setData(int data) {
        this.data = data;
    }
}
