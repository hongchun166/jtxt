package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

import java.util.List;

/**
 * 行业列表
 */
public class ListIndustryResult  extends BaseResult {

    List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean{
       private String id;
        private  String industryCode;
        private  String industryName;

        public DataBean() {
        }

        public DataBean( String industryName,String industryCode) {
            this.industryCode = industryCode;
            this.industryName = industryName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getIndustryCode() {
            return industryCode;
        }

        public void setIndustryCode(String industryCode) {
            this.industryCode = industryCode;
        }

        public String getIndustryName() {
            return industryName;
        }

        public void setIndustryName(String industryName) {
            this.industryName = industryName;
        }
    }
}
