
package com.linkb.jstx.database;

import com.linkb.jstx.model.MomentRule;
import com.j256.ormlite.stmt.DeleteBuilder;

import java.sql.SQLException;
import java.util.List;

public class MomentRuleRepository extends BaseRepository<MomentRule, Long> {
    private static MomentRuleRepository manager = new MomentRuleRepository();

    public static void add(MomentRule model) {
        manager.createOrUpdate(model);
    }
    public static void saveAll(List<MomentRule> list) {
        manager.clearAll();
        manager.innerSaveAll(list);
    }

    public static boolean hasExist(String source, String target, String type) {
        try {
             return  manager.databaseDao.queryBuilder().where()
                    .eq("source", source)
                    .and().eq("target", target)
                    .and().eq("type", type).countOf() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void remove(MomentRule model) {
        try {
            DeleteBuilder builder = manager.databaseDao.deleteBuilder();
            builder.where().eq("source", model.source);
            builder.where().eq("target", model.target);
            builder.where().eq("type", model.type);
            builder.delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
