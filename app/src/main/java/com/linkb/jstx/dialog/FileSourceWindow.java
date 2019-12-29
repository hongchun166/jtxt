
package com.linkb.jstx.dialog;

import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.linkb.jstx.listener.OnModeChangedListener;
import com.linkb.R;

public class FileSourceWindow extends BottomSheetDialog implements View.OnClickListener {
    private OnModeChangedListener onModeChangedListener;
    public FileSourceWindow(Context context) {
        super(context);
        setContentView(R.layout.dialog_file_source);
        findViewById(R.id.item_app).setOnClickListener(this);
        findViewById(R.id.item_sdcard).setOnClickListener(this);

    }

    public void setOnModeChangedListener(OnModeChangedListener onModeChangedListener) {
        this.onModeChangedListener = onModeChangedListener;
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if (v.getId() == R.id.item_app){
            onModeChangedListener.onModeChanged(0);
        }
        if (v.getId() == R.id.item_sdcard){
            onModeChangedListener.onModeChanged(1);
        }
    }
}
