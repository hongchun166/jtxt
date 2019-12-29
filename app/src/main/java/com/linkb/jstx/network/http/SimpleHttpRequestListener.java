
package com.linkb.jstx.network.http;


import com.linkb.jstx.network.result.BaseResult;

public class SimpleHttpRequestListener<T extends BaseResult>   implements HttpRequestListener<T> {

    public void onHttpRequestSucceed(T result, OriginalCall call) {
    }

    @Override
    public void onHttpRequestFailure(Exception e, OriginalCall call) {
    }
}
