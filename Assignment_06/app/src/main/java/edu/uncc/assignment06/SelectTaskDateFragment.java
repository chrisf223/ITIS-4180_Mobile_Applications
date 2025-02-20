package edu.uncc.assignment06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import edu.uncc.assignment06.databinding.FragmentSelectTaskDateBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectTaskDateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectTaskDateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SelectTaskDateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SelectTaskDateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SelectTaskDateFragment newInstance(String param1, String param2) {
        SelectTaskDateFragment fragment = new SelectTaskDateFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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

        binding.calendarView.setMaxDate(System.currentTimeMillis());
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancel();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long selectedDate = binding.calendarView.getDate();

                if (selectedDate == -1) {
                    Toast.makeText(getContext(), "Please select a date", Toast.LENGTH_SHORT).show();
                } else {
                    // Successfully selected a date, pass it back to the activity
                    mListener.submitDate(convertDateToString(selectedDate));
                }
            }
        });
    }

    CalendarTasksListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CalendarTasksListener) context;
    }

    private String convertDateToString(long dateInMillis) {
        // Convert long date to a human-readable string format
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(dateInMillis);
        return sdf.format(date); // Example: "February 19, 2025"
    }

    public interface CalendarTasksListener {
        void submitDate(String date);
        void cancel();
    }
}