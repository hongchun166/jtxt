
package com.linkb.jstx.message.parser;

import com.linkb.R;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;

import java.util.Objects;


public class UserMessageParser extends MessageParser {


    @Override
    public MessageSource getMessageSource(Message msg) {
        if (Objects.equals(msg.sender,Global.getCurrentAccount())) {
            return FriendRepository.queryFriend(msg.receiver);
        }
        return FriendRepository.queryFriend(msg.sender);
    }

    @Override
    public void displayInRecentView(SystemMessageViewHolder holder, Message message) {
    }


    @Override
    public String getMessagePreviewText(Message message) {
        boolean self = Global.getCurrentAccount().equals(message.sender);
        return MessageParserFactory.getPreviewText(message.format, message.content, self);
    }

    @Override
    public void getMessagePreviewText(Message message, EmoticonTextView textView) {
        String str = "";
        boolean self = Global.getCurrentAccount().equals(message.sender);
        str = MessageParserFactory.getPreviewText(message.format, message.content, self);
        if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
            String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, str);
            textView.setText(content);
        }else {
            textView.setText(str);
        }
    }


}
