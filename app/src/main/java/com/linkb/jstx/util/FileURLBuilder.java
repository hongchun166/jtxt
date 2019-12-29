package com.linkb.jstx.util;

import android.net.Uri;


import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.app.URLConstant;

import java.io.File;
import java.util.Date;

/**
 * 阿里云 OSS 文件 地址构建
 */
public class FileURLBuilder {

    //用户头像存储bucket
    public static final String BUCKET_USER_ICON = "user-icon";

    //群头像存储bucket
    public static final String BUCKET_GROUP_ICON = "group-icon";

    //公众号logo 存储 bucket
    private static final String BUCKET_MICROSERVER_ICON = "microserver-icon";

    //聊天文件图片等其他文件
    public static final String BUCKET_CHAT = "chat-space";

    //朋友圈图片等其他文件存储bucket
    public static final String BUCKET_MOMENT= "moment-space";

    //小应用LOGO
    public static final String BUCKET_MICROAPP_ICON = "microapp-icon";

    private FileURLBuilder() {
    }

    public static String getUserIconUrl(String account) {
        int suiji = (int) new Date().getTime();
        return String.format(URLConstant.FILE_DOWNLOAD_URL, BUCKET_USER_ICON, account, suiji);
    }

    public static String getServerLogoUrl(String account) {
        int suiji = (int) new Date().getTime();
        return String.format(URLConstant.FILE_DOWNLOAD_URL, BUCKET_MICROSERVER_ICON, account, suiji);
    }

    public static String getGroupIconUrl(long groupId) {
        int suiji = (int) new Date().getTime();
        return String.format(URLConstant.FILE_DOWNLOAD_URL, BUCKET_GROUP_ICON, groupId, suiji);
    }

    public static String getMomentFileUrl(String key) {
        int suiji = (int) new Date().getTime();
        return String.format(URLConstant.FILE_DOWNLOAD_URL, BUCKET_MOMENT, key, suiji);
    }
    public static String getAppIconUrl(String account) {
        int suiji = (int) new Date().getTime();
        return String.format(URLConstant.FILE_DOWNLOAD_URL, BUCKET_MICROAPP_ICON, account, suiji);
    }


    public static String getFileUrl(String bucket,String key) {
        int suiji = (int) new Date().getTime();
        return String.format(URLConstant.FILE_DOWNLOAD_URL, bucket, key, suiji);
    }

    public static String getChatFileUrl(String key) {
        int suiji = (int) new Date().getTime();
        if (key == null) {
            return null;
        }
        File file = new File(LvxinApplication.CACHE_DIR_IMAGE, key);
        if (file.exists()) {
            return Uri.fromFile(file).toString();
        }
        return String.format(URLConstant.FILE_DOWNLOAD_URL, BUCKET_CHAT, key, suiji);
    }

    public static boolean isLogo(String url) {
        int suiji = (int) new Date().getTime();
        return url.startsWith(String.format(URLConstant.FILE_DOWNLOAD_URL, BUCKET_USER_ICON, "", suiji))
                || url.startsWith(String.format(URLConstant.FILE_DOWNLOAD_URL, BUCKET_MICROAPP_ICON, "", suiji))
                || url.startsWith(String.format(URLConstant.FILE_DOWNLOAD_URL, BUCKET_MICROSERVER_ICON, "", suiji))
                || url.startsWith(String.format(URLConstant.FILE_DOWNLOAD_URL, BUCKET_GROUP_ICON, "", suiji));
    }
}
