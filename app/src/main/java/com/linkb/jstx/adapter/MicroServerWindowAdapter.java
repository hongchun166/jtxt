
package com.linkb.jstx.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.adapter.viewholder.MicroServerWindowHolder;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.R;
import com.linkb.jstx.util.AppTools;

import java.util.Objects;

public class MicroServerWindowAdapter extends BaseChatListViewAdapter<MicroServerWindowHolder>{
    private static final int TYPE_TO_TEXT = 0;
    private static final int TYPE_FROM_TEXT = 1;
    private static final int TYPE_FROM_LINK = 2;
    private static final int TYPE_FROM_LINKPANEL = 3;

    public MicroServerWindowAdapter(MessageSource source) {
        super(source);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = dataList.get(position);
        if(item instanceof Long){

            return TYPE_DATE_TIME;
        }
        Message message = (Message) item;
        String type = message.format;
        boolean isSelf = Objects.equals(self.account,message.sender);

        if (!isSelf && Constant.MessageFormat.FORMAT_TEXT.equals(type)) {
            return TYPE_FROM_TEXT;
        }
        if (!isSelf && Constant.MessageFormat.FORMAT_LINK.equals(type)) {
            return TYPE_FROM_LINK;
        }
        if (!isSelf && Constant.MessageFormat.FORMAT_LINKLIST.equals(type)) {
            return TYPE_FROM_LINKPANEL;
        }
        return TYPE_TO_TEXT;
    }

    @Override
    public MicroServerWindowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View chatItemView = null;
        if (viewType == TYPE_TO_TEXT) {
            chatItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_microserver_to_text, parent, false);
        }

        if (viewType == TYPE_FROM_TEXT) {
            chatItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_microserver_from_text, parent, false);
        }
        if (viewType == TYPE_FROM_LINK) {
            chatItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_microserver_from_link, parent, false);
        }
        if (viewType == TYPE_FROM_LINKPANEL) {
            chatItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_microserver_from_linkpanel, parent, false);
        }
        if (viewType == TYPE_DATE_TIME) {
            chatItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatting_datetime, parent, false);
        }
        return new MicroServerWindowHolder(chatItemView);
    }

    @Override
    public void onBindDateTimeViewHolder(MicroServerWindowHolder viewHolder, long time){
        viewHolder.dateTime.setText(AppTools.howTimeAgo(time));
    }

    @Override
    public void onBindMessageViewHolder(MicroServerWindowHolder viewHolder, Message message) {

        if (viewHolder.toTextMessageView != null)
        {
            viewHolder.toTextMessageView.displayMessage(message,others);
            viewHolder.toTextMessageView.setOnMessageDeleteListener(this);
            return;
        }
        if (viewHolder.replyMessageView != null)
        {
            viewHolder.replyMessageView.displayMessage(message,others);
            viewHolder.replyMessageView.setOnMessageDeleteListener(this);
        }
    }

}
