package com.linkb.jstx.dialog;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.linkb.R;
import com.linkb.jstx.component.WebImageView;
import com.linkb.jstx.util.FileURLBuilder;

/** 发送个人名片的弹出框
* */
public class SendCardDialog extends AppCompatDialog implements View.OnClickListener {

    private SendCardsClickListener onDialogButtonClickListener;
    private WebImageView avatarImage;
    private TextView toNameTv, sendNameTv;
    private EditText leaveMessageEdt;
    private String senderAccount, sendName;

    public SendCardDialog(Context context, String toAccount, String toName,String senderAccount, String sendName ) {
        super(context, R.style.CommonDialogStyle);
        setContentView(R.layout.dialog_send_cards);
        findViewById(R.id.leftButton).setOnClickListener(this);
        findViewById(R.id.rightButton).setOnClickListener(this);
        setCanceledOnTouchOutside(false);
        avatarImage = findViewById(R.id.imageView45);
        toNameTv = findViewById(R.id.textView156);
        sendNameTv = findViewById(R.id.textView157);
        leaveMessageEdt = findViewById(R.id.editText4);
        toNameTv.setText(toName);
        sendNameTv.setText(context.getString(R.string.person_card_tips, sendName));
        this.senderAccount = senderAccount;
        this.sendName = sendName;
    }


    public void setOnDialogButtonClickListener(SendCardsClickListener onDialogButtonClickListener) {
        this.onDialogButtonClickListener = onDialogButtonClickListener;
    }

    public Object getTag() {

        return getWindow().getDecorView().getTag();
    }

    public void setTag(Object tag) {
        getWindow().getDecorView().setTag(tag);
    }

    public void loadAvatar(String account) {
        avatarImage.load(FileURLBuilder.getUserIconUrl(account), R.mipmap.lianxiren, 999);
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
        if (onDialogButtonClickListener==null)
        {
            dismiss();
            return;
        }
        if (view.getId() == R.id.leftButton) {
            dismiss();
            onDialogButtonClickListener.onCancelClick();
        }
        if (view.getId() == R.id.rightButton) {
            dismiss();
            onDialogButtonClickListener.onSendClick(leaveMessageEdt.getText().toString(),senderAccount,  sendName);
        }
    }

    public interface SendCardsClickListener {
        void onSendClick(String leaveMessage, String senderAccount, String sendName);
        void onCancelClick();
    }
}
