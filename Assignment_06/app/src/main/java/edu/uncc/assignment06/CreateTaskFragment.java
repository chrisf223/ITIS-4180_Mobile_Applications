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

import edu.uncc.assignment06.databinding.FragmentCreateTaskBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateTaskFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static String mDate = "";

    public static void setDate(String date) {
        mDate = date;
    }

    public CreateTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreateTaskFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreateTaskFragment newInstance(String param1, String param2) {
        CreateTaskFragment fragment = new CreateTaskFragment();
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

    FragmentCreateTaskBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCreateTaskBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create Task");

        binding.textViewDate.setText(mDate);
        mDate="";
        binding.buttonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goToCalendar();
            }
        });

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.cancel();
            }
        });

        binding.buttonSubmit.setOnClickListener(v -> {
            String taskName = binding.editTextTaskName.getText().toString();
            String taskPriority = binding.radioGroup.getCheckedRadioButtonId() == R.id.radioButtonHigh ? "High" :
                    binding.radioGroup.getCheckedRadioButtonId() == R.id.radioButtonMedium ? "Medium" : "Low";
            String taskDate = binding.textViewDate.getText().toString();

            if (taskName.isEmpty() || taskDate.isEmpty() || taskPriority.isEmpty()) {
                Toast.makeText(getContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                Task newTask = new Task(taskName, taskDate, taskPriority);
                mListener.submit(newTask);
            }
        });

    }

    CreateTasksListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CreateTasksListener) context;
    }
    public interface CreateTasksListener {
        void goToCalendar();
        void cancel();
        void submit(Task task);
    }
}