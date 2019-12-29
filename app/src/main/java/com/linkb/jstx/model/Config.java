
package com.linkb.jstx.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "t_lvxin_config")
public class Config implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public long id;

    @DatabaseField(id = true)
    public String key;
    @DatabaseField
    public String value;
    public String domain;
    public String description;

}
