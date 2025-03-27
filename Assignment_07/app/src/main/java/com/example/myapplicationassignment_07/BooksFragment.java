package com.example.myapplicationassignment_07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class BooksFragment extends Fragment {

    private ListView booksListView;
    private TextView genreText;

    public static BooksFragment newInstance(String genre) {
        BooksFragment fragment = new BooksFragment();
        Bundle args = new Bundle();
        args.putString("genre", genre);
        fragment.setArguments(args);
        return fragment;
    }

    public BooksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_books, container, false);

        booksListView = rootView.findViewById(R.id.booksList);
        genreText = rootView.findViewById(R.id.genreNumberText);

        String genre = getArguments().getString("genre");
        genreText.setText(genre);

        ArrayList<Book> books = Data.getBooksByGenre(genre);

        BookAdapter bookAdapter = new BookAdapter(getContext(), books);
        booksListView.setAdapter(bookAdapter);

        View backButton = rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book selectedBook = (Book) parent.getItemAtPosition(position);
                BookDetailsFragment bookDetailsFragment = new BookDetailsFragment();
                Bundle args = new Bundle();
                args.putSerializable("selectedBook", selectedBook);
                bookDetailsFragment.setArguments(args);

                getFragmentManager().beginTransaction()
                        .replace(R.id.main, bookDetailsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}






