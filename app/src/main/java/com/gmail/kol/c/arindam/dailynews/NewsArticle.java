package com.gmail.kol.c.arindam.dailynews;

public class NewsArticle {
    private String webTitle;
    private String section;
    private long publishDate;
    private String authorName;
    private String newsUrl;

    public NewsArticle(String webTitle, String section) {
        this.webTitle = webTitle;
        this.section = section;
        this.publishDate = 0;
        this.authorName = null;
        this.newsUrl = null;
    }

    public NewsArticle(String webTitle, String section, long publishDate, String authorName, String newsUrl) {
        this.webTitle = webTitle;
        this.section = section;
        this.publishDate = publishDate;
        this.authorName = authorName;
        this.newsUrl = newsUrl;
    }

    public String getWebTitle() {
        return webTitle;
    }

    public String getSection() {
        return section;
    }

    public long getPublishDate() {
        return publishDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getNewsUrl() {
        return newsUrl;
    }
}
