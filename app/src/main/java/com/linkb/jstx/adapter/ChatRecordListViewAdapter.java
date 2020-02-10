
package com.linkb.jstx.adapter;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.linkb.R;
import com.linkb.jstx.adapter.viewholder.ChatRecordViewHolder;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.dialog.ReadDelteSetTimeDialog;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.util.AppTools;

import org.webrtc.ThreadUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatRecordListViewAdapter extends BaseChatListViewAdapter<ChatRecordViewHolder>{
    private static final int FACTOR = 9999;
    private static final int READ_DELETE_MSG_FORM = 9998;

    private Boolean enableCheckMemberInfo = true;
    Handler handler;
    String friendAccount;

    public ChatRecordListViewAdapter(MessageSource friend) {
        super(friend);
        handler=new Handler();
        startTime();
    }

    Timer timer;
    TimerTask timerTask;
    private void startTime(){
        timer=new Timer();
        timer.schedule(timerTask=new TimerTask() {
            @Override
            public void run() {
                try {
                    final List<String> list=checkTime();
                    if( list.size()>0 && handler!=null){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                for (String s : list) {
                                    notifyItemChanged(Integer.valueOf(s));
                                }
                            }
                        });
                    }
                } catch (Exception e){

                }
            }
        },1000,2000);
    }
    public void stopTime(){
        if(timer!=null){
           if(timerTask!=null) timerTask.cancel();
            timer.cancel();
            timerTask=null;
            timer=null;
        }
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler=null;
        }
    }

    public void setFriendAccount(String friendAccount) {
        this.friendAccount = friendAccount;
        if(TextUtils.isEmpty(friendAccount)){
            stopTime();
        }
    }

    public int getCountDownTime(Message message){
         return Global.getFriendToUserMsgTime(friendAccount);
    }
    private List<String> checkTime(){
        List<String> changeList=new ArrayList<>();
        int position=-1;
        for (Object obj : dataList) {
            position++;
            if(!(obj instanceof Message)){
                continue;
            }
            Message message= (Message) obj;
            String type = message.format;
            boolean isSelf = self.account.equals(message.sender);
            boolean isReadDeleteMsg=message.action.equals(Constant.MessageAction.ACTION_ReadDelete);
            int vType= isSelf ? Integer.parseInt(type): (isReadDeleteMsg?READ_DELETE_MSG_FORM:Integer.parseInt(type) + FACTOR);
            if(vType==READ_DELETE_MSG_FORM){
                if(Integer.valueOf(message.getReadDeleteState())<=Integer.valueOf(Message.STATUS_READ_DELETE_Read) &&
                        Integer.valueOf(message.getReadDeleteState())>Integer.valueOf(Message.STATUS_READ_DELETE_TimeOut)){
                    //点击过阅读
                    long time=System.currentTimeMillis()-message.getReadTime();
                    long time2=getCountDownTime(message)*1000-time;
                    long sysTime=time2<=0?0:time2;
                    String jiShua=null;
                    if(sysTime/1000<=0){
                        jiShua=Message.STATUS_READ_DELETE_TimeOut;
                    }else if(sysTime/1000<=2){
                        jiShua=Message.STATUS_READ_DELETE_CountDown2;
                    }else if(sysTime/1000<=3){
                        jiShua=Message.STATUS_READ_DELETE_CountDown3;
                    }else if(sysTime/1000<=5){
                        jiShua=Message.STATUS_READ_DELETE_CountDown5;
                    }
                    if(!TextUtils.isEmpty(jiShua)){
                        message.setReadDeleteState(jiShua);
                        changeList.add(String.valueOf(position));
                    }
                }
            }
        }
        return changeList;
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
        boolean isReadDeleteMsg=message.action.equals(Constant.MessageAction.ACTION_ReadDelete);

        return isSelf ? Integer.parseInt(type): (isReadDeleteMsg?READ_DELETE_MSG_FORM:Integer.parseInt(type) + FACTOR);

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

        if(viewType==READ_DELETE_MSG_FORM){
            return new ChatRecordViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_record_from_read_delete, parent, false));
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
