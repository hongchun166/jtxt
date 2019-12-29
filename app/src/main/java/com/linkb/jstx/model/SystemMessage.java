
package com.linkb.jstx.model;

import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.R;

import java.io.Serializable;


public class SystemMessage extends MessageSource implements Serializable {

    public static final long serialVersionUID = 4733464888738356502L;

    public final static String[] MESSAGE_ACTION_ARRAY = new String[]{Constant.MessageAction.ACTION_2,
            Constant.MessageAction.ACTION_102,
            Constant.MessageAction.ACTION_103,
            Constant.MessageAction.ACTION_104,
            Constant.MessageAction.ACTION_105,
            Constant.MessageAction.ACTION_106,
            Constant.MessageAction.ACTION_107,
            Constant.MessageAction.ACTION_112,};


    public final static String RESULT_AGREE = "1";

    public final static String RESULT_REFUSE = "2";

    public final static String RESULT_IGNORE = "3";

    private final static String ID = Constant.SYSTEM;

    private String name;

    private String type;

    public SystemMessage(String msgType) {
        type = msgType;
        name = getTypeText(msgType);
    }


    private static String getTypeText(String msgType) {

        if (Constant.MessageAction.ACTION_2.equals(msgType)) {
            return LvxinApplication.getInstance().getString(R.string.common_sysmessage);
        }
        if (Constant.MessageAction.ACTION_102.equals(msgType) || Constant.MessageAction.ACTION_105.equals(msgType)) {
            return LvxinApplication.getInstance().getString(R.string.tip_title_groupmessage);
        }

        return LvxinApplication.getInstance().getString(R.string.common_sysmessage);

    }

    @Override
    public int getDefaultIconRID() {
        return R.drawable.icon_system_notify;
    }

    @Override
    public int getNotifyIcon() {
        return R.drawable.icon_system_notify;
    }

    @Override
    public String getTitle() {

        return name;
    }



    @Override
    public int getThemeColor() {
        return R.color.theme_orange;
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getWebIcon() {
        return null;
    }

    @Override
    public String aysnTitle(TextView view) {
        return null;
    }

    @Override
    public String getSourceType() {
        return SOURCE_SYSTEM;
    }


    @Override
    public String getName() {

        return LvxinApplication.getInstance().getString(R.string.common_system);
    }

    @Override
    public String[] getMessageAction() {
        return MESSAGE_ACTION_ARRAY;
    }
}
