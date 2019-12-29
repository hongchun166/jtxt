
package com.linkb.jstx.message.builder;

import com.linkb.jstx.bean.User;
import com.linkb.jstx.model.Group;
import com.google.gson.Gson;

public class Action102Builder extends BaseBuilder {


    public String name;
    public String message;
    public long id;
    public String groupName;

    public String buildJsonString(User self, Group group, String tokenText) {
        account = self.account;
        name = self.name;
        message = tokenText;
        id = group.id;
        groupName = group.name;
        return new Gson().toJson(this);
    }
}
