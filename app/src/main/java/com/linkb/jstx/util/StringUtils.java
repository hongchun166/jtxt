
package com.linkb.jstx.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;


public class StringUtils {

    private StringUtils() {
    }


    public static boolean isEmpty(Object obj) {

        return null == obj || "".equals(obj.toString().trim());
    }


    public static String toString(Object obj) {

        return null == obj ? null : obj.toString();
    }

    public static boolean isNotEmpty(Object obj) {

        return !isEmpty(obj);
    }


    public static String getUUID() {

        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    public static boolean isWebUrl(String path) {
        if (!path.toLowerCase().startsWith("http://") && !path.toLowerCase().startsWith("https://")) {
            return false;
        }
        try {
            new URL(path);
        } catch (MalformedURLException e) {
            return false;
        }
        return true;
    }

}
