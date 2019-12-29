
package com.linkb.jstx.message.parser;


import android.support.v4.util.ArrayMap;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MessageParserFactory {

    private  ArrayMap<String, MessageParser> parsers = new ArrayMap<>();
    private  Properties properties = new Properties();
    private static class InstanceHolder{
        private static MessageParserFactory factory = new MessageParserFactory();
    }
    private MessageParserFactory() {
        //加载各个类型消息解析器
        try {
            InputStream in = LvxinApplication.getInstance().getAssets().open("properties/parsers.properties");
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MessageParserFactory getFactory() {
        return InstanceHolder.factory;
    }

    public static String getPreviewText(String fileType, String content, boolean self) {
        if (Constant.MessageFormat.FORMAT_IMAGE.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_image);
        }
        if (Constant.MessageFormat.FORMAT_FILE.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_file);
        }
        if (Constant.MessageFormat.FORMAT_VOICE.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_voice);
        }
        if (Constant.MessageFormat.FORMAT_MAP.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_map);
        }
        if (Constant.MessageFormat.FORMAT_VIDEO.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_video);
        }
        if (self && Constant.MessageFormat.FORMAT_TEXT.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_preview_normal, content);
        }
        if (self && Constant.MessageFormat.FORMAT_RED_PACKET.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_from_red_packet, content);
        }
        if (Constant.MessageFormat.FORMAT_SEND_CARDS.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_to_send_cards, content);
        }
        if (Constant.MessageFormat.FORMAT_COIN_TRANSFER.equals(fileType)) {
            return LvxinApplication.getInstance().getString(R.string.label_message_to_coin_transfer, content);
        }
        return content;
    }

    public MessageParser getMessageParser(String msgType) {

        MessageParser messageParser = parsers.get(msgType);
        if (messageParser == null) {
            try {
                messageParser = (MessageParser) Class.forName(properties.getProperty(msgType)).newInstance();
                parsers.put(msgType,messageParser);
            } catch (Exception e) {}
        }

        return messageParser;
    }

    public MessageSource parserMessageSource(Message message) {
        MessageParser messageParser = getMessageParser(message.action);
        if (messageParser == null) {
           return null;
        }
        return messageParser.getMessageSource(message);
    }

    public String parserMessageText(Message message) {
        MessageParser messageParser = getMessageParser(message.action);
        if (messageParser == null) {
            return null;
        }
        return messageParser.getMessagePreviewText(message);
    }

    public MessageSource parserMessageBody(com.farsunset.cim.sdk.android.model.Message message) {
        MessageParser messageParser = getMessageParser(message.getAction());
        if (messageParser == null) {
            return null;
        }
        return messageParser.getMessageBody(message.getContent());
    }
}
