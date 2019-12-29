package com.linkb.jstx.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.linkb.R;
import com.linkb.jstx.component.ChattingInputPanelView;
import com.linkb.video.utils.ToastUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/** 聊天输入框工具栏第二个页面的工具
* */
public class ChatInputToolSecondFragment extends LazyLoadFragment {

    private ChattingInputPanelView.ChattingPanelClickListener mListener;

    public static ChatInputToolSecondFragment getInstance(){
        ChatInputToolSecondFragment firstFragment = new ChatInputToolSecondFragment();
        return firstFragment;
    }

    public void setListener(ChattingInputPanelView.ChattingPanelClickListener mListener) {
        this.mListener = mListener;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_input_tool_second, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void requestData() {

    }

    @OnClick(R.id.tool_coin_transfer)
    public void onCoinTransfer(){
        if (mListener != null){
            mListener.onCoinTransfer();
        }
    }

    @OnClick(R.id.tool_recommend_contacts)
    public void onRecommendContact(){
        if (mListener != null){
            mListener.onRecommendContact();
        }
    }


}
