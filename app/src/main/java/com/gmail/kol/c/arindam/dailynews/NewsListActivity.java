package com.gmail.kol.c.arindam.dailynews;

import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;

public class NewsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {
    public static final String LOG_TAG = NewsListActivity.class.getName();

    private String API_key = "api-key=f964ed76-ea7b-4a82-be6c-ad0ec94ceab3";
    private String URL_Main_Request = "https://content.guardianapis.com/search?&format=json";
    private String URL_Query = "use-date=published&order-by=oldest&show-tags=contributor&show-fields=thumbnail&page-size=20";
    private String urlRequest;
    private NewsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        urlRequest = getURLRequest();
        ListView newsListView = findViewById(R.id.news_list);

        adapter = new NewsListAdapter(this, new ArrayList<NewsArticle>());

        newsListView.setAdapter(adapter);

        getLoaderManager().initLoader(1, null, this);
    }

    private String getURLRequest() {
        StringBuilder urlBilder = new StringBuilder(URL_Main_Request);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        urlBilder.append("&from-date=" + currentDate + '&');
        urlBilder.append(URL_Query + "&");
        urlBilder.append("page=" + 3 + "&");
        urlBilder.append(API_key);
        return urlBilder.toString();
    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this,urlRequest);
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
