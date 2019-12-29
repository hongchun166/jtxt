
package com.linkb.jstx.database;

import com.linkb.jstx.model.Comment;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentRepository extends BaseRepository<Comment, Long> {

    private static CommentRepository manager =  new CommentRepository();

    public static void saveAll(List<Comment> list) {

        if (list == null || list.isEmpty()) {
            return;
        }
        deleteByTargetId(list.get(0).targetId);
        manager.innerSaveAll(list);
    }
    public static void deleteById(String id) {
        deleteById(Long.parseLong(id));
    }
    public static void deleteById(Long id) {
        manager.innerDeleteById(id);
    }

    public static void deleteByTargetId(long targetId) {
        manager.innerExecuteSQL("delete from t_lvxin_comment where targetId=?", new String[]{String.valueOf(targetId)});
    }

    public static Comment query(String articleId, String account, String type) {
        try {
            return manager.databaseDao.queryBuilder().where().eq("targetId", articleId).and().eq("account", account).and().eq("type", type).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Comment> queryCommentList(long targetId) {

        try {
            return manager.databaseDao.queryBuilder().orderBy("timestamp", true).where().eq("targetId", targetId).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }
    public static void delete(Comment comment) {
        manager.innerDelete(comment);
    }
    public static void add(Comment comment) {
        manager.innerSave(comment);
    }
}
