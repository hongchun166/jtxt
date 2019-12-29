
package com.linkb.jstx.activity.contact;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.network.model.ChatFile;
import com.linkb.jstx.network.model.SNSChatImage;
import com.linkb.R;
import com.linkb.jstx.util.AppTools;
import com.linkb.jstx.util.StringUtils;
import com.google.gson.Gson;

import java.io.File;
import java.util.Objects;

public class MessageSharedActivity extends MessageForwardActivity {

    private Uri fileUri;
    @Override
    public void initComponents() {
        User user = Global.getCurrentUser();
        if (user == null || user.password == null){
            LvxinApplication.getInstance().restartSelf();
            finish();
            return;
        }
        super.initComponents();

        String type = getIntent().getType();
        fileUri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
        if (Objects.equals(type,"text/plain") && fileUri == null ){
            String text = getIntent().getStringExtra(Intent.EXTRA_TEXT);
            String title = getIntent().getStringExtra(Intent.EXTRA_SUBJECT);
            message.content = TextUtils.isEmpty(title) ? text : title + "\n" + text;
            message.format = Constant.MessageFormat.FORMAT_TEXT;
            return;
        }else if ((Objects.equals(type,"image/jpeg") || Objects.equals(type,"image/png")) && fileUri != null ){
            SNSChatImage chatImage = new SNSChatImage();
            chatImage.image = fileUri.toString();
            message.content = new Gson().toJson(chatImage);
            message.format = Constant.MessageFormat.FORMAT_IMAGE;
            return;
        }else if( fileUri != null){
            File file = new File(AppTools.getFilePath(fileUri,getContentResolver()));
            if (file.isFile() && file.length() > 0 && file.length() <  Constant.MAX_FILE_LIMIT){
                ChatFile chatFile = new ChatFile();
                chatFile.file = StringUtils.getUUID();
                chatFile.name = file.getName();
                chatFile.size = file.length();
                chatFile.path = file.getAbsolutePath();
                message.content = new Gson().toJson(chatFile);
                message.format = Constant.MessageFormat.FORMAT_FILE;
                return;
            }
            if (file.isFile() && file.length() >= Constant.MAX_FILE_LIMIT) {
                showToastView(R.string.tip_file_too_large);
                finish();
                return;
            }
        }
        showToastView(R.string.tips_not_support_shared);
        finish();
    }

    @Override
    public Message getMessage() {
        return new Message();
    }

    @Override
    public int getToolbarTitle() {
        return R.string.label_share_to_contact;
    }

    @Override
    public void onRightButtonClicked() {
        sharedDialog.dismiss();
        showProgressDialog(getString(R.string.tip_loading, getString(R.string.common_send)));
        message.receiver = getReceiver();
        if (Objects.equals(message.format,Constant.MessageFormat.FORMAT_TEXT)){
            HttpServiceManager.forwardText(message, this);
        } else if (Objects.equals(message.format,Constant.MessageFormat.FORMAT_IMAGE)){
            HttpServiceManager.forwardImage(message, fileUri, this);
        }else {
            HttpServiceManager.forwardFile(message, this);
        }
    }

}
