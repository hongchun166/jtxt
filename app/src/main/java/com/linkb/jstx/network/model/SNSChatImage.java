
package com.linkb.jstx.network.model;

import com.linkb.jstx.util.FileURLBuilder;

import java.io.Serializable;

public class SNSChatImage extends SNSImage implements Serializable {

    public String getBucket(){
        return FileURLBuilder.BUCKET_CHAT;
    }

    @Override
    public SNSChatImage clone() {
        SNSChatImage image = new SNSChatImage();
        image.image = this.image;
        image.oh = this.oh;
        image.ow = this.ow;
        image.th = this.th;
        image.thumb = this.thumb;
        image.tw = this.tw;

        return image;
    }
}
