package com.louisgeek.louisappbase.util;

/**
 * Created by louisgeek on 2016/11/2.
 */

public class StringUtil {
    public static boolean checkStringIsEmpty(String string){
        if (string!=null&&string.length()>0){
            return false;
        }else{
            return true;
        }
    }
    public static String filterNullString(String string){
        String str="";
        if (string!=null&&string.length()>0&&string.trim().length()>0&&!string.trim().toLowerCase().equals("null")){
            str= string;
        }
        return  str;
    }
}
