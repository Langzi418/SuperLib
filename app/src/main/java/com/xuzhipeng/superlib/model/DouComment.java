package com.xuzhipeng.superlib.model;

/**
 * Author: xuzhipeng
 * Email: langzi0418@gmail.com
 * Date: 2017/7/13
 */

public class DouComment {

    /**
     * rating : {"max":5,"value":"1","min":0}
     * votes : 261
     * author : {"name":"王少磊","is_banned":false,"is_suicide":false,"url":"https://api.douban
     * .com/v2/user/1071164","avatar":"https://img3.doubanio.com/icon/u1071164-1.jpg",
     * "uid":"1071164","alt":"https://www.douban.com/people/1071164/","type":"user",
     * "id":"1071164","large_avatar":"https://img3.doubanio.com/icon/up1071164-1.jpg"}
     * title : 我能动谁的窝头
     * updated : 2017-06-07 09:34:28
     * comments : 104
     * summary : 我选择心绪最佳的时候，努力用不带偏见的目光，来捧读这本传说中的小书。多年修炼之后，我能够仅凭标题、装帧、宣传口号、或者上柜的位置……断定其内容定...
     * useless : 83
     * published : 2007-08-08 15:10:13
     * alt : https://book.douban.com/review/1190701/
     * id : 1190701
     */

    private String title;
    private String updated;
    private String summary;
    private String alt;

    public String getTitle() {
        return title;
    }
    public String getUpdated() {
        return updated;
    }
    public String getSummary() {
        return summary;
    }
    public String getAlt() {
        return alt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public void setAlt(String alt) {
        this.alt = alt;
    }
}
