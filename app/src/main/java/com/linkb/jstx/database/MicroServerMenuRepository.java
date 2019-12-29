
package com.linkb.jstx.database;

import com.linkb.jstx.model.MicroServerMenu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MicroServerMenuRepository extends BaseRepository<MicroServerMenu, String> {

    private static MicroServerMenuRepository manager = new MicroServerMenuRepository();

    public static List<MicroServerMenu> queryPublicMenuList(String account) {

        try {
            return manager.databaseDao.queryBuilder().where().eq("account", account).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static MicroServerMenu queryById(String gid) {
        return manager.innerQueryById(gid);
    }

    public static void savePublicMenus(List<MicroServerMenu> list) {
        if (list != null && !list.isEmpty()) {
            deleteByAccount(list.get(0).account);
            manager.innerSaveAll(list);
        }

    }

    public static void deleteByAccount(String account) {

        String sql = "delete from " + MicroServerMenu.TABLE_NAME + " where account = ?";
        manager.innerExecuteSQL(sql, new String[]{account});

    }
}
