
package com.linkb.jstx.database;

import com.linkb.jstx.model.Tag;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TagRepository extends BaseRepository<Tag, Long> {

    private static TagRepository manager = new TagRepository();

    public static List<Tag> queryList() {
        try {
            return manager.databaseDao.queryBuilder().orderBy("id",true).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public static void saveAll(final List<Tag> list) {
        manager.clearAll();
        manager.innerSaveAll(list);
    }
    public static void add(Tag g) {
        manager.innerSave(g);
    }

    public static void delete(Long id) {
        manager.innerDeleteById(id);
    }
    public static void update(Tag tag) {
        manager.innerUpdate(tag);
    }
}
