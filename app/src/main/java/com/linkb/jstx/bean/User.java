
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
    public String area;//区域
    public String industry;//行业
    public String tag;//标签
    public String position;//职务

    public String backgroudUrl;//朋友圈背景图片

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

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMotto() {
        return motto;
    }

    public void setMotto(String motto) {
        this.motto = motto;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getMarrriage() {
        return marrriage;
    }

    public void setMarrriage(String marrriage) {
        this.marrriage = marrriage;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getBackgroudUrl() {
        return backgroudUrl;
    }

    public void setBackgroudUrl(String backgroudUrl) {
        this.backgroudUrl = backgroudUrl;
    }
}
