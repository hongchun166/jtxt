package com.linkb.jstx.event;

public class UserInfoChangeEvent {

    public String mChangedUserInfo;

    public UserInfoChangeEvent(String changedUserInfo) {
        this.mChangedUserInfo = changedUserInfo;
    }
}
