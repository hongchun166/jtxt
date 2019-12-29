package com.linkb.jstx.network.result;

import java.util.List;

public class GameResult extends BaseResult{


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
        private int id;
        private String downLoadUrl;
        private String name;
        private String desription;
        private String type;

        public String getDesription() {
            return desription;
        }

        public void setDesription(String desription) {
            this.desription = desription;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDownLoadUrl() {
            return downLoadUrl;
        }

        public void setDownLoadUrl(String downLoadUrl) {
            this.downLoadUrl = downLoadUrl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
