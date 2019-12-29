
package com.linkb.jstx.model;

import com.linkb.jstx.app.Constant;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 通知内容实体
 */
@DatabaseTable(tableName = Message.TABLE_NAME)
public class Message implements Serializable {

    public static final String TABLE_NAME = "t_lvxin_message";

    public static final long serialVersionUID = 1845362556725768545L;

    public static final String NAME = Message.class.getSimpleName();
    public static final String STATUS_NOT_READ = "0"; //自己还未阅读
    public static final String STATUS_READ = "1";//自己已经阅读
    public static final String STATUS_READ_OF_VOICE = "2";//自己已经阅读
    @DatabaseField(id = true)
    public long id;
    @DatabaseField
    public String sender;
    @DatabaseField
    public String receiver;
    @DatabaseField
    public String action;
    @DatabaseField
    public String format;
    @DatabaseField
    public String title;
    @DatabaseField
    public String content;
    @DatabaseField
    public String extra;
    @DatabaseField
    public String state;
    @DatabaseField
    public long timestamp;
    /**
      本地对消息处理状态 0: 未处理
     */
    @DatabaseField
    public String handle;

    private String[] isNeedSoundTypes = new String[]{Constant.MessageAction.ACTION_0,
            Constant.MessageAction.ACTION_2,
            Constant.MessageAction.ACTION_3,
            Constant.MessageAction.ACTION_102,
            Constant.MessageAction.ACTION_103,
            Constant.MessageAction.ACTION_104,
            Constant.MessageAction.ACTION_105,
            Constant.MessageAction.ACTION_106,
            Constant.MessageAction.ACTION_107
    };

    //是否为动作消息，无需记录，无需显示
    public boolean isActionMessage() {
        return action.startsWith("9");
    }

    public boolean isNeedSound() {
        return Arrays.asList(isNeedSoundTypes).contains(action);
    }

    //是否为动作消息，无需记录，无需显示
    public boolean isNoNeedShow() {

        return action.startsWith("9") || action.startsWith("8");
    }

    //是否在对话列表显示阅读状态
    public boolean isNeedShowReadStatus() {
        return action.equals(Constant.MessageAction.ACTION_0) || action.equals(Constant.MessageAction.ACTION_1);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Message) {
            return ((Message) o).id != 0 && ((Message) o).id == (id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

}
