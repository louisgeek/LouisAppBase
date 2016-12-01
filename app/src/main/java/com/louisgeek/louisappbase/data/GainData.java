package com.louisgeek.louisappbase.data;

import com.louisgeek.louisappbase.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by louisgeek on 2016/11/3.
 */

public class GainData {

    public static void gainListPageData(final String dataType, final int pageNum_index, final int pageSize, final PageDataCallBack pageDataCallBack){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pageDataCallBack.startLoadData();
                    }
                });
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                final List<String> strings=new ArrayList<>();
                for (int i = pageNum_index*pageSize; i < ((pageNum_index+1)*pageSize); i++) {
                    strings.add("DATA=="+dataType+"=>:"+i);
                }
                ThreadUtil.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pageDataCallBack.backData(strings);
                    }
                });
            }
        }).start();
    }

    public  interface  PageDataCallBack{
        void  backData(List<String> strings);
        void  startLoadData();
    }

}
