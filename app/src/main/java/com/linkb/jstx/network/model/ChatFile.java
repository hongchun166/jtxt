
package com.linkb.jstx.network.model;


import java.io.File;
import java.io.Serializable;

public class ChatFile implements Serializable {

    private static final long serialVersionUID = 1L;

    public long size;

    public String name;

    public String path;

    public String file;

    public File getLocalFile() {
        if (path == null) {
            return null;
        }
        return new File(path);
    }
}
