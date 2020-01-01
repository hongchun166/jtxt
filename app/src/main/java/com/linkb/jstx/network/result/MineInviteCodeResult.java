package com.linkb.jstx.network.result;

public class MineInviteCodeResult extends BaseResult {
    private MineInviteCode data;
    public static class MineInviteCode{
        private String inviteCode;

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }
    }

    public MineInviteCode getData() {
        return data;
    }

    public void setData(MineInviteCode data) {
        this.data = data;
    }
}
