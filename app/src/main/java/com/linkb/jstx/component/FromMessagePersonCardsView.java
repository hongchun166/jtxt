package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.gson.Gson;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.network.result.SendCardsResult;

/** 接收 个人名片
* */
public class FromMessagePersonCardsView extends BaseFromMessageView {


    public FromMessagePersonCardsView(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
    }

    @Override
    protected View getContentView() {
        return personCardsView;
    }

    @Override
    protected void displayMessage() {
        SendCardsResult.DataBean dataBean = new Gson().fromJson(message.extra, SendCardsResult.DataBean.class);
        initRedPacket(dataBean);
    }

    private void initRedPacket(SendCardsResult.DataBean dataBean) {
        personCardsView.setOnClickListener(personCardsView);
        personCardsView.initSendPersonCards(dataBean);
    }
}


