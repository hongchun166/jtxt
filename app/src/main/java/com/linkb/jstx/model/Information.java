package com.linkb.jstx.model;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.linkb.jstx.util.TimeUtils;

import java.io.Serializable;

/** 资讯
* */
@DatabaseTable(tableName = "t_lvxin_information")
public class Information implements Serializable {

    private transient static final long serialVersionUID = 123415431L;

    @DatabaseField(id = true)
    public long id;

    @DatabaseField
    public String content;

    @DatabaseField
    public String title;

    @DatabaseField
    public String textContent;

    @DatabaseField
    public String status;

    @DatabaseField
    public String author;

    @DatabaseField
    public long timestamp;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextContent() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getTimestampStr() {
        return TimeUtils.millis2String(timestamp, TimeUtils.getCustomFormat());
    }

    public String getTimestampHeaderStr() {
        String week = TimeUtils.millis2String(timestamp, TimeUtils.getCustomFormatHeader());
        String[] strArr = week.split("-");
        String formatStr = "";
        if (strArr.length > 1){
            formatStr = strArr[0] + "月" + strArr[1];
        }else if (strArr.length > 2){
            formatStr = strArr[0] + "月" + strArr[1] + "日  " + strArr[2];
        }

        return formatStr;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

}
