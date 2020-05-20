
package com.linkb.jstx.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.comparator.TimeDescComparator;
import com.linkb.jstx.database.ChatTopRepository;
import com.linkb.jstx.listener.OnChatingHandlerListener;
import com.linkb.jstx.message.parser.MessageParser;
import com.linkb.jstx.message.parser.MessageParserFactory;
import com.linkb.jstx.adapter.viewholder.RecentChatViewHolder;
import com.linkb.jstx.model.ChatTop;
import com.linkb.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

//最近对话列表
public class ConversationListViewAdapter extends RecyclerView.Adapter<RecentChatViewHolder> implements View.OnTouchListener, Comparator<ChatItem>, View.OnLongClickListener, View.OnClickListener {
    private OnChatingHandlerListener onChatItemHandleListner;
    private List<ChatItem> dataList = new ArrayList<>();
    private List<ChatTop> chatTopList = new ArrayList<>();
    private void log(String msg){
        System.out.println("testLog=="+msg);
    }
    @Override
    public RecentChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RecentChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recent_message, parent, false));
    }

    public void setOnChatItemHandleListner(OnChatingHandlerListener onChatItemHandleListner) {
        this.onChatItemHandleListner = onChatItemHandleListner;
    }

    @Override
    public void onBindViewHolder(RecentChatViewHolder viewHolder, int position) {
        ChatItem chatItem = dataList.get(position);
        viewHolder.itemView.setOnClickListener(this);
        viewHolder.itemView.setOnLongClickListener(this);
        viewHolder.itemView.setOnTouchListener(this);
        viewHolder.itemView.setBackgroundResource(hasChatTop(chatItem) ? R.drawable.chat_top_list_background : R.drawable.background_bottom_line_selector);
        MessageParser messageParser =  MessageParserFactory.getFactory().getMessageParser(chatItem.message.action);
        messageParser.displayItemRecentMessageView(viewHolder,chatItem);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addAll(List<ChatItem> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        dataList.clear();
        this.dataList.addAll(list);
        chatTopList.addAll(ChatTopRepository.queryList());
        Collections.sort(dataList, this);
        super.notifyDataSetChanged();
    }

    public boolean hasChatTop(ChatItem chat) {
        return indexOfChatTop(chat) >= 0;
    }

    public void cancelChatTop(ChatItem chatItem) {
        int index = indexOfChatTop(chatItem);
        chatTopList.remove(index);
        ChatTopRepository.delete(chatItem.getSourceClass(), chatItem.source.getId());
        int normalIndex = getNormalIndex(chatItem);
        dataList.remove(chatItem);
        dataList.add(normalIndex, chatItem);
        super.notifyItemMoved(index,normalIndex);
        super.notifyItemChanged(normalIndex);
    }

    /**
     * 当删除最后一条聊天记录是，在主页列表页面需要重新排序
     * @param sourceItem
     * @param currIndex
     */
    private void moveToOrderPosition(ChatItem sourceItem,int currIndex){
        int normalIndex = getNormalIndex(sourceItem);
        if (currIndex == normalIndex)
        {
            super.notifyItemChanged(currIndex);
            return;
        }

        dataList.remove(sourceItem);
        dataList.add(normalIndex, sourceItem);
        super.notifyItemMoved(currIndex,normalIndex);
        super.notifyItemChanged(normalIndex);

        /*
         当置顶的2个消息位置交换后，chatTopList也需要跟着变动
         */
        int srcTopIndex = indexOfChatTop(sourceItem);
        if (srcTopIndex > -1 && normalIndex <= chatTopList.size())
        {
            ChatTop srcTop = chatTopList.get(srcTopIndex);
            chatTopList.remove(srcTopIndex);
            chatTopList.add(normalIndex,srcTop);
            ChatTopRepository.updateSort(srcTop);
        }
    }


    /**
     * 发起新的聊天或者最新回复聊天，在主页列表需要移动到最顶部显示
     * @param chatItem
     */
    public void notifyItemMovedTop(ChatItem chatItem) {
        int topIndex = indexOfChatTop(chatItem);
        if (topIndex == -1) {
            log("notifyItemMovedTop===topIndex = -1");
            notifyItemMoved(chatItem, chatTopList.size());
            return;
        }

        ChatTop srcTop = chatTopList.get(topIndex);
        chatTopList.remove(srcTop);
        chatTopList.add(0,srcTop);
        ChatTopRepository.updateSort(srcTop);
        log("notifyItemMovedTop===");
        notifyItemMoved(chatItem, 0);

    }

    public void notifyItemMovedTop(ChatTop top, ChatItem chatItem) {
        chatTopList.remove(top);
        chatTopList.add(0, top);
        ChatTopRepository.updateSort(top);
        notifyItemMoved(chatItem, 0);
    }

    private void notifyItemMoved(ChatItem chatItem, int toPosition) {
        int index = dataList.indexOf(chatItem);
        log("==notifyItemMoved=="+"==index:"+index+",toPosition:"+toPosition+",sourceId:"+(chatItem.source==null?" null":chatItem.source.getId()));
        if(index==-1){
            boolean hasFind=false;
            int count=0;
            for (ChatItem item : dataList) {
                if(chatItem.source!=null){
                    if(item.source.getId().equals(chatItem.source.getId())){
                        hasFind=true;
                        break;
                    }
                }
                count++;
            }
            index=hasFind?count:-1;
            log("==index=="+index);
        }

        if (index > 0 && index != toPosition) {

            dataList.remove(index);
            dataList.add(toPosition, chatItem);
            super.notifyItemMoved(index,toPosition);
            super.notifyItemChanged(toPosition);
            return;
        }
        if (index > 0 && index == toPosition) {

            dataList.set(toPosition, chatItem);
            super.notifyItemChanged(toPosition);
            return;
        }

        if (index == 0) {
            dataList.set(toPosition, chatItem);
            super.notifyItemChanged(toPosition);
            return;
        }

        if (index < 0 && chatItem.message != null) {
            dataList.add(toPosition, chatItem);
            super.notifyItemInserted(toPosition);
        }


    }


    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public boolean isEmpty() {
        return dataList.isEmpty();
    }


    @Override
    public boolean onLongClick(View v) {
        onChatItemHandleListner.onChatLongClicked((ChatItem) v.getTag(R.id.message));
        return true;
    }

    @Override
    public void onClick(View v) {
        ChatItem chatItem = (ChatItem) v.getTag(R.id.message);
        onChatItemHandleListner.onChatClicked(chatItem);
    }

    public void notifyItemRemoved(ChatItem target) {
        int index = dataList.indexOf(target);
        if (index >= 0) {
            dataList.remove(target);
            notifyItemRemoved(index);
        }
        index = indexOfChatTop(target);
        if (index >= 0) {
            chatTopList.remove(index);
            ChatTopRepository.delete(target.getSourceClass(), target.source.getId());
        }
    }

    /**
     * 关闭对话页面时，判断是否需要更新主页列表
     * @param target
     */
    public void notifyItemChanged(ChatItem target) {
        int index = dataList.indexOf(target);
        if (index==-1)
        {
            notifyItemMovedTop(target);
            return;
        }

        dataList.set(index, target);
        //如果是最新的聊天记录，显示在顶部，否则重新排序到应该显示的位置
        if (isLastMessage(target)) {
            notifyItemMovedTop(target);
        } else {
            moveToOrderPosition(target,index);
        }
    }

    private int indexOfChatTop(ChatItem srcChat) {
        int count = chatTopList.size();
        for (int i = 0; i < count; i++) {
            ChatTop top = chatTopList.get(i);
            if (Objects.equals(top.sourceName,srcChat.getSourceClass().getName()) && Objects.equals(top.sender,srcChat.source.getId())) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 仅仅是刷新当前item的头像和名字，不涉及位置变换，比如收到用户头像更新通知的时候
     * @param item
     */
    public void notifyItemChangedOnly(ChatItem item) {
        int index = dataList.indexOf(item);
        if (index >= 0)
        {
            dataList.get(index).source = item.source;
            super.notifyItemChanged(index);
        }
    }
    /**
     * 返回按照规则，应该显示在列表的第几个位置
     * @param chatItem
     * @return
     */
    private int getNormalIndex(ChatItem chatItem) {
        if (chatItem.message == null){
            return 0;
        }
        List<Long> timeList = new ArrayList<>(dataList.size());
        for (ChatItem chat : dataList) {
            if (!hasChatTop(chat)) {
                timeList.add(chat.message.timestamp);
            }
        }
        Collections.sort(timeList, new TimeDescComparator());
        int index = timeList.indexOf(chatItem.message.timestamp);

        return index + chatTopList.size();
    }

    @Override
    public int compare(ChatItem arg0, ChatItem arg1) {

        int index0 = indexOfChatTop(arg0);
        int index1 = indexOfChatTop(arg1);
        if (index0 >= 0 && index1 >= 0) {
            return (int) (chatTopList.get(index1).sort - chatTopList.get(index0).sort);
        }
        if (index0 >= 0 && index1 < 0) {
            return -1;
        }
        if (index1 >= 0 && index0 < 0) {
            return 1;
        }
        return (int) (arg1.message.timestamp - arg0.message.timestamp);
    }


    private boolean isLastMessage(ChatItem item) {
        for (ChatItem chat : dataList) {
            if (chat.getLastTime() > item.getLastTime()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event){
        v.setTag(R.id.x,event.getX());
        v.setTag(R.id.y,event.getY());
        return false;
    }
}
