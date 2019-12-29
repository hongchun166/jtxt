
package com.linkb.jstx.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.R;

public class PermissionDialog extends CustomDialog implements OnDialogButtonClickListener {

    public PermissionDialog(Context context) {
        super(context);

    }

    public static PermissionDialog create(Context context){
        PermissionDialog dialog = new PermissionDialog(context);
        dialog.setIcon(R.drawable.icon_dialog_permission);
        dialog.setMessage(context.getString(R.string.tip_permission_open));
        dialog.setButtonsText(R.string.common_cancel,R.string.common_authorize);
        dialog.setOnDialogButtonClickListener(dialog);
        return dialog;
    }

    @Override
    public void onLeftButtonClicked() {
        dismiss();
    }

    @Override
    public void onRightButtonClicked() {
        dismiss();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:"+getContext().getPackageName()));
        getContext().startActivity(intent);
    }
}
