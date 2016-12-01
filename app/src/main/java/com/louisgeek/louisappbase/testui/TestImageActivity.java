package com.louisgeek.louisappbase.testui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.louisgeek.louisappbase.R;
import com.louisgeek.louisappbase.adapter.KooHFImagePickerRecyclerAdapter;
import com.louisgeek.louisappbase.imagepicker.ImagePickerHelper;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TestImageActivity extends AppCompatActivity {
    List<String> sL=new ArrayList<>();

    KooHFImagePickerRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_image);
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.id_rv);

        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        adapter=
                new KooHFImagePickerRecyclerAdapter(sL,null,recyclerView,R.layout.item_small_image);
        recyclerView.setAdapter(adapter);



        ImagePickerHelper.scanLocalImage(this, new ImagePickerHelper.ScanLocalImagesCallBack() {
            @Override
            public void onSuccess(Map<String, List<String>> groupMap) {
                KLog.d(groupMap.size());
                List<String> sssslll=new ArrayList<>();
                for (String kkkk:groupMap.keySet()) {
                    List<String> vvvv=groupMap.get(kkkk);
                    for (int i = 0; i < vvvv.size(); i++) {
                        sssslll.add(kkkk+"/"+vvvv.get(i));
                    }

                }
                KLog.d(sssslll.size());
                adapter.refreshDataList(sssslll);
            }

            @Override
            public void onError(String message) {

            }
        });
    }
}
