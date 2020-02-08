package com.linkb.jstx.activity.chat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.google.gson.Gson;
import com.linkb.R;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.component.ChatVoiceView;
import com.linkb.jstx.component.WebPhotoView;
import com.linkb.jstx.dialog.ChatReadDeleteDig;
import com.linkb.jstx.listener.CloudImageApplyListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.model.SNSChatImage;
import com.linkb.jstx.util.BackgroundThreadHandler;
import com.linkb.jstx.util.FileURLBuilder;

import java.util.Objects;

public class ChatReadDeleteDigOpt implements View.OnClickListener {

  public interface OnReadDelteCallback{
       void onReadDelteCallback(Message message);
   }

    Message message;
    ChatReadDeleteDig dialog;

    TextView viewTVTimeCount;
    TextView viewTVMsgContent;
    TextView viewTVClose;
    ChatVoiceView chatVoiceView;

    WebPhotoView viewImgContent;
    ProgressBar  progress;
    SNSChatImage snsChatImage;
    CloudImageApplyListenerImpl cloudImageApplyListener;
    int countDown=10;
    CountDownTimer countDownTimer;

    OnReadDelteCallback onReadDelteCallback;

    public ChatReadDeleteDigOpt setMessage(Message message){
        this.message=message;
        return this;
    }
    public ChatReadDeleteDigOpt setCountDown(int countDown){
        this.countDown=countDown;
        return this;
    }

    public void setOnReadDelteCallback(OnReadDelteCallback onReadDelteCallback) {
        this.onReadDelteCallback = onReadDelteCallback;
    }

    public ChatReadDeleteDigOpt build(Context context){
        View contentView= null;
        if(Constant.MessageFormat.FORMAT_TEXT.equals(message.format)){
            contentView=LayoutInflater.from(context).inflate(R.layout.dialog_read_delete_txt_layout,null);
        }else if(Constant.MessageFormat.FORMAT_VOICE.equals(message.format)){
            contentView=LayoutInflater.from(context).inflate(R.layout.dialog_read_delete_voice_layout,null);
        }else if(Constant.MessageFormat.FORMAT_IMAGE.equals(message.format)){
            contentView=LayoutInflater.from(context).inflate(R.layout.dialog_read_delete_img_layout,null);

        }
        dialog=new ChatReadDeleteDig(context);
        dialog.setContentView(contentView);
        dialog.setCanceledOnTouchOutside(false);
        initView(contentView);
        return this;
    }
    private void initView(View contentView){
        viewTVTimeCount=contentView.findViewById(R.id.viewTVTimeCount);
        viewTVClose=contentView.findViewById(R.id.viewTVClose);
        viewTVClose.setOnClickListener(this);

        if(Constant.MessageFormat.FORMAT_TEXT.equals(message.format)){
            viewTVMsgContent=contentView.findViewById(R.id.viewTVMsgContent);
            viewTVMsgContent.setText(message.content);
        }else if(Constant.MessageFormat.FORMAT_VOICE.equals(message.format)){
            chatVoiceView=contentView.findViewById(R.id.viewVoiceContent);
            chatVoiceView.display(message,true);
        }else if(Constant.MessageFormat.FORMAT_IMAGE.equals(message.format)){
            viewImgContent=contentView.findViewById(R.id.viewImgContent);
            progress=contentView.findViewById(R.id.progress);

            viewImgContent.setOnPhotoTapListener(new OnPhotoTapListenerImpl());
            snsChatImage= new Gson().fromJson(message.content, SNSChatImage.class);
            final String thumbnailUrl = getImageUrl(snsChatImage.image);
            viewImgContent.display(thumbnailUrl, cloudImageApplyListener=new CloudImageApplyListenerImpl());
        }
    }
    private void showToView(){


    }
    public void show(){
        if(dialog!=null){
            dialog.show();
            timeCountDownStart();
        }
        showToView();
    }
    public void hide(){

        if(onReadDelteCallback!=null){
            onReadDelteCallback.onReadDelteCallback(message);
        }

        if(dialog!=null){
            if(dialog.isShowing()){
                dialog.dismiss();
            }
            release();
        }

    }

    private void release(){
        timeCountDownRelease();
        dialog=null;
        viewImgContent=null;
        chatVoiceView=null;
        cloudImageApplyListener=null;

    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.viewTVClose){
            hide();
        }
    }


    private void timeCountDownRelease(){
       if(viewTVTimeCount!=null && countDownTimer!=null ){
           countDownTimer.cancel();
           countDownTimer=null;
       }
    }
    private void timeCountDownStart(){
        countDownTimer= new CountDownTimer(countDown*1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if(viewTVTimeCount!=null){
                    viewTVTimeCount.setText(String.valueOf(millisUntilFinished/1000));
                }
            }
            @Override
            public void onFinish() {
                if(viewTVTimeCount!=null){
                    viewTVTimeCount.setText("0");
                }
                hide();
            }
        }.start();
    }

    private String getImageUrl(String key){
        return FileURLBuilder.getFileUrl(snsChatImage.getBucket(),key);
    }

    class OnPhotoTapListenerImpl implements OnPhotoTapListener{

        @Override
        public void onPhotoTap(ImageView view, float x, float y) {
            System.out.println("==onPhotoTap="+x+","+y);
//            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
//            BackgroundThreadHandler.postUIThreadDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    supportFinishAfterTransition();
//                }
//            },16);
        }
    }
    class CloudImageApplyListenerImpl implements CloudImageApplyListener{

        @Override
        public void onImageApplyFailure(Object key, ImageView target) {
            if(viewImgContent!=null){
                progress.setVisibility(View.GONE);
                viewImgContent.setImageResource(R.drawable.picture_def);
            }
        }
        @Override
        public void onImageApplySucceed(Object key, ImageView target, BitmapDrawable resource) {
           if(viewImgContent!=null){
               progress.setVisibility(View.GONE);
           }
        }
    }

}
