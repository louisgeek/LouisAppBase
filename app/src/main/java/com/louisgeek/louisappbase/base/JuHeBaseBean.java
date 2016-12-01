package com.louisgeek.louisappbase.base;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by louisgeek on 2016/11/11.
 */

public class JuHeBaseBean<T> {


    /**
     * error_code : 0
     * reason : Success
     * result : {"data":[{"content":"二汪","hashId":"13AF55EB201FADD4DB8AD3C0FC053E72","unixtime":1418954054,"updatetime":"2014-12-19 09:54:14","url":"http://img.juhe.cn/joke/201412/19/13AF55EB201FADD4DB8AD3C0FC053E72.gif"},{"content":"运动健身牛人","hashId":"A851715B4A002C927EB56D4B888367A2","unixtime":1418954054,"updatetime":"2014-12-19 09:54:14","url":"http://img.juhe.cn/joke/201412/19/A851715B4A002C927EB56D4B888367A2.gif"},{"content":"牧羊神器","hashId":"2EAFACE519BEAA9C6D139AAEE5CE1371","unixtime":1418954054,"updatetime":"2014-12-19 09:54:14","url":"http://img.juhe.cn/joke/201412/19/2EAFACE519BEAA9C6D139AAEE5CE1371.gif"},{"content":"希特勒","hashId":"CC3FBA9E77EE1CCD33F8BC23EF288EB6","unixtime":1418954054,"updatetime":"2014-12-19 09:54:14","url":"http://img.juhe.cn/joke/201412/19/CC3FBA9E77EE1CCD33F8BC23EF288EB6.gif"},{"content":"二汪抢镜成功","hashId":"EA38382125B92410EC854E84EA00534C","unixtime":1418954054,"updatetime":"2014-12-19 09:54:14","url":"http://img.juhe.cn/joke/201412/19/EA38382125B92410EC854E84EA00534C.jpg"},{"content":"不作死就不会死","hashId":"9DBF749EFE925E66CB28749299AEDE46","unixtime":1418954054,"updatetime":"2014-12-19 09:54:14","url":"http://img.juhe.cn/joke/201412/19/9DBF749EFE925E66CB28749299AEDE46.gif"},{"content":"收割萝卜神器","hashId":"E10FCD66FCD9C8049A58612BC6EF5B70","unixtime":1418954054,"updatetime":"2014-12-19 09:54:14","url":"http://img.juhe.cn/joke/201412/19/E10FCD66FCD9C8049A58612BC6EF5B70.gif"},{"content":"二货家长","hashId":"93A4A90C701233244F0E0E7969DECCD2","unixtime":1418954054,"updatetime":"2014-12-19 09:54:14","url":"http://img.juhe.cn/joke/201412/19/93A4A90C701233244F0E0E7969DECCD2.gif"},{"content":"我的铁头功可不是白练的","hashId":"D67999E98A1CDF7E264EFCADA99BEFE8","unixtime":1418954054,"updatetime":"2014-12-19 09:54:14","url":"http://img.juhe.cn/joke/201412/19/D67999E98A1CDF7E264EFCADA99BEFE8.gif"},{"content":"我是姑姑，你们饿了吧","hashId":"6C683F896457797B9BB1C53611280314","unixtime":1418954054,"updatetime":"2014-12-19 09:54:14","url":"http://img.juhe.cn/joke/201412/19/6C683F896457797B9BB1C53611280314.gif"}]}
     */

    private int error_code;
    private String reason;
    private T result;

    public int getError_code() {
        return error_code;
    }

    public void setError_code(int error_code) {
        this.error_code = error_code;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }
    ////////////////////
    /**
     * 推荐
     *
     * 用法
     *  TypeToken<BaseBean<OthersBean>> typeToken=new TypeToken<BaseBean<OthersBean>>(){};
     BaseBean<OthersBean> baseBean=BaseBean.fromJsonOne(body,typeToken);
     * @param json
     * @param token
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, TypeToken<T> token) {
        Gson gson = new Gson();
        Type objectType = token.getType();
        return gson.fromJson(json, objectType);
    }
}
