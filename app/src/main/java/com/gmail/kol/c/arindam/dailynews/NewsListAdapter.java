package com.gmail.kol.c.arindam.dailynews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsListAdapter extends ArrayAdapter <NewsArticle> {
    public NewsListAdapter(@NonNull Context context, @NonNull List<NewsArticle> newsArticleList) {
        super(context, 0, newsArticleList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View newsItemView = convertView;
        if (newsItemView == null) {
            newsItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        TextView sectionTextView = newsItemView.findViewById(R.id.section_text);
        sectionTextView.setText(getItem(position).getWebTitle());
        TextView titleTextView = newsItemView.findViewById(R.id.title_text);
        titleTextView.setText(getItem(position).getSection());

        return newsItemView;
    }
}
