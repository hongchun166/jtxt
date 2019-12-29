
package com.linkb.jstx.network.model;

import java.io.Serializable;
import java.util.List;

public class MicroServerLinkMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    public String title;//文字标题
    public String link;//链接地址
    public String image;//图片，只有第一行才显示横向横幅
    public List<MicroServerLinkMessage> items; //更多链接

    public boolean hasMore() {
        return items != null && !items.isEmpty();
    }

    public MicroServerLinkMessage getSubLink(int i) {
        return items.get(i);
    }
}
