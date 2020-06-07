
package com.linkb.jstx.app;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.linkb.jstx.bean.Location;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.model.MessageSource;
import com.linkb.jstx.util.StringUtils;
import com.linkb.BuildConfig;
import com.google.gson.Gson;

public class Global {

    private static final String MODEL_KEY = "CURRENT_USER";

    public static final String KEY_USER_MomentBackgroudUrl= "KEY_USER_MomentBackgroudUrl";
    public static final String KEY_USER_HeadUrl= "KEY_USER_HeadUrl";
    private static final String KEY_USER_ACCOUNT = "KEY_USER_ACCOUNT";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    private static final String KEY_USER_MOTTO = "KEY_USER_MOTTO";
    private static final String KEY_USER_GENDER = "KEY_USER_GENDER";
    public static final String KEY_USER_REGION = "KEY_USER_REGION";
    public static final String KEY_USER_MARRIAGE = "KEY_USER_MARRIAGE";
    public static final String KEY_USER_INDUSTRY = "KEY_USER_INDUSTRY";
    public static final String KEY_USER_LABEL = "KEY_USER_LABEL";
    public static final String KEY_USER_JOB = "KEY_USER_JOB";

    private static final String KEY_USER_EMAIL = "KEY_USER_EMAIL";
    private static final String KEY_USER_TELEPHONE = "KEY_USER_TELEPHONE";
    private static final String KEY_USER_ORGCODE = "KEY_USER_ORGCODE";

    private static final String KEY_PHOTO_GRAPH_FILE_PATH = "KEY_PHOTO_GRAPH_FILE_PATH";
    private static final String KEY_CROP_PHOTO_FILE_PATH = "KEY_CROP_PHOTO_FILE_PATH";


    private static final String KEY_CHAT_DRAFT = "KEY_CHAT_DRAFT_%1$s_%2$s";

    private static final String KEY_APP_INBACGROUND = "KEY_APP_INBACGROUND";

    private static final String KEY_APP_TOP_ACTIVITY = "KEY_APP_TOP_ACTIVITY";

    private static final String KEY_BETTERY_SAVING_SHOW = "KEY_BETTERY_SAVING_SHOW";

    private static final String KEY_CHATING_TEXTVIEW_WIDTH = "KEY_CHATING_TEXTVIEW_WIDTH";

    private static final String KEY_FRIST_LOGIN = "KEY_FRIST_LOGIN_%1$s";

    private static final String KEY_CURRENT_LOCATION = "KEY_CURRENT_LOCATION";


    private Global() {
    }

    public static User getCurrentUser() {

        Account account = getLoginedAccount();
        if (account != null) {
            AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
            User mUser = new User();
            mUser.account = accountManager.getUserData(account, KEY_USER_ACCOUNT);
            mUser.password = accountManager.getPassword(account);
            mUser.name = accountManager.getUserData(account, KEY_USER_NAME);
            mUser.gender = accountManager.getUserData(account, KEY_USER_GENDER);
            mUser.motto = accountManager.getUserData(account, KEY_USER_MOTTO);
            mUser.email = accountManager.getUserData(account, KEY_USER_EMAIL);
            mUser.telephone = accountManager.getUserData(account, KEY_USER_TELEPHONE);
            mUser.code = accountManager.getUserData(account, KEY_USER_ORGCODE);
            mUser.area = accountManager.getUserData(account, KEY_USER_REGION);
            mUser.setMarrriage(accountManager.getUserData(account, KEY_USER_MARRIAGE));
            mUser.industry = accountManager.getUserData(account, KEY_USER_INDUSTRY);
            mUser.tag = accountManager.getUserData(account, KEY_USER_LABEL);
            mUser.position = accountManager.getUserData(account, KEY_USER_JOB);
            mUser.headUrl = accountManager.getUserData(account, KEY_USER_HeadUrl);
            mUser.backgroudUrl = accountManager.getUserData(account, KEY_USER_MomentBackgroudUrl);
            return mUser;
        }

        return null;
    }

    public static void addAccount(User user) {
        if (user != null) {
            AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
            Account account = new Account(user.name, LvxinApplication.getInstance().getPackageName());

            Bundle bundle = new Bundle();
            bundle.putString(KEY_USER_ACCOUNT, user.account);
            bundle.putString(KEY_USER_NAME, user.name);
            bundle.putString(KEY_USER_GENDER, user.gender);
            bundle.putString(KEY_USER_MOTTO, user.motto);
            bundle.putString(KEY_USER_EMAIL, user.email);
            bundle.putString(KEY_USER_TELEPHONE, user.telephone);
            bundle.putString(KEY_USER_ORGCODE, user.code);
            bundle.putString(KEY_USER_REGION, user.area);
            bundle.putString(KEY_USER_MARRIAGE, user.getMarrriage());
            bundle.putString(KEY_USER_INDUSTRY, user.industry);
            bundle.putString(KEY_USER_LABEL,user.tag);
            bundle.putString(KEY_USER_JOB,user.position);
            bundle.putString(KEY_USER_HeadUrl,user.headUrl);
            bundle.putString(KEY_USER_MomentBackgroudUrl,user.backgroudUrl);
            accountManager.addAccountExplicitly(account, user.password, bundle);
        }
    }

    public static void modifyAccount(User user) {
        AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
        Account account = getLoginedAccount();
        if (account != null) {
            accountManager.setUserData(account, KEY_USER_NAME, user.name);
            accountManager.setUserData(account, KEY_USER_GENDER, user.gender);
            accountManager.setUserData(account, KEY_USER_MOTTO, user.motto);
            accountManager.setUserData(account, KEY_USER_TELEPHONE, user.telephone);
            accountManager.setUserData(account, KEY_USER_EMAIL, user.email);
            accountManager.setPassword(account, user.password);
            accountManager.setUserData(account, KEY_USER_REGION, user.area);
            accountManager.setUserData(account, KEY_USER_MARRIAGE, user.getMarrriage());
            accountManager.setUserData(account, KEY_USER_INDUSTRY, user.industry);
            accountManager.setUserData(account, KEY_USER_LABEL, user.tag);
            accountManager.setUserData(account, KEY_USER_JOB, user.position);
            accountManager.setUserData(account, KEY_USER_HeadUrl, user.headUrl);
            accountManager.setUserData(account, KEY_USER_MomentBackgroudUrl, user.backgroudUrl);
        }
    }

    public static void removeAccount(AccountManagerCallback callback) {
        AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
        Account account = getLoginedAccount();
        if (account != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                accountManager.removeAccountExplicitly(account);
                callback.run(null);
            } else {
                accountManager.removeAccount(account, callback, null);
            }
        } else {
            callback.run(null);
        }
    }

    public static void removePassword() {
        AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
        Account account = getLoginedAccount();
        if (account != null) {
            accountManager.clearPassword(account);
        }
    }


    private static Account getLoginedAccount() {
        AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());

        Account[] accounts = accountManager.getAccountsByType(BuildConfig.APPLICATION_ID);
        if (accounts != null && accounts.length > 0) {
            return accounts[0];
        }
        return null;
    }

    /**
     * 获取当前账号
     */
    public static String getCurrentAccount() {

        User user = getCurrentUser();
        if (user != null) {
            return user.account;
        }
        return "";
    }

    public static int getChatTextMaxWidth() {
        return LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE)
                .getInt(KEY_CHATING_TEXTVIEW_WIDTH, 0);
    }

    public static void setChatTextMaxWidth(int w) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putInt(KEY_CHATING_TEXTVIEW_WIDTH, w).apply();
    }

    /**
     * 获取拍照的照片文件地址
     *
     * @return
     */
    public static String getPhotoGraphFilePath() {
        return LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE)
                .getString(KEY_PHOTO_GRAPH_FILE_PATH, null);
    }

    /**
     * 设置拍照的照片文件地址
     */
    public static void setPhotoGraphFilePath(String path) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_PHOTO_GRAPH_FILE_PATH, path).apply();
    }

    /**
     * 获取裁剪图片文件地址
     *
     * @return
     */
    public static String getCropPhotoFilePath() {
        return LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE)
                .getString(KEY_CROP_PHOTO_FILE_PATH, null);
    }

    /**
     * 设置裁剪图片文件地址
     */
    public static void setCropPhotoFilePath(String path) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_CROP_PHOTO_FILE_PATH, path).apply();
    }

    /**
     * 保存聊天草稿
     */
    public static void saveChatDraft(MessageSource source, String text) {

        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(String.format(KEY_CHAT_DRAFT, source.getId(), source.getSourceType()), text).apply();

    }

    /**
     * 获取聊天草稿
     *
     * @return
     */
    public static String getLastChatDraft(MessageSource source) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getString(String.format(KEY_CHAT_DRAFT, source.getId(), source.getSourceType()), null);
    }

    /**
     * 删除聊天草稿
     */
    public static void removeChatDraft(MessageSource source) {

        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().remove(String.format(KEY_CHAT_DRAFT, source.getId(), source.getSourceType())).apply();

    }


    /**
     * 保存最上层activity名字
     */
    public static void saveTopActivityClassName(Class nameClass) {

        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_APP_TOP_ACTIVITY, nameClass.getName()).apply();

    }

    /**
     * 获取最上层activity名字
     *
     * @return
     */
    public static String getTopActivityClassName() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getString(KEY_APP_TOP_ACTIVITY, null);
    }

    /**
     * 保存应用是否切换到后台
     */
    public static void saveAppInBackground(boolean flag) {

        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_APP_INBACGROUND, flag).apply();

    }

    /**
     * 获取应用是否切换到后台
     *
     * @return
     */
    public static boolean getAppInBackground() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_APP_INBACGROUND, true);
    }

    /**
     * 设置用户是否第一次登录进来为true
     */
    public static void saveAlreadyLogin(String account) {

        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(String.format(KEY_FRIST_LOGIN, account), true).apply();

    }

    /**
     * 获取用户是否第一次登录进来
     */
    public static boolean getAlreadyLogin(String account) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(String.format(KEY_FRIST_LOGIN, account), false);
    }

    public static String getAccessToken() {
        Account account = getLoginedAccount();
        if (account != null) {
            AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
            return accountManager.getUserData(account, AccountManager.KEY_AUTHTOKEN);
        }
        return StringUtils.getUUID();
    }

    public static void saveAccessToken(String token) {
        Account account = getLoginedAccount();
        if (account != null) {
            AccountManager accountManager = AccountManager.get(LvxinApplication.getInstance());
            accountManager.setUserData(account, AccountManager.KEY_AUTHTOKEN, token);
        }
    }
    public static void removeAccessToken() {
        saveAccessToken("");
    }
    /**
     * 省电提示是否显示过
     *
     * @return
     */
    public static boolean getBetterySavingHasShow() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        return sp.getBoolean(KEY_BETTERY_SAVING_SHOW, false);
    }

    /**
     * 省电提示显示过
     *
     * @return
     */
    public static void setBetterySavingHasShow() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putBoolean(KEY_BETTERY_SAVING_SHOW, true).apply();
    }

    /**
     * 获取当前位置
     *
     * @return
     */
    public static Location getLocation() {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        String data = sp.getString(KEY_CURRENT_LOCATION, null);
        if (data != null) {
            return new Gson().fromJson(data, Location.class);
        }
        return null;
    }

    /**
     * 保存当前位置
     */
    public static void saveLocation(Location location) {
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(KEY_CURRENT_LOCATION, new Gson().toJson(location)).apply();

    }

    public static void saveFriendToUserMsgTime(String friendAccount,int time){
        String account=getCurrentUser().account;
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(friendAccount+"_"+account+"_FriendToUser",String.valueOf(time)).apply();
    }
    public static void saveUserToFriendMsgValidTime(String friendAccount,int time){
        String account=getCurrentUser().account;
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(account+"_"+friendAccount+"_UserToFriend",String.valueOf(time)).apply();
    }
    public static int getFriendToUserMsgTime(String friendAccount){
        String account=getCurrentUser().account;
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        String timeStr=sp.getString(friendAccount+"_"+account+"_FriendToUser",String.valueOf(10));
        return Integer.valueOf(timeStr);
    }
    public static int getUserToFriendMsgValidTime(String friendAccount){
        String account=getCurrentUser().account;
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        String timeStr=sp.getString(account+"_"+friendAccount+"_UserToFriend",String.valueOf(10));
        return Integer.valueOf(timeStr);
    }

    public static String getTradePassword(){
        String account=getCurrentUser().account;
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        String tradePasswordState=sp.getString(account+"_TradePasswordState","");
        return tradePasswordState;
    }
    public static void setTradePassword(String state){
        String account=getCurrentUser().account;
        SharedPreferences sp = LvxinApplication.getInstance().getSharedPreferences(MODEL_KEY, Context.MODE_PRIVATE);
        sp.edit().putString(account+"_TradePasswordState",state).apply();
    }
}
