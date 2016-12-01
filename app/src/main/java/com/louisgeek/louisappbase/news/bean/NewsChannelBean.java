package com.louisgeek.louisappbase.news.bean;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/5.
 */

public class NewsChannelBean {
        private int totalNum;
        private int ret_code;
        /**
         * channelId : 5572a108b3cdc86cf39001cd
         * name : 国内焦点
         */

        private List<ChannelListBean> channelList;

        public int getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(int totalNum) {
            this.totalNum = totalNum;
        }

        public int getRet_code() {
            return ret_code;
        }

        public void setRet_code(int ret_code) {
            this.ret_code = ret_code;
        }

        public List<ChannelListBean> getChannelList() {
            return channelList;
        }

        public void setChannelList(List<ChannelListBean> channelList) {
            this.channelList = channelList;
        }

        public static class ChannelListBean {
            private String channelId;
            private String name;

            public String getChannelId() {
                return channelId;
            }

            public void setChannelId(String channelId) {
                this.channelId = channelId;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
}
