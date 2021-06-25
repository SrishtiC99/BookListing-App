package com.example.booklistingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class BookAdapter extends ArrayAdapter<Book> {
    public BookAdapter(@NonNull Context context, List<Book> bookList) {
        super(context,0, (List<Book>) bookList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        // Check if the existing view is being reused, otherwise inflate the view
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_view, parent, false);
        }
        Book book = getItem(position);
        TextView titleView = (TextView)listItemView.findViewById(R.id.title_view);
        TextView authorView = (TextView)listItemView.findViewById(R.id.author_view);
        TextView langView = (TextView)listItemView.findViewById(R.id.lang_view);
        titleView.setText(book.getBookName());
        authorView.setText(book.getAuthorName());
        langView.setText(book.getLang());
        return listItemView;
    }
}
