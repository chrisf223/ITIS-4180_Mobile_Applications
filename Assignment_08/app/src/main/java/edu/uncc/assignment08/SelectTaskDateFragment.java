package edu.uncc.assignment08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.util.Calendar;
import java.util.Date;

import edu.uncc.assignment08.databinding.FragmentSelectTaskDateBinding;

public class SelectTaskDateFragment extends Fragment {
    public SelectTaskDateFragment() {
        // Required empty public constructor
    }

    FragmentSelectTaskDateBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSelectTaskDateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Select Task Date");
        binding.calendarView.setDate(System.currentTimeMillis());

        binding.calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                Date date = calendar.getTime();
                binding.calendarView.setDate(date.getTime());
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelAndPopBackStack();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date(binding.calendarView.getDate());
                mListener.sendSelectedDate(date);
            }
        });
    }

    SelectTaskDateListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SelectTaskDateListener) {
            mListener = (SelectTaskDateListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SelectTaskDateListener");
        }
    }

    interface SelectTaskDateListener {
        void cancelAndPopBackStack();
        void sendSelectedDate(Date date);
    }
}