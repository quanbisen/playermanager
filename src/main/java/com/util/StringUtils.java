package com.util;

/**
 * @author super lollipop
 * @date 6/21/20
 */
public class StringUtils {

    /**判断字符串是否为空
     * @param string 待判断的字符串
     * @return boolean*/
    public static boolean isEmpty(String string){
        return string == null || string.equals("") ? true : false;
    }

}
