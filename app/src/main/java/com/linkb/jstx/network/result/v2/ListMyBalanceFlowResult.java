package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.util.TimeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ListMyBalanceFlowResult extends BaseResult {

    /**
     * {"code":200,"message":"成功",
     * "data":[
     * {"id":5,"account":"18684758953","amount":200.00,"remark":"一级推荐奖励","currencyName":"KKC","currencyIcon":"图标","addTime":"2020-01-03T07:06:59.000+0000","type":3,"currencyId":1},
     * {"id":12,"account":"18684758953","amount":100.00,"remark":"二级推荐奖励","currencyName":"KKC","currencyIcon":"图标","addTime":"2020-01-03T07:21:27.000+0000","type":3,"currencyId":1}
     * ]}
     */
    List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean{

        private int id;
        private String account;
        private int amount;
        private  String remark;
        private  String currencyName;
        private String currencyIcon;
        private  String addTime;
        private  int type;
        private  String currencyId;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
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

        public String getAddTime() {
            return addTime;
        }
        public String getAddTimeFinal(){
            return TimeUtils.timeToStr(addTime);
        }
        public void setAddTime(String addTime) {
            this.addTime = addTime;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCurrencyId() {
            return currencyId;
        }

        public void setCurrencyId(String currencyId) {
            this.currencyId = currencyId;
        }

    }

}
