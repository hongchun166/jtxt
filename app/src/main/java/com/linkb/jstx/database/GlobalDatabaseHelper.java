
package com.linkb.jstx.database;

import android.database.sqlite.SQLiteDatabase;

import com.linkb.jstx.listener.DatabaseChangedListener;
import com.linkb.jstx.model.Comment;
import com.linkb.jstx.model.ContactInfo;
import com.linkb.jstx.model.MicroServer;
import com.linkb.jstx.model.MicroServerMenu;
import com.linkb.jstx.model.MomentRule;
import com.linkb.jstx.model.Organization;
import com.linkb.jstx.model.StarMark;
import com.linkb.jstx.model.Tag;
import com.linkb.jstx.app.LvxinApplication;
import com.linkb.jstx.model.ChatTop;
import com.linkb.jstx.model.Config;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.GlideImage;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.model.GroupMember;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.Moment;
import com.linkb.BuildConfig;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashSet;

public class GlobalDatabaseHelper extends OrmLiteSqliteOpenHelper {
    private final static HashSet<DatabaseChangedListener> DATABASE_LISTENER_SET = new HashSet();
    private static GlobalDatabaseHelper instance;

    private GlobalDatabaseHelper(String databaseName) {
        super(LvxinApplication.getInstance(), databaseName, null, BuildConfig.VERSION_CODE);
    }

    public static synchronized GlobalDatabaseHelper createDatabaseHelper(String database) {
        if (instance == null || !instance.isOpen() || !database.equals(instance.getDatabaseName())) {
            instance = new GlobalDatabaseHelper(database);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource source) {

        try {
            if (getDatabaseName().equals(BaseRepository.DATABASE_NAME)) {
                TableUtils.createTableIfNotExists(source, GlideImage.class);
                TableUtils.createTableIfNotExists(source, Friend.class);
                TableUtils.createTableIfNotExists(source, Organization.class);
                TableUtils.createTableIfNotExists(source, ContactInfo.class);
                return;
            }
            TableUtils.createTableIfNotExists(source, Message.class);
            TableUtils.createTableIfNotExists(source, Moment.class);
            TableUtils.createTableIfNotExists(source, Group.class);
            TableUtils.createTableIfNotExists(source, GroupMember.class);
            TableUtils.createTableIfNotExists(source, MicroServer.class);
            TableUtils.createTableIfNotExists(source, MicroServerMenu.class);
            TableUtils.createTableIfNotExists(source, Config.class);
            TableUtils.createTableIfNotExists(source, ChatTop.class);
            TableUtils.createTableIfNotExists(source, Comment.class);
            TableUtils.createTableIfNotExists(source, MomentRule.class);
            TableUtils.createTableIfNotExists(source, StarMark.class);
            TableUtils.createTableIfNotExists(source, Tag.class);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource source, int oldVersion, int newVersion) {
        onCreate(db, source);
        if (oldVersion < BuildConfig.VERSION_CODE){
            try {
                TableUtils.createTableIfNotExists(source, Tag.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    public static void registerListener(DatabaseChangedListener listener) {
        DATABASE_LISTENER_SET.add(listener);
    }

    public static void onServerChanged() {
        for (DatabaseChangedListener listener : DATABASE_LISTENER_SET)
        {
            listener.onTableClearAll();
        }
    }
    public static void onAccountChanged() {
        for (DatabaseChangedListener listener : DATABASE_LISTENER_SET)
        {
            listener.onDatabaseChanged();
        }
    }
}
