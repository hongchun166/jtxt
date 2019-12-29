
package com.linkb.jstx.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class FileModifiedDescComparator implements Comparator<File>, Serializable {
    @Override
    public int compare(File f1, File f2) {
        return Long.compare(f2.lastModified(),f1.lastModified());
    }

}
