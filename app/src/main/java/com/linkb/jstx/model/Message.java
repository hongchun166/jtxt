
package com.linkb.jstx.model;

import com.linkb.jstx.app.Constant;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.linkb.jstx.app.Global;

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

    public static final String STATUS_READ_DELETE_UnRead= "999";//未记时
    public static final String STATUS_READ_DELETE_Read= "59";//
    public static final String STATUS_READ_DELETE_CountDown5= "5";//倒计时
    public static final String STATUS_READ_DELETE_CountDown3= "3";//倒计时
    public static final String STATUS_READ_DELETE_CountDown2= "2";//倒计时
    public static final String STATUS_READ_DELETE_TimeOut= "0";//消息过了有效时间

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

    //20200209
    @DatabaseField
    public String readDeleteState=STATUS_READ_DELETE_UnRead;//0、未读，1、已读,2
    @DatabaseField
    public long readTime;//阅后-读取时间
    @DatabaseField
    public int effectiveTime=10;//阅后-有效时间

    @DatabaseField
    private String loginAccount;
    @DatabaseField(columnName = "msg_key", unique = true)
    private String msgByUserKey;

    public Message() {
        loginAccount= Global.getCurrentUser().account;
    }

    //是否为动作消息，无需记录，无需显示
    public boolean isActionMessage() {
        return action.startsWith("9");
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

    public String getReadDeleteState() {
        return readDeleteState;
    }

    public void setReadDeleteState(String readDeleteState) {
        this.readDeleteState = readDeleteState;
    }

    public long getReadTime() {
        return readTime;
    }

    public void setReadTime(long readTime) {
        this.readTime = readTime;
    }

    public int getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(int effectiveTime) {
        this.effectiveTime = effectiveTime;
    }
    public String getLoginAccount() {
        return loginAccount;
    }

    public void setLoginAccount(String loginAccount) {
        this.loginAccount = loginAccount;
    }

    public String getMsgByUserKey() {
        return msgByUserKey;
    }

    public void setMsgByUserKey(String msgByUserKey) {
        this.msgByUserKey = msgByUserKey;
    }
}
