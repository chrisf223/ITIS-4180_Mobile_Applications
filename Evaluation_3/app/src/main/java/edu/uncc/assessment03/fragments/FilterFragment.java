package edu.uncc.assessment03.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uncc.assessment03.CreditCategoryAdapter;
import edu.uncc.assessment03.R;
import edu.uncc.assessment03.databinding.FragmentFilterBinding;
import edu.uncc.assessment03.models.CreditCategory;


public class FilterFragment extends Fragment {

    public FilterFragment() {
        // Required empty public constructor
    }

    FragmentFilterBinding binding;
    CreditCategory[] creditCategories = {
            new CreditCategory("Excellent", R.drawable.excellent),
            new CreditCategory("Very Good", R.drawable.very_good),
            new CreditCategory("Good", R.drawable.good),
            new CreditCategory("Fair", R.drawable.fair),
            new CreditCategory("Poor", R.drawable.poor)};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFilterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Filter");

        CreditCategoryAdapter adapter = new CreditCategoryAdapter(getContext(), creditCategories);
        binding.listView.setAdapter(adapter);

        binding.listView.setOnItemClickListener((parent, view1, position, id) -> {
            CreditCategory selectedCategory = creditCategories[position];
            mListener.onFilterSelected(selectedCategory);
            getActivity().getSupportFragmentManager().popBackStack();
    });
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelAddOrSelection();
            }
        });

        binding.buttonNoFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFilterSelected(null);
            }
        });
    }

    FilterListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //surround by try catch block
        try {
            mListener = (FilterListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement FilterListener");
        }
    }

    public interface FilterListener{
        void onFilterSelected(CreditCategory creditCategory);
        void cancelAddOrSelection();
    }
}