
package com.linkb.jstx.database;

import android.text.TextUtils;

import com.linkb.jstx.app.Constant;
import com.linkb.jstx.app.Global;
import com.linkb.jstx.bean.ChatItem;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.message.builder.BaseBuilder;
import com.linkb.jstx.message.parser.MessageParserFactory;
import com.linkb.jstx.model.Friend;
import com.linkb.jstx.model.Message;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.model.SystemMessage;
import com.google.gson.Gson;
import com.j256.ormlite.dao.GenericRawResults;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;

import org.apache.commons.io.IOUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public  class MessageRepository extends BaseRepository<Message, Long> {

    private static final HashSet<String> INCLUDED_MESSAGE_TYPES = new HashSet<>();

    static {
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_102);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_103);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_104);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_105);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_106);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_107);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_112);
        INCLUDED_MESSAGE_TYPES.add(Constant.MessageAction.ACTION_113);
    }

    private static MessageRepository manager = new MessageRepository();

    private MessageRepository(){
        super();

        /**
         * 应用重启时，重置发送中状态的消息为发送失败状态
         */
        String sql = "update " + Message.TABLE_NAME + " set state = ? where state = ? ";
        innerExecuteSQL(sql, new String[]{Constant.MessageStatus.STATUS_SEND_FAILURE, Constant.MessageStatus.STATUS_SENDING});
    }
    public  static void add(Message msg) {

        //9开头的为无需记录的动作消息
        if (msg.isActionMessage()) {
            return;
        }
        if (INCLUDED_MESSAGE_TYPES.contains(msg.action)){
            if (queryMessage(msg, INCLUDED_MESSAGE_TYPES.toArray()).size() > 0) return;
        }
        manager.innerSave(msg);
    }

    public static List<Message> queryMessage(Message msg, Object[] includedTypes){
//        List<Message> umRedmessageList = new ArrayList<>();
//        String actions = TextUtils.join(",", includedTypes);
//        String account = Global.getCurrentAccount();
//        String sql = "SELECT id,sender,receiver,action,format,title,content,extra,state,handle,timestamp FROM ( SELECT receiver as source, * FROM " + Message.TABLE_NAME + " WHERE sender = ?   and action in("+actions+") UNION SELECT sender as source, * FROM " + Message.TABLE_NAME +" WHERE receiver = ? and action in("+actions+")) GROUP BY source ORDER BY max(timestamp) DESC";
//        GenericRawResults<String[]> rawResults = null;
//        try {
//            rawResults = manager.databaseDao.queryRaw(sql,account,account);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return umRedmessageList;
//        }
//
//        while (rawResults != null && rawResults.iterator().hasNext()) {
//            String [] row = rawResults.iterator().next();
//            Message message = new Message();
//            message.id = Long.valueOf(row[0]);
//            message.sender = row[1];
//            message.receiver = row[2];
//            message.action = row[3];
//            message.format = row[4];
//            message.title = row[5];
//            message.content = row[6];
//            message.extra = row[7];
//            message.state = row[8];
//            message.handle = row[9];
//            message.timestamp = Long.parseLong(row[10]);
//
//            MessageSource source = MessageParserFactory.getFactory().parserMessageSource(message);
//            if (source == null ) continue;
//            if (message.sender.equals(msg.sender)
//                    && message.receiver.equals(msg.receiver)
//                    && message.action.equals(msg.action)
//                    && message.content.equals(msg.content)
//                    && message.extra.equals(msg.extra)){
//                umRedmessageList.add(message);
//            }
//        }
//
//        IOUtils.closeQuietly(rawResults);
//        return umRedmessageList;

        try {
            List<Message> messageList = manager.databaseDao.queryBuilder().orderBy("timestamp", false)
                    .where().eq("sender", msg.sender)
                    .and()
                    .eq("receiver", msg.receiver)
                    .and()
                    .eq("action", msg.action)
                    .and()
                    .eq("content", msg.content)
                    .and()
                    .eq("extra", msg.extra)
                    .query();

            return messageList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    public static Message queryReceivedLastMessage(String sender, Object[] action) {
        try {

            return manager.databaseDao.queryBuilder()
                    .orderBy("timestamp", false)
                    .where().eq("sender", sender)
                    .and().in("action", action).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void deleteIncludedSystemMessageList(Object[] action) {

        String actions = TextUtils.join(",", action);
        String sql = "delete from " + Message.TABLE_NAME + "   where sender = ? and  action  in(" + actions + ") ";
        String[] params = new String[]{Constant.SYSTEM,};
        manager.innerExecuteSQL(sql, params);

    }

    public static List<Message> queryIncludedSystemMessageList(Object[] action) {

        try {
            return manager.databaseDao.queryBuilder().orderBy("timestamp", false)
                    .where()
                    .eq("sender", Constant.SYSTEM)
                    .and()
                    .in("action", action)
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static void updateStatus(long id, String status) {

        String sql = "update " + Message.TABLE_NAME + " set state = ? where id = ? ";
        manager.innerExecuteSQL(sql, new String[]{status, String.valueOf(id)});
    }
    public static List<MessageSource> getRecentContacts(Object[] includedTypes) {
        List<ChatItem> list = getRecentMessage(includedTypes);
        List<MessageSource> sourceList = new ArrayList<>();
        for (ChatItem chat : list) {
            sourceList.add(chat.source);
        }
        return sourceList;
    }

    /**
     * 查询首页最近消息列表。按照用户进行分组
     * @param includedTypes 只查询包含这些类型的消息
     * @return
     */
    public static List<ChatItem> getRecentMessage(Object[] includedTypes) {
        List<ChatItem> chatList = new ArrayList<>();
        String actions = TextUtils.join(",", includedTypes);
        String account = Global.getCurrentAccount();
        String sql = "SELECT id,sender,receiver,action,format,title,content,extra,state,handle,timestamp FROM ( SELECT receiver as source, * FROM " + Message.TABLE_NAME + " WHERE sender = ?   and action in("+actions+") UNION SELECT sender as source, * FROM " + Message.TABLE_NAME +" WHERE receiver = ? and action in("+actions+")) GROUP BY source ORDER BY max(timestamp) DESC";
        GenericRawResults<String[]> rawResults = null;
        try {
           rawResults = manager.databaseDao.queryRaw(sql,account,account);
        } catch (SQLException e) {
            e.printStackTrace();
            return chatList;
        }

        while (rawResults != null && rawResults.iterator().hasNext()) {
            String [] row = rawResults.iterator().next();
            Message message = new Message();
            message.id = Long.valueOf(row[0]);
            message.sender = row[1];
            message.receiver = row[2];
            message.action = row[3];
            message.format = row[4];
            message.title = row[5];
            message.content = row[6];
            message.extra = row[7];
            message.state = row[8];
            message.handle = row[9];
            message.timestamp = Long.parseLong(row[10]);

            MessageSource source = MessageParserFactory.getFactory().parserMessageSource(message);
            if (source == null ) continue;
            ChatItem chatItem = new ChatItem(source,message);
            chatList.add(chatItem);
        }

        IOUtils.closeQuietly(rawResults);
        return chatList;

    }

    public static long countNewMessage(Object[] action) {

        try {
            return manager.databaseDao.queryBuilder().where().eq("state", Message.STATUS_NOT_READ).and().in("action", action).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static long countNewBySender(String sender, String action) {

        if (sender == null) {
            return 0;
        }
        try {
            return manager.databaseDao.queryBuilder().where().eq("sender", sender).and().eq("state", Message.STATUS_NOT_READ).and().eq("action", action).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;

    }

    public static long countNewIncludedTypesBySender(String sender, Object[] action) {

        List<Message> umRedmessageList = new ArrayList<>();
        String actions = TextUtils.join(",", action);
        String account = Global.getCurrentAccount();
        String sql = "SELECT id,sender,receiver,action,format,title,content,extra,state,handle,timestamp FROM ( SELECT receiver as source, * FROM " + Message.TABLE_NAME + " WHERE sender = ?   and action in("+actions+") UNION SELECT sender as source, * FROM " + Message.TABLE_NAME +" WHERE receiver = ? and action in("+actions+")) GROUP BY source ORDER BY max(timestamp) DESC";
        GenericRawResults<String[]> rawResults = null;
        try {
            rawResults = manager.databaseDao.queryRaw(sql,account,account);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

        while (rawResults != null && rawResults.iterator().hasNext()) {
            String [] row = rawResults.iterator().next();
            Message message = new Message();
            message.id = Long.valueOf(row[0]);
            message.sender = row[1];
            message.receiver = row[2];
            message.action = row[3];
            message.format = row[4];
            message.title = row[5];
            message.content = row[6];
            message.extra = row[7];
            message.state = row[8];
            message.handle = row[9];
            message.timestamp = Long.parseLong(row[10]);

            MessageSource source = MessageParserFactory.getFactory().parserMessageSource(message);
            if (source == null || !message.state.equals("0")) continue;
            if (message.sender.equals(sender)) umRedmessageList.add(message);
        }

        IOUtils.closeQuietly(rawResults);
//        try {
//            return manager.databaseDao.queryBuilder().where().eq("sender", sender).and().eq("state", Message.STATUS_NOT_READ).and().in("action", action).countOf();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return 0;
        return umRedmessageList.size();

    }

    public static long countNewByType(String action) {

        try {
            return manager.databaseDao.queryBuilder().where().eq("action", action).and().eq("state", Message.STATUS_NOT_READ).countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void updateHandleState( long id,String status) {

        String sql = "update " + Message.TABLE_NAME + " set handle = ? where   id = ?";
        manager.innerExecuteSQL(sql, new String[]{status, String.valueOf(id)});

    }

    public static void deleteBySenderOrReceiver(long id) {
        String sql = "delete from " + Message.TABLE_NAME + " where sender = ? or receiver =?";
        manager.innerExecuteSQL(sql, new String[]{String.valueOf(id),String.valueOf(id)});
    }

    public static void deleteBySenderOrReceiver(String id) {
        String sql = "delete from " + Message.TABLE_NAME + " where sender = ? or receiver =?";
        manager.innerExecuteSQL(sql, new String[]{id,id});
    }

    public static void deleteByActions(String account, String[] action) {
        String actions = TextUtils.join(",", action);
        String sql = "delete from " + Message.TABLE_NAME + " where (sender = ?  or receiver = ?) and action in(" + actions + ")";
        manager.innerExecuteSQL(sql, new String[]{account, account});
    }

    public static void deleteByAction(String account, String action) {
        String sql = "delete from " + Message.TABLE_NAME + " where (sender = ?  or receiver = ?) and action =?";
        manager.innerExecuteSQL(sql, new String[]{account, account, action});
    }

    public static void deleteByActionAndContent(String action, String content) {
        String sql = "delete from " + Message.TABLE_NAME + " where content  = ? and action =?";
        manager.innerExecuteSQL(sql, new String[]{content, action});
    }

    public static void deleteByActionAndExtra(String action, String extra) {
        String sql = "delete from " + Message.TABLE_NAME + " where extra  = ? and action =?";
        manager.innerExecuteSQL(sql, new String[]{extra, action});
    }

    public static void deleteByAction(String action) {
        String sql = "delete from " + Message.TABLE_NAME + " where  action =?";
        manager.innerExecuteSQL(sql, new String[]{action});
    }

    public static Message queryById(long id) {

        return manager.innerQueryById(id);
    }

    public static void batchModifyAgree(String sourceAccount, String msgType) {

        try {
            List<Message> list = manager.databaseDao.queryForEq("action", msgType);
            for (Message m : list) {
                String account = new Gson().fromJson(m.content, BaseBuilder.class).account;
                if (sourceAccount.equals(account)) {
                    updateHandleState(m.id,SystemMessage.RESULT_AGREE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static int queryIncludedNewCount(Object[] includedTypes) {
        List<Message> umRedmessageList = new ArrayList<>();
        String actions = TextUtils.join(",", includedTypes);
        String account = Global.getCurrentAccount();
        String sql = "SELECT id,sender,receiver,action,format,title,content,extra,state,handle,timestamp FROM ( SELECT receiver as source, * FROM " + Message.TABLE_NAME + " WHERE sender = ?   and action in("+actions+") UNION SELECT sender as source, * FROM " + Message.TABLE_NAME +" WHERE receiver = ? and action in("+actions+")) GROUP BY source ORDER BY max(timestamp) DESC";
        GenericRawResults<String[]> rawResults = null;
        try {
            rawResults = manager.databaseDao.queryRaw(sql,account,account);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

        while (rawResults != null && rawResults.iterator().hasNext()) {
            String [] row = rawResults.iterator().next();
            Message message = new Message();
            message.id = Long.valueOf(row[0]);
            message.sender = row[1];
            message.receiver = row[2];
            message.action = row[3];
            message.format = row[4];
            message.title = row[5];
            message.content = row[6];
            message.extra = row[7];
            message.state = row[8];
            message.handle = row[9];
            message.timestamp = Long.parseLong(row[10]);

            MessageSource source = MessageParserFactory.getFactory().parserMessageSource(message);
            if (source == null || !message.state.equals("0")) continue;
            umRedmessageList.add(message);
        }

        IOUtils.closeQuietly(rawResults);


//
//        try {
//            return (int) manager.databaseDao.queryBuilder().where().eq("state", Message.STATUS_NOT_READ).and().in("action", includedTypes).countOf();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
        return umRedmessageList.size();


    }

    public static List<Message> queryMessage(String sender, Object[] action, int pagenow) {
        try {
            return manager.databaseDao.queryBuilder().offset(pagenow * Constant.MESSAGE_PAGE_SIZE).limit(Constant.MESSAGE_PAGE_SIZE)
                    .orderBy("timestamp", false)
                    .where().raw(formatSQLString("(receiver=? or sender=? )", sender, sender))
                    .and().in("action", action).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<Message> queryMessageByAction(String action) {
        try {
            return manager.databaseDao.queryBuilder().where().eq("action", action).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    public static List<Message> queryNewMoments(long maxSize) {
        try {
            return manager.databaseDao.queryBuilder().offset(0L).limit(maxSize)
                    .orderBy("timestamp", false)
                    .where().raw(formatSQLString("(action=?   or  action=?)", Constant.MessageAction.ACTION_501, Constant.MessageAction.ACTION_502))
                    .and().eq("state", Message.STATUS_NOT_READ).query();
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public static List<Message> queryMoments() {

        try {
            return manager.databaseDao.queryBuilder()
                    .orderBy("timestamp", false)
                    .where().raw(formatSQLString("(action=?   or  action=?)", Constant.MessageAction.ACTION_501, Constant.MessageAction.ACTION_502))
                    .query();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return new ArrayList<>();


    }

    public static Message queryNewMomentMessage() {
        try {
            return manager.databaseDao.queryBuilder().orderBy("timestamp", false).where().eq("action", Constant.MessageAction.ACTION_500).queryForFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void batchReadMessage(String[] actions) {

        String sql = "update " + Message.TABLE_NAME + " set state = ? where action in(" + TextUtils.join(",", actions) + ") ";
        manager.innerExecuteSQL(sql, new String[]{Message.STATUS_READ});

    }

    public static void batchReadMessage(String id, String[] actions) {
        String sql = "update " + Message.TABLE_NAME + " set state = ? where (sender = ? or receiver = ?) and action in(" + TextUtils.join(",", actions) + ") ";
        manager.innerExecuteSQL(sql, new String[]{Message.STATUS_READ, id, id});
    }

    private static String formatSQLString(String rawSql, Object... args) {
        StringBuilder sb = new StringBuilder(rawSql);
        for (Object arg : args) {
            int index = sb.indexOf("?");
            if (arg.getClass() == String.class) {
                sb.replace(index, index + 1, "'" + arg + "'");
            } else {
                sb.replace(index, index + 1, arg.toString());
            }
        }
        return sb.toString();
    }

    public static void updateSender(long id, String sender) {

        String sql = "update " + Message.TABLE_NAME + " set sender = ? where id = ? ";
        manager.innerExecuteSQL(sql, new String[]{sender, String.valueOf(id)});
    }

    public static void updateReceiver(long id, String receiver) {
        String sql = "update " + Message.TABLE_NAME + " set receiver = ? where   id = ?  ";
        manager.innerExecuteSQL(sql, new String[]{receiver, String.valueOf(id)});
    }

    public static void deleteById(long id) {
        manager.innerDeleteById(id);
    }

    public static void batchReadFriendMessage(String account) {
        String sql = "update " + Message.TABLE_NAME + " set state = ? where (action  = ? or action  = ?) and sender = ? ";
        manager.innerExecuteSQL(sql, new String[]{Message.STATUS_READ,Constant.MessageAction.ACTION_0,Constant.MessageAction.ACTION_ReadDelete,account});


    }


    public static void batchReadGroupMessage(String groupId) {
        String sql = "update " + Message.TABLE_NAME + " set state = ? where action  = ? and sender = ? ";
        manager.innerExecuteSQL(sql, new String[]{Message.STATUS_READ,Constant.MessageAction.ACTION_3,groupId});
    }


    public static List<Message> queryUserMessageSection(String sender,long start,long pageSize) {
        try {
            return manager.databaseDao.queryBuilder().offset(start).limit(pageSize)
                    .orderBy("timestamp", false)
                    .where().raw(formatSQLString("(receiver=? or sender=? )", sender, sender))
                    .and().eq("action", Constant.MessageAction.ACTION_0)
                    .or().eq("action", Constant.MessageAction.ACTION_ReadDelete).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    public static List<Message> queryGroupMessageSection(String sender,long start,long pageSize) {
        try {
            return manager.databaseDao.queryBuilder().offset(start).limit(pageSize)
                    .orderBy("timestamp", false)
                    .where().raw(formatSQLString("(receiver=? or sender=? )", sender, sender)).and().in("action",Constant.MessageAction.ACTION_1,Constant.MessageAction.ACTION_3).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


}
