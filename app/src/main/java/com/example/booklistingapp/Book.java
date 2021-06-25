package com.example.booklistingapp;

import java.util.ArrayList;

public class Book {
    private String bookName;
    private String authorNames;
    private String lang;

    public Book(String bookName, String authors, String lang){
        this.bookName = bookName;
        authorNames = authors;
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getAuthorName() {
        return authorNames;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }
}
