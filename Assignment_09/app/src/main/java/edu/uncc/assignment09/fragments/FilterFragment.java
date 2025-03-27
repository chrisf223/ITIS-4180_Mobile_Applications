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
import edu.uncc.assignment09.databinding.FragmentFilterBinding;
import edu.uncc.assignment09.databinding.RowItemCreditScoreBinding;
import edu.uncc.assignment09.models.CreditCategory;

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
    FilterAdapter adapter;
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

        adapter = new FilterAdapter(getContext(), creditCategories);
        binding.listView.setAdapter(adapter);
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CreditCategory selectedCategory = (CreditCategory) adapterView.getItemAtPosition(i);
                mListener.onFilterSelected(selectedCategory);
            }
        });
    }

    class FilterAdapter extends ArrayAdapter<CreditCategory>{
        public FilterAdapter(@NonNull Context context, @NonNull CreditCategory[] objects) {
            super(context, R.layout.row_item_credit_score, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            RowItemCreditScoreBinding itemBinding;
            if(convertView == null) {
                itemBinding = RowItemCreditScoreBinding.inflate(getLayoutInflater(), parent, false);
                convertView = itemBinding.getRoot();
                convertView.setTag(itemBinding);
            } else {
                itemBinding = (RowItemCreditScoreBinding) convertView.getTag();
            }

            CreditCategory creditCategory = getItem(position);
            itemBinding.textViewName.setText(creditCategory.getName());
            itemBinding.imageViewCreditScore.setImageResource(creditCategory.getImageResourceId());
            return itemBinding.getRoot();
        }
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