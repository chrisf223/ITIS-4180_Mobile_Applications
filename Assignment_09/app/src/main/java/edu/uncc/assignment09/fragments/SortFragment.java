package edu.uncc.assignment09.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import edu.uncc.assignment09.R;
import edu.uncc.assignment09.databinding.FragmentSortBinding;

public class SortFragment extends Fragment {
    public SortFragment() {
        // Required empty public constructor
    }

    String[] mSorts = {"Name", "Age", "Credit Score", "State"};
    ArrayAdapter<String> mAdapter;

    FragmentSortBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
                mListener.cancelAddOrSelection();
            }
        });
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mSorts);
        binding.listView.setAdapter(mAdapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String sort = mSorts[position];
                mListener.onSortSelected(sort);
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
            throw new RuntimeException(context.toString()
                    + " must implement SortListener");
        }
    }

    public interface SortListener {
        void onSortSelected(String sort);
        void cancelAddOrSelection();
    }
}