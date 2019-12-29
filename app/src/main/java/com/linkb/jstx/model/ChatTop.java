
package com.linkb.jstx.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_lvxin_chattop")
public class ChatTop implements Serializable {


    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    public String gid;

    @DatabaseField
    public String sender;

    @DatabaseField
    public String sourceName;

    @DatabaseField
    public long sort;


    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof ChatTop) {
            ChatTop chatTop = (ChatTop) o;
            return chatTop.sender.equals(sender) && chatTop.sourceName.equals(sourceName);
        }
        return false;
    }
}
