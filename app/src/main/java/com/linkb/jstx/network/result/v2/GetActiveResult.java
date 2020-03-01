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
        private int avtive;

        public int getAvtive() {
            return avtive;
        }

        public void setAvtive(int avtive) {
            this.avtive = avtive;
        }
    }

}
