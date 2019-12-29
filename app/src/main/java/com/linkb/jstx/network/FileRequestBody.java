
package com.linkb.jstx.network;

import com.linkb.jstx.listener.OnTransmitProgressListener;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

class FileRequestBody extends RequestBody {
    private final OnTransmitProgressListener listener;
    private File file;

    public FileRequestBody(File file, OnTransmitProgressListener listener) {
        this.listener = listener;
        this.file = file;
    }

    @Override
    public long contentLength() {
        return file.length();
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/octet-stream");
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        Source source = Okio.source(file);
        Buffer buf = new Buffer();
        Long remaining = contentLength();
        for (long readCount; (readCount = source.read(buf, 2048)) != -1; ) {
            sink.write(buf, readCount);
            remaining -= readCount;
            float progress = (contentLength() - remaining) * 100 / file.length();
            listener.onProgress(progress);
        }
        IOUtils.closeQuietly(source);
    }
}
