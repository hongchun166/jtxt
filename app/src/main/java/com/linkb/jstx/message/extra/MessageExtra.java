package com.linkb.jstx.message.extra;

import java.util.Arrays;
import java.util.List;

public class MessageExtra {

    public static String TYPE_AT = "AT://";

    public static List<String> getAtAccountList(String extra){
        //@人的时候的消息格式 为发送者@接受者: extra:13298682700AT://17602060697
        int atIndex = extra.indexOf(TYPE_AT);
        String content = extra.substring(atIndex);
        String data = content.replaceFirst(TYPE_AT,"");
        return Arrays.asList(data.split(","));
    }
}
