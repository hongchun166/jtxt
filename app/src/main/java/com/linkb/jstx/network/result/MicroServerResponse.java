
package com.linkb.jstx.network.result;


import com.linkb.jstx.network.model.MomentLink;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MicroServerResponse extends BaseResult {

    public String contentType;
    public String content;
    public String title;
    public String image;
    public String link;
    public ArrayList<MomentLink> items;

    @Override
    public boolean isSuccess() {
        return contentType != null;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

}
