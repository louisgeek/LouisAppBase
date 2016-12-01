package com.louisgeek.louisappbase.jokes.bean;

import java.util.List;

/**
 * Created by louisgeek on 2016/11/6.
 */

public class JokeImageAndTextListBean{


    /**
     * allPages : 545
     * ret_code : 0
     * contentlist : [{"id":"581c55416e36d227049bc184","title":"<b>这是一种按摩方式吗","img":"http://www.zbjuran.com/uploads/allimg/161104/1F32A929-0.gif","type":3,"ct":"2016-11-04 17:30:41.686"},{"id":"581c55416e36d227049bc180","title":"猪一样的队友，迷一样的操作","img":"http://www.zbjuran.com/uploads/allimg/161104/10-1611041F02Y47.gif","type":3,"ct":"2016-11-04 17:30:41.662"},{"id":"581c4e866e36d227049bb07a","title":"嘘~砰~","img":"http://www.zbjuran.com/uploads/allimg/161104/1AH51045-0.gif","type":3,"ct":"2016-11-04 17:01:58.811"},{"id":"581c320d6e36d227049b601a","title":"吓死老子了！！！","img":"http://www.zbjuran.com/uploads/allimg/161104/10-161104142424619.gif","type":3,"ct":"2016-11-04 15:00:29.016"},{"id":"581c320c6e36d227049b6017","title":"喝醉就别踢球了","img":"http://www.zbjuran.com/uploads/allimg/161104/10-161104144003H4.gif","type":3,"ct":"2016-11-04 15:00:28.291"},{"id":"581c2b1f6e36d227049b4239","title":"偷偷给你来一刀","img":"http://www.zbjuran.com/uploads/allimg/161104/10-161104141911162.gif","type":3,"ct":"2016-11-04 14:30:55.621"},{"id":"581c2b1f6e36d227049b4238","title":"吓死老子了！！！","img":"http://www.zbjuran.com/uploads/allimg/161104/10-161104142424619.gif","type":3,"ct":"2016-11-04 14:30:55.621"},{"id":"581c07db6e36d227049ac834","title":"这智商也是告别打牌了","img":"http://www.zbjuran.com/uploads/allimg/161104/10-161104115010955.gif","type":3,"ct":"2016-11-04 12:00:27.813"},{"id":"581c01866e36d227049aabd0","title":"少侠好功夫","img":"http://www.zbjuran.com/uploads/allimg/161104/10-161104111321Y7.gif","type":3,"ct":"2016-11-04 11:33:26.181"},{"id":"581bf9d66e36d227049a95dd","title":"我就想问清洁工还招人吗","img":"http://www.zbjuran.com/uploads/allimg/161104/10-161104103035455.gif","type":3,"ct":"2016-11-04 11:00:38.146"},{"id":"581bf9d66e36d227049a95dc","title":"风吹啊吹啊","img":"http://www.zbjuran.com/uploads/allimg/161104/10-161104103J6264.gif","type":3,"ct":"2016-11-04 11:00:38.145"},{"id":"581bf9d66e36d227049a95db","title":"就喜欢这么毫不做作的！","img":"http://www.zbjuran.com/uploads/allimg/161104/10-16110410422L64.gif","type":3,"ct":"2016-11-04 11:00:38.145"},{"id":"581bf2d66e36d227049a76c3","title":"让你骚让你浪","img":"http://www.zbjuran.com/uploads/allimg/161104/10-161104100JO57.gif","type":3,"ct":"2016-11-04 10:30:46.924"},{"id":"581bf2d56e36d227049a76bc","title":"猝不及防","img":"http://www.zbjuran.com/uploads/allimg/161104/10-161104095K5N4.gif","type":3,"ct":"2016-11-04 10:30:45.710"},{"id":"581b0b916e36d22704971f3e","title":"果然是亲生的！！！！","img":"http://www.zbjuran.com/uploads/allimg/161103/10-1611031IQAC.gif","type":3,"ct":"2016-11-03 18:04:01.966"},{"id":"581ad9e26e36d2270496707d","title":"这次真的是猫先动手的！","img":"http://www.zbjuran.com/uploads/allimg/161103/10-1611031409323b.gif","type":3,"ct":"2016-11-03 14:32:02.778"},{"id":"581ad9e26e36d2270496707c","title":"吃货看到食物的反应","img":"http://www.zbjuran.com/uploads/allimg/161103/10-161103140501337.gif","type":3,"ct":"2016-11-03 14:32:02.775"},{"id":"581ad2936e36d22704965bb5","title":"频临崩溃的孩子","img":"http://www.zbjuran.com/uploads/allimg/161103/10-16110313401T17.gif","type":3,"ct":"2016-11-03 14:00:51.036"},{"id":"581ad2936e36d22704965bb3","title":"绿灯：别说我没给你机会","img":"http://www.zbjuran.com/uploads/allimg/161103/10-161103134K0417.gif","type":3,"ct":"2016-11-03 14:00:51.035"},{"id":"581ab6df6e36d2270495f5af","title":"吓死宝宝了","img":"http://www.zbjuran.com/uploads/allimg/161103/10-161103114140449.gif","type":3,"ct":"2016-11-03 12:02:39.875"}]
     * currentPage : 1
     * allNum : 10882
     * maxResult : 20
     */

    private int allPages;
    private int ret_code;
    private int currentPage;
    private int allNum;
    private int maxResult;
    /**
     * id : 581c55416e36d227049bc184
     * title : <b>这是一种按摩方式吗
     * img : http://www.zbjuran.com/uploads/allimg/161104/1F32A929-0.gif
     * type : 3
     * ct : 2016-11-04 17:30:41.686
     */

    private List<ContentlistBean> contentlist;

    public int getAllPages() {
        return allPages;
    }

    public void setAllPages(int allPages) {
        this.allPages = allPages;
    }

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getAllNum() {
        return allNum;
    }

    public void setAllNum(int allNum) {
        this.allNum = allNum;
    }

    public int getMaxResult() {
        return maxResult;
    }

    public void setMaxResult(int maxResult) {
        this.maxResult = maxResult;
    }

    public List<ContentlistBean> getContentlist() {
        return contentlist;
    }

    public void setContentlist(List<ContentlistBean> contentlist) {
        this.contentlist = contentlist;
    }

    public static class ContentlistBean {
        private String id;
        private String title;
        private String img;
        private int type;
        private String ct;

        //2016-11-7 10:53:03兼容文本的
        private String text;
        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
        //兼容文本的


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getCt() {
            return ct;
        }

        public void setCt(String ct) {
            this.ct = ct;
        }
    }
}
