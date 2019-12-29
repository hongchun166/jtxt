
package com.linkb.jstx.message.handler;

import android.content.Context;
import android.util.ArrayMap;

import com.linkb.jstx.app.LvxinApplication;
import com.farsunset.cim.sdk.android.model.Message;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 各种消息解析处理器
 */
public class CustomMessageHandlerFactory {

    private ArrayMap<String, CustomMessageHandler> parsers = new ArrayMap<>();
    private Properties properties = new Properties();

    private static class InstanceHolder{
        private static  CustomMessageHandlerFactory factory = new CustomMessageHandlerFactory();
    }
    private CustomMessageHandlerFactory() {
        try {
            InputStream in = LvxinApplication.getInstance().getAssets().open("properties/handler.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static CustomMessageHandlerFactory getFactory() {
        return InstanceHolder.factory;
    }

    public boolean handle(Context context, Message message) {

        CustomMessageHandler handler = getMessageHandler(message.getAction());
        if (handler == null) {
            return true;
        }
        try {
            return handler.handle(context, message);
        } catch (Exception e) {
            return true;
        }


    }

    private CustomMessageHandler getMessageHandler(String action) {
        CustomMessageHandler messageHandler = parsers.get(action);
        if (messageHandler == null) {
            try {
                messageHandler = (CustomMessageHandler) Class.forName(properties.getProperty(action)).newInstance();
                parsers.put(action,messageHandler);
            } catch (Exception e) {
            }
        }
        return messageHandler;
    }

}
