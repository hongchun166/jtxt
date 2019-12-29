
package com.linkb.jstx.message.request;

import com.linkb.jstx.app.LvxinApplication;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

public class RequestHandlerFactory {

    private static RequestHandlerFactory factory;
    private HashMap<String, RequestHandler> parsers = new HashMap<>();

    private Properties properties = new Properties();

    private RequestHandlerFactory() {
        //加载各个类型消息解析器
        try {
            InputStream in = LvxinApplication.getInstance().getAssets().open("properties/request.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static RequestHandlerFactory getFactory() {
        if (factory == null) {
            factory = new RequestHandlerFactory();
        }

        return factory;
    }

    public RequestHandler getMessageHandler(String msgType) {

        if (parsers.get(msgType) == null) {

            try {
                parsers.put(msgType, (RequestHandler) Class.forName(properties.getProperty(msgType)).newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return parsers.get(msgType);
    }

}
