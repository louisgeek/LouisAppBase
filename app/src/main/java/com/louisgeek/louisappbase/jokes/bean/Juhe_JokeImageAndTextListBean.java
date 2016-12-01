package com.louisgeek.louisappbase.jokes.bean;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/11.
 */

public class Juhe_JokeImageAndTextListBean {
        /**
         * content : 二汪
         * hashId : 13AF55EB201FADD4DB8AD3C0FC053E72
         * unixtime : 1418954054
         * updatetime : 2014-12-19 09:54:14
         * url : http://img.juhe.cn/joke/201412/19/13AF55EB201FADD4DB8AD3C0FC053E72.gif
         */

        private List<DataBean> data;

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            private String content;
            private String hashId;
            private int unixtime;
            private String updatetime;
            private String url;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getHashId() {
                return hashId;
            }

            public void setHashId(String hashId) {
                this.hashId = hashId;
            }

            public int getUnixtime() {
                return unixtime;
            }

            public void setUnixtime(int unixtime) {
                this.unixtime = unixtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
}
