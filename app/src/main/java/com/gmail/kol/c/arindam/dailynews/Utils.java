package com.gmail.kol.c.arindam.dailynews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

//helper methods to fetch news articles from guardian api
public final class Utils {
    //Error massage tag
    private static final String LOG_TAG = Utils.class.getSimpleName();

    //blank constructor for final class
    private Utils () {}

    //convert string to url
    public static URL createUrl(String urlText) {
        URL url = null;
        try {
            url = new URL(urlText);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "URL not correct ", e);
        }
        return url;
    }

    //get json string from url
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // if url is null return blank json string
        if (url == null) {
            return jsonResponse;
        }

        //make network connection
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If response code 200, success. create json string.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to access json result.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    //get json string from url
    private static Bitmap getNewsImage (URL url) throws IOException {
        Bitmap bitmap = null;

        // if url is null return blank json string
        if (url == null) {
            return null;
        }

        //make network connection
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If response code 200, success. create json string.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to access json result.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return bitmap;
    }

    //convert url response to string
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    //create list of NewsArticle objects by parsing json string
    private static List<NewsArticle> extractFromJson(String jsonString) {
        //if the JSON string is empty or null, then return null.
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }

        List<NewsArticle> newsArticleList = new ArrayList<>();

        //parsing json string
        try {

            //create a json Object from the json string
            JSONObject baseJsonResponse = new JSONObject(jsonString);

            //extract the json object with the key "response",
            JSONObject responseObject = baseJsonResponse.getJSONObject("response");

            //extract json array with the key "result" which contains all the news article details
            JSONArray articleArray = responseObject.getJSONArray("results");

            //get article array length
            int count =  articleArray.length();

            //from article array extract individual article details
            for (int i = 0; i < count; i++) {

                JSONObject currentArticle = articleArray.getJSONObject(i);

                //extract string value for key "webTitle"
                String currentTitle = currentArticle.getString("webTitle");

                //extract string value for key "sectionName"
                String currentSection = currentArticle.getString("sectionName");

                //extract date value for key "webPublicationDate"
                String currentDateTime = currentArticle.getString("webPublicationDate");

                //extract url value for key "webUrl"
                String currentNewsArticleURL = currentArticle.getString("webUrl");

                //extract nestled jsonobject for key "fields"
                JSONObject fieldObject = currentArticle.getJSONObject("fields");

                //extract image url value for key "thumbnail"
                String currentImageURL = fieldObject.getString("thumbnail");
                URL imageURL = createUrl(currentImageURL);
                Bitmap currentImage = getNewsImage(imageURL);

                //extract json array for key "tags"
                JSONArray tagsArray = currentArticle.getJSONArray("tags");

                //extract author name for key "webTitle"
                List<String> currentAuthors = new ArrayList<>();
                int length = tagsArray.length();
                if(length>0) {
                    for (int j=0; j<length; j++) {
                        JSONObject currentTag = tagsArray.getJSONObject(j);
                        String temp = currentTag.getString("webTitle");
                        currentAuthors.add(temp);
                    }
                } else { currentAuthors = null; }

                //create NewsArticle object and add to list
                NewsArticle newsArticle = new NewsArticle(currentTitle,currentSection, currentDateTime, currentAuthors, currentNewsArticleURL, currentImage);
                newsArticleList.add(newsArticle);
            }

        } catch (JSONException e) {
            // catch the exception here & print a log message
            Log.e("Utils", "Problem parsing json results", e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Return the list of earthquakes
        return newsArticleList;
    }

    //return list of news articles from guardian url
    public static List<NewsArticle> getNewsArticleData (String requestUrl) {
        URL url = createUrl(requestUrl);

        //perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        //extract json response and create a list of news articles
        List<NewsArticle> newsArticleList = extractFromJson(jsonResponse);
        return newsArticleList;
    }
}
