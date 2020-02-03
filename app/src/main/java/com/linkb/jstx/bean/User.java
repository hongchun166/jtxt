
package com.linkb.jstx.bean;

import com.linkb.jstx.model.Friend;

import java.io.Serializable;

public class User implements Serializable {

    public transient static final long serialVersionUID = 4733464888738356502L;

    public transient static final String OFF_LINE = "0";

    public transient static final String ON_LINE = "1";

    public transient static final String GENDER_MAN = "1";

    public transient static final String GENDER_FEMALE = "0";
    public String headUrl;
    public String account;
    public String password;
    public String name;
    public String gender;//性别
    public String telephone;
    public String email;
    public String code;
    public String motto;//签名
    public int grade;
    public boolean disabled;
    public String marrriage;//婚姻
    public String region;//区域
    public String industry;//行业
    public String label;//标签
    public String job;//职务

    @Override
    public boolean equals(Object o) {
        if (o instanceof User) {
            User obj = (User) o;
            if (account != null && obj.account != null) {
                return account.equals(obj.account);
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }


    public static Friend UserToFriend(User user) {
        Friend friend = new Friend();
        friend.account = user.account;
        friend.name = user.name;
        friend.gender = user.gender;
        friend.telephone = user.telephone;
        friend.email = user.email;
        friend.code = user.code;
        friend.motto = user.motto;
        return friend;
    }

}
