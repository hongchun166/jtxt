
package com.linkb.jstx.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_lvxin_comment")
public class Comment implements Serializable {


    public static final String TYPE_0 = "0";//回复文章
    public static final String TYPE_1 = "1";//回复评论
    public static final String TYPE_2 = "2";//点赞
    private static final long serialVersionUID = 1L;
    @DatabaseField(id = true)
    public long id;

    @DatabaseField
    public long targetId;

    @DatabaseField
    public String account;

    @DatabaseField
    public String content;

    @DatabaseField
    public long sourceId;
    @DatabaseField
    public String reply;
    @DatabaseField
    public String type = TYPE_0;

    @DatabaseField
    public long timestamp;

    @Override
    public boolean equals(Object o) {
        if (o instanceof Comment) {
            return ((Comment) o).id == (id);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }
}
