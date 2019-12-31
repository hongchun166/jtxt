package com.linkb.jstx.network.result;

public class MineInviteInfoResult extends BaseResult {
    public InviteInfo data;

    public class InviteInfo {
        public String inviteCount;   //直接邀请人数
        public String rule;
        public String prize;  //邀请的奖励
        public String inviteCount2; //间接邀请的人数
    }
}
