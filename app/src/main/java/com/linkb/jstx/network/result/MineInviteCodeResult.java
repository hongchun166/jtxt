package com.linkb.jstx.network.result;

public class MineInviteCodeResult extends BaseResult {
    private MineInviteCode data;
    public static class MineInviteCode{
        private String inviteCode;
        private String downloadUrl;

        public String getInviteCode() {
            return inviteCode;
        }

        public void setInviteCode(String inviteCode) {
            this.inviteCode = inviteCode;
        }

        public String getDownloadUrl() {
            return downloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            this.downloadUrl = downloadUrl;
        }
    }

    public MineInviteCode getData() {
        return data;
    }

    public void setData(MineInviteCode data) {
        this.data = data;
    }
}
