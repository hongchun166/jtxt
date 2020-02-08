package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

public class GetMessageDestroySwithResult extends BaseResult {

   private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean{
        private int messageDestroySwith;

        public int getMessageDestroySwith() {
            return messageDestroySwith;
        }

        public void setMessageDestroySwith(int messageDestroySwith) {
            this.messageDestroySwith = messageDestroySwith;
        }
    }
}
