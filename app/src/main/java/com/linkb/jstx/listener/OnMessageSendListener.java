
package com.linkb.jstx.listener;

import com.linkb.jstx.model.Message;

public interface OnMessageSendListener {
    void onMessageSendSuccess(Message msg);

    void onMessageSendFailure(Message msg);

}
