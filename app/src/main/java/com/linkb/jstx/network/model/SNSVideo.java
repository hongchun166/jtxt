
package com.linkb.jstx.network.model;

import com.linkb.jstx.util.FileURLBuilder;

import java.io.Serializable;

public class SNSVideo implements Serializable {

    public static final byte HORIZONTAL = 0;
    public static final byte VERTICAL = 1;

    private static final long serialVersionUID = 1L;

    public int duration;

    public byte mode;

    public long size;

    public String video;

    public String image;

    public String getBucket(){
        return FileURLBuilder.BUCKET_CHAT;
    }

}
