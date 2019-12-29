
package com.linkb.jstx.message.parser;

import android.view.View;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.R;

public class SystemMessageParser extends MessageParser {


    @Override
    public void displayInRecentView(SystemMessageViewHolder holder, Message message) {
        holder.content.setText(message.content.trim());
        holder.content.setVisibility(View.VISIBLE);
        holder.icon.setImageResource(R.drawable.icon_system_notify);
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
