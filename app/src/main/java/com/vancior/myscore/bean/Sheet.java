package com.vancior.myscore.bean;

/**
 * Created by H on 2017/2/27.
 */

public class Sheet {

    private String bookmark;
    private String userName;
    private String postTime;
    private String viewNum;
    private String imgUrl;
    private String linkUrl;

    public Sheet(String bookmark, String userName, String linkUrl, String imgUrl) {
        this.bookmark = bookmark;
        this.userName = userName;
//        this.postTime = postTime;
//        this.viewNum = viewNum;
        this.linkUrl =linkUrl;
        this.imgUrl = imgUrl;
    }

    public String getBookmark() {
        return bookmark;
    }

    public String getUserName() {
        return userName;
    }

    public String getPostTime() {
        return postTime;
    }

    public String getViewNum() {
        return viewNum;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getLinkUrl() {
        return linkUrl;
    }
}
