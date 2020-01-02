package com.linkb.jstx.network.result;

import java.util.List;

public class FriendApplyBeResult extends BaseResult {
    public boolean isNotEmpty(){
        return getData() != null && getData().size() > 0;
    }
    private List<FriendBeAppLy> data;

    public List<FriendBeAppLy> getData() {
        return data;
    }

    public void setData(List<FriendBeAppLy> dataList) {
        this.data = dataList;
    }

    public static class FriendBeAppLy{
        private String name;
        private String gender;
        private String id;
        private String applyTime;
        /**
         * 0未处理
         * 1、已同意
         * 2、已拒绝
         * 3、已过期
         */
        private String state;
        private String expireTime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getApplyTime() {
            return applyTime;
        }

        public void setApplyTime(String applyTime) {
            this.applyTime = applyTime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getExpireTime() {
            return expireTime;
        }

        public void setExpireTime(String expireTime) {
            this.expireTime = expireTime;
        }
    }
}
