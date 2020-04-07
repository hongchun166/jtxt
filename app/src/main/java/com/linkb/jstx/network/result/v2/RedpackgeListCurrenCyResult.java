package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

import java.util.List;

public class RedpackgeListCurrenCyResult extends BaseResult {


    /**
     * code : 200
     * data : [{"id":1,"currencyName":"KKC","currencyIcon":"图标","currencyMark":"KKC","totalAmount":3000000,"frozenAmount":2000000,"createTime":"2020-01-03T09:02:37.000+0000","updateTime":"2020-01-03T09:05:50.000+0000","sort":1,"exchangeStatus":1,"detail":"币种介绍","deleteFlag":0,"auditor":"","transactionStatus":1,"releaseFlag":1,"price":0.1,"extractMin":1000,"extractMax":10000}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * currencyName : KKC
         * currencyIcon : 图标
         * currencyMark : KKC
         * totalAmount : 3000000.0
         * frozenAmount : 2000000.0
         * createTime : 2020-01-03T09:02:37.000+0000
         * updateTime : 2020-01-03T09:05:50.000+0000
         * sort : 1
         * exchangeStatus : 1
         * detail : 币种介绍
         * deleteFlag : 0
         * auditor :
         * transactionStatus : 1
         * releaseFlag : 1
         * price : 0.1
         * extractMin : 1000.0
         * extractMax : 10000.0
         */

        private int id;
        private String currencyName;
        private String currencyIcon;
        private String currencyMark;
        private double totalAmount;
        private double frozenAmount;
        private String createTime;
        private String updateTime;
        private int sort;
        private int exchangeStatus;
        private String detail;
        private int deleteFlag;
        private String auditor;
        private int transactionStatus;
        private int releaseFlag;
        private double price;
        private double extractMin;
        private double extractMax;


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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
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

        public int getReleaseFlag() {
            return releaseFlag;
        }

        public void setReleaseFlag(int releaseFlag) {
            this.releaseFlag = releaseFlag;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getExtractMin() {
            return extractMin;
        }

        public void setExtractMin(double extractMin) {
            this.extractMin = extractMin;
        }

        public double getExtractMax() {
            return extractMax;
        }

        public void setExtractMax(double extractMax) {
            this.extractMax = extractMax;
        }
    }
}
