package com.gmail.kol.c.arindam.dailynews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

        //set image & text view
        TextView sectionTextView = newsItemView.findViewById(R.id.section_text);
        sectionTextView.setText(getItem(position).getSection());
        TextView dateTextView = newsItemView.findViewById(R.id.date_text);
        dateTextView.setText(getLocalDate(getItem(position).getPublishDate()));
        TextView titleTextView = newsItemView.findViewById(R.id.title_text);
        titleTextView.setText(getItem(position).getWebTitle());
        TextView authorTexView = newsItemView.findViewById(R.id.authors_text);
        List<String> authorList = getItem(position).getAuthorNames();
        if(authorList!=null) {
            StringBuilder authorNames = new StringBuilder("");
            int count = authorList.size();
            for (int i=0; i<count; i++) {
                if(i==(count-1)) { authorNames.append(authorList.get(i)); }
                else { authorNames.append((authorList.get(i)+ ", ")); }
            }
            authorTexView.setVisibility(View.VISIBLE);
            authorTexView.setText(authorNames.toString());
        } else {
            authorTexView.setVisibility(View.GONE);
        }
        ImageView newsImage = newsItemView.findViewById(R.id.news_image);
        newsImage.setImageBitmap(getItem(position).getNewsImage());

        return newsItemView;
    }

    //Convert UTC time to local time
    private String getLocalDate(String utcDate) {
        String localDate;
        try {
            SimpleDateFormat utcDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.UK);
            utcDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date input = utcDateFormat.parse(utcDate);
            SimpleDateFormat localDateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm a",Locale.getDefault());
            localDate = localDateFormat.format(input);
        } catch (Exception e) {
            localDate = "00/00/0000 00:00";
        }
        return localDate;
    }
}
