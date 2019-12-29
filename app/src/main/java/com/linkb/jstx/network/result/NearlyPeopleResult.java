package com.linkb.jstx.network.result;

import java.util.List;

public class NearlyPeopleResult extends BaseResult{


    /**
     * code : 200
     * data : [{"gender":"1","distance":0,"motto":"战斗雄起","account":"13000000000","username":"一个瘦子"},{"gender":"1","distance":5.779160105640832,"account":"13017261766","username":"韩勇"},{"gender":"1","distance":848.4268559283566,"account":"13086505313","username":"blink976351"},{"gender":"1","distance":1111.0488264755095,"account":"13011669219","username":"监控安防"},{"gender":"1","distance":1405.2921442459497,"account":"13050902014","username":"blink416372"},{"gender":"1","distance":1461.8060313716041,"account":"13009440563","username":"吉祥如意"}]
     */

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * gender : 1
         * distance : 0
         * motto : 战斗雄起
         * account : 13000000000
         * username : 一个瘦子
         */

        private int gender;
        private double distance;
        private String motto;
        private String account;
        private String username;

        public int getGender() {
            return gender;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public double getDistance() {
            return distance;
        }

        public void setDistance(double distance) {
            this.distance = distance;
        }

        public String getMotto() {
            return motto;
        }

        public void setMotto(String motto) {
            this.motto = motto;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
    }
}
