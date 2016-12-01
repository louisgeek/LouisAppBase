package com.louisgeek.louisappbase.musicplayer;

import com.louisgeek.louisappbase.musicplayer.bean.LrcLineBean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by louisgeek on 2016/11/27.
 */

public class LrcHelper {


    public static List<LrcLineBean> parseAllLrc(String lrcAllStr) {
        List<LrcLineBean> lrcAllLineBeanList = new ArrayList<>();
        //
        StringReader reader = new StringReader(lrcAllStr);
        BufferedReader bufferedReader = new BufferedReader(reader);
        String tempLine;
        try {
            while ((tempLine = bufferedReader.readLine()) != null) {
                // String lrcLine = "[02:45.69][02:42.20][02:37.69][01:10.60]就在[记忆]里画一个叉";
                String lrcLine = tempLine;
                List<LrcLineBean> lrcLineBeanList = parseAllLrcLineToList(lrcLine);
                lrcAllLineBeanList.addAll(lrcLineBeanList);//把每行的集合放入总集合

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 排序  根据歌词的时间来排序
         */
        Collections.sort(lrcAllLineBeanList);


        /**
         * 检验
         */
   /*     for (LrcLineBean lrcLineBean :
                lrcAllLineBeanList) {
            System.out.println("lrcLineBean:" + lrcLineBean.getTimeStr() + lrcLineBean.getTimeMillisecond()+ lrcLineBean.getContent() );

        }*/

        return lrcAllLineBeanList;
    }

    /**
     * [02:45.69][02:42.20][02:37.69][01:10.60]就在记忆里画一个叉
     * <p>
     * or
     * <p>
     * <p>
     * [02:13.02]看到火车远远驶向前去
     * <p>
     *
     * @param lrcLine
     */
    private static List<LrcLineBean> parseAllLrcLineToList(String lrcLine) {
        /**
         * 每行可以转换歌词bean的list
         */
        List<LrcLineBean> lrcLineBeanList = new ArrayList<>();

        /**
         * 匹配歌词
         */
        String reg = "\\[(\\d{2}:\\d{2}\\.\\d{2})\\]";//歌词时间 格式
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(lrcLine);
        //开始不是 就反回了   可选
        if (!matcher.lookingAt()) {//lookingAt对前面的字符串进行匹配,只有匹配到的字符串在最前面才会返回true
            return lrcLineBeanList;//直接返回 不是的lrc格式了
        }
        List<String> lrcTimeStrList = new ArrayList<>();
        StringBuffer stringBuffer = new StringBuffer();

        matcher.reset();//将Matcher的状态重新设置为最初的状态
        //循环一次  寻找下一个匹配
        while (matcher.find()) {
            String timeStr = matcher.group();//返回匹配到的子字符串
            lrcTimeStrList.add(timeStr);
            int startIndex = matcher.start();//返回当前匹配到的字符串在原目标字符串中的位置
            int endIndex = matcher.end();//返回当前匹配的字符串的最后一个字符在原目标字符串中的索引位置
            matcher.appendReplacement(stringBuffer, "");//将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
        }
        matcher.appendTail(stringBuffer);//将最后一次匹配工作后剩余的字符串添加到一个StringBuffer对象里
        String contentStr = stringBuffer.toString();


        if (lrcTimeStrList != null && lrcTimeStrList.size() > 0) {
            for (int i = 0; i < lrcTimeStrList.size(); i++) {
                LrcLineBean lrb = new LrcLineBean();
                lrb.setTimeStr(lrcTimeStrList.get(i));
                lrb.setTimeMillisecond(parseLrcTimeStrToMillisecond(lrb.getTimeStr()));
                lrb.setContent(contentStr);
                if (lrb.getContent() != null && !lrb.getContent().trim().equals("")) {
                    lrcLineBeanList.add(lrb);
                }
            }
        }

        return lrcLineBeanList;
        // Log.d(TAG, "parseLrc: ");
        //System.out.println("lrcLineBeanList:" + lrcLineBeanList);
        //  }
    }

    /**
     * 解析时间，转换为毫秒格式
     *
     * @param lrcTimeStr [02:45.69]
     * @return
     */
    private static long parseLrcTimeStrToMillisecond(String lrcTimeStr) {
        String tempTime = lrcTimeStr.substring(1, lrcTimeStr.length() - 1);//02:45.69
        String[] tempTimes = tempTime.split(":");
        int minute = Integer.parseInt(tempTimes[0]);//02
        String[] tempTimes_temps = tempTimes[1].split("\\.");
        int second = Integer.parseInt(tempTimes_temps[0]);//45
        int millisecond = Integer.parseInt(tempTimes_temps[1]);//69
        return minute * 60 * 1000 + second * 1000 + millisecond * 10;
    }

}
