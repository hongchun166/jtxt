
package com.linkb.jstx.message.parser;

import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.linkb.jstx.app.ClientConfig;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.adapter.viewholder.RecentChatViewHolder;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.jstx.message.extra.MessageExtra;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.R;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;

public class GroupMessageParser extends MessageParser {


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
    public void displayInRecentView(SystemMessageViewHolder holder, Message message) {
    }

    public void displayItemRecentMessageView(RecentChatViewHolder viewHolder, ChatItem chatItem) {
        super.displayItemRecentMessageView(viewHolder,chatItem);
        Message message = chatItem.message;
        Group group = (Group) chatItem.source;
        if (group == null) return;
        if (ClientConfig.isIgnoredGroupMessage(group.id)){
            viewHolder.iconNotifyOff.setVisibility(View.VISIBLE);
            viewHolder.badge.setVisibility(View.GONE);
            int count = ClientConfig.getNewGroupMessageCount(group.id);
            if (count > 0){
                viewHolder.reddot.setVisibility(View.VISIBLE);
                viewHolder.preview.setText(viewHolder.itemView.getContext().getString(R.string.label_rencent_message_ignored,count,getMessagePreviewText(message)));
            }else {
                viewHolder.reddot.setVisibility(View.GONE);
            }
        }else {
            viewHolder.iconNotifyOff.setVisibility(View.GONE);
        }

        if (ClientConfig.hasAtMeGroupMessage(group.id)){
            String previewText = viewHolder.itemView.getContext().getString(R.string.label_rencent_message_at, TextUtils.htmlEncode(getMessagePreviewText(message)));
            viewHolder.preview.setText(Html.fromHtml(previewText));
        }
    }

    @Override
    public void getMessagePreviewText(final Message message, final EmoticonTextView textView) {


        String str = "";
        String name = "";
        final boolean self = message.sender.equals(Global.getCurrentAccount());
        if (!self) {
            if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
                str =  MessageParserFactory.getPreviewText(message.format, message.content, self);
                if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
                    String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, str);
                    textView.setText(content);
                }else {
                    textView.setText(str);
                }
            }else if (message.format.equals(Constant.MessageFormat.FORMAT_SEND_CARDS)){
                str =  MessageParserFactory.getPreviewText(message.format, message.content, self);
                textView.setText(str);
            }else {
                if (TextUtils.isEmpty(message.extra)){
                    name = LvxinApplication.getInstance().getString(R.string.manager) + "：";
                    str =  name + MessageParserFactory.getPreviewText(message.format, message.content, self);
                    if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
                        String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, str);
                        textView.setText(content);
                    }else {
                        textView.setText(str);
                    }
                }else {
//                    name = FriendRepository.queryFriendName(message.extra) + "：";
                    String sender = "";
                    if (message.extra.contains(MessageExtra.TYPE_AT)){
                        int atIndex = message.extra.indexOf(MessageExtra.TYPE_AT);
                        sender = message.extra.substring(0,atIndex);
                    }else {
                        sender = message.extra;
                    }
                    HttpServiceManager.queryPersonInfo(sender, new HttpRequestListener<BasePersonInfoResult>() {
                        @Override
                        public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
                            if (result.isSuccess()){
                                Friend friend = User.UserToFriend(result.getData());
                                FriendRepository.save(friend);
                                String nameStr = friend.name + "：";
                                String string = nameStr + MessageParserFactory.getPreviewText(message.format, message.content, self);
                                if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
                                    String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, string);
                                    textView.setText(content);
                                }else {
                                    textView.setText(string);
                                }
                            }
                        }

                        @Override
                        public void onHttpRequestFailure(Exception e, OriginalCall call) {

                        }
                    });

                }
            }
        }else {
            String strResult =  MessageParserFactory.getPreviewText(message.format, message.content, self);
            if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
                String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, strResult);
                textView.setText(content);
            }else {
                textView.setText(strResult);
            }
        }
    }


    @Override
    public String getMessagePreviewText(Message message) {

        String name = "";
        boolean self = message.sender.equals(Global.getCurrentAccount());
        if (!self) {
            if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
                return MessageParserFactory.getPreviewText(message.format, message.content, self);
            }else {
                if (TextUtils.isEmpty(message.extra)){
                    name = LvxinApplication.getInstance().getString(R.string.manager) + "：";
                }else {
                    if (message.extra.contains(MessageExtra.TYPE_AT)){  //@人的时候的消息格式 为发送者@接受者: extra:13298682700AT://17602060697
                        int atIndex = message.extra.indexOf(MessageExtra.TYPE_AT);
                        String sender = message.extra.substring(0,atIndex);
                        name = FriendRepository.queryFriendName(sender) + "：";
                    }else {
                        name = FriendRepository.queryFriendName(message.extra) + "：";
                    }
                }

                return name + MessageParserFactory.getPreviewText(message.format, message.content, self);
            }

        }
        return MessageParserFactory.getPreviewText(message.format, message.content, self);
    }

}
