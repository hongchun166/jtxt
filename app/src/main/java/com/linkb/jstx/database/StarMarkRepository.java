
package com.linkb.jstx.database;

import com.linkb.jstx.model.StarMark;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StarMarkRepository extends BaseRepository<StarMark, String> {
    private static StarMarkRepository manager = new StarMarkRepository();

    public static void save(String account) {
        StarMark mark = new StarMark();
        mark.account = account;
        manager.createOrUpdate(mark);
    }

    public static boolean isStarMark(String account) {
        try {
            return manager.databaseDao.queryBuilder().where().eq("account", account).countOf() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<String> queryAccountList() {
        List<String> accountList = new ArrayList<>();
        try {
            List<StarMark> list = manager.databaseDao.queryBuilder().query();
            for (StarMark starMark : list) {
                accountList.add(starMark.account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accountList;
    }

    public static void delete(String account) {
        manager.innerDeleteById(account);
    }
}
