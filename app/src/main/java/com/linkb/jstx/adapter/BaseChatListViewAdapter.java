
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.comparator.MessageTimeAscComparator;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.listener.OnMessageDeleteListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 普通聊天页面的和公众号对话页面消息列表适配，主要实现消息时间显示的逻辑处理
 */
public abstract class BaseChatListViewAdapter <T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> implements OnMessageDeleteListener {
    List<Object> dataList = new ArrayList<>();
    User self;
    MessageSource others;
    static final int TYPE_DATE_TIME = -1;
    BaseChatListViewAdapter(MessageSource friend) {
        super();
        self = Global.getCurrentUser();
        others = friend;
    }

    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public int getItemCount(long messageSize) {
        int count = 0;
        for (int i = 0 ;i<dataList.size(); i++){
            if (dataList.get(i) instanceof Message){
                count++;
            }
            if (count == messageSize){
                return i;
            }

        }
        return count;
    }


    public int getMessaeCount() {
        int count = 0;
        for (Object object : dataList){
            if (object instanceof Message){
                count++;
            }
        }
        return count;
    }

    @Override
    public void onBindViewHolder(T viewHolder, int position) {

        Object item = dataList.get(position);
        if(item instanceof Long){
            onBindDateTimeViewHolder(viewHolder, (Long) item);
            return;
        }
        onBindMessageViewHolder(viewHolder, (Message) item);
    }

    protected abstract void onBindDateTimeViewHolder(T viewHolder, long time);

    protected abstract void onBindMessageViewHolder(T viewHolder, Message message);

    @Override
    public void onDelete(Message msg) {
        MessageRepository.deleteById(msg.id);
        int index = dataList.indexOf(msg);
        /*
          刪除记录时，也要删除记录上面的时间，如果有的话
         */
        if (index > 0 && dataList.get(index-1) instanceof Long)
        {
            dataList.remove(index);
            dataList.remove(index-1);
            notifyItemRemoved(index);
            notifyItemRemoved(index-1);
        }else {
            dataList.remove(index);
            notifyItemRemoved(index);
        }

        /**
         * 删除了第一条消息后，显示最近一个消息的时间
         */
        if (!dataList.isEmpty() && (dataList.get(0) instanceof Message)){
            Message fristMessage = (Message) dataList.get(0);
            dataList.add(0,fristMessage.timestamp);
            notifyItemInserted(0);
        }
    }



    public void addMessage(Message message) {
        //如果是第一个消息，在消息上面显示时间
        if (dataList.isEmpty())
        {
            dataList.add(message.timestamp);
            dataList.add(message);
            notifyItemRangeInserted(0,2);
            return;
        }

        long lastTime =((Message) dataList.get(dataList.size()-1)).timestamp;
        long currTime =message.timestamp;
        //如果最新消息时间比上一个消息间隔2分钟以上，则在最新消息上显示时间
        if(currTime - lastTime >= Constant.CHATTING_TIME_SPACE){
            dataList.add(currTime);
            dataList.add(message);
            notifyItemRangeInserted(dataList.size()-2,2);
        }else {
            dataList.add(message);
            notifyItemInserted(dataList.size() - 1);
        }
    }

    public void addAllMessage(List<Message> list) {
        Collections.sort(list, new MessageTimeAscComparator());
        List<Object> subList = new ArrayList<>();
        for (Message message :list)
        {
            //上一页的消息第一条显示时间
            if (subList.isEmpty())
            {
                subList.add(message.timestamp);
                subList.add(message);
                continue;
            }

            long lastTime =((Message) subList.get(subList.size()-1)).timestamp;
            long currTime =message.timestamp;
            //如果最新消息时间比上一个消息间隔2分钟以上，则在最新消息上显示时间
            if(currTime - lastTime >= Constant.CHATTING_TIME_SPACE){
                subList.add(currTime);
            }
            subList.add(message);
        }
        this.dataList.addAll(0, subList);
        notifyItemRangeInserted(0, subList.size());
    }

    public Message getLastMessage() {
        return dataList.isEmpty() ? null : (Message) dataList.get(dataList.size() - 1);
    }

    public  boolean contains(Message message){
        return dataList.contains(message);
    }
}
