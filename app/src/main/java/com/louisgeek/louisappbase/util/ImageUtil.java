package com.louisgeek.louisappbase.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by louisgeek on 2016/11/16.
 */

public class ImageUtil {

    /**
     * 计算图片的缩放值
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize2(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }
    /**
     * 计算inSampleSize，用于压缩图片
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private static int calculateInSampleSize(BitmapFactory.Options options,
                                              int reqWidth, int reqHeight) {
        // 源图片的宽度
        int width = options.outWidth;
        int height = options.outHeight;

        int inSampleSize = 1;

        if (width > reqWidth || height > reqHeight) {
            int widthRadio = Math.round(width * 1.0f / reqWidth);
            int heightRadio = Math.round(height * 1.0f / reqHeight);

            inSampleSize = Math.min(widthRadio, heightRadio);
        }

        return inSampleSize;
    }

    /**
     * 根据计算的inSampleSize，得到压缩后图片
     *
     * @param pathName
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap decodeSampledBitmapFromPath(String pathName,
                                                     int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, options);

        return bitmap;
    }

    // 从Resources中加载图片
    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options); // 读取图片长宽
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 计算inSampleSize
        options.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
        return bitmap;
    }

    /**
     * inputStream进行了BufferedInputStream包装，以便可以进行reset()；
     * 直接网络返回的inputStream是不能使用两次的
     * mark  reset
     *
     * @param bufferedInputStream
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    @Deprecated
    private static Bitmap decodeSampledBitmapFromStream(BufferedInputStream bufferedInputStream,
                                                        int reqWidth, int reqHeight) {
        /**
         * http://blog.csdn.net/fbw6766880/article/details/51984740
         *
         * 使用BitmapFactory.decodeStream()方法的时候会使流的mark方法无效
         * 就是下面的bufferedInputStream.available()为0
         * 方案是给一个固定值1024*1024
         * 不过仅限 于 1024*1024大小的图片，如果超过这个大小的话，那么我们在获取流的时候就会不能获取到其所有的长度，会丢失数据，而且耗时较长
         */
        try {
            //bufferedInputStream标记
            bufferedInputStream.mark(bufferedInputStream.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(bufferedInputStream, null, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 计算inSampleSize
        options.inJustDecodeBounds = false;
        //
        try {
            //bufferedInputStream恢复后才能二次使用
            bufferedInputStream.reset();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap = BitmapFactory.decodeStream(bufferedInputStream, null, options);
        return bitmap;
    }

    private static Bitmap decodeSampledBitmapFromByteArray(byte[] dataByteArray,
                                                        int reqWidth, int reqHeight) {

        //
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(dataByteArray, 0,dataByteArray.length, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight); // 计算inSampleSize
        options.inJustDecodeBounds = false;
        //
        Bitmap bitmap = BitmapFactory.decodeByteArray(dataByteArray, 0,dataByteArray.length, options);
        return bitmap;
    }

    /**
     * 根据url下载图片在指定的文件
     *
     * @param urlStr
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static Bitmap downloadImageToBitmapWishImageSize(String urlStr, int reqWidth, int reqHeight) {
        Bitmap bitmap = null;
        FileOutputStream fileOutputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(500000);
            connection.setReadTimeout(500000);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            //##connection.setDoOutput(true);//Android  4.0 GET时候 用这句会变成POST  报错java.io.FileNotFoundException
            connection.setDoInput(true);
            connection.setUseCaches(false);
            //打开连接
            connection.connect();
            //获取内容长度
            int contentLength = connection.getContentLength();

            /**
             *
             */
            inputStream = connection.getInputStream();
            //BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();

            byte[] bytes=new byte[1024];
            int temp;
            while ((temp=inputStream.read(bytes))!=-1){
                byteArrayOutputStream.write(bytes,0,temp);
            }
            byte[] byteArrays= byteArrayOutputStream.toByteArray();
           // bitmap = decodeSampledBitmapFromStream(bufferedInputStream, reqWidth, reqHeight);
            bitmap = decodeSampledBitmapFromByteArray(byteArrays, reqWidth, reqHeight);

            //
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
            }
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
            }
        }

        return bitmap;

    }
}
