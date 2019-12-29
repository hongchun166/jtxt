
package com.linkb.jstx.network.model;

import java.io.Serializable;

public class MomentLink implements Serializable {
    public transient static final String NAME = MomentLink.class.getSimpleName();
    private static final long serialVersionUID = 1L;
    public String title;//文字标题
    public String content;//文字内容
    public String link;//链接地址
    public String image;//图片

    @Override
    public String toString() {
        return title == null ? "" : title + "\n" + link;
    }
}
