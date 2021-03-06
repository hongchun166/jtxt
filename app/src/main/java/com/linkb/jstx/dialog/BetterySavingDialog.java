
package com.linkb.jstx.dialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import com.linkb.jstx.listener.OnDialogButtonClickListener;
import com.linkb.R;

public class BetterySavingDialog extends CustomDialog implements OnDialogButtonClickListener {

    public BetterySavingDialog(Context context) {
        super(context);

    }

    public static BetterySavingDialog create(Context context){
        BetterySavingDialog dialog = new BetterySavingDialog(context);
        dialog.setIcon(R.drawable.icon_dialog_battery);
        dialog.setMessage(context.getString(R.string.tip_bettery_saving));
        dialog.setButtonsText(R.string.common_cancel,R.string.common_add);
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
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
        intent.setData(Uri.parse("package:"+getContext().getPackageName()));
        getContext().startActivity(intent);
    }
}
