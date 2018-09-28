package com.gmail.kol.c.arindam.dailynews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader <List<NewsArticle>> {
    private String guardianUrl;

    public NewsLoader(Context context, String guardianUrl) {
        super(context);
        this.guardianUrl = guardianUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<NewsArticle> loadInBackground() {
        if (guardianUrl == null) {
            return  null;
        }

        List<NewsArticle> newsArticleList = Utils.getNewsArticleData(guardianUrl);
        return newsArticleList;
    }
}
