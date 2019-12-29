
package com.linkb.jstx.network.model;

import com.linkb.jstx.util.FileURLBuilder;


public class SNSMomentVideo extends  SNSVideo {
    @Override
    public String getBucket(){
        return FileURLBuilder.BUCKET_MOMENT;
    }
}
