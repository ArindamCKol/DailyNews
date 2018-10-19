package com.gmail.kol.c.arindam.dailynews;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.util.TimeZone;

public class NewsListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsArticle>> {
    //string variables for URL
    private String GUARDIAN_URL_REQUEST = "https://content.guardianapis.com/search";

    //to count pages for url query
    private int currentPage = 1;

    //for getting data from setting activity
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.OnSharedPreferenceChangeListener listener;

    //adapter for news list
    private NewsListAdapter adapter;
    private TextView emptyView;
    private View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        //create views and link to resources
        ListView newsListView = findViewById(R.id.news_list);
        emptyView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyView);
        loadingIndicator = findViewById(R.id.loading_indicator);
        LayoutInflater inflater = getLayoutInflater();
        View footer = inflater.inflate(R.layout.footer,null); //inflate footer layout
        newsListView.addFooterView(footer); //add footer to list view
        TextView nextButton = footer.findViewById(R.id.next_button);
        adapter = new NewsListAdapter(this, new ArrayList<NewsArticle>());
        newsListView.setAdapter(adapter);

        //initialise shared preference & change listener
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
                getLoaderManager().restartLoader(1,null,NewsListActivity.this);
            }
        };

        //list item on click open related article in browser
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                NewsArticle currentNewsArticle = adapter.getItem(position);
                Uri newsArticleUri = Uri.parse(currentNewsArticle.getNewsUrl());

                Intent newsIntent = new Intent(Intent.ACTION_VIEW, newsArticleUri);
                startActivity(newsIntent);
            }
        });

        //add on click listener to footer next button & on click go to next page
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int totalPage = adapter.getItem(0).getPages();
                loadingIndicator.setVisibility(View.VISIBLE);
                if (currentPage < totalPage) {
                    currentPage++;
                } else {
                    currentPage = 1;
                }
                getLoaderManager().restartLoader(1,null,NewsListActivity.this);
            }
        });

        //get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        //get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        //if there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            //initialize the loader
            getLoaderManager().initLoader(1,null,this);
        } else {
            //hide loading indicator
            loadingIndicator.setVisibility(View.GONE);

            //show empty text message
            emptyView.setText(R.string.no_network);
        }
    }

    //Get current date in string
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        String currentDate = dateFormat.format(new Date());

        return currentDate;
    }

    @Override
    public Loader<List<NewsArticle>> onCreateLoader(int i, Bundle bundle) {
        //get selection from setting activity
        String orderBy = mSharedPreferences.getString(getString(R.string.order_by_key),getString(R.string.order_by_default));
        String useDate = mSharedPreferences.getString(getString(R.string.use_date_key),getString(R.string.use_date_default));

        //create URI by adding query
        Uri baseUri = Uri.parse(GUARDIAN_URL_REQUEST);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("from-date",getCurrentDate());
        uriBuilder.appendQueryParameter("use-date",useDate);
        uriBuilder.appendQueryParameter("order-by",orderBy);
        uriBuilder.appendQueryParameter("show-tags","contributor");
        uriBuilder.appendQueryParameter("show-fields","thumbnail");
        uriBuilder.appendQueryParameter("page-size","10");
        uriBuilder.appendQueryParameter("page",Integer.toString(currentPage));
        uriBuilder.appendQueryParameter("api-key", BuildConfig.API_KEY);

        return new NewsLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsArticle>> loader, List<NewsArticle> newsArticleList) {
        //hide loading indicator after data loading complete
        loadingIndicator.setVisibility(View.GONE);

        adapter.clear();

        //check that List has items, if not show empty text
        if (newsArticleList == null || newsArticleList.isEmpty()) {
            emptyView.setText(R.string.no_data);
        } else {
            adapter.addAll(newsArticleList);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsArticle>> loader) {
        adapter.clear();
    }

    //creating setting menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //launching setting activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_setting) {
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
            loadingIndicator.setVisibility(View.VISIBLE);
            mSharedPreferences.registerOnSharedPreferenceChangeListener(listener);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
