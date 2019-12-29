
package com.linkb.jstx.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@DatabaseTable(tableName = "t_lvxin_moment")
public class Moment implements Serializable {

    public final static String FORMAT_TEXT = "3";//文
    public final static String FORMAT_IMAGE = "0";//图
    public final static String FORMAT_LINK = "1";//网址连接
    public final static String FORMAT_VIDEO = "2";//视频
    public final static String FORMAT_MULTI_IMAGE = "4";// 多个图片

    private static final long serialVersionUID = 1L;
    @DatabaseField(id = true)
    public long id;

    @DatabaseField
    public String account;
    @DatabaseField
    public String text;
    @DatabaseField
    public String content;
    @DatabaseField
    public String extra;
    @DatabaseField
    public String type;
    @DatabaseField
    public long timestamp;

    private List<Comment> commentList = new ArrayList<>();

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> list) {
        commentList.clear();
        commentList.addAll(list);
    }


    public int getAllCount() {
        return commentList.size();
    }

    public List<Comment> getTextList() {
        List<Comment> textList = new ArrayList<>();
        for (Comment comment : commentList) {
            if (!comment.type.equals(Comment.TYPE_2)) {
                textList.add(comment);
            }
        }
        return textList;
    }

    public List<Comment> getPraiseList() {
        List<Comment> praiseList = new ArrayList<>();
        for (Comment comment : commentList) {
            if (comment.type.equals(Comment.TYPE_2)) {
                praiseList.add(comment);
            }
        }

        return praiseList;
    }

    public int getTextCount() {
        int count = 0;
        for (Comment comment : commentList) {
            if (!comment.type.equals(Comment.TYPE_2)) {
                count++;
            }
        }
        return count;
    }

    public int getPraiseCount() {
        int count = 0;
        for (Comment comment : commentList) {
            if (comment.type.equals(Comment.TYPE_2)) {
                count++;
            }
        }
        return count;
    }

    public void add(Comment comment) {
        commentList.add(comment);
    }

    public void remove(Comment comment) {
        commentList.remove(comment);
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Moment) {
            Moment target = (Moment) o;
            return id ==(target.id) && target.commentList.equals(commentList);
        }
        return false;
    }

}
