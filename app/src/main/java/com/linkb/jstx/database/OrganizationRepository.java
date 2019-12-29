
package com.linkb.jstx.database;


import com.linkb.jstx.model.Organization;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganizationRepository extends BaseRepository<Organization, String> {
    private static OrganizationRepository manager = new OrganizationRepository();

    /**
     * 组织为公共数据，不需要清除
     */
    @Override
    public  void clearTable() {
    }

    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static  List<Organization> queryLike(String name) {

        try {
            return manager.databaseDao.queryBuilder().where().like("name", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public  static List<Organization> queryList(String code) {

        try {
            return manager.databaseDao.queryBuilder().orderBy("sort",true).where().eq("parentCode", code).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static  List<Organization> queryRootList() {

        try {
            return manager.databaseDao.queryBuilder().where().isNull("parentCode").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static  String queryOrgName(String code) {
        if (code == null) {
            return null;
        }
        Organization target = manager.innerQueryById(code);
        return target == null ? null : target.name;
    }

    public static  void saveAll(final List<Organization> list) {
        manager.clearAll();
        manager.innerSaveAll(list);
    }

    public static Organization queryOne(String code) {
       return manager.innerQueryById(code);
    }
}
