
package com.linkb.jstx.message.parser;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.message.builder.Action103Builder;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.model.Group;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

public class Action103MessageParser extends MessageParser {


    private Action103Builder decodeContent(String content) {

        return new Gson().fromJson(content, Action103Builder.class);
    }

    @Override
    public String getMessagePreviewText(Message message) {
        Action103Builder builder = decodeContent(message.content);
        return LvxinApplication.getInstance().getString(R.string.tip_agree_joingroup, builder.name, builder.groupName);
    }

    @Override
    public void displayInRecentView(SystemMessageViewHolder holder, Message message) {

        Action103Builder builder = decodeContent(message.content);
        holder.icon.load(FileURLBuilder.getGroupIconUrl(builder.id), R.drawable.logo_group_normal, 99);
        holder.content.setText(getMessagePreviewText(message));
    }

    @Override
    public void getMessagePreviewText(Message message, EmoticonTextView textView) {
        Action103Builder builder = decodeContent(message.content);
        String result = LvxinApplication.getInstance().getString(R.string.tip_agree_joingroup, builder.name, builder.groupName);

        if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
            String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, result);
            textView.setText(content);
        }else {
            textView.setText(result);
        }
    }

    @Override
    public int getThemeColor() {
        return R.color.theme_blue;
    }

    @Override
    public Group getMessageBody(String content) {
        Action103Builder builder = decodeContent(content);
        Group group = new Group();
        group.id = builder.id;
        group.name = builder.groupName;
        group.category = builder.category;
        group.founder = builder.founder;
        group.summary = builder.summary;
        return group;
    }

}
