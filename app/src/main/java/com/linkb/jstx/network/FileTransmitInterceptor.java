
package com.linkb.jstx.network;

import com.linkb.jstx.listener.OnTransmitProgressListener;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

class FileTransmitInterceptor implements Interceptor {
    private final OnTransmitProgressListener progressListener;
    private ResponseBody responseBody;
    private long fullContentLength;
    private long startContentLength;

    public FileTransmitInterceptor(OnTransmitProgressListener progressListener, long fullContentLength, long startContentLength) {
        this.progressListener = progressListener;
        this.startContentLength = startContentLength;
        this.fullContentLength = fullContentLength;

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        responseBody = originalResponse.body();
        return originalResponse.newBuilder()
                .body(new FileResponseBody())
                .build();
    }

    class FileResponseBody extends ResponseBody {
        private BufferedSource bufferedSource;

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytes = startContentLength;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    totalBytes += bytesRead != -1 ? bytesRead : 0;
                    float progress = totalBytes * 100 / fullContentLength;
                    progressListener.onProgress(progress);
                    return bytesRead;
                }
            };
        }
    }
}
