package com.linkb.jstx.network.result.v2;

import com.google.gson.annotations.SerializedName;
import com.linkb.jstx.network.result.BaseResult;

public class GetEditorInfoResult extends BaseResult {


    /**
     * code : 200
     * data : {"id":1561684038288,"title":"中国区块链测评联盟：《政务区块链行业应用标准》标准工作组第二次会","timestamp":1561684038288,"author":"转载","textContent":"","type":1,"min":1,"status":"1","max":200,"url":"https://www.jinse.com/blockchain/399254.html","lottery_amount":19.35,"content":"6月27日，由中国区块链测评联盟指导的政务区块链标准工作组第二次工作会议在北京如期召开，就已经形成的标准草案分章节","litimg":"https://blink-oss-pro.oss-cn-hongkong.aliyuncs.com/litimg/litimg_1561684038288"}
     * flag : true
     */

    private DataBean data;
    private boolean flag;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public static class DataBean {
        /**
         * id : 1561684038288
         * title : 中国区块链测评联盟：《政务区块链行业应用标准》标准工作组第二次会
         * timestamp : 1561684038288
         * author : 转载
         * textContent :
         * type : 1    （类型 1新闻，2资讯）
         * min : 1      （红包最小金额）
         * status : 1       （0、未发布，1、已发布）
         * max : 200       （红包最大金额）
         * url : https://www.jinse.com/blockchain/399254.html
         * lottery_amount : 19.35   （已领取金额）
         * content : 6月27日，由中国区块链测评联盟指导的政务区块链标准工作组第二次工作会议在北京如期召开，就已经形成的标准草案分章节
         * litimg : https://blink-oss-pro.oss-cn-hongkong.aliyuncs.com/litimg/litimg_1561684038288
         */

        private long id;
        private String title;
        private long timestamp;
        private String author;
        private String textContent;
        private int type;
        private int min;
        private String status;
        private int max;
        private String url;
        private Double lottery_amount;
        private String content;
        private String litimg;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getTextContent() {
            return textContent;
        }

        public void setTextContent(String textContent) {
            this.textContent = textContent;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }


        public Double getLottery_amount() {
            return lottery_amount;
        }

        public void setLottery_amount(Double lottery_amount) {
            this.lottery_amount = lottery_amount;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getLitimg() {
            return litimg;
        }

        public void setLitimg(String litimg) {
            this.litimg = litimg;
        }
    }
}
