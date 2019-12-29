
package com.linkb.jstx.listener;


import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.model.MessageSource;

public interface OnChatingHandlerListener {

    void onChatClicked(ChatItem chatItem);

    void onChatLongClicked(ChatItem chat);
}
