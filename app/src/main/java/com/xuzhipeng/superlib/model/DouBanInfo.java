package com.xuzhipeng.superlib.model;

import java.util.List;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/11
 */

public class DouBanInfo {


    /**
     * rating : {"max":10,"numRaters":28793,"average":"7.2","min":0}
     * subtitle :
     * author : ["[美] 斯宾塞·约翰逊"]
     * pubdate : 2001-9
     * tags : [{"count":4811,"name":"励志","title":"励志"},{"count":3781,"name":"谁动了我的奶酪",
     * "title":"谁动了我的奶酪"},{"count":2558,"name":"管理","title":"管理"},{"count":2165,"name":"寓言",
     * "title":"寓言"},{"count":1975,"name":"哲理","title":"哲理"},{"count":1392,"name":"美国",
     * "title":"美国"},{"count":1120,"name":"外国文学","title":"外国文学"},{"count":843,"name":"经典",
     * "title":"经典"}]
     * origin_title : Who Moved My Cheese?
     * image : https://img3.doubanio.com/mpic/s1035374.jpg
     * binding : 精装
     * translator : ["吴立俊"]
     * catalog : 代序：变化与困惑
     我们多面的人性
     故事背后的故事
     芝加哥的同学聚会
     “谁动了我的奶酪”的故事
     讨论
     作者简介

     * pages : 91
     * images : {"small":"https://img3.doubanio.com/spic/s1035374.jpg",
     * "large":"https://img3.doubanio.com/lpic/s1035374.jpg","medium":"https://img3.doubanio
     * .com/mpic/s1035374.jpg"}
     * alt : https://book.douban.com/subject/1021056/
     * id : 1021056
     * publisher : 中信出版社
     * isbn10 : 7800733661
     * isbn13 : 9787800733666
     * title : 谁动了我的奶酪？
     * url : https://api.douban.com/v2/book/1021056
     * alt_title : Who Moved My Cheese?
     * author_intro : 斯宾塞·约翰逊（Spencer
     * Johnson），医学博士，他是全球知名的思想先锋、演说家和畅销书作家。他的许多观点，使成千上万的人发现了生活中的简单真理，使人们的生活更加健康、更成功、更轻松。
     * summary :
     * 《谁动了我的奶酪？》是个简单的寓言故事，内容充满了人生中有关变化寓意深长的真理。这是个有趣且能启蒙智慧的故事，内容是在描绘四个住在“迷宫”里的人物，他们竭尽所能地在寻找能滋养他们身心、使他们快乐的“奶酪”的过程。
     这四个小人物中，有两只是名叫“嗅嗅”和“匆匆”的老鼠；其他两位则是身体大小和老鼠差不多的小人，名叫“唧唧”和“哼哼”，而且这两个小人的外型与行为和现今的人类差不多。
     这里所谓的“奶酪”是一种比喻，它可以被当成我们生命中最想得到的东西。它可能是一份工作、人际关系、金钱、财产、健康、心灵的宁静。
     书中所谓的“迷宫”，代表的是一个你花费时间与精力追寻你所欲求的东西的地方，它可以是你所服务的机构或你所居住的社区。
     在故事里，这些人物面临突如其来的变化。最后，他们之中有一个成功地对这些变化做出适当的应变，并在迷宫的墙上写下他改变自己的心路历程及从中所得到的经验。
     当你看到那些墙上的标语时，你就能自己找出处理变化的方法，了解了这些方法，你就不会感到太多压力，并且能够在生活中或工作中得到更多的成就感（不管你怎么定义这些成就感和压力）。
     * price : 16.80元
     */

    private RatingBean rating;
    private String pubdate;
    private ImagesBean images;
    private String publisher;
    private String title;
    private String summary;
    private List<String> author;

    public RatingBean getRating() {
        return rating;
    }

    public void setRating(RatingBean rating) {
        this.rating = rating;
    }

    public ImagesBean getImages() {
        return images;
    }

    public void setImages(ImagesBean images) {
        this.images = images;
    }

    public String getPubdate() {
        return pubdate;
    }

    public void setPubdate(String pubdate) {
        this.pubdate = pubdate;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public static class RatingBean {
        /**
         * max : 10
         * numRaters : 28793
         * average : 7.2
         * min : 0
         */

        private int numRaters;
        private String average;

        public int getNumRaters() {
            return numRaters;
        }

        public void setNumRaters(int numRaters) {
            this.numRaters = numRaters;
        }

        public String getAverage() {
            return average;
        }

        public void setAverage(String average) {
            this.average = average;
        }
    }

    public static class ImagesBean {
        /**
         * small : https://img3.doubanio.com/spic/s1035374.jpg
         * large : https://img3.doubanio.com/lpic/s1035374.jpg
         * medium : https://img3.doubanio.com/mpic/s1035374.jpg
         */

        private String large;

        public String getLarge() {
            return large;
        }

        public void setLarge(String large) {
            this.large = large;
        }
    }
}
