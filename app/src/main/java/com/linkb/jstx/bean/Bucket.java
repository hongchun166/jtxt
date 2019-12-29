
package com.linkb.jstx.bean;

import android.net.Uri;

import java.io.Serializable;

public class Bucket implements Serializable {

    /**
     *
     */
    private transient static final long serialVersionUID = 1L;
    public String id;
    public String name;
    public long size;
    public Uri cover;

}
