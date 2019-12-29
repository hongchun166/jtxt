
package com.linkb.jstx.network.http;


import java.io.Serializable;
import java.util.Objects;

public class OriginalCall implements Serializable {
    private String url;
    private String method;

    public OriginalCall(String url,String method){
        this.url = url;
        this.method = method;
    }
    public boolean equals(String url){
        return Objects.equals(this.url,url);
    }

    public boolean equalsDelete(String url){
        return equals(url,HttpMethod.DELETE);
    }
    public boolean equalsPost(String url){
        return equals(url,HttpMethod.POST);
    }
    public boolean equalsPatch(String url){
        return equals(url,HttpMethod.PATCH);
    }
    public boolean equalsGet(String url){
        return equals(url,HttpMethod.GET);
    }

    public String getUrl() {
        return url;
    }


    private boolean equals(String url, String method){
        return Objects.equals(this.url,url) && Objects.equals(this.method,method);
    }
}
