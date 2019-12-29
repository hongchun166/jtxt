package com.linkb.jstx.model;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.linkb.jstx.util.Cn2Spell;

import java.io.Serializable;

/**
 * @author hydCoder
 * @date 2017/10/11 10:50
 * @desc 手机联系人的数据bean
 * @email hyd_coder@163.com
 */
@DatabaseTable(tableName = ContactInfo.TABLE_NAME)
public class ContactInfo implements Comparable<ContactInfo>, Serializable {
    private static final long serialVersionUID = 4456456466L;

    public static final String TABLE_NAME = "t_lvxin_phone_contacts";
    @DatabaseField(id = true)
    public String id;
    @DatabaseField
    public String name;
    @DatabaseField
    public String phone;
    public String pinyin; // 姓名对应的拼音
    public String firstLetter; // 拼音的首字母
    public String userAvatar;
    public String userName;
    public String userNick;
    public int    isFriend;
    public String userId;
    public int gradeLevel;
    public String userPosition;
    public String userCompany;
    public int userType;
    public boolean isUser = false;

    public ContactInfo() {
    }

    public ContactInfo(String id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        pinyin = Cn2Spell.getPinYin(name); // 根据姓名获取拼音
        if (!TextUtils.isEmpty(pinyin)){
            firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
        }
        if (firstLetter == null || !firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
            firstLetter = "#";
        }
    }

    @Override
    public int compareTo(@NonNull ContactInfo another) {
        if (firstLetter.equals("#") && !another.firstLetter.equals("#")) {
            return 1;
        } else if (!firstLetter.equals("#") && another.firstLetter.equals("#")){
            return -1;
        } else {
            return pinyin.compareToIgnoreCase(another.pinyin);
        }
    }
}

