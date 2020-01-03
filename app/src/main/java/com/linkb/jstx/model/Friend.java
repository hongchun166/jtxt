
package com.linkb.jstx.model;

import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.linkb.jstx.app.Constant;
import com.linkb.R;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.database.FriendRepository;
import com.linkb.jstx.network.http.HttpRequestListener;
import com.linkb.jstx.network.http.HttpServiceManager;
import com.linkb.jstx.network.http.OriginalCall;
import com.linkb.jstx.network.result.BasePersonInfoResult;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.network.result.FriendListResultV2;
import com.linkb.jstx.network.result.FriendQueryResult;
import com.linkb.jstx.network.result.FriendResult;
import com.linkb.jstx.util.FileURLBuilder;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = Friend.TABLE_NAME)
public class Friend extends MessageSource implements Serializable {
    public static final String TABLE_NAME = "t_lvxin_user";

    private static final long serialVersionUID = 1L;
    private final static String[] MESSAGE_ACTION = new String[]{Constant.MessageAction.ACTION_0};
    @DatabaseField(id = true)
    public String account;
    @DatabaseField
    public String name;
    @DatabaseField
    public String motto;
    @DatabaseField
    public String gender;
    @DatabaseField
    public String telephone;
    @DatabaseField
    public String email;
    @DatabaseField
    public String code;
    public String fristPinyin;

    public String friendAccount;

    public String getFriendAccount() {
        return friendAccount;
    }

    public void setFriendAccount(String friendAccount) {
        this.friendAccount = friendAccount;
    }
    public Friend() {
    }

    public Friend(String accont) {
        this.account = accont;
    }

    @Override
    public String getWebIcon() {


        return FileURLBuilder.getUserIconUrl(account);
    }

    public  String getWebFriendIcon(){
        return FileURLBuilder.getUserIconUrl(friendAccount);
    }

    @Override
    public String aysnTitle(final TextView view) {
        HttpServiceManager.queryPersonInfo(account, new HttpRequestListener<BasePersonInfoResult>() {
            @Override
            public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
                if (result.isSuccess()){
                    Friend friend = User.UserToFriend(result.getData());
                    FriendRepository.save(friend);
                    view.setText(friend.name);
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
        return null;
    }

    public static void asynTextViewName(final TextView textView, String account){
        HttpServiceManager.queryPersonInfo(account, new HttpRequestListener<BasePersonInfoResult>() {
            @Override
            public void onHttpRequestSucceed(BasePersonInfoResult result, OriginalCall call) {
                if (result.isSuccess()){
                    Friend friend = User.UserToFriend(result.getData());
                    FriendRepository.save(friend);
                    textView.setText(friend.name);
                }
            }

            @Override
            public void onHttpRequestFailure(Exception e, OriginalCall call) {

            }
        });
    }

    @Override
    public String getTitle() {

        return name;
    }



    @Override
    public int getDefaultIconRID() {
        return R.mipmap.lianxiren;
    }

    @Override
    public String getId() {
        return account;
    }

    @Override
    public String getSourceType() {
        return SOURCE_USER;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public  int getNotificationPriority(){
        return NotificationCompat.PRIORITY_HIGH;
    }

    @Override
    public String[] getMessageAction() {
        return MESSAGE_ACTION;
    }

    @Override
    public int getThemeColor() {
        return R.color.theme_green;
    }

    @Override
    public Friend clone() {
        Friend friend = new Friend();
        friend.account = account;
        friend.email = email;
        friend.name = name;
        friend.gender = gender;
        friend.code = code;
        friend.telephone = telephone;
        friend.motto = motto;
        friend.fristPinyin = fristPinyin;
        return friend;
    }

    @Override
    public int hashCode() {
        return getClass().getName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Friend) {
            if (TextUtils.isEmpty(((Friend) o).name) || TextUtils.isEmpty(((Friend) o).account)){
                return false;
            }
            return ((Friend) o).name.equals(name) && ((Friend) o).account.equals(account);
        }
        return false;
    }

    public static Friend searchResultToFriend(FriendQueryResult.DataListBean dataBean){
        Friend friend = new Friend();
        friend.name = dataBean.getName();
        friend.account = dataBean.getAccount();
        friend.gender = dataBean.getGender();
        friend.code = dataBean.getCode();
        return friend;
    }
    public static Friend friendShipToFriend(FriendListResultV2.FriendShip friendShip){
        Friend friend = new Friend();
        friend.name = friendShip.getName();
        friend.account = friendShip.getFriendAccount();
        friend.code = friendShip.getCode();
        friend.friendAccount=friendShip.getAccount();
        return friend;
    }
}
