
package com.linkb.jstx.model;

import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.app.Constant;
import com.linkb.R;
import com.linkb.jstx.util.FileURLBuilder;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.List;


/**
 * 公众账号
 */
@DatabaseTable(tableName = "t_lvxin_microserver")

public class MicroServer extends MessageSource implements Serializable {


    public static final long serialVersionUID = 1L;

    private final static String[] MESSAGE_ACTION = new String[]{Constant.MessageAction.ACTION_200, Constant.MessageAction.ACTION_201};

    @DatabaseField(id = true)
    public String account;

    @DatabaseField
    public String description;


    @DatabaseField
    public String name;


    @DatabaseField
    public String power;

    @DatabaseField
    public String website;

    @DatabaseField
    public String greet;

    @DatabaseField
    public String url;

    public List<MicroServerMenu> menuList;

    @Override
    public String getSourceType() {

        return SOURCE_MICROSERVER;
    }

    @Override
    public String getWebIcon() {

        return FileURLBuilder.getServerLogoUrl(account);
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
    public String getName() {

        return name;
    }

    @Override
    public String getId() {

        return account;
    }

    @Override
    public int getDefaultIconRID() {

        return R.drawable.icon_microserver;
    }

    @Override
    public String[] getMessageAction() {

        return MESSAGE_ACTION;
    }

    @Override
    public int getTitleColor() {
        return R.color.text_blue;
    }
}
