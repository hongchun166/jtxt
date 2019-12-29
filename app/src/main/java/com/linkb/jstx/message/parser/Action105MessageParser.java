
package com.linkb.jstx.message.parser;

import android.view.View;

import com.google.gson.Gson;
import com.linkb.R;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.message.builder.Action105Builder;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;

public class Action105MessageParser extends Action102MessageParser {


    @Override
    public Action105Builder decodeContent(String content) {

        return new Gson().fromJson(content, Action105Builder.class);
    }


    @Override
    public String getMessagePreviewText(Message msg) {
        Action105Builder builder = decodeContent(msg.content);
        return (LvxinApplication.getInstance().getString(R.string.tip_request_invitegroup, builder.name, builder.groupName));

    }

    @Override
    public void getMessagePreviewText(Message message, EmoticonTextView textView) {
        Action105Builder builder = decodeContent(message.content);

        String str = (LvxinApplication.getInstance().getString(R.string.tip_request_invitegroup, builder.name, builder.groupName));
        if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
            String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, str);
            textView.setText(content);
        }else {
            textView.setText(str);
        }
    }

    /**
     * 解析消息的来源对象
     *
     * @param content
     * @return
     */
    @Override
    public MessageSource getMessageBody(String content) {
        Action105Builder builder = decodeContent(content);
        Friend friend = new Friend();
        friend.name = builder.name;
        friend.account = builder.account;
        return friend;
    }

    @Override
    public void displayInRecentView(final SystemMessageViewHolder holder, final Message message) {
        super.displayInRecentView(holder, message);
        holder.content.setVisibility(View.VISIBLE);
        holder.content.setText(getMessagePreviewText(message));
    }

    @Override
    public int getThemeColor() {
        return R.color.theme_blue;
    }
}
