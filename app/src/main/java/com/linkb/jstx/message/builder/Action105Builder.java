
package com.linkb.jstx.message.builder;


import com.linkb.jstx.bean.User;
import com.linkb.jstx.model.Group;
import com.google.gson.Gson;

public class Action105Builder extends Action102Builder {


    public String founder;
    public String category;
    public String summary;


    public String buildJsonString(User self, Group group) {
        account = self.account;
        name = self.name;
        id = group.id;
        groupName = group.name;
        founder = group.founder;
        category = group.category;
        summary = group.summary;
        return new Gson().toJson(this);
    }
}
