
package com.linkb.jstx.message.handler;

import android.content.Context;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.database.MessageRepository;
import com.linkb.jstx.network.CloudFileDownloader;
import com.linkb.jstx.network.model.SNSChatImage;
import com.linkb.jstx.network.model.SNSVideo;
import com.linkb.jstx.util.AppTools;
import com.farsunset.cim.sdk.android.model.Message;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.network.CloudImageLoaderFactory;
import com.linkb.jstx.network.model.ChatFile;
import com.linkb.jstx.network.model.ChatMap;
import com.linkb.jstx.network.model.ChatVoice;
import com.linkb.jstx.util.FileURLBuilder;
import com.google.gson.Gson;

import java.io.File;

public class Action000MessageHandler implements CustomMessageHandler {


    @Override
    public boolean handle(Context context, Message message) {
        if (!FriendRepository.isFriend(message.getSender())) {
            MessageRepository.deleteById(message.getId());
            return false;
        }

        beforehandLoadFiles(message);

        return true;
    }

    void beforehandLoadFiles(Message message) {

        if (message.getFormat().equals(Constant.MessageFormat.FORMAT_VOICE)) {
            ChatVoice chatVoice = new Gson().fromJson(message.getContent(), ChatVoice.class);
            CloudFileDownloader.asyncDownload(FileURLBuilder.BUCKET_CHAT, chatVoice.file, LvxinApplication.CACHE_DIR_VOICE, null);
        }
        if (message.getFormat().equals(Constant.MessageFormat.FORMAT_IMAGE)) {
            SNSChatImage snsImage = new Gson().fromJson(message.getContent(), SNSChatImage.class);
            CloudImageLoaderFactory.get().downloadOnly(FileURLBuilder.getChatFileUrl(snsImage.thumb));
        }
        if (message.getFormat().equals(Constant.MessageFormat.FORMAT_MAP)) {
            ChatMap chatMap = new Gson().fromJson(message.getContent(), ChatMap.class);
            CloudImageLoaderFactory.get().downloadOnly(FileURLBuilder.getChatFileUrl(chatMap.image));
        }
        if (message.getFormat().equals(Constant.MessageFormat.FORMAT_FILE) && AppTools.isWifiConnected()) {
            ChatFile chatFile = new Gson().fromJson(message.getContent(), ChatFile.class);
            File desFile = new File(LvxinApplication.CACHE_DIR_FILE,chatFile.name);
            AppTools.creatFileQuietly(desFile);
            desFile.setLastModified(message.getTimestamp());
            CloudFileDownloader.asyncDownload(FileURLBuilder.getChatFileUrl(chatFile.file),desFile , null);
        }
        if (message.getFormat().equals(Constant.MessageFormat.FORMAT_VIDEO)) {
            SNSVideo chatVideo = new Gson().fromJson(message.getContent(), SNSVideo.class);
            CloudImageLoaderFactory.get().downloadOnly(FileURLBuilder.getChatFileUrl(chatVideo.image));
            if (LvxinApplication.getInstance().isConnectWifi()) {
                CloudFileDownloader.asyncDownload(FileURLBuilder.BUCKET_CHAT, chatVideo.video, LvxinApplication.CACHE_DIR_VIDEO, null);
            }
        }
    }

}
