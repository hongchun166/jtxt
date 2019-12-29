
package com.linkb.jstx.model;

import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.app.Constant;
import com.linkb.R;

import java.io.Serializable;

public abstract class MessageSource implements Serializable {

    public static final String NAME = MessageSource.class.getSimpleName();
    static final String SOURCE_USER = Constant.MessageAction.ACTION_0;
    static final String SOURCE_GROUP = Constant.MessageAction.ACTION_1;
    static final String SOURCE_SYSTEM = Constant.MessageAction.ACTION_2;
    static final String SOURCE_MICROSERVER = Constant.MessageAction.ACTION_200;
    private static final long serialVersionUID = 1L;

    public abstract String getSourceType();

    public abstract String getWebIcon();

    public  String getWebFriendIcon(){
        return "";
    }

    public abstract String aysnTitle(TextView view);

    public abstract String getTitle();

    public abstract String getName();

    //消息列表展示的消息类型
    public abstract String[] getMessageAction();

    public abstract String getId();

    public abstract int getDefaultIconRID();

    public int getNotifyIcon() {
        return getDefaultIconRID();
    }


    public int getTitleColor() {
        return android.R.color.black;
    }

    public int getThemeColor() {
        return R.color.theme_orange;
    }

    public  int getNotificationPriority(){
        return NotificationCompat.PRIORITY_DEFAULT;
    }

    public  String getIdentityId(){

        return  getSourceType() + "#" +getId();
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MessageSource) {
            MessageSource target = (MessageSource) o;
            return getSourceType().equals(target.getSourceType()) && target.getId().equals(getId());
        }
        return false;
    }


}
