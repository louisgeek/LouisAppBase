package com.louisgeek.louisappbase.musicplayer;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import com.louisgeek.louisappbase.musicplayer.bean.MusicBean;
import com.louisgeek.louisappbase.util.SdTool;
import com.louisgeek.louisappbase.util.ThreadUtil;
import com.socks.library.KLog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2016/11/21.
 */

public class MusicPlayerHelper {
    private static boolean isSuccess = false;
    private static String message = "";
    private static List<MusicBean> mMusicBeanList = new ArrayList<>();

    /**
     * @param context
     * @return
     */
    public static void scanLocalMusicList(final Context context, final ScanLocalMusicCallBack scanLocalMusicCallBack) {
        KLog.d("SDF 扫描开始");
        if (!SdTool.hasSDCardMounted()) {
            KLog.e("无外部存储");
            isSuccess = false;
            message = "无外部存储";
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                /**
                 *
                 */
                Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null, null, null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

                if (cursor == null) {
                    KLog.d("mCursor is null");
                    isSuccess = false;
                    message = "mCursor is null";
                    return;
                }
                mMusicBeanList.clear();
                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex(MediaStore.Audio.Media._ID);
                    int titleIndex = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
                    int artistIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
                    int durationIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
                    int sizeIndex = cursor.getColumnIndex(MediaStore.Audio.Media.SIZE);//The size of the file in bytes
                    int dataIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
                    int displayNameIndex = cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME);
                    int albumidIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID);
                    int albumIndex = cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
                    int isMusicIndex = cursor.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC);

                    int id = cursor.getInt(idIndex);
                    String title = cursor.getString(titleIndex);
                    String artist = cursor.getString(artistIndex);
                    long duration = cursor.getLong(durationIndex);
                    long size = cursor.getLong(sizeIndex);
                    String data = cursor.getString(dataIndex);
                    String displayName = cursor.getString(displayNameIndex);
                    int albumid = cursor.getInt(albumidIndex);
                    String album = cursor.getString(albumIndex);
                    int isMusic = cursor.getInt(isMusicIndex);

                    File file = new File(data);
                    if (!file.exists()) {
                        KLog.d("file is not exists");
                        continue;
                    }

                    if (isMusic != 0) {
                        MusicBean musicBean = new MusicBean();
                        musicBean.setId(id);
                        musicBean.setArtist(artist);
                        musicBean.setTime(duration);
                        musicBean.setName(title);
                        musicBean.setUrl(data);
                        musicBean.setSize(size);
                        musicBean.setDisplayName(displayName);
                        musicBean.setAlbumid(albumid);
                        musicBean.setAlbumName(album);
                        //   KLog.d("album:"+album);
                        mMusicBeanList.add(musicBean);
                    }
                }

                isSuccess = true;
                message = "图片扫描完成";
                KLog.d("SDF 扫描结束");
                /**
                 *close
                 */
                cursor.close();


                if (isSuccess) {
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scanLocalMusicCallBack.onSuccess(mMusicBeanList);
                        }
                    });

                } else {
                    ThreadUtil.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            scanLocalMusicCallBack.onError(message);
                        }
                    });
                }
            }
        }).start();
    }

    /**
     * 获取mp3文件内置的专辑图
     *
     * @param albumid MediaStore.Audio.Media.ALBUM_ID 对应的id
     * @return
     */
    public static Bitmap getAlbumArtBitmap(Context context, int albumid) {
        Bitmap bitmap = null;
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums.ALBUM_ART},
                MediaStore.Audio.Albums._ID + "=?", new String[]{String.valueOf(albumid)}, null);

        while (cursor.moveToNext()) {
            int albumArtIndex = cursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART);
            String albumArt = cursor.getString(albumArtIndex);
            bitmap = BitmapFactory.decodeFile(albumArt);
        }
        return bitmap;
    }

    /**
     * 获取mp3文件内置的专辑图 way2
     *
     * @param filePath like  /storage/emulated/0/Music/陈冠蒲 - 就让你走.mp3
     * @return
     */
    public static Bitmap createAlbumArtBitmap(final String filePath) {
        KLog.d("filePath:" + filePath);
        Bitmap bitmap = null;
        //能够获取多媒体文件元数据的类
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        try {
            retriever.setDataSource(filePath); //设置数据源
            byte[] embedPic = retriever.getEmbeddedPicture(); //得到字节型数据
            bitmap = BitmapFactory.decodeByteArray(embedPic, 0, embedPic.length); //转换为图片
        } catch (Exception e) {
            // e.printStackTrace();
            KLog.w(e.getMessage());
        } finally {
            try {
                retriever.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }


    public interface ScanLocalMusicCallBack {
        void onSuccess(List<MusicBean> mMusicBeanList);

        void onError(String message);
    }

    /**
     * 格式化时间，将毫秒转换为分:秒格式
     *
     * @param time
     * @return
     */
    public static String formatTime(long time) {
        String min = time / (1000 * 60) + "";
        String sec = time % (1000 * 60) + "";
        if (min.length() < 2) {
            min = "0" + time / (1000 * 60) + "";
        } else {
            min = time / (1000 * 60) + "";
        }
        if (sec.length() == 4) {
            sec = "0" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 3) {
            sec = "00" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 2) {
            sec = "000" + (time % (1000 * 60)) + "";
        } else if (sec.length() == 1) {
            sec = "0000" + (time % (1000 * 60)) + "";
        }
        return min + ":" + sec.trim().substring(0, 2);
    }
}
