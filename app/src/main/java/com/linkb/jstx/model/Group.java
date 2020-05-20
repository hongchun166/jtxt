
package com.linkb.jstx.model;

import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.app.Constant;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@DatabaseTable(tableName = "t_lvxin_group")
public class Group extends MessageSource implements Serializable {
    public static final String NAME = Group.class.getSimpleName();
    public static final long serialVersionUID = 4733464888738356502L;
    private final static String[] MESSAGE_ACTION = new String[]{Constant.MessageAction.ACTION_1, Constant.MessageAction.ACTION_3, Constant.MessageAction.ACTION_GrpRedPack,Constant.MessageAction.ACTION_112, Constant.MessageAction.ACTION_113};

    @DatabaseField(id = true)
    public long id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String summary;

    @DatabaseField
    public String category; //群简介

    @DatabaseField
    public String founder;

    /** 是否全员禁言 1是全员禁言  0不是
    * */
    public int banned;

    /** 群等级
    * */
    public int level;

    /** 设置成员是否可以互加好友 0可以查看，1不可以查看
    * */
    public int memberAble;

    public List<GroupMember> memberList;
    public int memberSize;
    public String createTime;

    public Group() {
    }

    public Group(long groupId) {
        this.id = groupId;
    }


    @Override
    public String getWebIcon() {

        return FileURLBuilder.getGroupIconUrl(id);
    }

    @Override
    public String aysnTitle(TextView view) {
        return null;
    }

    @Override
    public String getTitle() {

        return name;
    }

    @Override
    public  int getNotificationPriority(){
        return NotificationCompat.PRIORITY_HIGH;
    }

    @Override
    public int getDefaultIconRID() {
        return R.drawable.logo_group_normal;
    }

    @Override
    public String getId() {

        return String.valueOf(id);
    }


    @Override
    public String getSourceType() {
        return SOURCE_GROUP;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getMessageAction() {
        return MESSAGE_ACTION;
    }

    @Override
    public int getThemeColor() {
        return R.color.theme_blue;
    }

    @Override
    public int getTitleColor() {
        return android.R.color.holo_blue_light;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMemberAble() {
        return memberAble;
    }

    public void setMemberAble(int memberAble) {
        this.memberAble = memberAble;
    }


    public int getMemberSize() {
        return memberSize;
    }

    public void setMemberSize(int memberSize) {
        this.memberSize = memberSize;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Group) {
            Group group = (Group) o;
            return Objects.equals(group.id, id);
        }
        return false;
    }

}
