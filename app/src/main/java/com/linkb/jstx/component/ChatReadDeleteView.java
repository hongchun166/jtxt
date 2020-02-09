
package com.linkb.jstx.component;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.activity.chat.ChatReadDeleteDigOpt;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.dialog.ReadDelteSetTimeDialog;
import com.linkb.jstx.listener.OnMessageDeleteListener;
import com.linkb.jstx.model.Message;

import java.util.Timer;
import java.util.TimerTask;

public class ChatReadDeleteView extends RelativeLayout implements OnClickListener,ChatReadDeleteDigOpt.OnReadDelteCallback {

    private ImageView image;
    TextView viewReadDeleteMsgBotmHint;
    private String key;
    private Message message;
    OnMessageDeleteListener onMessageDeleteListener;

    ChatReadDeleteDigOpt chatReadDeleteDig;

    public ChatReadDeleteView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }
    public void setOnMessageDeleteListener(OnMessageDeleteListener onMessageDeleteListener) {
        this.onMessageDeleteListener = onMessageDeleteListener;

    }
    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        image = this.findViewById(R.id.image);
        viewReadDeleteMsgBotmHint=getRootView().findViewById(R.id.viewReadDeleteMsgBotmHint);
    }
    public void initViews(Message msg) {
        this.message=msg;
        if(message.getReadDeleteState().equals(Message.STATUS_READ_DELETE_UnRead)
                || message.getReadDeleteState().equals(Message.STATUS_READ_DELETE_TimeOut)){
            updateMsgStateByState(message.getReadDeleteState());
        }else if(Integer.valueOf(message.getReadDeleteState())<=Integer.valueOf(Message.STATUS_READ_DELETE_Read)){
            //点击过阅读
            long time=System.currentTimeMillis()-message.getReadTime();
            long time2=getCountDownTime()*1000-time;
            long sysTime=time2<=0?0:time2;
            onTimeTick(sysTime,message);
        }
    }
    private void updateMsgStateByState(String getReadDeleteState){

        if(viewReadDeleteMsgBotmHint==null){
            viewReadDeleteMsgBotmHint=getRootView().findViewById(R.id.viewReadDeleteMsgBotmHint);
        }
        if(getReadDeleteState.equals(Message.STATUS_READ_DELETE_UnRead) || getReadDeleteState.equals(Message.STATUS_READ_DELETE_Read)){
            image.setVisibility(VISIBLE);
            setBackgroundResource(R.drawable.blue_top_left_right_radiu);
            if(viewReadDeleteMsgBotmHint!=null)viewReadDeleteMsgBotmHint.setVisibility(VISIBLE);

        }else if(getReadDeleteState.equals(Message.STATUS_READ_DELETE_CountDown5)){
            if(viewReadDeleteMsgBotmHint!=null)viewReadDeleteMsgBotmHint.setVisibility(VISIBLE);
            setBackgroundResource(R.drawable.blue_9fc3f7_top_left_right_radiu);
            image.setVisibility(VISIBLE);

        }else if(getReadDeleteState.equals(Message.STATUS_READ_DELETE_CountDown3)){
            if(viewReadDeleteMsgBotmHint!=null)viewReadDeleteMsgBotmHint.setVisibility(VISIBLE);
            setBackgroundResource(R.drawable.gray_e1e1e1_top_left_right_radiu);
            image.setVisibility(VISIBLE);

        }else if(getReadDeleteState.equals(Message.STATUS_READ_DELETE_CountDown2)){
            image.setVisibility(GONE);
            if(viewReadDeleteMsgBotmHint!=null)viewReadDeleteMsgBotmHint.setVisibility(GONE);
            setBackgroundResource(R.mipmap.ic_msg_read_delete_dotread);

        }else if(getReadDeleteState.equals(Message.STATUS_READ_DELETE_TimeOut)){
            setBackgroundResource(R.color.color_transparent);
            image.setVisibility(GONE);
            if(viewReadDeleteMsgBotmHint!=null)viewReadDeleteMsgBotmHint.setVisibility(GONE);

        }
        if(!message.getReadDeleteState().equals(Message.STATUS_READ_DELETE_TimeOut)
            && !message.getReadDeleteState().equals(Message.STATUS_READ_DELETE_UnRead) ){
            MessageRepository.updateReadDeleteStatus(message.id,getReadDeleteState,-1);
        }
        message.setReadDeleteState(getReadDeleteState);
    }
    @Override
    public void onClick(View view) {
        if(Integer.valueOf(message.getReadDeleteState())<=Integer.valueOf(Message.STATUS_READ_DELETE_CountDown2)){
            System.out.println("消息不可以阅读，过了有效时间");
            return;
        }
        if(message.getReadDeleteState().equals(Message.STATUS_READ_DELETE_UnRead)){
            message.setReadTime(System.currentTimeMillis());
            message.setReadDeleteState(Message.STATUS_READ_DELETE_Read);
            MessageRepository.updateReadDeleteStatus(message.id,message.getReadDeleteState(),message.getReadTime());
        }
        long time=System.currentTimeMillis()-message.getReadTime();
        long time2=getCountDownTime()*1000-time;
        long sysTime=time2<=0?0:time2;
        if(sysTime>3000){
            chatReadDeleteDig=new ChatReadDeleteDigOpt();
            chatReadDeleteDig.setCountDown((int) (sysTime/1000));
            chatReadDeleteDig.setMessage(message).build(getContext()).show();
            chatReadDeleteDig.setOnReadDelteCallback(this);
        }
    }
    private int getCountDownTime(){
        return ReadDelteSetTimeDialog.getReadDeleteTime(message.sender);
    }
    @Override
    public void onTimeTick(long time,Message message) {
        System.out.println("onTimeTick=="+message.content+"=="+time);
        if(time/1000<=0){
            updateMsgStateByState(Message.STATUS_READ_DELETE_TimeOut);
        }else if(time/1000<=2){
            updateMsgStateByState(Message.STATUS_READ_DELETE_CountDown2);
        }else if(time/1000<=3){
            updateMsgStateByState(Message.STATUS_READ_DELETE_CountDown3);
        }else if(time/1000<=5){
            updateMsgStateByState(Message.STATUS_READ_DELETE_CountDown5);
        }
    }
    @Override
    public void onReadDelteCallback(boolean isTimeOut, Message message) {
        if(isTimeOut){
            updateMsgStateByState(Message.STATUS_READ_DELETE_TimeOut);
        }
    }
}
