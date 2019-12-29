package com.linkb.jstx.network.result;

import com.linkb.jstx.bean.User;

public class BasePersonInfoResult extends BaseResult {


    /**
     * code : 200
     * data : {"account":"13888888888","name":"嘻嘻嘻嘻嘻嘻嘻嘻","telephone":"15211065865","email":"52928560@qq.com","code":"blink882411","gender":"1","grade":0,"motto":"都好都好好多个低调一点","isLoginFlag":"1","state":"0","regTime":1551866841,"tradePassword":"4579a0a67759cd28a5a8176691604757","disabled":false}
     */

    private User data;

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }



}
