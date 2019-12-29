
package com.linkb.jstx.database;

import android.support.v4.util.ArrayMap;

import com.linkb.jstx.model.Group;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupRepository extends BaseRepository<Group, Long> {

    private static GroupRepository manager = new GroupRepository();
    private static ArrayMap<Long, Group> groupCache = new ArrayMap<>();

    public static List<Group> queryCreatedList(String account) {
        try {
            return manager.databaseDao.queryBuilder().where().eq("founder", account).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Group> queryJoinList(String account) {

        try {
            return manager.databaseDao.queryBuilder().where().ne("founder", account).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();

    }

    public static List<Group> queryLike(String name) {

        try {
            return manager.databaseDao.queryBuilder().where().like("name", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static void saveAll(final List<Group> list) {
        groupCache.clear();
        manager.clearAll();
        manager.innerSaveAll(list);
        for (Group g : list) {
            GroupMemberRepository.saveAll(g.memberList);
        }

    }

    public static void add(Group g) {
        manager.innerSave(g);
    }

    public static void deleteById(Long groupId) {
        manager.innerDeleteById(groupId);
        groupCache.remove(groupId);
    }

    public static void update(Group group) {
        manager.innerUpdate(group);
        groupCache.put(group.id,group);
    }

    public static Group queryById(String groupId) {
        return queryById(Long.parseLong(groupId));
    }

    public static String queryName(Long groupId) {
        Group group = queryById(groupId);
        return group == null ? null :group.name;
    }
    public static Group queryById(Long groupId) {
        Group group = groupCache.get(groupId);
        if (group == null) {
            group = manager.innerQueryById(groupId);
            groupCache.put(groupId, group);
        }
        return group;
    }
}
