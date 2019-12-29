
package com.linkb.jstx.message.parser;

import com.linkb.R;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.database.MicroServerRepository;
import com.linkb.jstx.message.builder.Action112Builder;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.model.MessageSource;


public class Action200MessageParser extends MessageParser {


    @Override
    public MessageSource getMessageSource(Message msg) {
        return MicroServerRepository.queryById(msg.receiver);
    }


    @Override
    public void displayInRecentView(SystemMessageViewHolder holder, Message message) {
    }


    @Override
    public String getMessagePreviewText(Message message) {
        return message.content;
    }


    @Override
    public void getMessagePreviewText(Message message, EmoticonTextView textView) {
        if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
            String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, message.content);
            textView.setText(content);
        }else {
            textView.setText(message.content);
        }
    }
}
