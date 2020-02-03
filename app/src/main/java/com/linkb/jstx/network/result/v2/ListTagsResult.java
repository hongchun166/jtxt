package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

import java.util.List;

public class ListTagsResult extends BaseResult {

    List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean{
       private String id;
        private String name;
        private  String countNumber;

        public DataBean() {
        }

        public DataBean(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCountNumber() {
            return countNumber;
        }

        public void setCountNumber(String countNumber) {
            this.countNumber = countNumber;
        }
    }
}
