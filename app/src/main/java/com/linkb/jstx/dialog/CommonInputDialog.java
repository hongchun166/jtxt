
package com.linkb.jstx.dialog;


import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.R;

/** 通用的留言弹出框
* */
public class CommonInputDialog extends AppCompatDialog implements View.OnClickListener {

    private InputGetListener mInputGetListner;
    private EditText mInputEdt;

    public CommonInputDialog(Context context) {

        super(context,R.style.CommonDialogStyle);
        setContentView(R.layout.dialog_common_input);
        findViewById(R.id.leftButton).setOnClickListener(this);
        findViewById(R.id.rightButton).setOnClickListener(this);
        mInputEdt = findViewById(R.id.input_edt);
        setCanceledOnTouchOutside(false);
    }


    public void setOnDialogButtonClickListener(InputGetListener onDialogButtonClickListener) {
        this.mInputGetListner = onDialogButtonClickListener;
    }

    public Object getTag() {

        return getWindow().getDecorView().getTag();
    }

    public void setTag(Object tag) {
        getWindow().getDecorView().setTag(tag);
    }

    public void setIcon(int icon) {
        ((ImageView) findViewById(R.id.icon)).setImageResource(icon);
    }

    public void setMessage(int sid) {
        ((TextView) findViewById(R.id.message)).setText(sid);
    }

    public void setMessage(CharSequence mesage) {
        ((TextView) findViewById(R.id.message)).setText(mesage);
    }

    public void hideCancelButton() {
        findViewById(R.id.leftButton).setEnabled(false);
    }

    public void setButtonsText(CharSequence left, CharSequence right) {
        ((TextView) findViewById(R.id.leftButton)).setText(left);
        ((TextView) findViewById(R.id.rightButton)).setText(right);
    }
    public void setRightButtonsText(@StringRes int right) {
        ((TextView) findViewById(R.id.rightButton)).setText(right);
    }

    public void setButtonsText(@StringRes int left, @StringRes int right) {
        ((TextView) findViewById(R.id.leftButton)).setText(left);
        ((TextView) findViewById(R.id.rightButton)).setText(right);
    }

    @Override
    public void onClick(View view) {
        if (mInputGetListner==null)
        {
            dismiss();
            return;
        }
        if (view.getId() == R.id.leftButton) {
            dismiss();
            mInputGetListner.cancel();
        }
        if (view.getId() == R.id.rightButton) {
            dismiss();
            mInputGetListner.onGetInputContent(mInputEdt.getText().toString());
        }
    }

    public interface InputGetListener {
        void onGetInputContent(String content);
        void cancel();
    }
}
