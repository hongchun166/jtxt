package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.gson.Gson;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.network.result.CoinTransferResult;

public class FromMessageCoinTransferView extends BaseFromMessageView {


    public FromMessageCoinTransferView(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    protected View getContentView() {
        return transferView;
    }

    @Override
    protected void displayMessage() {
        if (Constant.MessageStatus.STATUS_NO_SEND.equals(message.state)) {
            setVisibility(GONE);

        }else {
            CoinTransferResult.DataBean dataBean = new Gson().fromJson(message.extra, CoinTransferResult.DataBean.class);
            initCoinTransfer(dataBean);
        }
    }

    private void initCoinTransfer(CoinTransferResult.DataBean dataBean) {
        transferView.setOnClickListener(transferView);
        transferView.initCoinTransfer(dataBean, message);
    }
}


