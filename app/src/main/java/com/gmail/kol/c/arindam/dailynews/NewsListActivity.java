package com.gmail.kol.c.arindam.dailynews;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class NewsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {
    public static final String LOG_TAG = NewsListActivity.class.getName();

    private static final String URL_REQUEST =
            "https://content.guardianapis.com/search?&format=json&from-date=2018-09-28&show-tags=contributor&show-fields=headline,thumbnail,short-url&page-size=20&api-key=f964ed76-ea7b-4a82-be6c-ad0ec94ceab3";

    private NewsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        ListView newsListView = findViewById(R.id.news_list);

        adapter = new NewsListAdapter(this, new ArrayList<NewsArticle>());

        newsListView.setAdapter(adapter);

        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this,URL_REQUEST);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticleList) {
        adapter.addAll(newsArticleList);
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        adapter.clear();
    }
}
