
package com.linkb.jstx.message.parser;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.message.builder.Action104Builder;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.model.Message;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

public class Action104MessageParser extends MessageParser {


    @Override
    public String getMessagePreviewText(Message message) {
        Action104Builder builder = decodeContent(message);
        return LvxinApplication.getInstance().getString(R.string.tip_dissolve_group_completed, builder.name);

    }


    @Override
    public Action104Builder decodeContent(Message message) {

        return new Gson().fromJson(message.content, Action104Builder.class);
    }


    @Override
    public void displayInRecentView(SystemMessageViewHolder holder, Message message) {

        Action104Builder builder = decodeContent(message);
        holder.icon.load(FileURLBuilder.getGroupIconUrl(builder.id), R.drawable.logo_group_normal, 99);
        holder.content.setText(getMessagePreviewText(message));
    }

    @Override
    public void getMessagePreviewText(Message message, EmoticonTextView textView) {
        Action104Builder builder = decodeContent(message);
        String str = LvxinApplication.getInstance().getString(R.string.tip_dissolve_group_completed, builder.name);

        if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
            String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, str);
            textView.setText(content);
        }else {
            textView.setText(str);
        }

    }


    @Override
    public int getThemeColor() {
        return R.color.theme_blue;
    }
}
