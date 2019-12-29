
package com.linkb.jstx.message.parser;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.linkb.jstx.activity.chat.SystemMessageActivity;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.message.builder.Action102Builder;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.model.SystemMessage;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

public class Action102MessageParser extends MessageParser {

    @Override
    public int getThemeColor() {
        return R.color.theme_blue;
    }

    /**
     * 解析消息的来源对象
     *
     * @param content
     * @return
     */
    @Override
    public MessageSource getMessageBody(String content) {
        Action102Builder builder = decodeContent(content);
        Friend friend = new Friend();
        friend.name = builder.name;
        friend.account = builder.account;
        return friend;
    }

    @Override
    public void displayInRecentView(final SystemMessageViewHolder holder, final Message message) {
        holder.content.setVisibility(View.VISIBLE);
        holder.content.setText(getMessagePreviewText(message));
        Action102Builder builder = decodeContent(message.content);
        holder.tipText.setVisibility(View.VISIBLE);
        if (message.handle == null) {
            holder.handleButton.setVisibility(View.VISIBLE);

            holder.handleButton.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    ((SystemMessageActivity) holder.content.getContext()).onHandleButtonClick(message);
                }
            });

            holder.tipText.setVisibility(View.GONE);

        } else if (message.handle.equals(SystemMessage.RESULT_AGREE)) {

            holder.tipText.setText(R.string.common_has_agree);
            holder.tipText.setBackgroundResource(R.drawable.badge_button_green);
            holder.tipText.setTextColor(ContextCompat.getColor(LvxinApplication.getInstance(), R.color.theme_green));
        } else if (message.handle.equals(SystemMessage.RESULT_IGNORE)) {

            holder.tipText.setText(R.string.common_has_ignore);
            holder.tipText.setBackgroundResource(R.drawable.badge_button);
            holder.tipText.setTextColor(ContextCompat.getColor(LvxinApplication.getInstance(), R.color.text_grey));
        } else if (message.handle.equals(SystemMessage.RESULT_REFUSE)) {
            holder.tipText.setBackgroundResource(R.drawable.badge_button_red);
            holder.tipText.setTextColor(ContextCompat.getColor(LvxinApplication.getInstance(), android.R.color.white));
            holder.tipText.setText(R.string.common_has_refuse);
        }
        holder.icon.load(FileURLBuilder.getUserIconUrl(builder.account), R.mipmap.lianxiren, 99);

    }

    @Override
    public void getMessagePreviewText(Message message, EmoticonTextView textView) {
        StringBuilder sb = new StringBuilder();
        Action102Builder builder = decodeContent(message.content);
        sb.append(LvxinApplication.getInstance().getString(R.string.tip_request_joingroup, builder.name, builder.groupName)).append(TextUtils.isEmpty(builder.message) ? "" : "\n" + LvxinApplication.getInstance().getString(R.string.tip_request_verify, builder.message));

        if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
            String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, sb.toString());
            textView.setText(content);
        }else {
            textView.setText(sb.toString());
        }
    }


    Action102Builder decodeContent(String content) {

        return new Gson().fromJson(content, Action102Builder.class);
    }


    @Override
    public String getMessagePreviewText(Message message) {
        StringBuilder sb = new StringBuilder();
        Action102Builder builder = decodeContent(message.content);
        sb.append(LvxinApplication.getInstance().getString(R.string.tip_request_joingroup, builder.name, builder.groupName)).append(TextUtils.isEmpty(builder.message) ? "" : "\n" + LvxinApplication.getInstance().getString(R.string.tip_request_verify, builder.message));
        return sb.toString();
    }


    @Override
    public String getCategoryText() {
        return LvxinApplication.getInstance().getString(R.string.tip_title_groupmessage);
    }

}
