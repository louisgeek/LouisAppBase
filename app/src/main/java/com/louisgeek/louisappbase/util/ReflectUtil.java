package com.louisgeek.louisappbase.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by louisgeek on 2016/11/16.
 */

public class ReflectUtil {

    /**
     * bean2Map  常用
     *
     * @param beanObj
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> Map<K, V> bean2Map(Object beanObj) throws Exception {
        if (beanObj == null) {
            return null;
        }
        Map<K, V> map = new HashMap<>();

        Field[] fields = beanObj.getClass().getDeclaredFields(); //获取所有的属性
        StringBuilder stringBuilder = new StringBuilder();
        for (Field field : fields) {
            field.setAccessible(true);//打破封装
            map.put((K) field.getName(), (V) field.get(beanObj));
            // Log.d(TAG, "bean2Map: "+field.getName()+"="+field.get(beanObj));
            stringBuilder.append(field.getName());
            stringBuilder.append("=");
            stringBuilder.append(field.get(beanObj));
            stringBuilder.append("&");

        }
        // Log.d(TAG, "bean2Map: all:"+stringBuilder.toString());
        return map;
    }

    /**
     * 反射获得对象的值  Exception在外面处理
     * @param
     * @param fieldName
     * @return
     */
    public static <T, E> T getFieldValue(E e, String fieldName) throws Exception {
        Field field = e.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(e);
    }


    public static int getImageViewField(Object object, String fieldName) {
        int value = 0;
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);

            int fieldValue = field.getInt(object);
            if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
                value = fieldValue;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
        return value;
    }
}
