
package com.linkb.jstx.network.http;

import android.text.TextUtils;

import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.FriendApplyBeResult;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.network.result.FriendListResultV2;

public class HttpServiceManagerV2 {
    /**
     * 注册账号
     *
     * @param locale 0是中国，1是外国
     */
    public static void registerAccount(String account, String sex, String name
            , String password, String vertcode, String locale,String inviteCode
            , HttpRequestListener listener) {

        HttpRequestBody requestBody = new HttpRequestBody(URLConstant.REGISTER_ACCOUNT, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("name", name);
        requestBody.addParameter("gender", sex);
        requestBody.addParameter("password", password);
        requestBody.addParameter("vertcode", vertcode);
        requestBody.addParameter("locale", locale);
        if(!TextUtils.isEmpty(inviteCode)){
            requestBody.addParameter("inviteCode", locale);
        }
        String tradePassword=null;
        if(!TextUtils.isEmpty(tradePassword)) {
            requestBody.addParameter("tradePassword", "");
        }

        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 获取好友申请列表
     * /api/userFriend/listFriendApply
     */
    public static void getListFriendApplyV2(String account,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.LIST_FRIEND_APPLY_V2, FriendApplyBeResult.class);
        requestBody.setContentType(HttpRequestBody.JSON_MEDIATYPE);
        requestBody.addParameter("account",account);
//        HashMap<String,String> bean=new HashMap<>();
//        bean.put("account",account);
//        String beanStr=new Gson().toJson(bean);
//        requestBody.setContent(beanStr);

        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 申请好友关系
     */
    public static void applyFriendV2(String account, String receiverUser, String applyContent, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.ADD_FRIEND_APPLY_V2, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("receiverUser", receiverUser);
        requestBody.addParameter("applyContent", applyContent);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     *
     * @param account
     * @param id  好友ID  long
     * @param state   处理状态（1同意，2拒绝） integer
     * @param listener
     */
    public static void agreeFriendV2(String account, String id, String state, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.APPLY_FRIENDV_ANSWER_V2, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("id", id);
        requestBody.addParameter("state", state);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 删除好友
     * @param account
     * @param friendAccount
     * @param listener
     */
    public static void deleteFriendV2(String account, String friendAccount, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.APPLY_FRIENDV_DELETE_V2, BaseResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("friendAccount", friendAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 我的好友列表
     * @param account
     * @param listener
     */
    public static void listMyFriendV2(String account,  HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.LIST_MY_FRIENDS_V2, FriendListResultV2.class);
        requestBody.addParameter("account", account);
        HttpRequestLauncher.execute(requestBody, listener);
    }
}
