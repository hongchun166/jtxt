
package com.linkb.jstx.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_lvxin_glideimage")
public class GlideImage implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    public String url;
    @DatabaseField
    public String version;

}
