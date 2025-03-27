package com.example.myapplicationassignment_07;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class GenresFragment extends Fragment {

    private ListView genresListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_genres, container, false);

        genresListView = rootView.findViewById(R.id.genresList);
        ArrayList<String> genres = Data.getAllGenres();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, genres);
        genresListView.setAdapter(adapter);

        genresListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedGenre = genres.get(position);
                BooksFragment booksFragment = BooksFragment.newInstance(selectedGenre);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main, booksFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return rootView;
    }
}

