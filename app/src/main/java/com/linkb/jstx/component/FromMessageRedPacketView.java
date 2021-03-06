package com.linkb.jstx.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.linkb.R;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.network.result.v2.SendRedPacketResultV2;
import com.linkb.jstx.util.FileURLBuilder;

public class FromMessageRedPacketView extends BaseToMessageView {


    public FromMessageRedPacketView(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();

    }

    @Override
    protected View getContentView() {
        return redPacketView;
    }

    @Override
    protected void displayMessage() {
        if (Constant.MessageStatus.STATUS_NO_SEND.equals(message.state)) {
            setVisibility(GONE);

        }else {

            SendRedPacketResultV2.DataBean  dataBean =null;
            try {
                dataBean=new Gson().fromJson(message.extra, SendRedPacketResultV2.DataBean.class);
                initRedPacket(dataBean);
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }
        }
    }

    private void initRedPacket(SendRedPacketResultV2.DataBean dataBean) {
        redPacketView.setOnClickListener(redPacketView);
        redPacketView.initRedPacket(dataBean, message);
    }
}


