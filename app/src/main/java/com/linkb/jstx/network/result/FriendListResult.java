
package com.linkb.jstx.network.result;

import com.linkb.jstx.model.Friend;

import java.util.List;

public class FriendListResult extends BaseResult {


    public boolean isNotEmpty(){
        return getDataList() != null && getDataList().size() > 0;
    }
    /**
     * code : 200
     * dataList : [{"code":"blink382015","name":"哈哈","id":57,"state":"0","addDate":1551866956,"account":"17602060697","friendAccount":"13888888888"},{"code":"blink382015","name":"Blink客服","id":2073,"state":"0","addDate":1552280948,"account":"17602060697","friendAccount":"19999999999"},{"code":"blink382015","name":"泉","id":5337,"state":"0","addDate":1552473555,"account":"17602060697","friendAccount":"15802674030"},{"code":"blink382015","name":"三斤昵称","state":"0","id":5350,"addDate":1552530154,"account":"17602060697","friendAccount":"13000000000"}]
     */

    private List<FriendShip> dataList;

    public List<FriendShip> getDataList() {
        return dataList;
    }

    public void setDataList(List<FriendShip> dataList) {
        this.dataList = dataList;
    }

    /** 我的朋友关系
    * */
    public static class FriendShip {
        /**
         * code : blink382015
         * name : 哈哈
         * id : 57
         * state : 0
         * addDate : 1551866956
         * account : 17602060697
         * friendAccount : 13888888888
         */


        private String code;
        private String name;
        private String id;
        private String state;
        private long addDate;
        private String account;
        private String friendAccount;
        private String fristPinyin;


        /** 好友的首字母拼音
        * */
        public String getFristPinyin() {
            return fristPinyin;
        }

        public void setFristPinyin(String fristPinyin) {
            this.fristPinyin = fristPinyin;
        }

        /** 登录用户的UserCode
        * */
        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        /** 好友的name
         * */
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        /** 好友的Id
         * */
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public long getAddDate() {
            return addDate;
        }

        public void setAddDate(long addDate) {
            this.addDate = addDate;
        }

        /** 当前登录用户的账户
         * */
        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        /** 朋友的账户
         * */
        public String getFriendAccount() {
            return friendAccount;
        }

        public void setFriendAccount(String friendAccount) {
            this.friendAccount = friendAccount;
        }

        @Override
        public FriendShip clone() {
            FriendShip friendShip = new FriendShip();
            friendShip.setAccount(getAccount());
            friendShip.setFriendAccount(getFriendAccount());
            friendShip.setName(getName());
            friendShip.setFristPinyin(getFristPinyin());
            return friendShip;
        }

    }


    /** 从当前好友关系中提取好友信息
    * */
    public static Friend FriendShipToFriend(FriendShip friendShip){
        Friend friend = new Friend();
        friend.name = friendShip.getName();
        friend.account = friendShip.getFriendAccount();
        return friend;
    }
}
