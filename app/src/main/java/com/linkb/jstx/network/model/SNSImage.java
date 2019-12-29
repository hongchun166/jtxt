
package com.linkb.jstx.network.model;


import java.io.Serializable;

public abstract class SNSImage implements Serializable {

    private static final long serialVersionUID = 1L;

    public int ow;

    public int oh;

    public int tw;

    public int th;

    public String image;//原图key

    public String thumb;//缩略图key

    @Override
    public boolean equals(Object o) {
        if (o instanceof SNSImage) {
            SNSImage bean = (SNSImage) o;
            return bean.image.equals(this.image);
        }
        return false;
    }

    public abstract String getBucket();

    @Override
    public int hashCode() {
        return this.getClass().getName().hashCode();
    }
}
