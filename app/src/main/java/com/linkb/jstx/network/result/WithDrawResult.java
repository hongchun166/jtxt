package com.linkb.jstx.network.result;

public class WithDrawResult extends BaseResult {


    /**
     * code : 200
     * data : {"id":2,"userId":"15874986188","accountName":"15874986188","netAddress":"192.168.0.1","currency":{"id":18,"currencyName":"以太坊币","currencyIcon":"/upload/images/eth.png","currencyMark":"ETH","totalAmount":10000,"frozenAmount":1,"createTime":1520851385,"updateTime":1526997076,"sort":2,"tradeStatus":0,"exchangeStatus":0,"detail":"1211","deleteFlag":0,"auditor":"admin","transactionStatus":0,"release":0,"picture":"/upload/image/20180522/cae7ab91f45f15ebb12eaa57f150c4d0.png","isQuick":"1"},"frozenAmount":100,"frozenAmountStr":"100","amount":9790,"amountStr":"9790","amountStrs":"9790","status":"0","open":0,"walletAddress":"0x5461f1a862a15565ac6e1948f98faa5b9738b472","addDate":1550054573,"profit":0}
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
         * id : 2
         * userId : 15874986188
         * accountName : 15874986188
         * netAddress : 192.168.0.1
         * currency : {"id":18,"currencyName":"以太坊币","currencyIcon":"/upload/images/eth.png","currencyMark":"ETH","totalAmount":10000,"frozenAmount":1,"createTime":1520851385,"updateTime":1526997076,"sort":2,"tradeStatus":0,"exchangeStatus":0,"detail":"1211","deleteFlag":0,"auditor":"admin","transactionStatus":0,"release":0,"picture":"/upload/image/20180522/cae7ab91f45f15ebb12eaa57f150c4d0.png","isQuick":"1"}
         * frozenAmount : 100.0
         * frozenAmountStr : 100
         * amount : 9790.0
         * amountStr : 9790
         * amountStrs : 9790
         * status : 0
         * open : 0
         * walletAddress : 0x5461f1a862a15565ac6e1948f98faa5b9738b472
         * addDate : 1550054573
         * profit : 0.0
         */

        private int id;
        private String userId;
        private String accountName;
        private String netAddress;
        private CurrencyBean currency;
        private double frozenAmount;
        private String frozenAmountStr;
        private double amount;
        private String amountStr;
        private String amountStrs;
        private String status;
        private int open;
        private String walletAddress;
        private long addDate;
        private double profit;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getAccountName() {
            return accountName;
        }

        public void setAccountName(String accountName) {
            this.accountName = accountName;
        }

        public String getNetAddress() {
            return netAddress;
        }

        public void setNetAddress(String netAddress) {
            this.netAddress = netAddress;
        }

        public CurrencyBean getCurrency() {
            return currency;
        }

        public void setCurrency(CurrencyBean currency) {
            this.currency = currency;
        }

        public double getFrozenAmount() {
            return frozenAmount;
        }

        public void setFrozenAmount(double frozenAmount) {
            this.frozenAmount = frozenAmount;
        }

        public String getFrozenAmountStr() {
            return frozenAmountStr;
        }

        public void setFrozenAmountStr(String frozenAmountStr) {
            this.frozenAmountStr = frozenAmountStr;
        }

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public String getAmountStr() {
            return amountStr;
        }

        public void setAmountStr(String amountStr) {
            this.amountStr = amountStr;
        }

        public String getAmountStrs() {
            return amountStrs;
        }

        public void setAmountStrs(String amountStrs) {
            this.amountStrs = amountStrs;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getOpen() {
            return open;
        }

        public void setOpen(int open) {
            this.open = open;
        }

        public String getWalletAddress() {
            return walletAddress;
        }

        public void setWalletAddress(String walletAddress) {
            this.walletAddress = walletAddress;
        }

        public long getAddDate() {
            return addDate;
        }

        public void setAddDate(long addDate) {
            this.addDate = addDate;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public static class CurrencyBean {
            /**
             * id : 18
             * currencyName : 以太坊币
             * currencyIcon : /upload/images/eth.png
             * currencyMark : ETH
             * totalAmount : 10000.0
             * frozenAmount : 1.0
             * createTime : 1520851385
             * updateTime : 1526997076
             * sort : 2
             * tradeStatus : 0
             * exchangeStatus : 0
             * detail : 1211
             * deleteFlag : 0
             * auditor : admin
             * transactionStatus : 0
             * release : 0
             * picture : /upload/image/20180522/cae7ab91f45f15ebb12eaa57f150c4d0.png
             * isQuick : 1
             */

            private int id;
            private String currencyName;
            private String currencyIcon;
            private String currencyMark;
            private double totalAmount;
            private double frozenAmount;
            private int createTime;
            private int updateTime;
            private int sort;
            private int tradeStatus;
            private int exchangeStatus;
            private String detail;
            private int deleteFlag;
            private String auditor;
            private int transactionStatus;
            private int release;
            private String picture;
            private String isQuick;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getCurrencyName() {
                return currencyName;
            }

            public void setCurrencyName(String currencyName) {
                this.currencyName = currencyName;
            }

            public String getCurrencyIcon() {
                return currencyIcon;
            }

            public void setCurrencyIcon(String currencyIcon) {
                this.currencyIcon = currencyIcon;
            }

            public String getCurrencyMark() {
                return currencyMark;
            }

            public void setCurrencyMark(String currencyMark) {
                this.currencyMark = currencyMark;
            }

            public double getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(double totalAmount) {
                this.totalAmount = totalAmount;
            }

            public double getFrozenAmount() {
                return frozenAmount;
            }

            public void setFrozenAmount(double frozenAmount) {
                this.frozenAmount = frozenAmount;
            }

            public int getCreateTime() {
                return createTime;
            }

            public void setCreateTime(int createTime) {
                this.createTime = createTime;
            }

            public int getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(int updateTime) {
                this.updateTime = updateTime;
            }

            public int getSort() {
                return sort;
            }

            public void setSort(int sort) {
                this.sort = sort;
            }

            public int getTradeStatus() {
                return tradeStatus;
            }

            public void setTradeStatus(int tradeStatus) {
                this.tradeStatus = tradeStatus;
            }

            public int getExchangeStatus() {
                return exchangeStatus;
            }

            public void setExchangeStatus(int exchangeStatus) {
                this.exchangeStatus = exchangeStatus;
            }

            public String getDetail() {
                return detail;
            }

            public void setDetail(String detail) {
                this.detail = detail;
            }

            public int getDeleteFlag() {
                return deleteFlag;
            }

            public void setDeleteFlag(int deleteFlag) {
                this.deleteFlag = deleteFlag;
            }

            public String getAuditor() {
                return auditor;
            }

            public void setAuditor(String auditor) {
                this.auditor = auditor;
            }

            public int getTransactionStatus() {
                return transactionStatus;
            }

            public void setTransactionStatus(int transactionStatus) {
                this.transactionStatus = transactionStatus;
            }

            public int getRelease() {
                return release;
            }

            public void setRelease(int release) {
                this.release = release;
            }

            public String getPicture() {
                return picture;
            }

            public void setPicture(String picture) {
                this.picture = picture;
            }

            public String getIsQuick() {
                return isQuick;
            }

            public void setIsQuick(String isQuick) {
                this.isQuick = isQuick;
            }
        }
    }
}
