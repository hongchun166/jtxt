package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.gson.Gson;
import com.linkb.jstx.network.result.CoinTransferResult;

public class ToMessageCoinTransferView extends BaseToMessageView {


    public ToMessageCoinTransferView(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
//        int padding = redPacketView.getPaddingLeft() + redPacketView.getPaddingRight();
//        redPacketView.setMinimumWidth(padding + Global.getChatTextMaxWidth());
    }

    @Override
    protected View getContentView() {
        return transferView;
    }

    @Override
    protected void displayMessage() {
        CoinTransferResult.DataBean dataBean = new Gson().fromJson(message.extra, CoinTransferResult.DataBean.class);
        initCoinTransfer(dataBean);

//        if (Constant.MessageStatus.STATUS_NO_SEND.equals(message.state)) {
//            setVisibility(GONE);
//
//        }else {
//
//        }
    }

    private void initCoinTransfer(CoinTransferResult.DataBean dataBean) {
        transferView.setOnClickListener(transferView);
        transferView.initCoinTransfer(dataBean, message);
    }
}


