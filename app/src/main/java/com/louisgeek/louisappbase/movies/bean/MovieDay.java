package com.louisgeek.louisappbase.movies.bean;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/8.
 */

public class MovieDay {


    /**
     * ret_code : 0
     * datalist : [{"AvgPrice":"42","MovieDay":"5","Rank":"1","WomIndex":"","SumBoxOffice":"34233","BoxOffice_Up":"-80","MovieName":"奇异博士","AvpPeoPle":"2","BoxOffice":"739"},{"AvgPrice":"33","MovieDay":"5","Rank":"10","WomIndex":"","SumBoxOffice":"627","BoxOffice_Up":"-81","MovieName":"非常父子档","AvpPeoPle":"1","BoxOffice":"16"},{"AvgPrice":"32","MovieDay":"1","Rank":"2","WomIndex":"","SumBoxOffice":"130","BoxOffice_Up":"12800","MovieName":"邻家大贱谍","AvpPeoPle":"1","BoxOffice":"129"},{"AvgPrice":"35","MovieDay":"5","Rank":"3","WomIndex":"","SumBoxOffice":"4744","BoxOffice_Up":"-83","MovieName":"捉迷藏","AvpPeoPle":"1","BoxOffice":"116"},{"AvgPrice":"35","MovieDay":"12","Rank":"4","WomIndex":"","SumBoxOffice":"14003","BoxOffice_Up":"-84","MovieName":"驴得水","AvpPeoPle":"1","BoxOffice":"84"},{"AvgPrice":"33","MovieDay":"5","Rank":"5","WomIndex":"","SumBoxOffice":"1556","BoxOffice_Up":"-91","MovieName":"一句顶一万句","AvpPeoPle":"1","BoxOffice":"38"},{"AvgPrice":"31","MovieDay":"5","Rank":"6","WomIndex":"","SumBoxOffice":"1806","BoxOffice_Up":"-77","MovieName":"蜡笔小新：梦境世界大突击","AvpPeoPle":"1","BoxOffice":"33"},{"AvgPrice":"36","MovieDay":"12","Rank":"7","WomIndex":"","SumBoxOffice":"13040","BoxOffice_Up":"-77","MovieName":"但丁密码","AvpPeoPle":"2","BoxOffice":"30"},{"AvgPrice":"36","MovieDay":"19","Rank":"8","WomIndex":"","SumBoxOffice":"33466","BoxOffice_Up":"-83","MovieName":"机械师2：复活","AvpPeoPle":"1","BoxOffice":"28"},{"AvgPrice":"32","MovieDay":"40","Rank":"9","WomIndex":"","SumBoxOffice":"117086","BoxOffice_Up":"-84","MovieName":"湄公河行动","AvpPeoPle":"1","BoxOffice":"27"}]
     * remark : 成功
     */

        private String ret_code;
        private String remark;
        /**
         * AvgPrice : 42
         * MovieDay : 5
         * Rank : 1
         * WomIndex :
         * SumBoxOffice : 34233
         * BoxOffice_Up : -80
         * MovieName : 奇异博士
         * AvpPeoPle : 2
         * BoxOffice : 739
         */

        private List<DatalistBean> datalist;

        public String getRet_code() {
            return ret_code;
        }

        public void setRet_code(String ret_code) {
            this.ret_code = ret_code;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public List<DatalistBean> getDatalist() {
            return datalist;
        }

        public void setDatalist(List<DatalistBean> datalist) {
            this.datalist = datalist;
        }

        public static class DatalistBean {
            private String AvgPrice;
            private String MovieDay;
            private String Rank;
            private String WomIndex;
            private String SumBoxOffice;
            private String BoxOffice_Up;
            private String MovieName;
            private String AvpPeoPle;
            private String BoxOffice;

            public String getAvgPrice() {
                return AvgPrice;
            }

            public void setAvgPrice(String AvgPrice) {
                this.AvgPrice = AvgPrice;
            }

            public String getMovieDay() {
                return MovieDay;
            }

            public void setMovieDay(String MovieDay) {
                this.MovieDay = MovieDay;
            }

            public String getRank() {
                return Rank;
            }

            public void setRank(String Rank) {
                this.Rank = Rank;
            }

            public String getWomIndex() {
                return WomIndex;
            }

            public void setWomIndex(String WomIndex) {
                this.WomIndex = WomIndex;
            }

            public String getSumBoxOffice() {
                return SumBoxOffice;
            }

            public void setSumBoxOffice(String SumBoxOffice) {
                this.SumBoxOffice = SumBoxOffice;
            }

            public String getBoxOffice_Up() {
                return BoxOffice_Up;
            }

            public void setBoxOffice_Up(String BoxOffice_Up) {
                this.BoxOffice_Up = BoxOffice_Up;
            }

            public String getMovieName() {
                return MovieName;
            }

            public void setMovieName(String MovieName) {
                this.MovieName = MovieName;
            }

            public String getAvpPeoPle() {
                return AvpPeoPle;
            }

            public void setAvpPeoPle(String AvpPeoPle) {
                this.AvpPeoPle = AvpPeoPle;
            }

            public String getBoxOffice() {
                return BoxOffice;
            }

            public void setBoxOffice(String BoxOffice) {
                this.BoxOffice = BoxOffice;
            }
        }
}
