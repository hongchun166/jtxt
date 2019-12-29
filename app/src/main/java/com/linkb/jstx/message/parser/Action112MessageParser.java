
package com.linkb.jstx.message.parser;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.message.builder.Action107Builder;
import com.linkb.jstx.message.builder.Action112Builder;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.Message;
import com.linkb.R;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

public class Action112MessageParser extends MessageParser {


    @Override
    public MessageSource getMessageSource(Message msg) {
        Group group1 = GroupRepository.queryById(msg.sender);
        Group group2 = GroupRepository.queryById(msg.receiver);
//        if (Constant.MessageAction.ACTION_1.equals(msg.action)) {
//            group = GroupRepository.queryById(msg.receiver);
//        } else {
//            group = GroupRepository.queryById(msg.sender);
//
//        }
        return group1 == null ? group2 : group1;
    }

    @Override
    public Action112Builder decodeContent(Message message) {

        return new Gson().fromJson(message.content, Action112Builder.class);
    }

    @Override
    public String getMessagePreviewText(Message message) {
        Action112Builder builder = decodeContent(message);
        return (LvxinApplication.getInstance().getString(R.string.tip_quit_group_completed, builder.name, builder.groupName));

    }

    @Override
    public void getMessagePreviewText(Message message, EmoticonTextView textView) {
        Action112Builder builder = decodeContent(message);
        String str = (LvxinApplication.getInstance().getString(R.string.tip_quit_group_completed, builder.name, builder.groupName));
        if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
            String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, str);
            textView.setText(content);
        }else {
            textView.setText(str);
        }
    }

    @Override
    public void displayInRecentView(SystemMessageViewHolder holder, Message message) {

        Action112Builder builder = decodeContent(message);
        holder.icon.load(FileURLBuilder.getUserIconUrl(builder.account), R.mipmap.lianxiren, 99);
        holder.content.setText(getMessagePreviewText(message));
    }

    @Override
    public int getThemeColor() {
        return R.color.theme_blue;
    }

}
