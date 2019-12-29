
package com.linkb.jstx.component;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;

import com.linkb.jstx.activity.contact.MicroServerDetailedActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.util.MessageUtil;
import com.linkb.jstx.activity.contact.MessageForwardActivity;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.network.model.MicroServerTextMessage;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

public class MicroServerFromTextView extends BaseMicroServerFromView implements OnClickListener  {
    private WebImageView logo;
    private EmoticonTextView textView;

    public MicroServerFromTextView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }


    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        logo = findViewById(R.id.logo);
        logo.setOnClickListener(this);
        textView = findViewById(R.id.textview);
        int padding = textView.getPaddingLeft() + textView.getPaddingRight();
        textView.setMaxWidth(padding + Global.getChatTextMaxWidth());
    }

    @Override
    public void displayMessage() {
        textView.setOnLongClickListener(this);
        textView.setOnTouchListener(this);
        logo.load(FileURLBuilder.getServerLogoUrl(others.getId()), others.getDefaultIconRID());
        MicroServerTextMessage textMessage = new Gson().fromJson(message.content, MicroServerTextMessage.class);
        textView.setFaceSize(Constant.EMOTION_FACE_SIZE);
        textView.setClickable(false);
        textView.setText(textMessage.content);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.logo) {
            Intent intent = new Intent(getContext(), MicroServerDetailedActivity.class);
            intent.putExtra(MicroServer.NAME, others);
            getContext().startActivity(intent);
        }

    }

    @Override
    public void onMenuItemClicked(int id) {

        super.onMenuItemClicked(id);

        if (id == R.id.menu_copy) {
            ClipboardManager cmb = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            cmb.setPrimaryClip(ClipData.newPlainText(null, textView.getText().toString()));
            Snackbar.make(this, R.string.tip_copy_successed, Snackbar.LENGTH_SHORT).show();
        }

        if (id == R.id.menu_forward) {
            Message target = MessageUtil.clone(message);
            target.content = textView.getText().toString();
            Intent intent = new Intent(getContext(), MessageForwardActivity.class);
            intent.putExtra(Message.NAME, target);
            getContext().startActivity(intent);
        }
    }


    @Override
    public Object getTag() {
        return logo.getTag(R.id.logo);
    }


    @Override
    public void setTag(Object obj) {
        logo.setTag(R.id.logo, obj);
    }

}
