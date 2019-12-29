package com.linkb.jstx.event;

public class WechatShareEvent {

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }


    private boolean success;


    public WechatShareEvent(boolean success) {
        this.success = success;
    }
}
