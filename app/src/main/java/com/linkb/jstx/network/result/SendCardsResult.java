package com.linkb.jstx.network.result;

import java.io.Serializable;

/** 发送名片数据
* */
public class SendCardsResult extends BaseResult implements Serializable {

    private static final long serialVersionUID = 23201891249012480L;


    public static class DataBean implements Serializable {

        private static final long serialVersionUID = 23018824676712480L;

        /**
         * toUserAccount : 15874986188
         * toUsername:
         * leaveMessage :
         * cardsUserAccount :
         * cardsUserName :
         */

        /** 接收者的账号
        * */
        private String toUserAccount;
        private String toUsername;
        private String leaveMessage;
        /** 名片的账号
        * */
        private String cardsUserAccount;
        private String cardsUserName;
        /** 发送者的账号
        * */
        private String sendCardAccount;
        private String sendCardName;

        public String getSendCardAccount() {
            return sendCardAccount;
        }

        public void setSendCardAccount(String sendCardAccount) {
            this.sendCardAccount = sendCardAccount;
        }

        public String getSendCardName() {
            return sendCardName;
        }

        public void setSendCardName(String sendCardName) {
            this.sendCardName = sendCardName;
        }

        public String getToUserAccount() {
            return toUserAccount;
        }

        public void setToUserAccount(String toUserAccount) {
            this.toUserAccount = toUserAccount;
        }

        public String getToUsername() {
            return toUsername;
        }

        public void setToUsername(String toUsername) {
            this.toUsername = toUsername;
        }

        public String getLeaveMessage() {
            return leaveMessage;
        }

        public void setLeaveMessage(String leaveMessage) {
            this.leaveMessage = leaveMessage;
        }

        public String getCardsUserAccount() {
            return cardsUserAccount;
        }

        public void setCardsUserAccount(String cardsUserAccount) {
            this.cardsUserAccount = cardsUserAccount;
        }

        public String getCardsUserName() {
            return cardsUserName;
        }

        public void setCardsUserName(String cardsUserName) {
            this.cardsUserName = cardsUserName;
        }

    }
}
