
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.farsunset.cim.sdk.android.model.Message;


public interface CustomMessageHandler {

    /**
     * @param @param context
     * @param @param message
     * @return boolean    处理过后该消息是否还有效
     * @throws
     * @Title: handle
     * @Description: TODO(处理各种不同的消息)
     */
    boolean handle(Context context, Message message);
}
