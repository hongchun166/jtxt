package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.gson.Gson;
import com.linkb.jstx.network.result.SendCardsResult;

/**  个人名片的对话框布局, 发送时用
* */
public class ToMessagePersonCardsView extends BaseToMessageView {


    public ToMessagePersonCardsView(Context context, AttributeSet paramAttributeSet) {
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
        initPersonCards(dataBean);

    }

    private void initPersonCards(SendCardsResult.DataBean dataBean) {
        personCardsView.setOnClickListener(personCardsView);
        personCardsView.initSendPersonCards(dataBean);
    }
}


