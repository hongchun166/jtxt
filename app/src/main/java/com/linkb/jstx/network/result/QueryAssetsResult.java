package com.linkb.jstx.network.result;

import java.io.Serializable;
import java.util.List;

public class QueryAssetsResult extends BaseResult implements Serializable {
    private static final long serialVersionUID = 2318388123212480L;


    /**
     * code : 200
     * data : {"totalBTC":6141315.27093,"totalCNY":12466870}
     * dataList : [{"userId":"15874986188","accountName":"15874986188","currency":{"id":17,"currencyName":"plus币","currencyIcon":"/upload/images/plus.png","currencyMark":"PLUS","totalAmount":10000,"frozenAmount":0,"createTime":1520824034,"updateTime":1527066844,"sort":1,"tradeStatus":0,"exchangeStatus":1,"detail":"111","deleteFlag":0,"auditor":"admin","transactionStatus":0,"release":1,"picture":"/upload/image/20180523/dce0b777c8177d066ee80e70d5c1c1ca.png","isQuick":"1","price":225,"priceStr":"225"},"frozenAmount":0,"frozenAmountStr":"0","amount":0,"amountStr":"0","amountStrs":"0","price":0,"priceStr":"0","profit":0},{"id":1,"userId":"15874986188","accountName":"15874986188","netAddress":"192.168.0.1","currency":{"id":18,"currencyName":"以太坊币","currencyIcon":"/upload/images/eth.png","currencyMark":"ETH","totalAmount":10000,"frozenAmount":1,"createTime":1520851385,"updateTime":1526997076,"sort":2,"tradeStatus":0,"exchangeStatus":0,"detail":"1211","deleteFlag":0,"auditor":"admin","transactionStatus":0,"release":0,"picture":"/upload/image/20180522/cae7ab91f45f15ebb12eaa57f150c4d0.png","isQuick":"1"},"frozenAmount":10,"frozenAmountStr":"10","amount":100,"amountStr":"100","amountStrs":"100","status":"0","open":0,"walletAddress":"0X123456789","price":81350,"priceStr":"81350","profit":0},{"id":2,"userId":"15874986188","accountName":"15874986188","netAddress":"192.168.0.1","currency":{"id":19,"currencyName":"比特币","currencyIcon":"/upload/images/btc.png","currencyMark":"BTC","totalAmount":10000,"frozenAmount":0,"createTime":1523150999,"updateTime":1526997086,"sort":3,"tradeStatus":0,"exchangeStatus":0,"detail":"0","deleteFlag":0,"auditor":"admin","transactionStatus":0,"release":0,"picture":"/upload/image/20180522/b9b47f3e6a9978f4b4220151c4edb66e.png","isQuick":"1"},"frozenAmount":100,"frozenAmountStr":"100","amount":500,"amountStr":"500","amountStrs":"500","status":"0","open":0,"walletAddress":"0B123456789123456789","price":12206210,"priceStr":"12206210","profit":0},{"id":3,"userId":"15874986188","accountName":"15874986188","netAddress":"192.168.0.1","currency":{"id":20,"currencyName":"莱特币","currencyIcon":"/upload/images/ltc.png","currencyMark":"LTC","totalAmount":1000,"frozenAmount":0,"createTime":1524213808,"updateTime":1526997099,"sort":4,"tradeStatus":0,"exchangeStatus":0,"detail":" ","deleteFlag":0,"auditor":"admin","transactionStatus":0,"release":0,"picture":"/upload/image/20180522/884e8c91a02853aaeebfbfea05aa46b9.png","isQuick":"1"},"frozenAmount":100,"frozenAmountStr":"100","amount":600,"amountStr":"600","amountStrs":"600","status":"0","open":0,"price":179310,"priceStr":"179310","profit":0},{"userId":"15874986188","accountName":"15874986188","currency":{"id":21,"currencyName":"狗狗币","currencyIcon":"/upload/images/doge.png","currencyMark":"DOGE","totalAmount":0,"frozenAmount":0,"createTime":1526989674,"updateTime":1526997035,"sort":5,"tradeStatus":0,"exchangeStatus":0,"detail":"3333","deleteFlag":0,"auditor":"admin","transactionStatus":0,"release":0,"picture":"/upload/image/20180522/909719784367f8599920b90becada626.png","isQuick":"1"},"frozenAmount":0,"frozenAmountStr":"0","amount":0,"amountStr":"0","amountStrs":"0","profit":0},{"userId":"15874986188","accountName":"15874986188","currency":{"id":22,"currencyName":"泰达币","currencyMark":"USDT","totalAmount":0,"frozenAmount":0,"createTime":1526989674,"updateTime":1526997035,"sort":6,"tradeStatus":0,"exchangeStatus":0,"deleteFlag":1,"transactionStatus":0,"release":0,"picture":"","isQuick":"0"},"frozenAmount":0,"frozenAmountStr":"0","amount":0,"amountStr":"0","amountStrs":"0","price":0,"priceStr":"0","profit":0},{"userId":"15874986188","accountName":"15874986188","currency":{"id":23,"currencyName":"瑞波币","currencyMark":"XRP","totalAmount":0,"frozenAmount":0,"createTime":1526989674,"updateTime":1526989674,"sort":7,"tradeStatus":0,"exchangeStatus":0,"deleteFlag":0,"transactionStatus":0,"release":0,"picture":"","isQuick":"1"},"frozenAmount":0,"frozenAmountStr":"0","amount":0,"amountStr":"0","amountStrs":"0","price":0,"priceStr":"0","profit":0},{"userId":"15874986188","accountName":"15874986188","currency":{"id":24,"currencyName":"比特现金","currencyMark":"BCH","totalAmount":100,"frozenAmount":0,"createTime":1526989674,"updateTime":1526989674,"sort":8,"tradeStatus":0,"exchangeStatus":0,"deleteFlag":0,"auditor":"admin","transactionStatus":0,"release":0,"picture":"1","isQuick":"1"},"frozenAmount":0,"frozenAmountStr":"0","amount":0,"amountStr":"0","amountStrs":"0","price":0,"priceStr":"0","profit":0},{"userId":"15874986188","accountName":"15874986188","currency":{"id":25,"currencyName":"达士币","currencyMark":"DASH","totalAmount":0,"frozenAmount":0,"createTime":1526989674,"updateTime":1526989674,"sort":9,"tradeStatus":0,"exchangeStatus":0,"deleteFlag":0,"transactionStatus":0,"release":0,"picture":"0","isQuick":"1"},"frozenAmount":0,"frozenAmountStr":"0","amount":0,"amountStr":"0","amountStrs":"0","price":0,"priceStr":"0","profit":0},{"userId":"15874986188","accountName":"15874986188","currency":{"id":26,"currencyName":"eos币","currencyIcon":"/upload/images/eos.png","currencyMark":"EOS","totalAmount":0,"frozenAmount":0,"createTime":1526989674,"updateTime":1526989674,"sort":10,"tradeStatus":0,"exchangeStatus":0,"deleteFlag":0,"transactionStatus":0,"release":0,"picture":"/upload/images/eosdog.png","isQuick":"1"},"frozenAmount":0,"frozenAmountStr":"0","amount":0,"amountStr":"0","amountStrs":"0","price":0,"priceStr":"0","profit":0}]
     */

    private DataBean data;
    private List<DataListBean> dataList;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataBean {
        /**
         * totalBTC : 6141315.27093
         * totalCNY : 12466870
         */

        private double totalBTC;
        private double totalCNY;

        public double getTotalBTC() {

            return totalBTC;
        }

        public void setTotalBTC(double totalBTC) {
            this.totalBTC = totalBTC;
        }

        public double getTotalCNY() {
            return totalCNY;
        }

        public void setTotalCNY(double totalCNY) {
            this.totalCNY = totalCNY;
        }
    }

    public static class DataListBean implements Serializable {
        private static final long serialVersionUID = 23183881212480L;
        /**
         * userId : 15874986188
         * accountName : 15874986188
         * currency : {"id":17,"currencyName":"plus币","currencyIcon":"/upload/images/plus.png","currencyMark":"PLUS","totalAmount":10000,"frozenAmount":0,"createTime":1520824034,"updateTime":1527066844,"sort":1,"tradeStatus":0,"exchangeStatus":1,"detail":"111","deleteFlag":0,"auditor":"admin","transactionStatus":0,"release":1,"picture":"/upload/image/20180523/dce0b777c8177d066ee80e70d5c1c1ca.png","isQuick":"1","price":225,"priceStr":"225"}
         * frozenAmount : 0.
         *
         * frozenAmountStr : 0
         * amount : 0
         * amountStr : 0
         * amountStrs : 0
         * price : 0
         * priceStr : 0
         * profit : 0
         * id : 1
         * netAddress : 192.168.0.1
         * status : 0
         * open : 0
         * walletAddress : 0X123456789
         */

        private String userId;
        private String accountName;
        private CurrencyBean currency;
        private double frozenAmount;
        private String frozenAmountStr;
        /** 总余额
        * */
        private double amount;
        private String amountStr;
        private String amountStrs;
        private double price;
        private String priceStr;
        private double profit;
        private int id;
        private String netAddress;
        private String status;
        private int open;
        private String walletAddress;

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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getPriceStr() {
            return priceStr;
        }

        public void setPriceStr(String priceStr) {
            this.priceStr = priceStr;
        }

        public double getProfit() {
            return profit;
        }

        public void setProfit(double profit) {
            this.profit = profit;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNetAddress() {
            return netAddress;
        }

        public void setNetAddress(String netAddress) {
            this.netAddress = netAddress;
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

        public static class CurrencyBean implements Serializable{
            private static final long serialVersionUID = 23183885212480L;
            /**
             * id : 17
             * currencyName : plus币
             * currencyIcon : /upload/images/plus.png
             * currencyMark : PLUS
             * totalAmount : 10000
             * frozenAmount : 0
             * createTime : 1520824034
             * updateTime : 1527066844
             * sort : 1
             * tradeStatus : 0
             * exchangeStatus : 1
             * detail : 111
             * deleteFlag : 0
             * auditor : admin
             * transactionStatus : 0
             * release : 1
             * picture : /upload/image/20180523/dce0b777c8177d066ee80e70d5c1c1ca.png
             * isQuick : 1
             * price : 225
             * priceStr : 225
             */

            private int id;
            private String currencyName;
            private String currencyIcon;
            private String currencyMark;
            private double totalAmount;
            private double frozenAmount;
            private long createTime;
            private long updateTime;
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
            private double price;
            private String priceStr;

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

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public long getUpdateTime() {
                return updateTime;
            }

            public void setUpdateTime(long updateTime) {
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

            public double getPrice() {
                return price;
            }

            public void setPrice(double price) {
                this.price = price;
            }

            public String getPriceStr() {
                return priceStr;
            }

            public void setPriceStr(String priceStr) {
                this.priceStr = priceStr;
            }
        }
    }
}
