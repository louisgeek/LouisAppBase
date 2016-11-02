package com.louisgeek.louisappbase.util;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/2.
 */

public class ListUtil {

    protected boolean checkListIsEmpty(List<Object> list){
        if (list!=null&&list.size()>0){
            return false;
        }else{
            return true;
        }
    }
}
