
package com.linkb.jstx.comparator;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

public class FileDirectoryComparator implements Comparator<File>, Serializable {
    @Override
    public int compare(File f1, File f2) {

        if ((f1.isDirectory() && f2.isDirectory()) || (f1.isFile() && f2.isFile())) {
            return f1.getName().compareToIgnoreCase(f2.getName());
        }

        if (f1.isDirectory() && f2.isFile()) {
            return -1;
        }

        if (f1.isFile() && f2.isDirectory()) {
            return 1;
        }
        return f1.compareTo(f2);
    }

}
