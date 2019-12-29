package com.linkb.jstx.network.result;

import java.util.List;

public class GroupQueryResult extends BaseResult {


    /**
     * code : 200
     * dataList : [{"id":21,"name":"2501","summary":"大喊大叫都觉得就到家第几集","founder":"13888888888","amount":2},{"id":47,"name":"1830611374","summary":"","founder":"15535865655","amount":1},{"id":72,"name":"1860525326","summary":"","founder":"15839860229","amount":1},{"id":73,"name":"1860525386","summary":"","founder":"15040008111","amount":1},{"id":117,"name":"势成社区交流10群","summary":"","founder":"18683168879","amount":1},{"id":151,"name":" 133135124","summary":"","founder":"15107953033","amount":1},{"id":152,"name":"1830611374","summary":"","founder":"15118686880","amount":1}]
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
         * id : 21
         * name : 2501
         * summary : 大喊大叫都觉得就到家第几集
         * founder : 13888888888
         * amount : 2
         */

        private long id;
        private String name;
        private String summary;
        private String founder;
        private int amount;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getFounder() {
            return founder;
        }

        public void setFounder(String founder) {
            this.founder = founder;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
