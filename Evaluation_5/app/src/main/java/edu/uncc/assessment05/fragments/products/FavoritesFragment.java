package edu.uncc.assessment05.fragments.products;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uncc.assessment05.R;
import edu.uncc.assessment05.databinding.FragmentFavoritesBinding;

public class FavoritesFragment extends Fragment {
    public FavoritesFragment() {
        // Required empty public constructor
    }

   FragmentFavoritesBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Favorites");
        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.doneFavorites();
            }
        });
    }

    FavoritesListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FavoritesListener) {
            mListener = (FavoritesListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement FavoritesListener");
        }
    }

    public interface FavoritesListener{
        void doneFavorites();
    }
}