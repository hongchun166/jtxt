package com.linkb.jstx.message.extra;

import android.text.TextUtils;

import java.util.HashSet;
import java.util.Set;

public class MessageExtraAt   {

    private Set<String> accountList = new HashSet<>();
    public void add(String account){
        accountList.add(account);
    }
    public void remove(String account){
        accountList.remove(account);
    }

    public String toExtraString(){
        if (accountList.isEmpty()){
            return null;
        }
        return MessageExtra.TYPE_AT + TextUtils.join(",",accountList);
    }

    public void clear() {
        accountList.clear();
    }
}
