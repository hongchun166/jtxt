
package com.linkb.jstx.database;

import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.util.BackgroundThreadHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MicroServerRepository extends BaseRepository<MicroServer, String> {

    private static MicroServerRepository manager = new MicroServerRepository();

    public static List<MicroServer> queryList() {

        return manager.innerQueryAll();
    }

    public static List<MicroServer> queryLike(String name) {

        try {
            return manager.databaseDao.queryBuilder().where().like("name", "%" + name + "%").or().like("account", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static MicroServer queryById(String account) {

        return manager.innerQueryById(account);
    }

    public static void saveAll(final List<MicroServer> list) {
        BackgroundThreadHandler.post(new Runnable() {
            @Override
            public void run() {
                manager.clearAll();
                manager.innerSaveAll(list);
                for (MicroServer pub : list) {
                    MicroServerMenuRepository.savePublicMenus(pub.menuList);
                }
            }
        });

    }

    public static void add(MicroServer u) {
        manager.innerSave(u);
    }

    public static void delete(String gid) {
        manager.innerDeleteById(gid);
    }

}
