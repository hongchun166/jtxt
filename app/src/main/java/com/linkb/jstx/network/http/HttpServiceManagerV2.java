
package com.linkb.jstx.network.http;

import android.text.TextUtils;

import com.linkb.jstx.app.Global;
import com.linkb.jstx.app.URLConstant;
import com.linkb.jstx.bean.User;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.network.result.FriendApplyBeResult;
import com.linkb.jstx.network.result.FriendListResult;
import com.linkb.jstx.network.result.FriendListResultV2;
import com.linkb.jstx.network.result.FriendQueryResult;
import com.linkb.jstx.network.result.GroupQueryResult;
import com.linkb.jstx.network.result.NewsDataResult;
import com.linkb.jstx.network.result.WithdrawBillResult;
import com.linkb.jstx.network.result.v2.AccountBalanceResult;
import com.linkb.jstx.network.result.v2.CheckInGroupResult;
import com.linkb.jstx.network.result.v2.CurrencyInfoResult;
import com.linkb.jstx.network.result.v2.FindGroupsResult;
import com.linkb.jstx.network.result.v2.FindPersonsResult;
import com.linkb.jstx.network.result.v2.GetActiveResult;
import com.linkb.jstx.network.result.v2.GetMessageDestroySwithResult;
import com.linkb.jstx.network.result.v2.ListIndustryResult;
import com.linkb.jstx.network.result.v2.ListMyBalanceFlowResult;
import com.linkb.jstx.network.result.v2.ListMyCurrencyResult;
import com.linkb.jstx.network.result.v2.ListTagsResult;
import com.linkb.jstx.network.result.v2.QueryUserInfoResult;
import com.linkb.jstx.network.result.v2.UpdateMessageDestroyTimeResult;

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

    /**
     * 获取用户信息
     * @param account
     * @param friendAccount
     * @param listener
     */
    public static void queryUserInfo(String account,String friendAccount,  HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.QUERY_USER_INFO_V2, QueryUserInfoResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("friendAccount", friendAccount);
        HttpRequestLauncher.execute(requestBody, listener);
    }


    /**
     * 获取账户总余额
     * @param account
     * @param listener
     */
    public static void getAccountBalance(String account,  HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getAccountBalance, AccountBalanceResult.class);
        requestBody.addParameter("account", account);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询我的钱包某个币种余额信息
     * @param account
     * @param currencyId  钱包ID
     * @param listener
     */
    public static void getMyCurrencyById(String account,String currencyId,  HttpRequestListener<CurrencyInfoResult> listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getMyCurrencyById, CurrencyInfoResult.class);
        requestBody.addParameter("account", account);
        requestBody.addParameter("currencyId", currencyId);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询钱包账户流水
     * @param account
     * @param type
     * 流水类型(1收入2支出3奖励)(不填则查询所有流水类型)
     * @param currencyId
     * 钱包I(不填则查询所有币种余额)（
     * @param listener
     */
    public static void listMyBalanceFlow(String account,String type,String currencyId,  HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.listMyBalanceFlow, ListMyBalanceFlowResult.class);
        requestBody.addParameter("account", account);//
        if(!TextUtils.isEmpty(type)) requestBody.addParameter("type", type);
        if(!TextUtils.isEmpty(currencyId)) requestBody.addParameter("currencyId", currencyId);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 查询我的钱包列表
     * @param account
     * @param currencyId
     * 钱包ID（不填则查询所有币种）
     * @param listener
     */
    public static void listMyCurrency(String account,String currencyId,  HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.listMyCurrency, ListMyCurrencyResult.class);
        requestBody.addParameter("account", account);//
        if(!TextUtils.isEmpty(currencyId)) requestBody.addParameter("currencyId", currencyId);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取标签列表
     * @param listener
     */
    public static void getLisTags(String account,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.lisTags, ListTagsResult.class);
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 获取行业列表
     * @param listener
     */
    public static void getListIndustry(String account,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.listIndustry, ListIndustryResult.class);
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 修改个人资料
     * @param listener
     */
    public static void updateUserInfo(User user, HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.updateUserInfo, BaseResult.class);
        if(!TextUtils.isEmpty(user.headUrl)) requestBody.addParameter("headUrl", user.headUrl);
        if(!TextUtils.isEmpty(user.name)) requestBody.addParameter("name", user.name);
        if(!TextUtils.isEmpty(user.gender)) requestBody.addParameter("gender", user.gender);
        if(!TextUtils.isEmpty(user.marrriage)) requestBody.addParameter("marrriage", user.marrriage);
        if(!TextUtils.isEmpty(user.telephone)) requestBody.addParameter("telephone", user.telephone);
        if(!TextUtils.isEmpty(user.industry)) requestBody.addParameter("industry", user.industry);
        if(!TextUtils.isEmpty(user.position)) requestBody.addParameter("position", user.position);
        if(!TextUtils.isEmpty(user.area)) requestBody.addParameter("area", user.area);
        if(!TextUtils.isEmpty(user.tag)) requestBody.addParameter("tag", user.tag);
        if(!TextUtils.isEmpty(user.motto)) requestBody.addParameter("motto", user.motto);
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 找一找（群）
     * @param listener
     */
    public static void findGroups(String content,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.findGroups, FindGroupsResult.class);
        requestBody.addParameter("content", content);//
        requestBody.addParameter("currentPage", 0);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 检查自己是否加入了该群
     * @param listener
     */
    public static void checkInGroup(String account,String groupId,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.checkInGroup, CheckInGroupResult.class);
        requestBody.addParameter("account", account);//
        requestBody.addParameter("groupId", groupId);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 检查是否为好友
     * @param listener
     */
    public static void checkFriend(String account,String friendAccount,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.checkFriend, CheckInGroupResult.class);
        requestBody.addParameter("account", account);//
        requestBody.addParameter("friendAccount", friendAccount);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 找一找（人）
     * @param content
     * @param area  区域
     * @param industry  行业
     * @param tag      标签
     * @param gender      性别(0女1男)
     * @param listener
     */
    public static void findPersons(String content,String area,String industry,String tag,String gender,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.findPersons, FindPersonsResult.class);
        requestBody.addParameter("userAccount", Global.getCurrentUser().account);//
        requestBody.addParameter("content", content);//
        requestBody.addParameter("currentPage", 0);//
        if(!TextUtils.isEmpty(area)) requestBody.addParameter("area", area);//
        if(!TextUtils.isEmpty(industry)) requestBody.addParameter("industry", industry);//
        if(!TextUtils.isEmpty(tag))requestBody.addParameter("tag", tag);//
        if(!TextUtils.isEmpty(gender))requestBody.addParameter("gender", gender);//
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取阅读即焚开关状态
     * @param listener
     */
    public static void getMessageDestroySwith(String account,String friendAccount,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getMessageDestroySwith, GetMessageDestroySwithResult.class);
        requestBody.addParameter("account", account);//
        requestBody.addParameter("friendAccount", friendAccount);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 修改阅读即焚开关状态
     * @param account
     * @param state  阅读即焚状态（0关闭，1开启）
     * @param listener
     */
    public static void updateMessageDestroySwith(String account,String friendAccount,int state,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.updateMessageDestroySwith, GetMessageDestroySwithResult.class);
        requestBody.addParameter("account", account);//
        requestBody.addParameter("friendAccount", friendAccount);//
        requestBody.addParameter("state", String.valueOf(state));//
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 设置阅读即焚时长
     * @param listener
     */
    public static void updateMessageDestroyTime(String account,String receiverMessageAccount,int time,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.updateMessageDestroyTime, UpdateMessageDestroyTimeResult.class);
        requestBody.addParameter("receiverMessageAccount", receiverMessageAccount);//
        requestBody.addParameter("time", time);//
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 获取阅读即焚时长
     * @param listener
     */
    public static void getMessageDestroyTime(String account,String receiverMessageAccount,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getMessageDestroyTime, UpdateMessageDestroyTimeResult.class);
        requestBody.addParameter("sendMessageAccount", receiverMessageAccount);//
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * H获取活跃度
     * @param listener
     */
    public static void getActive(String account,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getActive, GetActiveResult.class);
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 邀请奖励明细
     * @param listener
     */
    public static void listInvitePrizeDetail(String account,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.listInvitePrizeDetail, BaseResult.class);
        requestBody.addParameter("account", account);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     * 抽红包(资讯)
     * @param listener
     * @param id 资讯或新闻ID
     *
     */
    public static void getRedBag(String account,String id,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.POST, URLConstant.getRedBag, BaseResult.class);
        requestBody.addParameter("account", account);//
        requestBody.addParameter("id", id);//
        HttpRequestLauncher.execute(requestBody, listener);
    }

    /**
     * 资讯列表
     * @param currentPage 当前页
     * @param type       分类1 新闻 2 资讯
     * @param listener
     */
    public static void getEditorList(int currentPage,int type,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.editorList, NewsDataResult.class);
        requestBody.addPathVariable("currentPage", currentPage);
        requestBody.addPathVariable("type", type);
        HttpRequestLauncher.execute(requestBody, listener);
    }
    /**
     *   资讯闲情
     * @param id 资讯或新闻ID
     * @param listener
     */
    public static void getEditorInfo(String id,HttpRequestListener listener) {
        HttpRequestBody requestBody = new HttpRequestBody(HttpMethod.GET, URLConstant.editorInfo, BaseResult.class);
        requestBody.addPathVariable("id", id);//
        HttpRequestLauncher.execute(requestBody, listener);
    }
}
