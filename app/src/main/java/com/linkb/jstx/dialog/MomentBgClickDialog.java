
package com.linkb.jstx.dialog;


import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.view.Window;

import com.farsunset.cim.sdk.android.CIMPushManager;
import com.linkb.R;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.jstx.network.http.HttpServiceManager;

public class MomentBgClickDialog extends BottomSheetDialog implements View.OnClickListener {
    public static interface OnDialogMomentBgClick{
        void  onDialogMomentBgClick(View view);
    }

    OnDialogMomentBgClick onDialogMomentBgClick;

    public MomentBgClickDialog(Activity activity) {
        super(activity);
        setOwnerActivity(activity);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_moment_bg_click_dialog);
        findViewById(R.id.viewUpdateMomentBg).setOnClickListener(this);
        findViewById(R.id.viewCancel).setOnClickListener(this);

    }

    public void setOnDialogMomentBgClick(OnDialogMomentBgClick onDialogMomentBgClick) {
        this.onDialogMomentBgClick = onDialogMomentBgClick;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
       if (v.getId() == R.id.viewUpdateMomentBg) {
            if(onDialogMomentBgClick!=null)onDialogMomentBgClick.onDialogMomentBgClick(v);
       }
    }
}
