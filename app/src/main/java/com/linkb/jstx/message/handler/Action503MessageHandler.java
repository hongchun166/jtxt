
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.database.MomentRepository;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.database.CommentRepository;


public class Action503MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {

        long articleId = Long.parseLong(message.getContent());
        MomentRepository.deleteById(articleId);
        CommentRepository.deleteByTargetId(articleId);
        MessageRepository.deleteById(message.getId());
        MessageRepository.deleteByActionAndContent(Constant.MessageAction.ACTION_500, message.getContent());
        return true;
    }

}
