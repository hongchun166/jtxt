
package com.linkb.jstx.database;


import com.linkb.jstx.app.Global;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class FriendRepository extends BaseRepository<Friend, String> {
    private static FriendRepository manager = new FriendRepository();
    private static ConcurrentHashMap<String, Friend> friendCache = new ConcurrentHashMap<>();

    /**
     * 通讯录为公共数据，不需要清除
     */
    @Override
    public  void clearTable() {
    }

    @Override
    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    public static List<Friend> queryLike(String name) {

        try {
            return manager.databaseDao.queryBuilder().where().like("name", "%" + name + "%").or().like("account", "%" + name + "%").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<Friend> queryLikeList(List<String> account,boolean in,String name) {

        try {
            if (in){
                return manager.databaseDao.queryBuilder().where().like("name", "%" + name + "%").and().in("account",account) .query();
            }else {
                return manager.databaseDao.queryBuilder().where().like("name", "%" + name + "%").and().notIn("account",account) .query();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Friend> queryFriendList() {
        try {
            return manager.databaseDao.queryBuilder().where().ne("account", Global.getCurrentAccount()).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }


    public static List<Friend> queryFriendList(List<String> account,boolean in) {

        try {
            if (in){
                return manager.databaseDao.queryBuilder().where().in("account",account).query();
            }else {
                return manager.databaseDao.queryBuilder().where().notIn("account",account).query();
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
        return new ArrayList<>();

    }

    public static List<Friend> queryFriendList(String code) {
        try {
            return (manager.databaseDao.queryBuilder().where().eq("code", code).query());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<Friend> queryRootFriendList() {
        try {
            return manager.databaseDao.queryBuilder().where().isNull("code").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static Friend queryFriend(String account) {
        Friend friend = manager.innerQueryById(account);
        if (friend == null){
            friend = new Friend();
            friend.account = account;
        }
        return friend;
    }

    public static Friend queryFriendExit(String account) {
        Friend friend = manager.innerQueryById(account);

        return friend;
    }


    public static Friend queryFriend(String account, HttpRequestListener<BasePersonInfoResult> listener) {
        Friend friend = manager.innerQueryById(account);
        if (friend == null) {
            HttpServiceManager.queryPersonInfo(account, listener);
            return null;
        }

        return friend;

//        Friend friend = friendCache.get(account);
//        if (friend == null) {
//            friend = manager.innerQueryById(account);
//            if (friend == null) return null;
//            friendCache.put(account, friend);
//        }
//        return friend;
    }

    private HttpRequestListener<BasePersonInfoResult> mListener = new HttpRequestListener<BasePersonInfoResult>() {
        @Override
        public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {

        }

        @Override
        public void onHttpRequestFailure(Exception e, OriginalCall call) {

        }
    };

    public static String queryFriendName(String account) {

        Friend friend = queryFriend(account);
        return friend == null ? "Unknow" : friend.name;
    }

    public static boolean isFriend(String account) {

        return queryFriend(account) != null;
    }

    public static boolean isNotExisted(String sender, String receiver) {

        if (Global.getCurrentAccount().equals(sender)) {
            return queryFriend(receiver) == null;
        }
        return queryFriend(sender) == null;
    }

    public static void saveAll(final List<Friend> list) {
        manager.clearAll();
        manager.innerSaveAll(list);
        friendCache.clear();
    }

    public static void modifyOnlineStatus(String account, String online) {

        String sql = "update " + Friend.TABLE_NAME + " set online=? where source=?";
        manager.innerExecuteSQL(sql, new String[]{online, account});
    }

    public static void save(Friend friend) {
        manager.innerSave(friend);
    }

    public static void update(Friend friend) {
        manager.innerUpdate(friend);
        friendCache.put(friend.account, friend);
    }


    /** 设置备注
    * */
    public static void modifyName(String account, String name) {

        String sql = "update " + Friend.TABLE_NAME + " set name=?  where source=?";
        manager.innerExecuteSQL(sql, new String[]{name, account});

        Friend friend = friendCache.get(account);
        if (friend != null) {
            friend.name = name;
        }
    }

    public static void modifyNameAndMotto(String account, String name, String motto) {

        String sql = "update " + Friend.TABLE_NAME + " set name=?,motto = ?  where source=?";
        manager.innerExecuteSQL(sql, new String[]{name, motto, account});

        Friend friend = friendCache.get(account);
        if (friend != null) {
            friend.name = name;
            friend.motto = motto;
        }
    }

    public static long count() {
        try {
            return manager.databaseDao.countOf();
        } catch (SQLException e) {
            return 0L;
        }
    }

    public interface QueryFriendListener {
        void postQueryFriend(Friend friend);
    }
}
