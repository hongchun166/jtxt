
package com.linkb.jstx.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.linkb.R;

public class CustomProgressDialog extends ProgressDialog {


    public CustomProgressDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        init();
    }

    private void init(){
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.layout_progress_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    public void setMessage(String content) {
//        ((TextView) findViewById(R.id.tv_load_dialog)).setText(content);
    }

}
