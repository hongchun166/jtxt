
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.MessageRepository;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.database.CommentRepository;

public class Action504MessageHandler implements CustomMessageHandler {
    @Override
    public boolean handle(Context context, Message message) {
        MessageRepository.deleteById(message.getId());
        CommentRepository.deleteById(message.getContent());
        MessageRepository.deleteByActionAndExtra(Constant.MessageAction.ACTION_501, message.getContent());
        MessageRepository.deleteByActionAndExtra(Constant.MessageAction.ACTION_502, message.getContent());

        return true;
    }

}
