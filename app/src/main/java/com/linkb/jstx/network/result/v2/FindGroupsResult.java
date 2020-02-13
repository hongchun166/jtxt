package com.linkb.jstx.network.result.v2;

import com.google.gson.annotations.SerializedName;
import com.linkb.jstx.model.Group;
import com.linkb.jstx.network.result.BaseResult;
import com.linkb.jstx.util.TimeUtils;

import java.io.Serializable;
import java.util.List;

public class FindGroupsResult extends BaseResult {


    /**
     * code : 200
     * data : {"content":[{"id":463,"name":"比特币团队","summary":"比特币是世界未来货币！","founder":"17788989622","amount":1,"banned":0,"memberAble":0,"level":0}],"pageable":{"sort":{"sorted":false,"unsorted":true,"empty":true},"pageSize":10,"pageNumber":0,"offset":0,"paged":true,"unpaged":false},"totalElements":1,"last":true,"totalPages":1,"first":true,"sort":{"sorted":false,"unsorted":true,"empty":true},"numberOfElements":1,"size":10,"number":0,"empty":false}
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
         * content : [{"id":463,"name":"比特币团队","summary":"比特币是世界未来货币！","founder":"17788989622","amount":1,"banned":0,"memberAble":0,"level":0}]
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

        public static class ContentBean implements Serializable {
            /**
             * id : 463
             * name : 比特币团队
             * summary : 比特币是世界未来货币！
             * founder : 17788989622
             * amount : 1
             * banned : 0
             * memberAble : 0
             * level : 0
             * createTime  2020-02-12T13:01:03.000+0000
             */

            private String id;
            private String name;
            private String summary;
            private String founder;
            private int amount;
            private int banned;
            private int memberAble;
            private int level;
            private String createTime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getSummary() {
                return summary;
            }

            public void setSummary(String summary) {
                this.summary = summary;
            }

            public String getFounder() {
                return founder;
            }

            public void setFounder(String founder) {
                this.founder = founder;
            }

            public int getAmount() {
                return amount;
            }

            public void setAmount(int amount) {
                this.amount = amount;
            }

            public int getBanned() {
                return banned;
            }

            public void setBanned(int banned) {
                this.banned = banned;
            }

            public int getMemberAble() {
                return memberAble;
            }

            public void setMemberAble(int memberAble) {
                this.memberAble = memberAble;
            }

            public int getLevel() {
                return level;
            }

            public void setLevel(int level) {
                this.level = level;
            }

            public String getCreateTime() {
                return createTime;
            }
            public String getCreateTimeFinal() {
                return TimeUtils.timeToStr(createTime);
            }
            public void setCreateTime(String createTime) {
                this.createTime = createTime;
            }

            public Group toGroupB(){
                Group group=new Group();
                group.id=Long.valueOf(id);
                group.memberAble=memberAble;
                group.name=name;
                group.summary=summary;
                group.category=summary;
                group.founder=founder;
                group.banned=banned;
                group.level=level;
                group.memberSize=amount;
                group.createTime=getCreateTimeFinal();
                return group;
            }
        }
    }
}
