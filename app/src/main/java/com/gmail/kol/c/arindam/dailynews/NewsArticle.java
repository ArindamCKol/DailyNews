package com.gmail.kol.c.arindam.dailynews;

import android.graphics.Bitmap;
import java.util.List;

public class NewsArticle {
    private String webTitle;
    private String section;
    private String publishDate;
    private List<String> authorNames;
    private String newsUrl;
    private Bitmap newsImage;

    public NewsArticle(String webTitle, String section, String publishDate, List<String> authorNames, String newsUrl, Bitmap newsImage) {
        this.webTitle = webTitle;
        this.section = section;
        this.publishDate = publishDate;
        this.authorNames = authorNames;
        this.newsUrl = newsUrl;
        this.newsImage = newsImage;
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

    public List<String> getAuthorNames() {
        return authorNames;
    }

    public String getNewsUrl() {
        return newsUrl;
    }

    public Bitmap getNewsImage() {
        return newsImage;
    }
}
