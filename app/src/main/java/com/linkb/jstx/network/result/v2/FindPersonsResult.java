package com.linkb.jstx.network.result.v2;

import com.linkb.jstx.network.result.BaseResult;

import java.util.List;

public class FindPersonsResult extends BaseResult {


    /**
     * code : 200
     * message : 成功
     * data : {"content":[{"account":"18274839631","name":"黑狐","telephone":"18274839631","email":"ghjjwdv","code":"Kov10021","gender":"1","motto":"好哦了喔，哦哦","isLoginFlag":"0","state":"0","regTime":1577855996,"tradePassword":"4579a0a67759cd28a5a8176691604757","inviteCode":"MHFZNEK","referrerNumber":0,"jianjieReferrerNumber":0,"lockBalanceFreed":0,"lot":0,"lat":0,"disabled":false}],"pageable":{"sort":{"sorted":false,"unsorted":true,"empty":true},"pageSize":10,"pageNumber":0,"offset":0,"paged":true,"unpaged":false},"totalElements":1,"last":true,"totalPages":1,"first":true,"sort":{"sorted":false,"unsorted":true,"empty":true},"numberOfElements":1,"size":10,"number":0,"empty":false}
     */


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public boolean isDataListEmpty() {
        if (data == null) {
            return true;
        } else if (data.getContent() == null || data.getContent().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    public static class DataBean {
        /**
         * content : [{"account":"18274839631","name":"黑狐","telephone":"18274839631","email":"ghjjwdv","code":"Kov10021","gender":"1","motto":"好哦了喔，哦哦","isLoginFlag":"0","state":"0","regTime":1577855996,"tradePassword":"4579a0a67759cd28a5a8176691604757","inviteCode":"MHFZNEK","referrerNumber":0,"jianjieReferrerNumber":0,"lockBalanceFreed":0,"lot":0,"lat":0,"disabled":false}]
         * pageable : {"sort":{"sorted":false,"unsorted":true,"empty":true},"pageSize":10,"pageNumber":0,"offset":0,"paged":true,"unpaged":false}
         * totalElements : 1
         * last : true
         * totalPages : 1
         * first : true
         * sort : {"sorted":false,"unsorted":true,"empty":true}
         * numberOfElements : 1
         * size : 10
         * number : 0
         * empty : false
         */

        private PageableBean pageable;
        private int totalElements;
        private boolean last;
        private int totalPages;
        private boolean first;
        private SortBeanX sort;
        private int numberOfElements;
        private int size;
        private int number;
        private boolean empty;
        private List<ContentBean> content;

        public PageableBean getPageable() {
            return pageable;
        }

        public void setPageable(PageableBean pageable) {
            this.pageable = pageable;
        }

        public int getTotalElements() {
            return totalElements;
        }

        public void setTotalElements(int totalElements) {
            this.totalElements = totalElements;
        }

        public boolean isLast() {
            return last;
        }

        public void setLast(boolean last) {
            this.last = last;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public boolean isFirst() {
            return first;
        }

        public void setFirst(boolean first) {
            this.first = first;
        }

        public SortBeanX getSort() {
            return sort;
        }

        public void setSort(SortBeanX sort) {
            this.sort = sort;
        }

        public int getNumberOfElements() {
            return numberOfElements;
        }

        public void setNumberOfElements(int numberOfElements) {
            this.numberOfElements = numberOfElements;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public boolean isEmpty() {
            return empty;
        }

        public void setEmpty(boolean empty) {
            this.empty = empty;
        }

        public List<ContentBean> getContent() {
            return content;
        }

        public void setContent(List<ContentBean> content) {
            this.content = content;
        }

        public static class PageableBean {
            /**
             * sort : {"sorted":false,"unsorted":true,"empty":true}
             * pageSize : 10
             * pageNumber : 0
             * offset : 0
             * paged : true
             * unpaged : false
             */

            private SortBean sort;
            private int pageSize;
            private int pageNumber;
            private int offset;
            private boolean paged;
            private boolean unpaged;

            public SortBean getSort() {
                return sort;
            }

            public void setSort(SortBean sort) {
                this.sort = sort;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public int getPageNumber() {
                return pageNumber;
            }

            public void setPageNumber(int pageNumber) {
                this.pageNumber = pageNumber;
            }

            public int getOffset() {
                return offset;
            }

            public void setOffset(int offset) {
                this.offset = offset;
            }

            public boolean isPaged() {
                return paged;
            }

            public void setPaged(boolean paged) {
                this.paged = paged;
            }

            public boolean isUnpaged() {
                return unpaged;
            }

            public void setUnpaged(boolean unpaged) {
                this.unpaged = unpaged;
            }

            public static class SortBean {
                /**
                 * sorted : false
                 * unsorted : true
                 * empty : true
                 */

                private boolean sorted;
                private boolean unsorted;
                private boolean empty;

                public boolean isSorted() {
                    return sorted;
                }

                public void setSorted(boolean sorted) {
                    this.sorted = sorted;
                }

                public boolean isUnsorted() {
                    return unsorted;
                }

                public void setUnsorted(boolean unsorted) {
                    this.unsorted = unsorted;
                }

                public boolean isEmpty() {
                    return empty;
                }

                public void setEmpty(boolean empty) {
                    this.empty = empty;
                }
            }
        }

        public static class SortBeanX {
            /**
             * sorted : false
             * unsorted : true
             * empty : true
             */

            private boolean sorted;
            private boolean unsorted;
            private boolean empty;

            public boolean isSorted() {
                return sorted;
            }

            public void setSorted(boolean sorted) {
                this.sorted = sorted;
            }

            public boolean isUnsorted() {
                return unsorted;
            }

            public void setUnsorted(boolean unsorted) {
                this.unsorted = unsorted;
            }

            public boolean isEmpty() {
                return empty;
            }

            public void setEmpty(boolean empty) {
                this.empty = empty;
            }
        }

        public static class ContentBean {
            /**
             * account : 18274839631
             * name : 黑狐
             * telephone : 18274839631
             * email : ghjjwdv
             * code : Kov10021
             * gender : 1
             * motto : 好哦了喔，哦哦
             * isLoginFlag : 0
             * state : 0
             * regTime : 1577855996
             * tradePassword : 4579a0a67759cd28a5a8176691604757
             * inviteCode : MHFZNEK
             * referrerNumber : 0
             * jianjieReferrerNumber : 0
             * lockBalanceFreed : 0.0
             * lot : 0.0
             * lat : 0.0
             * "position": "总经理",
             * "marrriage": "单身",
             * "industry": "金融/银行/投资/保险",
             * "tag": "比特币",
             * "area": "加拿大-伦敦",
             * disabled : false
             *
             *
             * {"area":"湖北-武汉","isFriends":"0","account":"13823526884","name":"冰室123"}
             */


            private String area;
            private String isFriends;
            private String account;
            private String name;

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getIsFriends() {
                return isFriends;
            }

            public void setIsFriends(String isFriends) {
                this.isFriends = isFriends;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }
}
