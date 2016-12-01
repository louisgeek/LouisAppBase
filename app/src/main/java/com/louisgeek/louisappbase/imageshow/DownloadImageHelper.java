package com.louisgeek.louisappbase.imageshow;

import android.util.Log;

import com.louisgeek.louisappbase.util.DateTool;
import com.louisgeek.louisappbase.util.MediaFiletool;
import com.louisgeek.louisappbase.util.SdTool;
import com.louisgeek.louisappbase.util.ThreadUtil;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by louisgeek on 2016/11/14.
 */

public class DownloadImageHelper {
    private static String mUrlStr;

    // 获得存储卡的路径
    private static String sd_path = SdTool.getSDCardPath();
    private static String filePath = sd_path + "MyFileDir" + File.separator + "ReaderSaveImages" + File.separator;
    private static String saveFileAllName;
    private static OnDownLoadImageListener mOnDownLoadImageListener;

    public static void downLoadFile(final String imageUrlStr, OnDownLoadImageListener onDownLoadImageListener) {
        mUrlStr = imageUrlStr;
        mOnDownLoadImageListener = onDownLoadImageListener;
        //MediaFiletool.isImageFileType(MediaFiletool.getFileTypeForMimeType())


        //
        new Thread(new DownloadFileThreadRunnable()).start();

        //
    }

    public static String encode(String str) {
        try {
            str = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    private static class DownloadFileThreadRunnable implements Runnable {
        boolean isSuccess = false;

        @Override
        public void run() {
            FileOutputStream fileOutputStream = null;
            InputStream inputStream = null;
            //
            try {
                URL url = new URL(mUrlStr);
                KLog.d("mUrlStr:" + mUrlStr);
                //获取连接
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);
                connection.setRequestProperty("Connection", "Keep-Alive");
                connection.setRequestProperty("Charset", "UTF-8");
                //##connection.setDoOutput(true);//Android  4.0 GET时候 用这句会变成POST  报错java.io.FileNotFoundException
                connection.setDoInput(true);
                connection.setUseCaches(false);
                //打开连接
                connection.connect();
                //获取内容长度
                int contentLength = connection.getContentLength();

                //like   image/jpeg
                String contentType = connection.getContentType();
                KLog.d("contentType:" + contentType);

                ///////
                String extension = MediaFiletool.getExtensionByMimeType(contentType);
                KLog.d("extension:" + extension);
                String fileName = DateTool.getFileNameByDateTime("IMG", extension);

                saveFileAllName = filePath + fileName;


                File file = new File(filePath);
                // 判断文件目录是否存在
                if (!file.exists()) {
                    file.mkdir();
                }
                //file.mkdirs();

                //输入流
                inputStream = connection.getInputStream();

                File myFile = new File(saveFileAllName);
                //输出流
                fileOutputStream = new FileOutputStream(myFile);

                byte[] bytes = new byte[1024];
                // int  index=0;
                long totalReaded = 0;
                int temp_Len;
                while ((temp_Len = inputStream.read(bytes)) != -1) {
                    // bytes[index]= (byte) temp_Len;
                    // index++;
                    totalReaded += temp_Len;
                    Log.i("XXXX", "run: totalReaded:" + totalReaded);
                    long progress = totalReaded * 100 / contentLength;
                    Log.i("XXXX", "run: progress:" + progress);
                    fileOutputStream.write(bytes, 0, temp_Len);

                }
              /*  byte[] bytes = new byte[1024];
                while (inputStream.read(bytes)!=-1){
                    fileOutputStream.write(bytes);
                }*/

                isSuccess = true;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            if (isSuccess) {
                ThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOnDownLoadImageListener.onSuccess(saveFileAllName);
                    }
                });
            } else {
                mOnDownLoadImageListener.onError();
            }

        }
    }

    public interface OnDownLoadImageListener {
        void onSuccess(String filepath);

        void onError();
    }

}
