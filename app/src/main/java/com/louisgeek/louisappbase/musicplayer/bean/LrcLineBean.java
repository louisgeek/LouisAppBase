package com.louisgeek.louisappbase.musicplayer.bean;

/**
 * Created by louisgeek on 2016/11/26.
 */

public class LrcLineBean implements Comparable<LrcLineBean> {
    private String timeStr;
    private long timeMillisecond;

    public String getTimeStr() {
        return timeStr;
    }

    public void setTimeStr(String timeStr) {
        this.timeStr = timeStr;
    }

    public long getTimeMillisecond() {
        return timeMillisecond;
    }

    public void setTimeMillisecond(long timeMillisecond) {
        this.timeMillisecond = timeMillisecond;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;

    @Override
    public int compareTo(LrcLineBean otherLrcLineBean) {
        return (int) (this.getTimeMillisecond() - otherLrcLineBean.getTimeMillisecond());
    }
}
