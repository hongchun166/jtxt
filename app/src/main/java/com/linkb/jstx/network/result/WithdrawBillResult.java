package com.linkb.jstx.network.result;

import java.util.List;

public class WithdrawBillResult extends BaseResult {


    /**
     * code : 200
     * data : [{"money":20,"add_date":1550217253,"red_resi_count":0,"type":true,"see":false,"userid":"15874986188","id":20,"wallet_address":"0x5461f1a862a15565ac6e1948f98faa5b9738b472","examine":false,"red_type":0,"currencyMark":"ETH","event":"提币","red_count":1,"show_type":false,"red_flag":"tibi425600","icon":"/upload/currency/eth.png","total":80,"auditor":"15874986188","currency_id":18,"red_single":0,"primary_money":100,"from_userid":"me","status":0,"tftype":false,"rate_money":1},{"money":20,"add_date":1550217253,"red_resi_count":0,"type":true,"icon":"/upload/currency/btc.png","see":false,"userid":"15874986188","id":21,"examine":false,"red_type":0,"event":"提币","red_count":1,"show_type":false,"red_flag":"tibi425666","currencyMark":"BTC","auditor":"15874986188","wallet_address":"0x5461f1a862a15565ac6e1948f98faa5b9738b466","red_single":0,"currency_id":19,"rate_money":2,"from_userid":"me","primary_money":200,"total":180,"status":0,"tftype":false},{"red_resi_count":0,"id":24,"type":true,"userid":"15874986188","money":100,"wallet_address":"0x5461f1a862a15565ac6e1948f98faa5b9738b472","red_type":0,"currencyMark":"ETH","total":1000.2555,"event":"提币","red_count":1,"add_date":1550480973,"icon":"/upload/currency/eth.png","auditor":"15874986188","primary_money":1100.2555,"currency_id":18,"red_single":0,"red_flag":"tibi562036","from_userid":"me","money_remark":"提币100","tftype":false,"rate_money":1},{"red_resi_count":0,"type":true,"userid":"15874986188","money":100,"total":9690,"red_type":0,"money_remark":"","primary_money":9790,"currencyMark":"ETH","event":"提币","red_count":1,"add_date":1550818831,"icon":"/upload/currency/eth.png","wallet_address":"0x80acc6b903c2fc1e8a01850ad1552ab96e35283c","auditor":"15874986188","currency_id":18,"red_single":0,"id":46,"from_userid":"me","red_flag":"tibi790975","tftype":false,"rate_money":1},{"red_resi_count":0,"type":true,"total":9600,"userid":"15874986188","money":90,"primary_money":9690,"red_flag":"tibi223635","red_type":0,"currencyMark":"ETH","event":"提币","red_count":1,"add_date":1550824649,"icon":"/upload/currency/eth.png","wallet_address":"0x80acc6b903c2fc1e8a01850ad1552ab96e35283c","auditor":"15874986188","currency_id":18,"red_single":0,"id":47,"rate_money":0.9,"from_userid":"me","money_remark":"测试","tftype":false},{"red_resi_count":0,"type":true,"money":60,"userid":"15874986188","primary_money":9600,"red_type":0,"money_remark":"","currencyMark":"ETH","add_date":1550825037,"event":"提币","red_count":1,"rate_money":0.6,"icon":"/upload/currency/eth.png","wallet_address":"0x80acc6b903c2fc1e8a01850ad1552ab96e35283c","auditor":"15874986188","id":48,"currency_id":18,"red_single":0,"total":9540,"from_userid":"me","red_flag":"tibi326382","tftype":false},{"red_resi_count":0,"type":true,"red_flag":"tibi394838","userid":"15874986188","total":9485,"rate_money":0.55,"red_type":0,"money_remark":"","currencyMark":"ETH","event":"提币","red_count":1,"icon":"/upload/currency/eth.png","add_date":1550826827,"wallet_address":"0x80acc6b903c2fc1e8a01850ad1552ab96e35283c","auditor":"15874986188","id":49,"money":55,"currency_id":18,"red_single":0,"primary_money":9540,"from_userid":"me","tftype":false}]
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
         * money : 20.0
         * add_date : 1550217253
         * red_resi_count : 0
         * type : true
         * see : false
         * userid : 15874986188
         * id : 20
         * wallet_address : 0x5461f1a862a15565ac6e1948f98faa5b9738b472
         * examine : false
         * red_type : 0
         * currencyMark : ETH
         * event : 提币
         * red_count : 1
         * show_type : false
         * red_flag : tibi425600
         * icon : /upload/currency/eth.png
         * total : 80.0
         * auditor : 15874986188
         * currency_id : 18
         * red_single : 0.0
         * primary_money : 100.0
         * from_userid : me
         * status : 0
         * tftype : false
         * rate_money : 1.0
         * money_remark : 提币100
         */

        private double money;
        private long add_date;
        private int red_resi_count;
        private boolean type;
        private boolean see;
        private String userid;
        private int id;
        private String wallet_address;
        private boolean examine;
        private int red_type;
        private String currencyMark;
        private String event;
        private int red_count;
        private boolean show_type;
        private String red_flag;
        private String icon;
        private double total;
        private String auditor;
        private int currency_id;
        private double red_single;
        private double primary_money;
        private String from_userid;
        private int status;
        private boolean tftype;
        private double rate_money;
        private String money_remark;

        public int getBillType() {
            return billType;
        }

        public void setBillType(int billType) {
            this.billType = billType;
        }

        private int billType;

        public double getMoney() {
            return money;
        }

        public void setMoney(double money) {
            this.money = money;
        }

        public long getAdd_date() {
            return add_date;
        }

        public void setAdd_date(long add_date) {
            this.add_date = add_date;
        }

        public int getRed_resi_count() {
            return red_resi_count;
        }

        public void setRed_resi_count(int red_resi_count) {
            this.red_resi_count = red_resi_count;
        }

        public boolean isType() {
            return type;
        }

        public void setType(boolean type) {
            this.type = type;
        }

        public boolean isSee() {
            return see;
        }

        public void setSee(boolean see) {
            this.see = see;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getWallet_address() {
            return wallet_address;
        }

        public void setWallet_address(String wallet_address) {
            this.wallet_address = wallet_address;
        }

        public boolean isExamine() {
            return examine;
        }

        public void setExamine(boolean examine) {
            this.examine = examine;
        }

        public int getRed_type() {
            return red_type;
        }

        public void setRed_type(int red_type) {
            this.red_type = red_type;
        }

        public String getCurrencyMark() {
            return currencyMark;
        }

        public void setCurrencyMark(String currencyMark) {
            this.currencyMark = currencyMark;
        }

        public String getEvent() {
            return event;
        }

        public void setEvent(String event) {
            this.event = event;
        }

        public int getRed_count() {
            return red_count;
        }

        public void setRed_count(int red_count) {
            this.red_count = red_count;
        }

        public boolean isShow_type() {
            return show_type;
        }

        public void setShow_type(boolean show_type) {
            this.show_type = show_type;
        }

        public String getRed_flag() {
            return red_flag;
        }

        public void setRed_flag(String red_flag) {
            this.red_flag = red_flag;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

        public String getAuditor() {
            return auditor;
        }

        public void setAuditor(String auditor) {
            this.auditor = auditor;
        }

        public int getCurrency_id() {
            return currency_id;
        }

        public void setCurrency_id(int currency_id) {
            this.currency_id = currency_id;
        }

        public double getRed_single() {
            return red_single;
        }

        public void setRed_single(double red_single) {
            this.red_single = red_single;
        }

        public double getPrimary_money() {
            return primary_money;
        }

        public void setPrimary_money(double primary_money) {
            this.primary_money = primary_money;
        }

        public String getFrom_userid() {
            return from_userid;
        }

        public void setFrom_userid(String from_userid) {
            this.from_userid = from_userid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isTftype() {
            return tftype;
        }

        public void setTftype(boolean tftype) {
            this.tftype = tftype;
        }

        public double getRate_money() {
            return rate_money;
        }

        public void setRate_money(double rate_money) {
            this.rate_money = rate_money;
        }

        public String getMoney_remark() {
            return money_remark;
        }

        public void setMoney_remark(String money_remark) {
            this.money_remark = money_remark;
        }
    }
}
