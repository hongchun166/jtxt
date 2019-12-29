
package com.linkb.jstx.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.linkb.R;
import com.linkb.jstx.adapter.viewholder.ChatRecordViewHolder;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.util.AppTools;

public class ChatRecordListViewAdapter extends BaseChatListViewAdapter<ChatRecordViewHolder>{
    private static final int FACTOR = 9999;



    private Boolean enableCheckMemberInfo = true;

    public ChatRecordListViewAdapter(MessageSource friend) {
        super(friend);
    }


    public void setEnableCheckMemberInfo(Boolean enableCheckMemberInfo) {
        this.enableCheckMemberInfo = enableCheckMemberInfo;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = dataList.get(position);
        if(item instanceof Long){

            return TYPE_DATE_TIME;
        }
        Message message = (Message) item;
        String type = message.format;
        boolean isSelf = self.account.equals(message.sender);
        return isSelf ? Integer.parseInt(type) : Integer.parseInt(type) + FACTOR;

    }

    @Override
    public ChatRecordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_TEXT)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_text, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_IMAGE)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_image, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_VOICE)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_voice, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_FILE)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_file, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_MAP)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_map, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_VIDEO)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_video, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_RED_PACKET)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_red_packet, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_SEND_CARDS)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_send_cards, parent, false));
        }
        if (String.valueOf(viewType).equals(Constant.MessageFormat.FORMAT_COIN_TRANSFER)) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_to_coin_transfer, parent, false));
        }

        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_TEXT) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_text, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_IMAGE) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_image, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_VOICE) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_voice, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_FILE) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_file, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_MAP) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_map, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_VIDEO) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_video, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_RED_PACKET) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_red_packet, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_SEND_CARDS) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_send_cards, parent, false));
        }
        if (viewType == Integer.parseInt(Constant.MessageFormat.FORMAT_COIN_TRANSFER) + FACTOR) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_coin_transfer, parent, false));
        }
        if (viewType == TYPE_DATE_TIME) {
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_chatting_datetime, parent, false));
        }

        return null;
    }


    @Override
    public void onBindMessageViewHolder(ChatRecordViewHolder viewHolder,Message message){
        if (viewHolder.toMessageView != null) {
            viewHolder.toMessageView.displayMessage(message, self, others);
            viewHolder.toMessageView.setOnMessageDeleteListener(this);

        } else {
            viewHolder.fromMessageView.displayMessage(message, others);
            viewHolder.fromMessageView.setOnMessageDeleteListener(this);
            viewHolder.fromMessageView.setEnableCheckMemberInfo(enableCheckMemberInfo);
        }
    }

    @Override
    public void onBindDateTimeViewHolder(ChatRecordViewHolder viewHolder,long time){
        viewHolder.dateTime.setText(AppTools.howTimeAgo(time));
    }
}
