
package com.linkb.jstx.dialog;


import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.linkb.jstx.listener.OnInputCompleteClickListener;
import com.linkb.R;


public class InputTagNameDialog extends AppCompatDialog implements View.OnClickListener, DialogInterface.OnShowListener {

    private OnInputCompleteClickListener onInputCompleteClickListener;

    private EditText textEdit;

    public InputTagNameDialog(Context context) {

        super(context,R.style.CommonDialogStyle);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        setContentView(R.layout.dialog_input_tagname);
        findViewById(R.id.leftButton).setOnClickListener(this);
        findViewById(R.id.rightButton).setOnClickListener(this);
        textEdit = findViewById(R.id.nameEdit);
        setCanceledOnTouchOutside(false);
        setOnShowListener(this);
    }


    public void setOnInputCompleteClickListener(OnInputCompleteClickListener onInputCompleteClickListener) {
        this.onInputCompleteClickListener = onInputCompleteClickListener;
    }


    public void dismiss() {
        textEdit.setText(null);
        super.dismiss();
    }
    public Object getTag() {
        return getWindow().getDecorView().getTag();
    }

    public void setTag(Object tag) {
        getWindow().getDecorView().setTag(tag);
    }

    public void setName(String name) {
        this.textEdit.setText(name);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.leftButton) {
            dismiss();
            return;
        }

        if (!TextUtils.isEmpty(textEdit.getText()) && view.getId() == R.id.rightButton){
            onInputCompleteClickListener.onInputCompleted(textEdit.getText().toString().trim());
            dismiss();
        }
    }

    @Override
    public void onShow(DialogInterface dialog) {
        textEdit.requestFocus();
    }
}
