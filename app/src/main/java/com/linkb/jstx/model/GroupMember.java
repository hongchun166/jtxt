
package com.linkb.jstx.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
@DatabaseTable(tableName = GroupMember.TABLE_NAME)
public class GroupMember implements Serializable {
    public static final String TABLE_NAME = "t_lvxin_groupMember";
    public static final long serialVersionUID = 4733464888738356502L;
    public final static String RULE_MANAGER = "2";
    public final static String RULE_FOUNDER = "1";
    public final static String RULE_NORMAL = "0";
    @DatabaseField(id = true)
    public long id;

    @DatabaseField
    public long groupId;

    @DatabaseField
    public String account;

    @DatabaseField
    public String host;     //0是普通，1是群主，2是管理员

    @DatabaseField
    public String name;

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof GroupMember) {
            GroupMember member = (GroupMember) o;
            return member.groupId ==(groupId) && member.account.equals(account);
        }
        return false;
    }

}
