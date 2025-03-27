package edu.uncc.assignment08;

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

import edu.uncc.assignment08.databinding.FragmentCreateTaskBinding;
import edu.uncc.assignment08.models.Task;

public class CreateTaskFragment extends Fragment {
    Date selectedDate;
    public CreateTaskFragment() {
        // Required empty public constructor
    }

    FragmentCreateTaskBinding binding;

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

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
        if(selectedDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            binding.textViewDate.setText(sdf.format(selectedDate));
        } else {
            binding.textViewDate.setText("N/A");
        }

        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelAndPopBackStack();
            }
        });

        binding.buttonSetDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.selectDate();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editTextTaskName.getText().toString();
                if(name.isEmpty()){
                    Toast.makeText(getActivity(), "Enter Name !!", Toast.LENGTH_SHORT).show();
                } else if(selectedDate == null) {
                    Toast.makeText(getActivity(), "Select Date !!", Toast.LENGTH_SHORT).show();
                } else {
                    int priority = 1;
                    int selectedId = binding.radioGroup.getCheckedRadioButtonId();
                    if(selectedId == R.id.radioButtonHigh) {
                        priority = 3;
                    } else if(selectedId == R.id.radioButtonMedium) {
                        priority = 2;
                    } else if(selectedId == R.id.radioButtonLow) {
                        priority = 1;
                    }
                    Task task = new Task(name, selectedDate, priority);
                    mListener.createTask(task);
                }
            }
        });
    }

    CreateTaskListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CreateTaskListener) {
            mListener = (CreateTaskListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement CreateTaskListener");
        }
    }

    interface CreateTaskListener {
        void createTask(Task task);
        void cancelAndPopBackStack();
        void selectDate();
    }
}