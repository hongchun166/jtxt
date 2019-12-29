package com.linkb.jstx.network.result;

public class QueryWithdrawAmountResult extends BaseResult {


    /**
     * code : 200
     * data : {"rateAmount":0,"serviceRate":0.001,"actulAmount":0,"useAmount":0}
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
         * rateAmount : 0
         * serviceRate : 0.001
         * actulAmount : 0
         * useAmount : 0
         * extractMin : 0.0
         * extractMax : 0.0
         */

        private double rateAmount;
        private double serviceRate;
        private double actulAmount;
        private double useAmount;
        private double extractMin;
        private double extractMax;

        public double getRateAmount() {
            return rateAmount;
        }

        public void setRateAmount(double rateAmount) {
            this.rateAmount = rateAmount;
        }

        public double getServiceRate() {
            return serviceRate;
        }

        public void setServiceRate(double serviceRate) {
            this.serviceRate = serviceRate;
        }

        public double getActulAmount() {
            return actulAmount;
        }

        public void setActulAmount(double actulAmount) {
            this.actulAmount = actulAmount;
        }

        public double getUseAmount() {
            return useAmount;
        }

        public void setUseAmount(double useAmount) {
            this.useAmount = useAmount;
        }

        public double getExtractMin() {
            return extractMin;
        }

        public void setExtractMin(double extractMin) {
            this.extractMin = extractMin;
        }

        public double getExtractMax() {
            return extractMax;
        }

        public void setExtractMax(double extractMax) {
            this.extractMax = extractMax;
        }
    }
}
