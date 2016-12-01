package com.louisgeek.louisappbase.musicplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.louisgeek.louisappbase.KooApplication;
import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.musicplayer.bean.MusicBean;
import com.louisgeek.louisappbase.util.AppTool;
import com.louisgeek.louisappbase.util.InfoHolderSingleton;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2016/11/15.
 * <p>
 * notification 和 service能单独脱离aty运行
 * 脑图 https://code.aliyun.com/hi31588535/outside_chain/raw/master/music_service.png
 */

public class MusicService extends Service implements MusicPlayInterface, AudioManager.OnAudioFocusChangeListener {

    private MusicServiceBinder mMusicServiceBinder = new MusicServiceBinder();
    //控件
    private MediaPlayer mMediaPlayer;

    private String mUrl;
    private int mNowSongAllTime;
    //int mCurrentPosition;

    private MusicServiceBroadcastReceiver mMusicServiceBroadcastReceiver;


    //通知栏显示所用到的布局文件
    private RemoteViews mContentView;
    private NotificationCompat.Builder mBuilder;

    //boolean isPlaying = false;

    private List<MusicBean> mMusicBeanList = new ArrayList<>();
    /**
     * 修改mMusicNowPosition 的操作全部放在Service里
     */
    private int mMusicNowPosition;

    //一系列动作
    @Deprecated
    public static final String MUSIC_SERVICE_ACTION_START = "ACT_START";
    public static final String MUSIC_SERVICE_ACTION_PAUSE = "ACT_PAUSE";
    public static final String MUSIC_SERVICE_ACTION_RESUME = "ACT_RESUME";
    @Deprecated
    public static final String MUSIC_SERVICE_ACTION_STOP = "ACT_STOP";
    public static final String MUSIC_SERVICE_ACTION_NEXT = "ACT_NEXT_SONG";
    public static final String MUSIC_SERVICE_ACTION_PRE = "ACT_PRE_SONG";
    public static final String MUSIC_SERVICE_ACTION_CLOSE = "ACT_CLOSE";
    public static final String MUSIC_SERVICE_ACTION_CHANGE_PRO = "ACT_CHANGE_PRO";

    public static final String MUSIC_SERVICE_ACTION_MODEL = "ACT_CHANGE_MODEL";
    public static final String MUSIC_SERVICE_ACTION_CHANGE_VOLUME = "ACT_CHANGE_VOLUME";
    public static final String[] mActionArr = new String[]{
            MUSIC_SERVICE_ACTION_PAUSE,
            MUSIC_SERVICE_ACTION_START,
            MUSIC_SERVICE_ACTION_RESUME,
            MUSIC_SERVICE_ACTION_STOP,
            MUSIC_SERVICE_ACTION_NEXT,
            MUSIC_SERVICE_ACTION_PRE,
            MUSIC_SERVICE_ACTION_CLOSE,
            MUSIC_SERVICE_ACTION_CHANGE_PRO,
            MUSIC_SERVICE_ACTION_MODEL,
            MUSIC_SERVICE_ACTION_CHANGE_VOLUME
    };

    /**
     * 音频占用监听 OnAudioFocusChangeListener
     *
     * @param focusChange
     */
    @Override
    public void onAudioFocusChange(int focusChange) {

        switch (focusChange) {
            //暂时失去Audio Focus，并会很快再次获得。必须停止Audio的播放，但是因为可能会很快再次获得AudioFocus，这里可以不释放Media资源；
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Pause playback
                pauseOperation();
                break;
            //失去了Audio Focus，并将会持续很长的时间。这里因为可能会停掉很长时间，所以不仅仅要停止Audio的播放，最好直接释放掉Media资源。
            case AudioManager.AUDIOFOCUS_LOSS:
               /* am.unregisterMediaButtonEventReceiver(RemoteControlReceiver);
                am.abandonAudioFocus(afChangeListener);*/
                // Stop playback
                pauseOperation();
                break;
            //暂时失去AudioFocus，但是可以继续播放，不过要在降低音量。
            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lower the volume
                lowerVolume();
                break;
            //获得了Audio Focus；
            case AudioManager.AUDIOFOCUS_GAIN:
                // Resume playback or Raise it back to normal
                resumeOperation();
                break;
        }
    }

    private void lowerVolume() {
        /**
         *  streamType常用：
         STREAM_ALARM 警报
         STREAM_MUSIC 音乐回放即媒体音量
         STREAM_NOTIFICATION 窗口顶部状态栏Notification,
         STREAM_RING 铃声
         STREAM_SYSTEM 系统
         STREAM_VOICE_CALL 通话
         STREAM_DTMF 双音多频,拨号键的声音
         */

        /**
         * direction,是调整的方向,增加或减少,可以是:
         ADJUST_LOWER 降低音量
         ADJUST_RAISE 升高音量
         ADJUST_SAME 保持不变,这个主要用于向用户展示当前的音量
         */
        /**
         * flags是一些附加参数,只介绍两个常用的
         FLAG_PLAY_SOUND 调整音量时播放声音
         FLAG_SHOW_UI 调整时显示音量条,就是按音量键出现的那个
         FLAG_REMOVE_SOUND_AND_VIBRATE 无振动无声音
         */
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);

    }

    private void raiseVolume() {
        mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);

    }

    private enum CycleModel {
        ALL, ONE, RANDOM
    }

    /**
     * 默认是列表循环CycleModel.ALL
     */
    private CycleModel mCycleModel = CycleModel.ALL;

    @Override
    public void onCreate() {
        super.onCreate();
        KLog.d("onCreate");


        initMediaPlayer();

        initAudioManager();


        initMusicServiceBroadcastReceiverListenMsg();


        /**
         * 电话监听
         */
        // 添加来电监听事件
        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE); // 获取系统服务
        telManager.listen(new MyPhoneStateListener(),
                PhoneStateListener.LISTEN_CALL_STATE);
    }

    private AudioManager mAudioManager;

    private void initAudioManager() {
        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

    }

    /**
     * 电话状态监听way 2
     * <p>
     * 播放的时候 regPhoneNotifBroadcastReceiver
     * <p>
     * 暂停的时候 unRegPhoneNotifBroadcastReceiver
     */
    NoisyAudioStreamBroadcastReceiver mNoisyAudioStreamBroadcastReceiver;

    private void regPhoneNotifBroadcastReceiver() {
        mNoisyAudioStreamBroadcastReceiver = new NoisyAudioStreamBroadcastReceiver();
        IntentFilter noisyFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        registerReceiver(mNoisyAudioStreamBroadcastReceiver, noisyFilter);
    }

    private void unRegPhoneNotifBroadcastReceiver() {
        unregisterReceiver(mNoisyAudioStreamBroadcastReceiver);
    }

    public class NoisyAudioStreamBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            pauseOperation();
        }
    }

    /**
     * 电话状态监听
     */
    private class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:// 挂机状态
                    Intent intent_cont_resume = new Intent();
                    intent_cont_resume.setAction(MUSIC_SERVICE_ACTION_RESUME);
                    sendBroadcast(intent_cont_resume);
                    KLog.d("CALL_STATE_IDLE");
                    break;
                case TelephonyManager.CALL_STATE_RINGING://响铃状态
                case TelephonyManager.CALL_STATE_OFFHOOK://通话状态
                    Intent intent_cont_pause = new Intent();
                    intent_cont_pause.setAction(MUSIC_SERVICE_ACTION_PAUSE);
                    sendBroadcast(intent_cont_pause);
                    KLog.d("CALL_STATE_RINGING or CALL_STATE_OFFHOOK:state" + state);
                    break;
            }
        }
    }

    private void initMusicServiceBroadcastReceiverListenMsg() {
        mMusicServiceBroadcastReceiver = new MusicServiceBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        // intentFilter.addAction(MUSIC_SERVICE_ACTION_PAUSE); //为BroadcastReceiver指定action，即要监听的消息名字。
        for (int i = 0; i < mActionArr.length; i++) {
            intentFilter.addAction(mActionArr[i]);
        }
        registerReceiver(mMusicServiceBroadcastReceiver, intentFilter);
    }

    private void initMediaPlayer() {

        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                KLog.d("onCompletion");
                /**
                 *
                 */
                if (mCycleModel == CycleModel.ALL) {
                    //do noting
                } else if (mCycleModel == CycleModel.ONE) {
                    //对单曲循环单独的处理
                    //  播放完后先把pos -1 再执行nextSong()变相实现单曲循环
                    mMusicNowPosition--;
                    if (mMusicNowPosition < 0) {
                        mMusicNowPosition = mMusicBeanList.size() - 1;
                    }
                } else if (mCycleModel == CycleModel.RANDOM) {
                    //do noting
                }
                /**
                 *
                 */
                nextSong();
                /**
                 * 通知activity
                 */
                Intent intent_next = new Intent();
                intent_next.setAction(MusicPlayerActivity.MUSIC_Player_ACTION_NEXT);
                sendBroadcast(intent_next);
                /**
                 *
                 */


            }
        });
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                /// KLog.d("onPrepared");
                //
                mNowSongAllTime=mMediaPlayer.getDuration();
                // 目的是得到歌曲的时长 秒
                int duration =mNowSongAllTime/1000;//milliseconds /1000
                KLog.d("duration:" + duration);
                mMediaPlayer.start();
            }
        });
        mMediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                KLog.d("onError");
                return false;
            }
        });
        /**
         * 取得进度的循环任务
         */
        handler.post(taskRunnable);//立即启动
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        KLog.d("onBind");
        return mMusicServiceBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        KLog.d("onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        KLog.d("onRebind");
        super.onRebind(intent);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // MusicBean musicBean = (MusicBean) InfoHolderSingleton.getInstance().getMapObj("musicBean");
        mMusicNowPosition = (int) InfoHolderSingleton.getInstance().getMapObj("musicNowPosition");
        List<MusicBean> musicBeanList = (List<MusicBean>) InfoHolderSingleton.getInstance().getMapObj("musicBeanList");


        KLog.d("mMusicNowPosition:" + mMusicNowPosition);

        mMusicBeanList.clear();
        mMusicBeanList.addAll(musicBeanList);


        KLog.d("onStartCommand");

        /**
         * 请求AudioFocus
         */
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);


        refreshMusicAndPlay(true);
        /**
         *
         */
        initNotification(getApplicationContext(), mMusicBeanList.get(mMusicNowPosition));

        /**
         * 手动返回START_STICKY，当service因内存不足被kill，当内存又有的时候，service又被重新创建，
         * 比较不错，但是不能保证任何情况下都被重建，比如进程被干掉了
         */
        return super.onStartCommand(intent, flags, startId);
        // return START_REDELIVER_INTENT;//重传Intent。使用这个返回值时，如果在执行完onStartCommand后，服务被异常kill掉，系统会自动重启该服务，并将Intent的值传入。
        //  return START_STICKY;//如果service进程被kill掉，保留service的状态为开始状态，但不保留递送的intent对象。随后系统会尝试重新创建service，由于服务状态为开始状态，所以创建服务后一定会调用onStartCommand(Intent,int,int)方法。如果在此期间没有任何启动命令被传递到service，那么参数Intent将为null。
    }

    private void refreshMusicAndPlay(boolean isFirstPlay) {
        MusicBean musicBean = mMusicBeanList.get(mMusicNowPosition);
        mUrl = musicBean.getUrl();
        playSong();
        /**
         *
         */
        if (!isFirstPlay) {

            setupNotificationPersonalViewInfo(musicBean);
            refreshNotification();
        }

        //!!!!!
        InfoHolderSingleton.getInstance().putMapObj("musicNowPosition", mMusicNowPosition);
    }

    private void setupNotificationPersonalViewInfo(MusicBean musicBean) {
        Bitmap largeBitmap = AppTool.getIconBitmap(getApplicationContext());
        mContentView.setImageViewBitmap(R.id.notification_music_logo, largeBitmap);
        Bitmap album_bm = MusicPlayerHelper.getAlbumArtBitmap(getApplicationContext(), musicBean.getAlbumid());
        if (album_bm != null) {
            mContentView.setImageViewBitmap(R.id.notification_music_logo, album_bm);
        }
        /**
         *
         */
        mContentView.setTextViewText(R.id.notification_music_name, musicBean.getName());
        mContentView.setTextViewText(R.id.notification_music_singer, musicBean.getArtist() + "-" + musicBean.getAlbumName());
    }

    private void initNotification(Context context, MusicBean musicBean) {

        //
        //##NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //启用前台服务，主要是startForeground()
        mBuilder = new NotificationCompat.Builder(context);
        mBuilder.setTicker("开始播放。。。");//设置收到通知时在顶部显示的文字信息
        mBuilder.setSmallIcon(R.drawable.tab_icon_me);//不可少  单独设置
        // mBuilder.setSubText("——记住我叫叶良辰")                    //内容下面的一小段文字
        mBuilder.setWhen(System.currentTimeMillis());
        //设置通知默认效果
        //notification.flags = Notification.FLAG_SHOW_LIGHTS;
        mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        //mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS | NotificationCompat.DEFAULT_VIBRATE);

        Intent intentClick = new Intent(context, MusicPlayerActivity.class);
        /**
         *设置msg的事件
         */
        PendingIntent pendingIntent4Click2Aty = PendingIntent.getActivity(context, 0, intentClick, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pendingIntent4Click2Aty);


        mContentView = new RemoteViews(context.getPackageName(), R.layout.notification_musicplayer);
        /**
         *设置自定义布局上view的事件
         */
        /**
         * 暂停事件
         */
        Intent intent_cont_pause = new Intent();
        intent_cont_pause.setAction(MUSIC_SERVICE_ACTION_PAUSE);
        PendingIntent pi4Click2Broadcast_cont_pause = PendingIntent.getBroadcast(context, 0, intent_cont_pause, PendingIntent.FLAG_UPDATE_CURRENT);
        mContentView.setOnClickPendingIntent(R.id.notification_music_Control, pi4Click2Broadcast_cont_pause);

        /**
         * 上一曲
         */
        Intent intent_pre = new Intent();
        intent_pre.setAction(MUSIC_SERVICE_ACTION_PRE);
        PendingIntent pi4Click2Broadcast_pre = PendingIntent.getBroadcast(context, 0, intent_pre, PendingIntent.FLAG_UPDATE_CURRENT);
        //pre
        mContentView.setOnClickPendingIntent(R.id.notification_music_Previous, pi4Click2Broadcast_pre);


        /**
         * 下一曲
         */
        Intent intent_next = new Intent();
        intent_next.setAction(MUSIC_SERVICE_ACTION_NEXT);
        PendingIntent pi4Click2Broadcast_next = PendingIntent.getBroadcast(context, 0, intent_next, PendingIntent.FLAG_UPDATE_CURRENT);
        //next
        mContentView.setOnClickPendingIntent(R.id.notification_music_Next, pi4Click2Broadcast_next);
        /**
         * 关闭
         */
        Intent intent_close = new Intent();
        intent_close.setAction(MUSIC_SERVICE_ACTION_CLOSE);
        PendingIntent pi4Click2Broadcast_close = PendingIntent.getBroadcast(context, 0, intent_close, PendingIntent.FLAG_UPDATE_CURRENT);
        //next
        mContentView.setOnClickPendingIntent(R.id.notification_music_Close, pi4Click2Broadcast_close);

        //
        //contentView.setIcon();
        // contentView.setImageViewResource(R.id.content_view_image, R.drawable.ic_file_download_light_green_700_18dp);
        setupNotificationPersonalViewInfo(musicBean);
        //一样 mBuilder.setCustomContentView(contentView);
        mBuilder.setContent(mContentView);


        Notification notification = mBuilder.build();

        //### notificationManager.notify(1, notification); //换成 startForeground(1, notification);
        /**
         *  启用前台服务  让 app kill后服务还能继续运行
         */
        startForeground(1, notification);
    }

    private void refreshNotification() {

        mBuilder.setContent(mContentView);

        Notification notification = mBuilder.build();

        //### notificationManager.notify(1, notification); //换成 startForeground(1, notification);
        /**
         *  启用前台服务  让 app kill后服务还能继续运行
         */
        startForeground(1, notification);
    }

    @Override
    public void playSong() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }
        KLog.d("playSong");
        try {
            // 把各项参数恢复到初始状态
            mMediaPlayer.reset();
            KLog.d("mUrl:" + mUrl);
            ///mUrl="http://ws.stream.qqmusic.qq.com/104897799.m4a?fromtag=46";
            //设置地址
            //## mMediaPlayer.setDataSource(getApplication(), Uri.parse(mUrl));
            mMediaPlayer.setDataSource(mUrl);
            // 进行缓冲
            //### mMediaPlayer.prepare();
            mMediaPlayer.prepareAsync();//回调
            //isPlaying = true;
            // isPlaying = true;//其实此时未真正播放  回调中才是

        } catch (Exception e) {
            Log.e("Ledou", "MusicService play:" + e.getMessage());
        }

    }

    @Override
    public void pauseSong() {
        KLog.d("pauseSong");
        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            //  mCurrentPosition = mMediaPlayer.getCurrentPosition();
            mMediaPlayer.pause();
            //  isPlaying = false;
        }
    }

    @Override
    public void resumePlay() {
        KLog.d("resumePlay");
        if (mMediaPlayer != null) {
            mMediaPlayer.start();
        }
        // Log.e("Ledou", MUSIC_CURRENTTIME + "5: " + currentTime);
        //  if (mCurrentPosition > 0) {
        // mMediaPlayer.seekTo(mCurrentPosition);
        // }
        //isPlaying = true;
    }

    @Override
    public void stopPlay() {
        KLog.d("stopPlay");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            //isPlaying = false;
        }
    }

    @Override
    public void nextSong() {
        if (mCycleModel == CycleModel.ALL) {
            mMusicNowPosition++;
            if (mMusicNowPosition > mMusicBeanList.size() - 1) {
                KLog.d("没有下一曲了。。。回到第一首");
                mMusicNowPosition = 0;
                //musicBeanList_position = mMusicBeanList.size() - 1;
            }
            KLog.d("列表循环：mMusicNowPosition" + mMusicNowPosition);
        } else if (mCycleModel == CycleModel.ONE) {
            mMusicNowPosition++;
            if (mMusicNowPosition > mMusicBeanList.size() - 1) {
                KLog.d("没有下一曲了。。。回到第一首");
                mMusicNowPosition = 0;
                //musicBeanList_position = mMusicBeanList.size() - 1;
            }
            KLog.d("单曲循环：mMusicNowPosition" + mMusicNowPosition);
        } else if (mCycleModel == CycleModel.RANDOM) {
            int randomIndex = getRandomIndex(mMusicBeanList.size() - 1);
            //假如还是当前这个
            if (randomIndex == mMusicNowPosition) {
                mMusicNowPosition = randomIndex + 1;
            } else {
                mMusicNowPosition = randomIndex;
            }
            KLog.d("随机播放：mMusicNowPosition" + mMusicNowPosition);
        }

        refreshMusicAndPlay(false);

    }

    /**
     * 获取随机位置
     *
     * @param end
     * @return
     */
    protected int getRandomIndex(int end) {
        int index = (int) (Math.random() * end);//Math.random()*2+1，设置一个随机1到3(取不到3)的变量
        return index;
    }

    @Override
    public void preSong() {
        if (mCycleModel == CycleModel.ALL) {
            mMusicNowPosition--;
            if (mMusicNowPosition < 0) {
                // musicBeanList_position = 0;
                KLog.d("没有上一曲了。。。回到最后一首");
                mMusicNowPosition = mMusicBeanList.size() - 1;
            }
            KLog.d("列表循环：mMusicNowPosition" + mMusicNowPosition);
        } else if (mCycleModel == CycleModel.ONE) {
            mMusicNowPosition--;
            if (mMusicNowPosition < 0) {
                // musicBeanList_position = 0;
                KLog.d("没有上一曲了。。。回到最后一首");
                mMusicNowPosition = mMusicBeanList.size() - 1;
            }
            KLog.d("单曲循环：mMusicNowPosition" + mMusicNowPosition);
        } else if (mCycleModel == CycleModel.RANDOM) {
            int randomIndex = getRandomIndex(mMusicBeanList.size() - 1);
            //假如还是当前这个
            if (randomIndex == mMusicNowPosition) {
                mMusicNowPosition = randomIndex + 1;
            } else {
                mMusicNowPosition = randomIndex;
            }
            KLog.d("随机播放：mMusicNowPosition" + mMusicNowPosition);
        }


        refreshMusicAndPlay(false);

    }

    public void close() {
        this.stopSelf();
    }

    public void changeProgress(int songProgressInSeconds) {
        //  mCurrentPosition=songProgressInSeconds;
        if (songProgressInSeconds > 0) {
            int milliseconds = songProgressInSeconds * 1000;
            mMediaPlayer.seekTo(milliseconds);
        }

    }

    public class MusicServiceBinder extends Binder {

        /**
         * 获取当前Service的实例
         *
         * @return
         */
        public MusicServiceBinder getNowService() {
            KLog.d("getNowService");
            return MusicServiceBinder.this;
        }

    }

    @Override
    public void onDestroy() {
        KLog.d("onDestroy");
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        /**
         *
         */
        unregisterReceiver(mMusicServiceBroadcastReceiver);
        stopForeground(true);//取消前台服务
        super.onDestroy();
    }


    private class MusicServiceBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            KLog.d("action:" + action);
            switch (action) {
                case MUSIC_SERVICE_ACTION_START:
                /*    MusicBean musicBean = mMusicBeanList.get(musicBeanList_position);
                    mUrl = musicBean.getUrl();
                    playSong();*/
                    break;
                case MUSIC_SERVICE_ACTION_PAUSE:
                    pauseOperation();
                    break;
                case MUSIC_SERVICE_ACTION_RESUME:
                    resumeOperation();
                    break;
                case MUSIC_SERVICE_ACTION_STOP:
                    stopPlay();
                    break;
                case MUSIC_SERVICE_ACTION_NEXT:
                    nextSong();
                    //
                    changeNotificationToPause();//此处这样用  内部有重复调用 待优化
                    //通知aty下一曲
                    Intent intentToAty_next = new Intent(MusicPlayerActivity.MUSIC_Player_ACTION_NEXT);
                    sendBroadcast(intentToAty_next);
                    break;
                case MUSIC_SERVICE_ACTION_PRE:
                    preSong();
                    //
                    changeNotificationToPause();//此处这样用  内部有重复调用 待优化
                    //通知aty上一曲
                    Intent intentToAty_pre = new Intent(MusicPlayerActivity.MUSIC_Player_ACTION_PRE);
                    sendBroadcast(intentToAty_pre);
                    break;
                case MUSIC_SERVICE_ACTION_CLOSE:
                    //通知aty
                    Intent intentToAty_close = new Intent(MusicPlayerActivity.MUSIC_Player_ACTION_CLOSE);
                    sendBroadcast(intentToAty_close);
                    /**
                     *
                     */
                    close();
                    break;
                case MUSIC_SERVICE_ACTION_CHANGE_PRO:
                    int songProgressInSeconds = intent.getIntExtra("songProgressInSeconds", 0);
                    changeProgress(songProgressInSeconds);
                    break;
                case MUSIC_SERVICE_ACTION_MODEL:
                    String model = intent.getStringExtra("musicModel");
                    switch (model) {
                        case "all":
                            Toast.makeText(getApplicationContext(), "列表循环", Toast.LENGTH_SHORT).show();
                            mCycleModel = CycleModel.ALL;
                            break;
                        case "one":
                            Toast.makeText(getApplicationContext(), "单曲循环", Toast.LENGTH_SHORT).show();
                            mCycleModel = CycleModel.ONE;
                            break;
                        case "ram":
                            Toast.makeText(getApplicationContext(), "随机播放", Toast.LENGTH_SHORT).show();
                            mCycleModel = CycleModel.RANDOM;
                            break;
                    }
                    //
                    break;
                case MUSIC_SERVICE_ACTION_CHANGE_VOLUME:
                    lowerVolume();
                    //
                    break;
                ///

            }
        }
    }

    private void resumeOperation() {
        resumePlay();
        //
        changeNotificationToPause();
        //通知aty恢复
        Intent intentToAty_resume = new Intent(MusicPlayerActivity.MUSIC_Player_ACTION_RESUME);
        sendBroadcast(intentToAty_resume);

        /**
         * 请求AudioFocus
         */
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

    }

    private void pauseOperation() {
        pauseSong();
        //
        changeNotificationToResume();
        //通知aty暂停
        Intent intentToAty_pause = new Intent(MusicPlayerActivity.MUSIC_Player_ACTION_PAUSE);
        sendBroadcast(intentToAty_pause);

        /**
         *归还AudioFocus
         */
        mAudioManager.abandonAudioFocus(this);
    }

    /**
     * 通知改成恢复播放按钮
     */
    private void changeNotificationToResume() {

        Intent intent_cont_resume = new Intent();
        intent_cont_resume.setAction(MUSIC_SERVICE_ACTION_RESUME);
        PendingIntent pi4Click2Broadcast_cont_resume = PendingIntent.getBroadcast(KooApplication.getAppContext(), 0, intent_cont_resume, PendingIntent.FLAG_UPDATE_CURRENT);
        mContentView.setOnClickPendingIntent(R.id.notification_music_Control, pi4Click2Broadcast_cont_resume);
        mContentView.setImageViewResource(R.id.notification_music_Control, R.drawable.selector_music_icon_resume);
        //
        refreshNotification();
    }

    /**
     * 通知改成恢复暂停按钮
     */
    private void changeNotificationToPause() {
        Intent intent_cont_pause = new Intent();
        intent_cont_pause.setAction(MUSIC_SERVICE_ACTION_PAUSE);
        PendingIntent pi4Click2Broadcast_cont_pause = PendingIntent.getBroadcast(KooApplication.getAppContext(), 0, intent_cont_pause, PendingIntent.FLAG_UPDATE_CURRENT);
        mContentView.setOnClickPendingIntent(R.id.notification_music_Control, pi4Click2Broadcast_cont_pause);
        mContentView.setImageViewResource(R.id.notification_music_Control, R.drawable.selector_music_icon_pause);
        //
        refreshNotification();
    }


    /**
     * MediaPlayer 没有进度监听器 替代
     * 存放运行状态
     */
    private Handler handler = new Handler();
    private Runnable taskRunnable = new Runnable() {
        public void run() {
            //需要执行的代码
            InfoHolderSingleton.getInstance().putMapObj("currentPosition", mMediaPlayer.getCurrentPosition());
            InfoHolderSingleton.getInstance().putMapObj("isPlaying", mMediaPlayer.isPlaying());
            handler.postDelayed(taskRunnable, 500);//设置延迟时间  500毫秒
            //### handler.post(taskRunnable);
            Intent intentToAty_changeTime = new Intent(MusicPlayerActivity.MUSIC_PLAYER_ACTION_CHANGE_TIME);
            intentToAty_changeTime.putExtra("time",mMediaPlayer.getCurrentPosition());
            intentToAty_changeTime.putExtra("nowSongAllTime",mNowSongAllTime);//当前播放歌曲时长
            sendBroadcast(intentToAty_changeTime);
        }
    };

}
