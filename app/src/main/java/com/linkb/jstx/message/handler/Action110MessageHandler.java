
package com.linkb.jstx.message.handler;


import android.content.Context;
import android.content.Intent;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.GlideImageRepository;
import com.linkb.jstx.database.MessageRepository;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.util.FileURLBuilder;

public class Action110MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        MessageRepository.deleteById(message.getId());
        if (message.getSender().equals(message.getReceiver())) {
            return false;
        }

        String account = message.getContent();

        GlideImageRepository.save(FileURLBuilder.getUserIconUrl(account), String.valueOf(System.currentTimeMillis()));

        Friend friend = FriendRepository.queryFriend(account, mListener);
        if (friend != null){
            ChatItem chatItem = new ChatItem(friend,null);
            Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_LOGO);
            intent.putExtra(ChatItem.NAME, chatItem);
            LvxinApplication.sendLocalBroadcast(intent);
        }

        return true;

    }

    private HttpRequestListener<BasePersonInfoResult> mListener = new HttpRequestListener<BasePersonInfoResult>() {
        @Override
        public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
            if (result.isSuccess()){
                Friend friend = User.UserToFriend(result.getData());
                FriendRepository.save(friend);
                postQueryFriend(friend);
            }
        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    private void postQueryFriend(Friend friend){
        ChatItem chatItem = new ChatItem(friend,null);
        Intent intent = new Intent(Constant.Action.ACTION_RECENT_REFRESH_LOGO);
        intent.putExtra(ChatItem.NAME, chatItem);
        LvxinApplication.sendLocalBroadcast(intent);
    }
}
