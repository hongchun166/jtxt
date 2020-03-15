package com.linkb.jstx.network.result.v2;

import android.text.TextUtils;

import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.util.TimeUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ListMyBalanceFlowResult extends BaseResult {
//     * Type
//     * ShouRu(1, "收入"),
//     * ZhiChu(2, "支出"),
//     * JiangLi(3, "奖励锁定币");
//     *
//             *
//             * SourceType
//              JiangLi(1, "奖励锁定币"),
//              Freed(2, "释放币"),
//              TiXian(3, "提现"),
//              ChongZhi(4, "充值"),
//              ZhuanRu(5, "转入"),
//              ZhuanChu(6, " 转出");
//     1: 奖励锁定币, 2: 释放币-收入,3: 提现-支出 , 4: 充值-收入 ,5: 转入-收入 6: 转出-支出
    /**
     * {
     *     "code":200,
     *     "message":"成功",
     *     "data":[
     *         {
     *             "id":271,
     *             "account":"18274839631",
     *             "amount":106.62,
     *             "remark":"资讯红包",
     *             "currencyName":"KKC",
     *             "currencyIcon":"图标",
     *             "addTime":"2020-03-15T04:26:00.000+0000",
     *             "type":3,
     *             "sourceType":1,
     *             "currencyId":1
     *         }
     *     ]
     * }
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
        private double amount;
        private  String remark;
        private  String currencyName;
        private String currencyIcon;
        private  String addTime;
        private  int type;
        private  String currencyId;
        int sourceType;

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

        public double getAmount() {
            return amount;
        }

        public void setAmount(double amount) {
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
            if(TextUtils.isEmpty(addTime)) return "";
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

        public int getSourceType() {
            return sourceType;
        }

        public void setSourceType(int sourceType) {
            this.sourceType = sourceType;
        }
    }

}
