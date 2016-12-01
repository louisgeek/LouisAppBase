package com.louisgeek.louisappbase.news.bean;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/8.
 */

public class LocalNews_AreaBean {

    /**
     * totalNum : 34
     * ret_code : 0
     * cityList : [{"areaName":"北京","areaId":"55818af5085b7bc0c73836b4"},{"areaName":"上海","areaId":"55818af5085b7bc0c73836b5"},{"areaName":"天津","areaId":"55818af5085b7bc0c73836b6"},{"areaName":"重庆","areaId":"55818af5085b7bc0c73836b7"},{"areaName":"广东","areaId":"55818af5085b7bc0c73836b8"},{"areaName":"河北","areaId":"55818af5085b7bc0c73836b9"},{"areaName":"辽宁","areaId":"55818af6085b7bc0c73836ba"},{"areaName":"吉林","areaId":"55818af6085b7bc0c73836bb"},{"areaName":"甘肃","areaId":"55818af6085b7bc0c73836bc"},{"areaName":"山西","areaId":"55818af6085b7bc0c73836bd"},{"areaName":"四川","areaId":"55818af6085b7bc0c73836be"},{"areaName":"陕西","areaId":"55818af6085b7bc0c73836bf"},{"areaName":"河南","areaId":"55818af6085b7bc0c73836c0"},{"areaName":"山东","areaId":"55818af6085b7bc0c73836c1"},{"areaName":"湖南","areaId":"55818af6085b7bc0c73836c2"},{"areaName":"湖北","areaId":"55818af6085b7bc0c73836c3"},{"areaName":"江西","areaId":"55818af6085b7bc0c73836c4"},{"areaName":"江苏","areaId":"55818af6085b7bc0c73836c5"},{"areaName":"浙江","areaId":"55818af6085b7bc0c73836c6"},{"areaName":"安徽","areaId":"55818af7085b7bc0c73836c7"},{"areaName":"福建","areaId":"55818af7085b7bc0c73836c8"},{"areaName":"广西","areaId":"55818af7085b7bc0c73836c9"},{"areaName":"贵州","areaId":"55818af7085b7bc0c73836ca"},{"areaName":"香港","areaId":"55818af7085b7bc0c73836cb"},{"areaName":"澳门","areaId":"55818af7085b7bc0c73836cc"},{"areaName":"海南","areaId":"55818af7085b7bc0c73836cd"},{"areaName":"台湾","areaId":"55818af7085b7bc0c73836ce"},{"areaName":"云南","areaId":"55818af7085b7bc0c73836cf"},{"areaName":"内蒙古","areaId":"55818af7085b7bc0c73836d0"},{"areaName":"青海","areaId":"55818af7085b7bc0c73836d1"},{"areaName":"宁夏","areaId":"55818af7085b7bc0c73836d2"},{"areaName":"新疆","areaId":"55818af8085b7bc0c73836d3"},{"areaName":"西藏","areaId":"55818af8085b7bc0c73836d4"},{"areaName":"黑龙江","areaId":"55818af8085b7bc0c73836d5"}]
     */

        private int totalNum;
        private int ret_code;
        /**
         * areaName : 北京
         * areaId : 55818af5085b7bc0c73836b4
         */

        private List<CityListBean> cityList;

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

        public List<CityListBean> getCityList() {
            return cityList;
        }

        public void setCityList(List<CityListBean> cityList) {
            this.cityList = cityList;
        }

        public static class CityListBean {
            private String areaName;
            private String areaId;

            public String getAreaName() {
                return areaName;
            }

            public void setAreaName(String areaName) {
                this.areaName = areaName;
            }

            public String getAreaId() {
                return areaId;
            }

            public void setAreaId(String areaId) {
                this.areaId = areaId;
            }
        }
}
