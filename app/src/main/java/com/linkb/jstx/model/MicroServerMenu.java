
package com.linkb.jstx.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;


/**
 * 公众账号菜单
 */
@DatabaseTable(tableName = MicroServerMenu.TABLE_NAME)
public class MicroServerMenu implements Serializable {
    public static final String TABLE_NAME = "t_lvxin_microserver_menu";

    private final static String ACTION_ROOT = "0";
    private final static String ACTION_WEB = "2";
    private final static String ACTION_API = "1";
    private final static String FORMAT_TEXT = "3";
    public static final long serialVersionUID = 1L;


    @DatabaseField(id = true)
    public String id;


    @DatabaseField
    public String fid;

    @DatabaseField
    public String account;

    @DatabaseField
    public String name;

    @DatabaseField
    public String code;

    /*
    0:一级菜单
    1:调用接口
    2:网页地址
    3:回复文字,回复菜单的 content字段内容
    */
    @DatabaseField
    private String type;


    @DatabaseField
    public String content;

    @DatabaseField
    public int sort;

    public boolean hasSubMenu() {
        return ACTION_ROOT.equals(type);
    }

    public boolean isRootMenu() {
        return fid == null;
    }

    public boolean isApiMenu() {
        return ACTION_API.equals(type);
    }

    public boolean isWebMenu() {
        return ACTION_WEB.equals(type);
    }

    public boolean isTextMenu() {
        return FORMAT_TEXT.equals(type);
    }
}
