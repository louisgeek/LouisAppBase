package com.louisgeek.louisappbase;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.louisgeek.checkappupdatelib.helper.CheckUpdateHelper;
import com.louisgeek.checkappupdatelib.tool.SimpleCheckUpdateTool;
import com.louisgeek.louisappbase.custom.KooLrcView;
import com.louisgeek.louisappbase.data.Pgyer;
import com.louisgeek.louisappbase.imagepicker.ImageListActivity;
import com.louisgeek.louisappbase.imagepicker.ImagePickerActivity;
import com.louisgeek.louisappbase.musicplayer.MusicPlayListActivity;
import com.louisgeek.louisappbase.testui.NormalActivity;
import com.louisgeek.louisappbase.testui.SimpleActivity;
import com.louisgeek.louisappbase.webviews.WebViewActivity;
import com.louisgeek.louisappbase.webviews.X5WebViewActivity;

public class StartActivity extends AppCompatActivity {

 /*   Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            // handler自带方法实现定时器
            try {
                long duation = 1 * 5000;
               // mKooLrcView.nextLine(duation);
                mKooLrcView.setTimeMillisecond(98000);
              //  handler.postDelayed(this, duation);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };*/
    KooLrcView mKooLrcView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        mKooLrcView = (KooLrcView) findViewById(R.id.id_koolrc);


        //  String lrcStr = RawUtil.getStringFromRawWithRN(mContext, R.raw.wwyxhqc);


        //handler.post(runnable);
       // handler.postDelayed(runnable,5000);






       Button id_simple= (Button) findViewById(R.id.id_simple);

       Button  id_normal= (Button) findViewById(R.id.id_normal);

       Button  id_news= (Button) findViewById(R.id.id_news);


       Button  id_web= (Button) findViewById(R.id.id_web);

        Button  id_x5web= (Button) findViewById(R.id.id_x5web);
        Button  id_music= (Button) findViewById(R.id.id_music);
        Button  id_localimages= (Button) findViewById(R.id.id_localimages);
        Button  id_netImages= (Button) findViewById(R.id.id_netImages);
        Button  id_test= (Button) findViewById(R.id.id_test);



        id_simple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, SimpleActivity.class));
            }
        });

        id_normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, NormalActivity.class));
            }
        });

        id_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, ReaderActivity.class));
            }
        });

        id_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, WebViewActivity.class));
            }
        });
        id_x5web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, X5WebViewActivity.class));
            }
        });
        id_music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(StartActivity.this, MusicPlayListActivity.class);
                startActivity(intent);

            }
        });
        id_localimages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(StartActivity.this, ImagePickerActivity.class);
                startActivity(intent);

            }
        });
        id_netImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(StartActivity.this, ImageListActivity.class);
                startActivity(intent);
            }
        });

        SimpleCheckUpdateTool.updateNormal_HasNoMsg(StartActivity.this, Pgyer.APP_ID,Pgyer.API_KEY);

        id_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleCheckUpdateTool.updateNormal(StartActivity.this,Pgyer.APP_ID,Pgyer.API_KEY);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //System.exit(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CheckUpdateHelper.unregisterCheckUpdate(this);
    }
}
