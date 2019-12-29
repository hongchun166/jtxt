
package com.linkb.jstx.database;


import com.linkb.jstx.app.Global;
import com.linkb.jstx.listener.DatabaseChangedListener;
import com.linkb.jstx.util.MD5;
import com.j256.ormlite.dao.Dao;

import org.apache.commons.io.IOUtils;

import java.lang.reflect.ParameterizedType;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseRepository<T, ID> implements DatabaseChangedListener {
    public final static String DATABASE_NAME = "common.db";
    private GlobalDatabaseHelper mDatabaseHelper;
    Dao<T, ID> databaseDao;

    BaseRepository() {
        GlobalDatabaseHelper.registerListener(this);
        create();
    }

    private void create(){
        mDatabaseHelper = GlobalDatabaseHelper.createDatabaseHelper(getDatabaseName());
        try {
            Class  rawType = (Class)((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            databaseDao = (Dao<T, ID>) mDatabaseHelper.getDao(rawType);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDatabaseChanged(){
        create();
    }

    public void onTableClearAll(){
        clearTable();
    }

    void clearTable(){
        clearAll();
    }
    @Override
    public void onDatabaseDestroy(){
        destroy();
    }
    String getDatabaseName() {
        return MD5.digest(Global.getCurrentAccount()) + ".db";
    }

    void innerSave(T obj) {
        try {
            databaseDao.create(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void innerSaveAll(List<T> list) {
        try {
            databaseDao.create(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void saveIfNotExists(T obj) {
        try {
            databaseDao.createIfNotExists(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    List<T> innerQueryAll() {
        List<T> list = new ArrayList<>();
        try {
            list = databaseDao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    void innerUpdate(T obj) {
        try {
            databaseDao.update(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void clearAll() {
        try {
            databaseDao.deleteBuilder().delete();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void createOrUpdate(T obj) {
        try {
            databaseDao.createOrUpdate(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    T innerQueryById(ID gid) {
        try {
            return databaseDao.queryForId(gid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    void innerExecuteSQL(String SQL, String[] strings) {

        try {
            databaseDao.executeRaw(SQL, strings);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void innerExecuteSQL(String SQL) {
        try {
            databaseDao.executeRawNoArgs(SQL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void innerDeleteById(ID gid) {
        try {
            databaseDao.deleteById(gid);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void innerDelete(T obj) {
        try {
            databaseDao.delete(obj);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void destroy() {
        mDatabaseHelper.close();
        mDatabaseHelper = null;
        databaseDao.clearObjectCache();
        IOUtils.closeQuietly(databaseDao.getConnectionSource());
    }

}
