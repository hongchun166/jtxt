package com.linkb.jstx.network.result;

import java.util.List;

public class QueryExchangeRateResult extends BaseResult {


    /**
     * code : 200
     * dataList : [{"id":317,"exchangeName":"Huobi","coinUrl":"https://www.jinse.com/coin/bitcoin-cash","currencyCode":"bch","logo":"https://resource.jinse.com/www/img/cslogo/huobipro.png?v=1617","vol":0.83,"last":825.26,"high":843.65,"low":814.68,"upTime":1549951078,"num":1012},{"id":314,"exchangeName":"Huobi","coinUrl":"https://www.jinse.com/coin/bitcoin","currencyCode":"btc","logo":"https://resource.jinse.com/www/img/cslogo/huobipro.png?v=1617","vol":1.55,"last":24412.42,"high":24626.6,"low":24140.29,"upTime":1549950931,"num":1012},{"id":316,"exchangeName":"Huobi","coinUrl":"https://www.jinse.com/coin/dash","currencyCode":"dash","logo":"https://resource.jinse.com/www/img/cslogo/huobipro.png?v=1617","vol":6.2,"last":562.26,"high":570.7,"low":520.64,"upTime":1549951071,"num":1012},{"id":315,"exchangeName":"Huobi","coinUrl":"https://www.jinse.com/coin/eos","currencyCode":"eos","logo":"https://resource.jinse.com/www/img/cslogo/huobipro.png?v=1617","vol":781.51,"last":18.7,"high":18.88,"low":18.26,"upTime":1549951065,"num":1012},{"id":320,"exchangeName":"Huobi","coinUrl":"https://www.jinse.com/coin/ethereum","currencyCode":"eth","logo":"https://resource.jinse.com/www/img/cslogo/huobipro.png?v=1617","vol":23.83,"last":813.5,"high":831.27,"low":797.61,"upTime":1549951103,"num":1012},{"id":321,"exchangeName":"Huobi","coinUrl":"https://www.jinse.com/coin/litecoin","currencyCode":"ltc","logo":"https://resource.jinse.com/www/img/cslogo/huobipro.png?v=1617","vol":13.75,"last":298.85,"high":305.21,"low":286.24,"upTime":1549951115,"num":1012},{"id":319,"exchangeName":"Kraken","coinUrl":"https://www.jinse.com/coin/tether","currencyCode":"usdt","logo":"https://resource.jinse.com/www/img/cslogo/kraken.png?v=1617","vol":297.93,"last":6.7,"high":6.74,"low":6.69,"upTime":1549951089,"num":1012},{"id":318,"exchangeName":"Huobi","coinUrl":"https://www.jinse.com/coin/ripple","currencyCode":"xrp","logo":"https://resource.jinse.com/www/img/cslogo/huobipro.png?v=1617","vol":3033.77,"last":2.03,"high":2.08,"low":2,"upTime":1549951084,"num":1012}]
     */

    private List<DataListBean> dataList;

    public List<DataListBean> getDataList() {
        return dataList;
    }

    public void setDataList(List<DataListBean> dataList) {
        this.dataList = dataList;
    }

    public static class DataListBean {
        /**
         * id : 317
         * exchangeName : Huobi
         * coinUrl : https://www.jinse.com/coin/bitcoin-cash
         * currencyCode : bch
         * logo : https://resource.jinse.com/www/img/cslogo/huobipro.png?v=1617
         * vol : 0.83
         * last : 825.26
         * high : 843.65
         * low : 814.68
         * upTime : 1549951078
         * num : 1012
         */

        private int id;
        private String exchangeName;
        private String coinUrl;
        private String currencyCode;
        private String logo;
        private double vol;
        /** 最终汇率
        * */
        private double last;
        private double high;
        private double low;
        private int upTime;
        private int num;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getExchangeName() {
            return exchangeName;
        }

        public void setExchangeName(String exchangeName) {
            this.exchangeName = exchangeName;
        }

        public String getCoinUrl() {
            return coinUrl;
        }

        public void setCoinUrl(String coinUrl) {
            this.coinUrl = coinUrl;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public double getVol() {
            return vol;
        }

        public void setVol(double vol) {
            this.vol = vol;
        }

        public double getLast() {
            return last;
        }

        public void setLast(double last) {
            this.last = last;
        }

        public double getHigh() {
            return high;
        }

        public void setHigh(double high) {
            this.high = high;
        }

        public double getLow() {
            return low;
        }

        public void setLow(double low) {
            this.low = low;
        }

        public int getUpTime() {
            return upTime;
        }

        public void setUpTime(int upTime) {
            this.upTime = upTime;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }
    }
}
