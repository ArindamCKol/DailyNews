package com.gmail.kol.c.arindam.dailynews;

public class NewsArticle {
    private String webTitle;
    private String section;
    private String publishDate;
    private String authorName;
    private String newsUrl;

    public NewsArticle(String webTitle, String section, String publishDate, String newsUrl) {
        this.webTitle = webTitle;
        this.section = section;
        this.publishDate = publishDate;
        this.authorName = null;
        this.newsUrl = newsUrl;
    }

    public NewsArticle(String webTitle, String section, String publishDate, String authorName, String newsUrl) {
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

    public String getPublishDate() {
        return publishDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getNewsUrl() {
        return newsUrl;
    }
}
