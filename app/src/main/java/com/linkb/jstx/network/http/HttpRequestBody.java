
package com.linkb.jstx.network.http;


import android.support.v4.util.ArrayMap;

import com.linkb.jstx.app.Global;
import com.linkb.jstx.network.result.BaseResult;


public class HttpRequestBody {

    public static final String JSON_MEDIATYPE = "application/json;charset=UTF-8";
    public static final String ACCESS_TOKEN = "access-token";
    //接口地址
    private String url;

    //是否回调到主线程
    private boolean mainTheadCallback = true;
    //post的 body内容
    private String content;
    //post的 body内容
    private String contentType;
    private String method = HttpMethod.POST;
    //返回的结构
    private Class<? extends BaseResult> dataClass;

    //参数
    private ArrayMap<String, String> requestParamMap = new ArrayMap<>();

    //参数
    private ArrayMap<String, String> pathVariableMap = new ArrayMap<>();

    //header
    private ArrayMap<String, String> header = new ArrayMap<>();

    public HttpRequestBody(String method, String url, Class<? extends BaseResult> dataClass) {
        this.url = url;
        this.method = method;
        this.dataClass = dataClass;
        header.put(ACCESS_TOKEN, Global.getAccessToken());
    }

    public HttpRequestBody(String url, Class<? extends BaseResult> dataClass) {
        this(HttpMethod.POST, url, dataClass);
    }

    public void addPathVariable(String key, String value) {
        pathVariableMap.put(key, value);
    }
    public void addPathVariable(String key, long value) {
        pathVariableMap.put(key, String.valueOf(value));
    }

    public void addParameter(String key, long value) {
        addParameter(key, String.valueOf(value));
    }
    public void addParameter(String key, String value) {
        if (key == null || value == null) {
           if(key!=null){
               requestParamMap.put(key, "valueNull");
           }
            return;
        }
        requestParamMap.put(key, value);
    }

    public boolean isMainTheadCallback() {
        return mainTheadCallback;
    }

    public void setRunWithOtherThread(){
        mainTheadCallback = false;
    }
    public void removeParameter(String key) {
        requestParamMap.remove(key);
    }
    public void clearParameter() {
        requestParamMap.clear();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class<? extends BaseResult> getDataClass() {
        return dataClass;
    }

    public String getParameterValue(String key) {
        return requestParamMap.get(key);
    }
    public String getPathVariable(String key) {
        return pathVariableMap.get(key);
    }

    public ArrayMap<String, String> getParameter() {
        return requestParamMap;
    }

    public ArrayMap<String, String> getHeader() {
        return header;
    }

    public String getHeaderValue(String key) {
        return header.get(key);
    }

    public String getMethod() {
        return method;
    }

    public void patch() {
        this.method = HttpMethod.PATCH;
    }
    public void get() {
        this.method = HttpMethod.GET;
    }
    public void delete() {
        this.method = HttpMethod.DELETE;
    }
    public void put() {
        this.method = HttpMethod.PUT;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public ArrayMap<String, String> getPathVariableMap() {
        return pathVariableMap;
    }
}
