
package com.linkb.jstx.database;

import com.linkb.jstx.util.StringUtils;
import com.linkb.jstx.model.ChatTop;
import com.linkb.jstx.model.MessageSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ChatTopRepository extends BaseRepository<ChatTop, String> {

    private static ChatTopRepository manager = new ChatTopRepository();

    public static List<ChatTop> queryList() {
        try {
            return manager.databaseDao.queryBuilder().orderBy("sort", false).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void updateSort(ChatTop top){
        top.sort = System.currentTimeMillis();
        manager.innerUpdate(top);
    }
    public static ChatTop setTop(Class<? extends MessageSource> sourceName, String sender) {
        ChatTop top = new ChatTop();
        top.sender = sender;
        top.sourceName = sourceName.getName();
        top.sort = System.currentTimeMillis();
        top.gid = StringUtils.getUUID();
        manager.innerSave(top);
        return top;
    }

    public static void delete(Class<? extends MessageSource> sourceName, String sender) {
        manager.innerExecuteSQL("delete from t_lvxin_chattop where sourceName = ? and sender = ?", new String[]{sourceName.getName(), sender});
    }
}
