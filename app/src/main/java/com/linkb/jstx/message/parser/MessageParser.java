
package com.linkb.jstx.message.parser;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.adapter.viewholder.RecentChatViewHolder;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.message.builder.BaseBuilder;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.model.SystemMessage;
import com.linkb.R;
import com.google.gson.Gson;

public abstract class MessageParser {


    /**
     * 将json格式消息内容解析为一个对象
     *
     * @param message
     * @return
     */
    public BaseBuilder decodeContent(Message message) {

        return new Gson().fromJson(message.content, BaseBuilder.class);
    }

    /**
     * 解析消息的来源对象
     *
     * @return
     */
    public MessageSource getMessageSource(Message message) {

        return new SystemMessage(message.action);
    }

    /**
     * 解析消息的内容对象
     *
     * @return
     */
    public MessageSource getMessageBody(String content) {

        return new SystemMessage(Constant.MessageAction.ACTION_2);
    }

    /**
     * 将消息内容再listview中显示
     *
     * @param holder
     * @param message
     */
    public abstract void displayInRecentView(SystemMessageViewHolder holder, Message message);


    /**
     * 将消息内容最近聊天列表中显示
     *
     */
    public void displayItemRecentMessageView(RecentChatViewHolder viewHolder, ChatItem chatItem){
        MessageSource source = chatItem.source;
        if (source == null) return;
        Message message = chatItem.message;
        viewHolder.itemView.setTag(source);
        viewHolder.itemView.setTag(R.id.message, chatItem);
        viewHolder.reddot.setVisibility(View.GONE);
        viewHolder.iconNotifyOff.setVisibility(View.GONE);
        //显示消息来源名字
        if (source != null && !TextUtils.isEmpty(source.getTitle())){
            viewHolder.name.setText(source.getTitle());
        }else if (TextUtils.isEmpty(source.getTitle())){
            source.aysnTitle(viewHolder.name);
        }
        viewHolder.name.setTextColor(ContextCompat.getColor(viewHolder.itemView.getContext(), source.getTitleColor()));
        //显示消息来源头像
        if (source.getWebIcon() == null) {
            viewHolder.icon.setImageResource(source.getDefaultIconRID());
        } else {
            viewHolder.icon.load(source.getWebIcon(), source.getDefaultIconRID(), 999);
        }

        long noReadSum = MessageRepository.countNewIncludedTypesBySender(source.getId(), source.getMessageAction());
        viewHolder.badge.setText(noReadSum>=99?"99+":String.valueOf(noReadSum));
        viewHolder.badge.setVisibility(noReadSum > 0 ? View.VISIBLE : View.GONE);


        viewHolder.preview.setCompoundDrawables(null, null, null, null);
        if (Constant.MessageStatus.STATUS_SENDING.equals(message.state)) {
            Drawable image = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.item_msg_state_sending);
            image.setBounds(0, 0, (int) (Resources.getSystem().getDisplayMetrics().density * 18), (int) (Resources.getSystem().getDisplayMetrics().density * 18));
            viewHolder.preview.setCompoundDrawables(image, null, null, null);
        }

        if (Constant.MessageStatus.STATUS_SEND_FAILURE.equals(message.state)) {
            Drawable image = ContextCompat.getDrawable(viewHolder.itemView.getContext(), R.drawable.item_msg_state_fail);
            image.setBounds(0, 0, (int) (Resources.getSystem().getDisplayMetrics().density * 15), (int) (Resources.getSystem().getDisplayMetrics().density * 15));
            viewHolder.preview.setCompoundDrawables(image, null, null, null);
        }
        viewHolder.time.setText(AppTools.howTimeAgo(message.timestamp));
        String draft = Global.getLastChatDraft(source);
        if (TextUtils.isEmpty(draft)) {
//            if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
//                String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, getMessagePreviewText(message));
//                viewHolder.preview.setText(content);
//            }else {
//                viewHolder.preview.setText(getMessagePreviewText(message));
//            }
            getMessagePreviewText(message, viewHolder.preview);

        } else {
            viewHolder.preview.setText(Html.fromHtml( viewHolder.itemView.getContext().getString(R.string.label_chat_draft, draft)));
        }

    }

    public abstract String getMessagePreviewText(Message message);

    /**
     * 将消息内容解码为一句字符串
     *
     * @param message
     * @return
     */
    public abstract void getMessagePreviewText(Message message, EmoticonTextView textView);

    public String getCategoryText() {
        return LvxinApplication.getInstance().getString(R.string.common_sysmessage);
    }

    public int getThemeColor() {
        return R.color.theme_orange;
    }


}
