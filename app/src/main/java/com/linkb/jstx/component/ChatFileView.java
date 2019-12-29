
package com.linkb.jstx.component;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkb.jstx.activity.chat.FileViewerActivity;
import com.linkb.jstx.app.Constant;
import com.linkb.jstx.listener.OnTransmitProgressListener;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.model.ChatFile;
import com.linkb.R;
import com.linkb.jstx.util.FileUtils;
import com.google.gson.Gson;

public class ChatFileView extends FrameLayout implements OnClickListener, OnTransmitProgressListener {

    private CircleProgressView uploadProgressView;
    private Message message;
    private TextView fileSize;
    private TextView fileName;
    private ImageView icon;

    public ChatFileView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    @Override
    public void onFinishInflate() {
        super.onFinishInflate();
        fileName = findViewById(R.id.fileName);
        fileSize = findViewById(R.id.fileSize);
        icon = findViewById(R.id.fileIcon);
    }

    public void setSendProgressView(CircleProgressView circleProgressView) {
        this.uploadProgressView = circleProgressView;
    }

    public void initView(Message message) {

        this.message = message;
        ChatFile chatFile = new Gson().fromJson(message.content, ChatFile.class);

        setTag(chatFile.file);

        fileSize.setText(org.apache.commons.io.FileUtils.byteCountToDisplaySize(chatFile.size));
        fileName.setText(chatFile.name);
        icon.setImageResource(FileUtils.getFileIcon(chatFile.name));

    }


    @Override
    public void onClick(View v) {

        if (message.state.equals(Constant.MessageStatus.STATUS_SENDING)
                || message.state.equals(Constant.MessageStatus.STATUS_SEND_FAILURE)) {
            return;
        }
        Intent intent = new Intent(getContext(), FileViewerActivity.class);
        intent.putExtra("id", message.id);
        getContext().startActivity(intent);
    }

    @Override
    public void onProgress(float progress) {
        uploadProgressView.setProgress((int) progress);
        if (progress >= 100) {
            uploadProgressView.setVisibility(GONE);
            return;
        }
        if (progress > 0) {
            uploadProgressView.setVisibility(VISIBLE);
        }
    }
}
