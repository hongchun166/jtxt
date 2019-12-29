
package com.linkb.jstx.util;

import android.text.TextUtils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class CharacterParser {

    /**
     * 返回字符串的 大写首字母
     * @param chines
     * @return
     */
    public  static String getFirstPinYinChar(String chines){
        if (!TextUtils.isEmpty(chines)){
            char nameChar = chines.trim().charAt(0);
            HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
            if (nameChar > 128) {
                try {
                    String[] pingyingStr = PinyinHelper.toHanyuPinyinStringArray(nameChar, defaultFormat);
                    if (pingyingStr != null && pingyingStr.length > 0){
                        return pingyingStr[0].toUpperCase();
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                }
            } else if (String.valueOf(nameChar).toUpperCase().matches("[A-Z]")) {
                return String.valueOf(nameChar).toUpperCase();
            }
        }

        return "#";
    }
}
