
package com.linkb.jstx.bean;

import java.io.File;

public class FileResource {
    public String key;
    public File file;
    public String bucket;

    public FileResource(String key, File file)
    {
        this.key = key;
        this.file = file;
    }
    public FileResource(String key, String bucket, File file)
    {
        this.key = key;
        this.file = file;
        this.bucket = bucket;
    }
}
