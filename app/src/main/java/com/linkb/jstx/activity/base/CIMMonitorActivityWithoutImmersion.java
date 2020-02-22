
package com.linkb.jstx.activity.base;

import android.net.NetworkInfo;
import android.os.Bundle;

import com.farsunset.cim.sdk.android.CIMEventListener;
import com.farsunset.cim.sdk.android.CIMListenerManager;
import com.farsunset.cim.sdk.android.model.Message;
import com.farsunset.cim.sdk.android.model.ReplyBody;
import com.farsunset.cim.sdk.android.model.SentBody;

public abstract class CIMMonitorActivityWithoutImmersion extends BaseActivityWithoutImmersion implements CIMEventListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        CIMListenerManager.registerMessageListener(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CIMListenerManager.removeMessageListener(this);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        CIMListenerManager.registerMessageListener(this);
    }


    @Override
    public void onReplyReceived(ReplyBody reply) {
    }

    @Override
    public void onMessageReceived(Message arg0) {
    }


    @Override
    public void onNetworkChanged(NetworkInfo info) {
    }

    @Override
    public void onConnectionClosed() {
    }


    @Override
    public int getEventDispatchOrder() {
        return 0;
    }
    //3.5
//    @Override
//    public void onSentSuccessed(SentBody body) {
//    }
//
//    @Override
//    public void onConnectionSuccessed(boolean hasAutoBind) {
//    }
//    @Override
//    public void onConnectionFailed() {
//    }

    //3.8
    @Override
    public void onSendFinished(SentBody sentBody) {

    }

    @Override
    public void onConnectFinished(boolean b) {

    }

    @Override
    public void onConnectFailed() {

    }
}
