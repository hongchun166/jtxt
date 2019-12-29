
package com.linkb.jstx.database;

import com.linkb.jstx.model.GroupMember;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GroupMemberRepository extends BaseRepository<GroupMember, String> {

    private static GroupMemberRepository manager = new GroupMemberRepository();

    public static List<GroupMember> queryMemberList(long groupId) {
        List<GroupMember> list = new ArrayList<>();
        try {
            list.addAll(manager.databaseDao.queryBuilder().orderBy("host", false).where().eq("groupId", groupId).query());
//            for (GroupMember member : list) {
//                member.name = FriendRepository.queryFriendName(member.account);
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public static List<String> queryMemberAccountList(long groupId) {

        List<String> list = new ArrayList<>();
        List<GroupMember> data = queryMemberList(groupId);
        for (GroupMember m : data) {
            list.add(m.account);
        }
        return list;
    }

    public static void saveAll(List<GroupMember> list) {

        if (list == null || list.isEmpty()) {
            return;
        }

        deleteByGroupId(list.get(0).groupId);
        manager.innerSaveAll(list);
    }

    public static void saveMember(GroupMember g) {
        manager.createOrUpdate(g);
    }

    private static void deleteByGroupId(long groupId) {
        manager.innerExecuteSQL("delete from " + GroupMember.TABLE_NAME + " where groupId=?", new String[]{String.valueOf(groupId)});
    }

    public static void delete(long groupId, String account) {
        manager.innerExecuteSQL("delete from " + GroupMember.TABLE_NAME + " where groupId=? and account=?", new String[]{String.valueOf(groupId), account});
    }

    public static void delete(long groupId, List<String> list) {
        try {
            DeleteBuilder builder = manager.databaseDao.deleteBuilder();
            builder.where().eq("groupId", groupId).and().in("account", list);
            builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
