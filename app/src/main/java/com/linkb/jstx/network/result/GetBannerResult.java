package com.linkb.jstx.network.result;

import java.util.List;

public class GetBannerResult extends BaseResult {

    /**
     * code : 200
     * dataList : [{"id":1,"name":"地方","url":"https://blink-news.oss-cn-shenzhen.aliyuncs.com/banner/banner_1"}]
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
         * id : 1
         * name : 地方
         * url : https://blink-news.oss-cn-shenzhen.aliyuncs.com/banner/banner_1
         */

        private int id;
        private String name;
        private String url;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
