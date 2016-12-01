package com.louisgeek.louisappbase.musicplayer;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.custom.KooLrcView;
import com.louisgeek.louisappbase.musicplayer.bean.MusicBean;
import com.louisgeek.louisappbase.util.AppTool;
import com.louisgeek.louisappbase.util.InfoHolderSingleton;
import com.louisgeek.louisappbase.util.ServiceUtil;
import com.socks.library.KLog;

import java.util.List;

import co.mobiwise.library.MaskProgressView;


public class MusicPlayerActivity extends AppCompatActivity implements View.OnClickListener {

    private MusicBean mMusicBean;
    private MaskProgressView id_maskProgressView;
    private ImageView id_iv_album;
    private TextView id_textName;
    private TextView id_textSinger;
    private List<MusicBean> mMusicBeanList;
    private ImageView id_iv_Control;
    private ImageView id_iv_Next;
    private ImageView id_iv_Previous;
    private ImageView id_iv_model;
    private ImageView id_iv_yl;
    private int currentPosition = 0;
    private boolean isServicePlaying = true;//因为启动就播放  所以默认true
    //一系列动作
    @Deprecated
    public static final String MUSIC_Player_ACTION_START = "Player_ACT_START";
    public static final String MUSIC_Player_ACTION_PAUSE = "Player_ACT_PAUSE";
    public static final String MUSIC_Player_ACTION_RESUME = "Player_ACT_RESUME";
    @Deprecated
    public static final String MUSIC_Player_ACTION_STOP = "Player_ACT_STOP";
    public static final String MUSIC_Player_ACTION_NEXT = "Player_ACT_NEXT_SONG";
    public static final String MUSIC_Player_ACTION_PRE = "Player_ACT_PRE_SONG";
    public static final String MUSIC_Player_ACTION_CLOSE = "Player_ACT_CLOSE";
    public static final String MUSIC_PLAYER_ACTION_CHANGE_TIME = "Player_ACT_CHANGE_TIME";
    @Deprecated
    public static final String MUSIC_Player_ACTION_CHANGE_PRO = "Player_ACT_CHANGE_PRO";
    public static final String[] mPlayerActionArr = new String[]{
            MUSIC_Player_ACTION_PAUSE,
            MUSIC_Player_ACTION_START,
            MUSIC_Player_ACTION_RESUME,
            MUSIC_Player_ACTION_STOP,
            MUSIC_Player_ACTION_NEXT,
            MUSIC_Player_ACTION_PRE,
            MUSIC_Player_ACTION_CLOSE,
            MUSIC_Player_ACTION_CHANGE_PRO,
            MUSIC_PLAYER_ACTION_CHANGE_TIME
    };
    private MyMusicPlayerAtyBroadcastReceiver mMyMusicPlayerAtyBroadcastReceiver;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            KLog.d("onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            KLog.d("onServiceDisconnected");
        }
    };
    KooLrcView mKooLrcView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        boolean needRestartMusicService = getIntent().getBooleanExtra("needRestartMusicService", false);
        /**
         * 取得进度
         */
        if (InfoHolderSingleton.getInstance().getMapObj("currentPosition") != null) {
            currentPosition = (int) InfoHolderSingleton.getInstance().getMapObj("currentPosition");
        }
        /**
         * 取得播放状态
         */
        if (InfoHolderSingleton.getInstance().getMapObj("isPlaying") != null) {
            isServicePlaying = (boolean) InfoHolderSingleton.getInstance().getMapObj("isPlaying");
        }

        KLog.d("onCreate currentPosition:" + currentPosition);
        KLog.d("onCreate:isServicePlaying:" + isServicePlaying);

        int musicNowPosition = (int) InfoHolderSingleton.getInstance().getMapObj("musicNowPosition");
        mMusicBeanList = (List<MusicBean>) InfoHolderSingleton.getInstance().getMapObj("musicBeanList");


        final Intent intent = new Intent(MusicPlayerActivity.this, MusicService.class);
        // intent.putExtra("url", "http://ws.stream.qqmusic.qq.com/104897799.m4a?fromtag=46");
        //intent.putExtra("musicNowPosition",musicNowPosition);
        /// bindService(intent,mServiceConnection,BIND_AUTO_CREATE);

        /**
         * 启动的时候
         */
        String appPack = AppTool.getPackageName(this);
        // if (!ServiceUtil.isServiceRunning("com.louisgeek.louisappbase.musicplayer.MusicService")) {
        /**
         * 如果服务运行
         */
        if (ServiceUtil.isServiceRunning(appPack + ".musicplayer.MusicService") && !needRestartMusicService) {
            KLog.d("isServiceRunning needRestartMusicService:" + needRestartMusicService);
        } else {
            KLog.d("startService");
            startService(intent);
        }
        /**
         *
         */
        mMusicBean = mMusicBeanList.get(musicNowPosition);


        id_iv_Control = (ImageView) findViewById(R.id.id_iv_Control);
        id_iv_Control.setOnClickListener(this);
        id_iv_Next = (ImageView) findViewById(R.id.id_iv_Next);
        id_iv_Next.setOnClickListener(this);
        id_iv_Previous = (ImageView) findViewById(R.id.id_iv_Previous);
        id_iv_Previous.setOnClickListener(this);
        id_iv_model = (ImageView) findViewById(R.id.id_iv_model);
        id_iv_model.setOnClickListener(this);
        id_iv_yl = (ImageView) findViewById(R.id.id_iv_yl);
        id_iv_yl.setOnClickListener(this);

        /**
         *
         *
         */
        mKooLrcView = (KooLrcView) findViewById(R.id.id_klv);


        /**
         * 控制音量
         */
        SeekBar id_sb_vol = (SeekBar) findViewById(R.id.id_sb_vol);
        final AudioManager audiomanage = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC);  //获取系统最大音量
        id_sb_vol.setMax(maxVolume);   //拖动条最高值与系统最大声匹配
        int currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC);  //获取当前值
        id_sb_vol.setProgress(currentVolume);
        id_sb_vol.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC,progress,
                        AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        /**
         *专辑ICON
         */
        id_iv_album = (ImageView) findViewById(R.id.id_iv_album);
        Bitmap bitmap_album = MusicPlayerHelper.getAlbumArtBitmap(this, mMusicBean.getAlbumid());
        if (bitmap_album == null) {
            bitmap_album = AppTool.getIconBitmap(this);
        }
        id_iv_album.setImageBitmap(bitmap_album);

        /**
         *
         */
        id_textName = (TextView) findViewById(R.id.id_textName);
        id_textName.setText(mMusicBean.getName());
        id_textSinger = (TextView) findViewById(R.id.id_textSinger);
        id_textSinger.setText(mMusicBean.getArtist() + "-" + mMusicBean.getAlbumName());

        /**
         *
         */
        //id_maskProgressView = (MaskProgressView) findViewById(R.id.id_maskProgressView);
       // id_maskProgressView.setCoverImage(bitmap_album);
        int time = (int) (mMusicBean.getTime() / 1000);
       // id_maskProgressView.setmMaxSeconds(time);
       // id_maskProgressView.setmCurrentSeconds(currentPosition / 1000);
     /*   id_maskProgressView.setOnProgressDraggedListener(new OnProgressDraggedListener() {
            @Override
            public void onProgressDragged(int i) {
                KLog.d("onProgressDragged:" + i);
                Intent intent_change_progress = new Intent();
                intent_change_progress.setAction(MusicService.MUSIC_SERVICE_ACTION_CHANGE_PRO);
                intent_change_progress.putExtra("songProgressInSeconds", i);
                sendBroadcast(intent_change_progress);
            }

            @Override
            public void onProgressDragging(int i) {
                // KLog.d("onProgressDragging:" + i);
            }
        });
        id_maskProgressView.setAnimationCompleteListener(new AnimationCompleteListener() {
            @Override
            public void onAnimationCompleted() {
                KLog.d("onAnimationCompleted");
            }
        });*/
        if (isServicePlaying) {
            changeControlToResume(false);
        } else {
            changeControlToPause(false);
        }


        /**
         *注册广播
         */
        mMyMusicPlayerAtyBroadcastReceiver = new MyMusicPlayerAtyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        for (int i = 0; i < mPlayerActionArr.length; i++) {
            intentFilter.addAction(mPlayerActionArr[i]);
        }
        registerReceiver(mMyMusicPlayerAtyBroadcastReceiver, intentFilter);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        KLog.d("onNewIntent");
        /**
         * 取得通知栏点击时候的进度
         */
        int test = getIntent().getIntExtra("test", 0);
        KLog.d("test:" + test);
    }

    private void loadMusicModel() {
        String test_url = "http://ws.stream.qqmusic.qq.com/104897799.m4a?fromtag=46";
        int test_second = 208;
        String test_music_picture = "http://i.gtimg.cn/music/photo/mid_album_300/2/3/001CqGRZ4cQN23.jpg";
        String test_music_name = "海阔天空 (Live)";
        String test_art_name = "汪小敏";
        String test_art_picture = "http://i.gtimg.cn/music/photo/mid_album_90/2/3/001CqGRZ4cQN23.jpg";

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_iv_Control:
                if (id_iv_Control.getTag(R.id.id_music_pause_image_state_holder) != null &&
                        id_iv_Control.getTag(R.id.id_music_pause_image_state_holder).equals(R.drawable.selector_music_icon_resume)) {

                    String appPack = AppTool.getPackageName(this);
                    //  if (!ServiceUtil.isServiceRunning("com.louisgeek.louisappbase.musicplayer.MusicService")) {
                    if (!ServiceUtil.isServiceRunning(appPack + ".musicplayer.MusicService")) {
                        /**
                         * 如果没有启动service  启动它
                         */
                        Intent intent = new Intent(MusicPlayerActivity.this, MusicService.class);
                        startService(intent);

                        //改变icon
                        changeControlToResume(true);
                    } else {
                        KLog.d("isServiceRunning");
                        //-》恢复
                        Intent intent_cont_resume = new Intent();
                        intent_cont_resume.setAction(MusicService.MUSIC_SERVICE_ACTION_RESUME);
                        sendBroadcast(intent_cont_resume);
                        //改变icon
                        changeControlToResume(false);
                    }


                } else {
                    //-》暂停
                    Intent intent_cont_pause = new Intent();
                    intent_cont_pause.setAction(MusicService.MUSIC_SERVICE_ACTION_PAUSE);
                    sendBroadcast(intent_cont_pause);

                    changeControlToPause(false);
                }

                break;
            case R.id.id_iv_Next:
                Intent intent_next = new Intent();
                intent_next.setAction(MusicService.MUSIC_SERVICE_ACTION_NEXT);
                sendBroadcast(intent_next);
                //不直接调用下一曲
                break;
            case R.id.id_iv_Previous:
                Intent intent_pre = new Intent();
                intent_pre.setAction(MusicService.MUSIC_SERVICE_ACTION_PRE);
                sendBroadcast(intent_pre);
                //不直接调用上一曲
                break;
            case R.id.id_iv_yl:
                Intent intent_volume = new Intent();
                intent_volume.setAction(MusicService.MUSIC_SERVICE_ACTION_CHANGE_VOLUME);
                sendBroadcast(intent_volume);
                break;
            case R.id.id_iv_model:
                Intent intent_model = new Intent();
                intent_model.setAction(MusicService.MUSIC_SERVICE_ACTION_MODEL);

                if (id_iv_model.getTag(R.id.id_music_model_image_state_holder) != null) {
                    ////////
                    if (id_iv_model.getTag(R.id.id_music_model_image_state_holder).equals(R.drawable.ic_cached_black_18dp)) {
                        intent_model.putExtra("musicModel", "all");
                        //设置成全部循环
                        id_iv_model.setImageResource(R.drawable.ic_cached_black_18dp);
                        //下一个单曲循环
                        id_iv_model.setTag(R.id.id_music_model_image_state_holder, R.drawable.ic_repeat_one_black_24dp);
                    } else if (id_iv_model.getTag(R.id.id_music_model_image_state_holder).equals(R.drawable.ic_repeat_one_black_24dp)) {
                        intent_model.putExtra("musicModel", "one");
                        //设置成单曲循环
                        id_iv_model.setImageResource(R.drawable.ic_repeat_one_black_24dp);
                        //下一个随机模式
                        id_iv_model.setTag(R.id.id_music_model_image_state_holder, R.drawable.ic_polymer_black_18dp);
                    } else if (id_iv_model.getTag(R.id.id_music_model_image_state_holder).equals(R.drawable.ic_polymer_black_18dp)) {
                        intent_model.putExtra("musicModel", "ram");
                        //设置成随机模式
                        id_iv_model.setImageResource(R.drawable.ic_polymer_black_18dp);
                        //下一个全部循环
                        id_iv_model.setTag(R.id.id_music_model_image_state_holder, R.drawable.ic_cached_black_18dp);
                    }

                    ////////
                } else {
                    //默认列表循环后的单曲循环
                    /**
                     * 默认是列表CycleModel.ALL
                     * xml 里也是默认  all  android:src="@drawable/ic_cached_black_18dp"
                     * 点击后变成单曲循环
                     */
                    intent_model.putExtra("musicModel", "one");
                    //设置成单曲循环
                    id_iv_model.setImageResource(R.drawable.ic_repeat_one_black_24dp);
                    //下一个随机模式
                    id_iv_model.setTag(R.id.id_music_model_image_state_holder, R.drawable.ic_polymer_black_18dp);
                }
                sendBroadcast(intent_model);
                break;
        }
    }

    private void changeControlToResume(boolean resetProgress) {
        //改成暂停icon
        id_iv_Control.setTag(R.id.id_music_pause_image_state_holder, R.drawable.selector_music_icon_pause);
        id_iv_Control.setImageResource(R.drawable.selector_music_icon_pause);

     /*   if (resetProgress) {
            id_maskProgressView.setmCurrentSeconds(0);
        }
        //-》恢复
        id_maskProgressView.start();*/
    }

    private void changeControlToPause(boolean resetProgress) {
        //改成恢复icon
        id_iv_Control.setTag(R.id.id_music_pause_image_state_holder, R.drawable.selector_music_icon_resume);
        id_iv_Control.setImageResource(R.drawable.selector_music_icon_resume);

     /*   if (resetProgress) {
            id_maskProgressView.setmCurrentSeconds(0);
        }
        //-》暂停
        id_maskProgressView.pause();*/
    }


    private void changeSong() {
        int musicNowPosition = (int) InfoHolderSingleton.getInstance().getMapObj("musicNowPosition");
        mMusicBean = mMusicBeanList.get(musicNowPosition);
        refreshUI();
    }

/*
    private void nextSong(int musicNowPosition) {
        mMusicNowPosition=musicNowPosition;
        mMusicBean = mMusicBeanList.get(musicNowPosition);
        refreshUI();
    }
*/

    private void refreshUI() {
        /**
         *专辑ICON
         */
        Bitmap bitmap_album = MusicPlayerHelper.getAlbumArtBitmap(this, mMusicBean.getAlbumid());
        if (bitmap_album == null) {
            bitmap_album = AppTool.getIconBitmap(this);
        }
        id_iv_album.setImageBitmap(bitmap_album);

        /**
         *
         */
        id_textName.setText(mMusicBean.getName());
        id_textSinger.setText(mMusicBean.getArtist() + "-" + mMusicBean.getAlbumName());

        /**
         *
         */
        /*id_maskProgressView.setCoverImage(bitmap_album);
        int time = (int) (mMusicBean.getTime() / 1000);
        id_maskProgressView.setmMaxSeconds(time);//会重置当前seconds的
        id_maskProgressView.start();*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mMyMusicPlayerAtyBroadcastReceiver);
    }

    private class MyMusicPlayerAtyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            KLog.d("action:" + action);
            switch (action) {
                case MUSIC_Player_ACTION_PAUSE:
                    changeControlToPause(false);
                    break;
                case MUSIC_Player_ACTION_RESUME:
                    changeControlToResume(false);
                    break;
                case MUSIC_Player_ACTION_NEXT:
                    changeSong();
                    changeControlToResume(true);
                    break;
                case MUSIC_Player_ACTION_PRE:
                    changeSong();
                    changeControlToResume(true);
                    break;
                case MUSIC_Player_ACTION_CLOSE:
                    changeControlToPause(true);
                    break;
                case MUSIC_PLAYER_ACTION_CHANGE_TIME:
                   int time=intent.getIntExtra("time",0);
                   long nowSongAllTime=intent.getIntExtra("nowSongAllTime",0);
                    KLog.d("time:"+time);
                   /* KLog.d("nowSongAllTime:"+nowSongAllTime);
                    KLog.d("nowSongAllTime fen:"+nowSongAllTime*1.0f/(1000*60));*/
                    mKooLrcView.setTimeMillisecond(time);
                    break;
            }
        }
    }
}
