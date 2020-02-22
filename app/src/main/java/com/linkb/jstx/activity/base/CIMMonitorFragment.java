
package com.linkb.jstx.activity.base;


import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import com.farsunset.cim.sdk.android.CIMEventListener;
import com.farsunset.cim.sdk.android.CIMListenerManager;
import com.farsunset.cim.sdk.android.model.Message;
import com.farsunset.cim.sdk.android.model.ReplyBody;
import com.farsunset.cim.sdk.android.model.SentBody;
import com.linkb.R;
import com.linkb.jstx.dialog.CustomProgressDialog;
import com.linkb.jstx.fragment.LazyLoadFragment;

public abstract class CIMMonitorFragment extends LazyLoadFragment implements CIMEventListener {

    private CustomProgressDialog progressDialog;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CIMListenerManager.registerMessageListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        CIMListenerManager.removeMessageListener(this);
    }

    protected View findViewById(int id) {
        return this.getView().findViewById(id);
    }


    private void showProgressDialog(String content, boolean cancancelable) {
        progressDialog = new CustomProgressDialog(this.getActivity(), R.style.CustomDialogStyle);
        progressDialog.setMessage(content);
        progressDialog.setCancelable(cancancelable);
        progressDialog.show();
    }

    public void showProgressDialog(String content) {
        showProgressDialog(content, false);
    }

    public void hideProgressDialog() {

        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

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
