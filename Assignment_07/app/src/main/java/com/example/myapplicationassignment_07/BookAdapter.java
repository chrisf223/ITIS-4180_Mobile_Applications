package com.example.myapplicationassignment_07;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class BookAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Book> books;

    public BookAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_book, parent, false);
        }

        Book book = books.get(position);

        TextView bookTitle = convertView.findViewById(R.id.bookTitle);
        TextView bookAuthor = convertView.findViewById(R.id.bookAuthor);
        TextView bookGenre = convertView.findViewById(R.id.bookGenre);
        TextView bookYear = convertView.findViewById(R.id.bookYear);

        bookTitle.setText(book.getTitle());
        bookAuthor.setText(book.getAuthor());
        bookGenre.setText(book.getGenre());
        bookYear.setText(String.valueOf(book.getYear())); // Convert the year to String and display

        return convertView;
    }
}


