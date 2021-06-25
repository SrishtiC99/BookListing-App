package com.example.booklistingapp;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import java.util.List;

public class BookLoader extends AsyncTaskLoader<List<Book>> {
    public static String LOG_TAG = BookLoader.class.getSimpleName();
    private String queryUrl;
    public BookLoader(@NonNull Context context,String queryUrl) {
        super(context);
        this.queryUrl = queryUrl;
    }
    @Override
    protected void onStartLoading() {
        forceLoad();
    }
    @Nullable
    @Override
    public List<Book> loadInBackground() {
        if(queryUrl == null){
            Log.e(LOG_TAG, " queryUrl == null");
            return null;
        }
        List<Book> result = BookQuery.getAllBookData(queryUrl);
        return result;
    }
}
