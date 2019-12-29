package com.linkb.jstx.network.result;

public class CoinSearchResult extends BaseResult {

    /**
     * code : 200
     * data : {"address":"0xE0B7927c4aF23765Cb51314A0E0521A9645F0E2A","ethToken":"DGD (DGD)"}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * address : 0xE0B7927c4aF23765Cb51314A0E0521A9645F0E2A
         * ethToken : DGD (DGD)
         */

        private String address;
        private String ethToken;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEthToken() {
            return ethToken;
        }

        public void setEthToken(String ethToken) {
            this.ethToken = ethToken;
        }
    }
}
