package com.linkb.jstx.network.result;

import com.linkb.jstx.model.Group;

import java.util.List;

public class QueryMineGroupResult extends BaseResult {


    /**
     * code : 200
     * dataList : [{"id":22,"name":"战斗天使","summary":"战斗天使\n战\n战斗天使","founder":"15874986188","amount":4,"banned":0,"memberList":[{"id":42,"groupId":22,"account":"15874986188","host":"1"},{"id":768,"groupId":22,"account":"13000000000","host":"0"},{"id":44,"groupId":22,"account":"17602060697","host":"0"},{"id":45,"groupId":22,"account":"15802674030","host":"0"}]},{"id":23,"name":"测试群","summary":"","founder":"18627559628","amount":6,"banned":0,"memberList":[{"id":46,"groupId":23,"account":"18627559628","host":"1"},{"id":779,"groupId":23,"account":"19999999999","host":"0"},{"id":47,"groupId":23,"account":"17602060697","host":"0"},{"id":48,"groupId":23,"account":"15874986188","host":"0"},{"id":50,"groupId":23,"account":"15802674030","host":"0"},{"id":51,"groupId":23,"account":"13000000000","host":"0"}]},{"id":135,"name":"测试邀请一","summary":"","founder":"17602060697","amount":1,"banned":0,"memberList":[{"id":671,"groupId":135,"account":"17602060697","host":"1"}]},{"id":139,"name":"一队","summary":"公告","founder":"13888888888","amount":15,"banned":0,"memberList":[{"id":676,"groupId":139,"account":"13888888888","host":"1"},{"id":802,"groupId":139,"account":"13000000000","host":"0"},{"id":677,"groupId":139,"account":"17602060697","host":"0"}]},{"id":158,"name":"敢死队","founder":"17602060697","amount":84,"banned":0,"memberList":[{"id":790,"groupId":158,"account":"17602060697","host":"1"},{"id":792,"groupId":158,"account":"18888888888","host":"0"},{"id":799,"groupId":158,"account":"13000000000","host":"0"},{"id":800,"groupId":158,"account":"13888888888","host":"0"}]},{"id":21,"name":"2501","summary":"大喊大叫都觉得就到家第几集","founder":"13888888888","amount":9,"banned":0,"memberList":[{"id":40,"groupId":21,"account":"13888888888","host":"1"},{"id":793,"groupId":21,"account":"17602060697","host":"0"},{"id":795,"groupId":21,"account":"18888888888","host":"0"}]}]
     */

    private List<Group> dataList;

    public List<Group> getDataList() {
        return dataList;
    }

    public void setDataList(List<Group> dataList) {
        this.dataList = dataList;
    }

}
