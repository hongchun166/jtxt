package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

public class GetActiveResult extends BaseResult {

    DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private Double avtive;

        public Double getAvtive() {
            return avtive;
        }

        public void setAvtive(Double avtive) {
            this.avtive = avtive;
        }
    }

}
