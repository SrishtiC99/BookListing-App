package com.example.booklistingapp;

import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class BookQuery {
    public static String LOG_TAG = BookQuery.class.getSimpleName();
    private BookQuery() {}

    // Step 1. Form HTTP Request
    // Step 2. Send the request
    // Step 3. Receive the request and make sense of it
    // Step 4. Update the UI

    public static List<Book> getAllBookData(String requestUrl){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        URL url = createUrl(requestUrl);
        String JSONResponse = null;
        try {
            JSONResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }
        List<Book> books = extractFeaturesFromJSON(JSONResponse);
        return books;
    }
    private static URL createUrl(String requestUrl){
        URL url = null;
        try {
            url = new URL(requestUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws IOException{
        String JSONResponse = "";
        if(url == null) return JSONResponse;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                JSONResponse = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG, "Problem retrieving the Book JSON results.", e);
        }finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return JSONResponse;
    }
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder outputResponse = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line!=null){
                outputResponse.append(line);
                line = bufferedReader.readLine();
            }
        }
        return outputResponse.toString();
    }
    private static List<Book> extractFeaturesFromJSON(String JSONResponse){
        List<Book> bookList = new ArrayList<>();
        if(TextUtils.isEmpty(JSONResponse)) return null;
        try{
            JSONObject root = new JSONObject(JSONResponse);
            JSONArray itemsArray = root.getJSONArray("items");
            for(int i=0;i<itemsArray.length();i++){
                JSONObject currentItem = itemsArray.getJSONObject(i);
                JSONObject info = currentItem.getJSONObject("volumeInfo");
                StringBuilder bookName = new StringBuilder();
                String title = info.getString("title");
                bookName.append(title);
                if(info.has("subtitle")){
                    String subtitle = info.getString("subtitle");
                    bookName.append(": ").append(subtitle);
                }
                StringBuilder authors = new StringBuilder();
                if(info.has("authors")){
                    JSONArray authorsArray = info.getJSONArray("authors");
                    int j = 0;
                    j++;
                    authors.append(authorsArray.getString(0));
                    for(;j<authorsArray.length()-1;j++){
                        String author = authorsArray.getString(j);
                        authors.append(" | ");
                        authors.append(author);
                    }if(j<authorsArray.length()){
                        authors.append(authorsArray.getString(j));
                    }
                }
                else{
                    authors.append(" Unknown author");
                }
                String language = info.getString("language");
                JSONObject imageLinks = info.getJSONObject("imageLinks");
                String coverImageUrl = imageLinks.getString("smallThumbnail");
                Book book = new Book(bookName.toString(),authors.toString(),language);
                bookList.add(book);
            }
            return bookList;
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the Book JSON results", e);
        }
        return null;
    }
}
