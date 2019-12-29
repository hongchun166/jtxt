package com.linkb.jstx.database;

import com.linkb.jstx.model.ContactInfo;
import com.linkb.jstx.model.Friend;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/*
* 手机通讯录缓存数据库
* */
public class PhoneContactsRepository extends BaseRepository<ContactInfo, String> {

    private static PhoneContactsRepository manager = new PhoneContactsRepository();
    private static ConcurrentHashMap<String, Friend> friendCache = new ConcurrentHashMap<>();


    @Override
    public  void clearTable() {
    }


    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static List<ContactInfo> queryLike(String name) {

        try {
            return manager.databaseDao.queryBuilder().where().like("name", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<ContactInfo> queryContactsList() {
        return manager.innerQueryAll();
    }

    public static void saveAll(final List<ContactInfo> list) {
        manager.clearAll();
        manager.innerSaveAll(list);
        friendCache.clear();
    }

    public static long count() {
        try {
            return manager.databaseDao.countOf();
        } catch (SQLException e) {
            return 0L;
        }
    }
}
