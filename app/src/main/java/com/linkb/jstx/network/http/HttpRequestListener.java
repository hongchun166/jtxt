
package com.linkb.jstx.network.http;

import com.linkb.jstx.network.result.BaseResult;

public interface HttpRequestListener<T extends BaseResult> {
    void onHttpRequestSucceed(T result, OriginalCall call);
    void onHttpRequestFailure(Exception e, OriginalCall call);
}
