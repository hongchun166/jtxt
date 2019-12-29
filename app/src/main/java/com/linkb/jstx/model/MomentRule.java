
package com.linkb.jstx.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_lvxin_momentrule")
public class MomentRule implements Serializable {
    /**
     *
     */

    public static final String TYPE_0 = "0";
    public static final String TYPE_1 = "1";
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    public String id;
    @DatabaseField
    public String source;

    @DatabaseField
    public String target;

    @DatabaseField
    public String type;
}
