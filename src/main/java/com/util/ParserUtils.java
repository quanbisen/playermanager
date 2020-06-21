package com.util;

import com.alibaba.fastjson.JSON;
import com.config.Category;
import com.pojo.Album;
import com.pojo.Singer;
import com.pojo.Song;
import java.util.List;

public final class ParserUtils {

    /**解析响应结果获得对象的函数
     * @param responseString 响应字符串
     * @param category 解析的对象类型*/
    public static List parseResponseStringList(String responseString, Category category){
        List list = null;
        switch (category){
            case Singer:{
                list = JSON.parseArray(responseString, Singer.class);
                break;
            }
            case Album:{
                list = JSON.parseArray(responseString, Album.class);
                break;
            }
            case Song:{
                list = JSON.parseArray(responseString, Song.class);
                break;
            }
            default:
        }
        return list;
    }

}
