
package com.linkb.jstx.network.model;

import com.linkb.jstx.util.FileURLBuilder;

import java.io.Serializable;

public class SNSMomentImage extends SNSImage implements Serializable {

    public String getBucket(){
        return FileURLBuilder.BUCKET_MOMENT;
    }
}
