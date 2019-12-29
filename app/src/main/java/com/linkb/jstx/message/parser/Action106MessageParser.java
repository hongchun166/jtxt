
package com.linkb.jstx.message.parser;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.component.EmoticonTextView;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.message.builder.Action103Builder;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.database.GroupRepository;
import com.linkb.R;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.util.FileURLBuilder;

public class Action106MessageParser extends Action103MessageParser {

    @Override
    public void getMessagePreviewText(final Message message, final EmoticonTextView textView) {
        HttpServiceManager.queryPersonInfo(message.content, new HttpRequestListener<BasePersonInfoResult>() {
            @Override
            public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
                if (result.isSuccess()){
                    Friend friend = User.UserToFriend(result.getData());
                    FriendRepository.save(friend);

                    String name = friend.name;
                    String groupName  = GroupRepository.queryName(Long.valueOf(message.extra));
                    String str = (LvxinApplication.getInstance().getString(R.string.tip_agree_invitegroup,name, groupName));

                    if (message.format.equals(Constant.MessageFormat.FORMAT_RED_PACKET)){
                        String content = LvxinApplication.getInstance().getString(R.string.label_message_to_red_packet, str);
                        textView.setText(content);
                    }else {
                        textView.setText(str);
                    }
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });

    }

    @Override
    public String getMessagePreviewText(Message message) {
        String name = FriendRepository.queryFriendName(message.content);
        String groupName  = GroupRepository.queryName(Long.valueOf(message.extra));
        return (LvxinApplication.getInstance().getString(R.string.tip_agree_invitegroup,name, groupName));

    }


    @Override
    public void displayInRecentView(SystemMessageViewHolder holder, Message message) {

        holder.icon.load(FileURLBuilder.getUserIconUrl(message.content), R.mipmap.lianxiren, 99);
        holder.content.setText(getMessagePreviewText(message));
    }


    @Override
    public int getThemeColor() {
        return R.color.theme_blue;
    }
}
