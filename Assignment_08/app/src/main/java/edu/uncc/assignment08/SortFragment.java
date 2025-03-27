package edu.uncc.assignment08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uncc.assignment08.databinding.FragmentSortBinding;
import edu.uncc.assignment08.models.SortSelection;

public class SortFragment extends Fragment {
    public SortFragment() {
        // Required empty public constructor
    }

    FragmentSortBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSortBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Sort");
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelAndPopBackStack();
            }
        });

        binding.imageViewDateASC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortSelection sortSelection = new SortSelection("date", "ASC");
                mListener.sendSortSelection(sortSelection);
            }
        });

        binding.imageViewDateDESC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortSelection sortSelection = new SortSelection("date", "DESC");
                mListener.sendSortSelection(sortSelection);
            }
        });

        binding.imageViewNameASC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortSelection sortSelection = new SortSelection("name", "ASC");
                mListener.sendSortSelection(sortSelection);
            }
        });

        binding.imageViewNameDESC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortSelection sortSelection = new SortSelection("name", "DESC");
                mListener.sendSortSelection(sortSelection);
            }
        });

        binding.imageViewPriorityASC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortSelection sortSelection = new SortSelection("priority", "ASC");
                mListener.sendSortSelection(sortSelection);
            }
        });

        binding.imageViewPriorityDESC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SortSelection sortSelection = new SortSelection("priority", "DESC");
                mListener.sendSortSelection(sortSelection);
            }
        });
    }

    SortListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SortListener) {
            mListener = (SortListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SortListener");
        }
    }

    interface SortListener{
        void cancelAndPopBackStack();
        void sendSortSelection(SortSelection sortSelection);
    }
}