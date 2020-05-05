
package com.linkb.jstx.dialog;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.Window;

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.R;

public class QuitAppDialog extends BottomSheetDialog implements View.OnClickListener, OnDialogButtonClickListener {
    private CustomDialog dialog;

    public QuitAppDialog(Activity activity) {
        super(activity);
        setOwnerActivity(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_quit_app_dialog);
        findViewById(R.id.menu_logout).setOnClickListener(this);
        findViewById(R.id.menu_killapp).setOnClickListener(this);
        dialog = new CustomDialog(activity);
        dialog.setOwnerActivity(activity);
        dialog.setOnDialogButtonClickListener(this);
    }

    private void doLogout() {
        Global.removePassword();
//        Global.removeAccessToken();
        CIMPushManager.stop(LvxinApplication.getInstance());
        HttpServiceManager.logout();
        LvxinApplication.getInstance().restartSelf();
        NotificationManager notificationMgr =  (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationMgr.cancelAll();
    }
    private void doKillApp() {
        CIMPushManager.destroy(LvxinApplication.getInstance());
        getOwnerActivity().finish();
    }
    @Override
    public void onClick(View v) {
        this.dismiss();
       if (v.getId() == R.id.menu_logout)
       {
           dialog.setIcon(R.drawable.icon_dialog_logout);
           dialog.setMessage(R.string.label_logout_hint);
           dialog.setTag(R.id.menu_logout);
           dialog.show();
       }
        if (v.getId() == R.id.menu_killapp)
        {
            dialog.setIcon(R.drawable.icon_dialog_close);
            dialog.setMessage(R.string.label_killapp_hint);
            dialog.setTag(R.id.menu_killapp);
            dialog.show();
        }
    }
    @Override
    public void onLeftButtonClicked() {
        dialog.dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        dialog.dismiss();
        if (dialog.getTag().equals(R.id.menu_logout)) {
            doLogout();
        }
        if (dialog.getTag().equals(R.id.menu_killapp)) {
            doKillApp();
        }
    }
}
