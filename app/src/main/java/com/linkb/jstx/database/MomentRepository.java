
package com.linkb.jstx.database;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.model.Moment;
import com.linkb.jstx.util.BackgroundThreadHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MomentRepository extends BaseRepository<Moment, Long> {

    private static MomentRepository manager = new MomentRepository();

    public static void add(final Moment article) {
        BackgroundThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                manager.saveIfNotExists(article);
                CommentRepository.saveAll(article.getCommentList());
            }
        });
    }

    public static void saveAll(final List<Moment> list) {
        BackgroundThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                for (Moment article : list) {
                    manager.saveIfNotExists(article);
                    CommentRepository.saveAll(article.getCommentList());
                }
            }
        });

    }

    public static List<Moment> queryFirstPage() {
        try {
            List<Moment> list = manager.databaseDao.queryBuilder().offset(0L).limit(Constant.MOMENT_PAGE_SIZE).orderBy("timestamp", false).query();
            for (Moment article : list) {
                article.setCommentList(CommentRepository.queryCommentList(article.id));
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Moment> queryFirstPage(String account, int pagenow) {

        long startRow = (long) (pagenow - 1) * Constant.MESSAGE_PAGE_SIZE;
        try {
            return manager.databaseDao.queryBuilder().offset(startRow).limit(Constant.MESSAGE_PAGE_SIZE)
                    .orderBy("timestamp", false)
                    .where().eq("account", account)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new ArrayList<>();
    }


    public static void deleteById(long gid) {
        manager.innerDeleteById(gid);
        CommentRepository.deleteByTargetId(gid);
    }

    public static Moment queryById(long gid) {
        Moment article = manager.innerQueryById(gid);
        if (article == null) {
            return null;
        }
        article.setCommentList(CommentRepository.queryCommentList(article.id));
        return article;
    }
}
