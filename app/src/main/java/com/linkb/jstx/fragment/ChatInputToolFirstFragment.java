package com.linkb.jstx.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.R;
import com.linkb.jstx.component.ChattingInputPanelView;

import butterknife.ButterKnife;
import butterknife.OnClick;

/** 聊天输入框工具栏第一个页面的工具
* */
public class ChatInputToolFirstFragment extends LazyLoadFragment {



    private ChattingInputPanelView.ChattingPanelClickListener mListener;


    public static ChatInputToolFirstFragment getInstance(){
        ChatInputToolFirstFragment firstFragment = new ChatInputToolFirstFragment();
        return firstFragment;
    }

    public void setListener(ChattingInputPanelView.ChattingPanelClickListener mListener) {
        this.mListener = mListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_input_tool_first, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void requestData() {

    }

    @OnClick(R.id.tool_camera)
    public void onChatInputCamera(){
        if (mListener != null){
            mListener.onChatInputCamera();
        }
    }

    @OnClick(R.id.tool_photo)
    public void onChatInputPhoto(){
        if (mListener != null){
            mListener.onChatInputPhoto();
        }
    }

    @OnClick(R.id.tool_file)
    public void onChatInputFile(){
        if (mListener != null){
            mListener.onChatInputFile();
        }
    }

    @OnClick(R.id.tool_location)
    public void onChatInputLocation(){
        if (mListener != null){
            mListener.onChatInputLocation();
        }
    }

    @OnClick(R.id.tool_red_packet)
    public void onChatInputRedPacket(){
        if (mListener != null){
            mListener.onChatInputRedPacket();
        }
    }

    @OnClick(R.id.tool_video_connect)
    public void onChatInputVideoConnect(){
        if (mListener != null){
            mListener.onClickVideoConnect();
        }
    }

    @OnClick(R.id.tool_voice_connect)
    public void onChatInputVoiceConnect(){
        if (mListener != null){
            mListener.onClickVoiceConnect();
        }
    }

    @OnClick(R.id.tool_send_cards)
    public void onChatInputSendCards(){
        if (mListener != null){
            mListener.onChatInputSendCards();
        }
    }
}
