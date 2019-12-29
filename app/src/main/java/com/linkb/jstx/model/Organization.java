
package com.linkb.jstx.model;

import android.view.View;
import android.widget.TextView;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;


/**
 * 组织表
 */
@DatabaseTable(tableName = Organization.TABLE_NAME)
public class Organization extends MessageSource implements Serializable {

    public static final String TABLE_NAME = "t_lvxin_organization";
    private static final long serialVersionUID = 4733464888738356502L;
    @DatabaseField(id = true)
    public String code;

    @DatabaseField
    public String name;

    @DatabaseField
    public String parentCode;
    @DatabaseField
    public int sort;

    @Override
    public String getSourceType() {
        return null;
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
    public String getTitle() {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getMessageAction() {
        return new String[0];
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public int getDefaultIconRID() {
        return 0;
    }
}
