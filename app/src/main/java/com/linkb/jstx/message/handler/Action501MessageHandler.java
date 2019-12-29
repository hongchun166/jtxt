
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.model.Comment;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.database.CommentRepository;
import com.google.gson.Gson;


public class Action501MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        Comment comment = new Gson().fromJson(message.getContent(), Comment.class);
        CommentRepository.add(comment);

        return true;
    }

}
