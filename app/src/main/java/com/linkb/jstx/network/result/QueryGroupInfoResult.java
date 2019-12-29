package com.linkb.jstx.network.result;

import com.linkb.jstx.model.Group;

public class QueryGroupInfoResult extends BaseResult {


    /**
     * code : 200
     * data : {"id":164,"name":"新建一个群","founder":"17602060697","amount":3,"banned":0,"memberList":[{"id":838,"groupId":164,"account":"17602060697","host":"1"},{"id":874,"groupId":164,"account":"13000000000","name":"一斤昵称","host":"0"}]}
     */

    private Group data;

    public Group getData() {
        return data;
    }

    public void setData(Group data) {
        this.data = data;
    }

}
