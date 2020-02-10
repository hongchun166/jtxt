package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

public class UpdateMessageDestroyTimeResult extends BaseResult {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private int messageDestroyTime;

        public int getMessageDestroyTime() {
            return messageDestroyTime;
        }

        public void setMessageDestroyTime(int messageDestroyTime) {
            this.messageDestroyTime = messageDestroyTime;
        }
    }

}
