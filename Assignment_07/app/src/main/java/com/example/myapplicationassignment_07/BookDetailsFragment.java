package com.example.myapplicationassignment_07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class BookDetailsFragment extends Fragment {

    private TextView titleTextView;
    private TextView authorTextView;
    private TextView genreTextView;
    private TextView yearTextView;

    public static BookDetailsFragment newInstance(Book book) {
        BookDetailsFragment fragment = new BookDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable("book", book);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book_details, container, false);

        titleTextView = rootView.findViewById(R.id.bookTitleText);
        authorTextView = rootView.findViewById(R.id.authorNameText);
        genreTextView = rootView.findViewById(R.id.genreText);
        yearTextView = rootView.findViewById(R.id.yearText);

        Book selectedBook = (Book) getArguments().getSerializable("selectedBook");

        if (selectedBook != null) {
            titleTextView.setText(selectedBook.getTitle());
            authorTextView.setText(selectedBook.getAuthor());
            genreTextView.setText(selectedBook.getGenre());
            yearTextView.setText(String.valueOf(selectedBook.getYear()));
        }

        View backButton = rootView.findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            if (getActivity() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return rootView;
    }
}

