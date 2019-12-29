
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.adapter.viewholder.SystemMessageViewHolder;
import com.linkb.jstx.message.parser.MessageParser;
import com.linkb.jstx.message.parser.MessageParserFactory;
import com.linkb.jstx.model.Message;
import com.linkb.R;
import com.linkb.jstx.util.AppTools;

import java.util.ArrayList;
import java.util.List;


public class SystemMessageListAdapter extends RecyclerView.Adapter<SystemMessageViewHolder> {

    private List<Message> list = new ArrayList<>();

    @Override
    public SystemMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SystemMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_system_message, parent, false));
    }

    @Override
    public void onBindViewHolder(SystemMessageViewHolder holder, int position) {
        final Message msg = list.get(position);

        MessageParser messageParser = MessageParserFactory.getFactory().getMessageParser(msg.action);
        if (messageParser != null){
            holder.categoryText.setText(messageParser.getCategoryText());
            holder.tipText.setVisibility(View.GONE);
            holder.handleButton.setVisibility(View.GONE);
            messageParser.displayInRecentView(holder, msg);
            holder.time.setText(AppTools.getDateTimeString(msg.timestamp));
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addMessage(Message message) {
        list.add(0, message);
        notifyItemInserted(0);
    }

    public void addAllMessage(List<Message> list) {
        this.list.addAll(0, list);
        notifyItemRangeInserted(0, list.size());
    }

    public void onItemChanged(Message target) {
        int index = list.indexOf(target);
        if (index >= 0) {
            list.set(index, target);
            notifyItemChanged(index);
        }
    }


    public Message getLastMessage() {
        return list == null || list.isEmpty() ? null :list.get(0);
    }
}
