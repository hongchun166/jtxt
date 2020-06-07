
package com.linkb.jstx.network.model;

import android.text.TextUtils;

import com.linkb.jstx.util.FileURLBuilder;

import java.io.Serializable;

public class SNSVideo implements Serializable {

    public static final byte HORIZONTAL = 0;
    public static final byte VERTICAL = 1;

    private static final long serialVersionUID = 1L;

    public String duration;

    public byte mode;

    public long size;

    public String video;

    public String image;

    public String getBucket(){
        return FileURLBuilder.BUCKET_CHAT;
    }
    public int getDurationInt(){
        if(TextUtils.isEmpty(duration)){
            return 0;
        }else {
            try {
                double durationDouble=Double.parseDouble(duration);
                return Double.valueOf(durationDouble).intValue();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return 0;
            }
        }
    }
}
