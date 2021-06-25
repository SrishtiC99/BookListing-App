package com.example.booklistingapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>> {
    private String updatedUrl;
    private BookAdapter adapter;
    private TextView EmptyStateTextView;
    private ProgressBar progressBar;
    private ListView listview;
    private EditText query;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        progressBar = (ProgressBar)findViewById(R.id.loading_spinner);
        Button button = (Button) findViewById(R.id.search_button);
        query = (EditText)findViewById(R.id.edit_query);
        listview = (ListView)findViewById(R.id.list);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if(isConnected()){
                    String searchValue = query.getText().toString();
                    updatedUrl = updateUrl(searchValue);
                    listview.setVisibility(View.GONE);
                    EmptyStateTextView.setVisibility(View.GONE);
                    LoaderManager loaderManager = getSupportLoaderManager();
                    getSupportLoaderManager().restartLoader(1, null, MainActivity.this).forceLoad();
                }else{
                    progressBar.setVisibility(View.GONE);
                    EmptyStateTextView.setText(R.string.no_internet);
                }
            }
        });
    }
    private String updateUrl(String searchValue){
        if(searchValue.contains(" ")){
            searchValue = searchValue.replace(" ","+");
        }
        String requestUrl = "https://www.googleapis.com/books/v1/volumes?q=";
        return requestUrl + searchValue + "&filter=paid-ebooks&maxResults=40";
    }
    @NonNull
    @Override
    public Loader<List<Book>> onCreateLoader(int id, @Nullable Bundle args) {
        return new BookLoader(this,updatedUrl);
    }
    @Override
    public void onLoadFinished(@NonNull Loader<List<Book>> loader, List<Book> data) {
        progressBar.setVisibility(View.GONE);
        if(adapter!=null) {
            adapter.clear();
        }
        if(data == null) {
            EmptyStateTextView.setText(R.string.empty_text_view);
            return;
        }
        else{
            adapter = new BookAdapter(this, data);
            progressBar.setVisibility(View.GONE);
            listview.setVisibility(View.VISIBLE);
            listview.setEmptyView(EmptyStateTextView);
            listview.setAdapter(adapter);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Book>> loader) {
        adapter.clear();
    }

    public boolean isConnected(){
        // Get a reference to the ConnectivityManager to check state of network connectivity
        android.net.ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) return true;
        return false;
    }

}