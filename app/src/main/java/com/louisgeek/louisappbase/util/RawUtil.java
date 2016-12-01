package com.louisgeek.louisappbase.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by louisgeek on 2016/5/31.
 */
public class RawUtil {
    /**
     * getStringFromRaw
     *
     * @param rawID
     * @return
     */
    public static String getStringFromRaw(Context context, int rawID) {
        StringBuilder sb_result = new StringBuilder("");
        try {
            InputStream ssq_is = context.getResources().openRawResource(rawID);

            InputStreamReader inputReader = new InputStreamReader(ssq_is);
            //InputStreamReader inputReader = new InputStreamReader(ssq_is,"UTF-8");
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                sb_result.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb_result.toString();
    }

    public static String getStringFromRawWithRN(Context context, int rawID) {
        StringBuilder sb_result = new StringBuilder("");
        try {
            InputStream ssq_is = context.getResources().openRawResource(rawID);

            InputStreamReader inputReader = new InputStreamReader(ssq_is);
            //InputStreamReader inputReader = new InputStreamReader(ssq_is,"UTF-8");
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                if (line.trim().equals("")) {
                    continue;
                } else {
                    sb_result.append(line.trim() + "\r\n");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb_result.toString();
    }

    /**
     * 很多行 会卡死
     *
     * @param context
     * @param rawID
     * @return
     */
    @Deprecated
    public static String getStringFromRawOld(Context context, int rawID) {
        String result = "";
        try {
            InputStream ssq_is = context.getResources().openRawResource(rawID);

            InputStreamReader inputReader = new InputStreamReader(ssq_is);
            //InputStreamReader inputReader = new InputStreamReader(ssq_is,"UTF-8");
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            while ((line = bufReader.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
