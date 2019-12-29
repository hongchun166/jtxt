
package com.linkb.jstx.message.parser;

import com.linkb.R;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.database.MicroServerRepository;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.network.model.MicroServerLinkMessage;
import com.linkb.jstx.network.model.MicroServerTextMessage;
import com.linkb.jstx.network.model.MomentLink;
import com.google.gson.Gson;


public class Action201MessageParser extends MessageParser {


    @Override
    public MessageSource getMessageSource(Message msg) {
        return MicroServerRepository.queryById(msg.sender);
    }


    @Override
    public void displayInRecentView(SystemMessageViewHolder holder, Message message) {
    }

    @Override
    public void getMessagePreviewText(Message message, EmoticonTextView textView) {
        String str = "";
        if (Constant.MessageFormat.FORMAT_LINK.equals(message.format)) {
            str =  new Gson().fromJson(message.content, MomentLink.class).title;
        }
        if (Constant.MessageFormat.FORMAT_LINKLIST.equals(message.format)) {
            str = new Gson().fromJson(message.content, MicroServerLinkMessage.class).title;
        }
        if (Constant.MessageFormat.FORMAT_TEXT.equals(message.format)) {
            str = new Gson().fromJson(message.content, MicroServerTextMessage.class).content;
        }

        if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
            String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, str);
            textView.setText(content);
        }else {
            textView.setText(str);
        }
    }


    @Override
    public String getMessagePreviewText(Message message) {

        if (Constant.MessageFormat.FORMAT_LINK.equals(message.format)) {
            return new Gson().fromJson(message.content, MomentLink.class).title;
        }
        if (Constant.MessageFormat.FORMAT_LINKLIST.equals(message.format)) {
            return new Gson().fromJson(message.content, MicroServerLinkMessage.class).title;
        }
        if (Constant.MessageFormat.FORMAT_TEXT.equals(message.format)) {
            return new Gson().fromJson(message.content, MicroServerTextMessage.class).content;
        }
        return message.content;
    }


}
