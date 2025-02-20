package edu.uncc.assignment06;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import edu.uncc.assignment06.databinding.FragmentTasksBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TasksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TasksFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TasksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TasksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TasksFragment newInstance(String param1, String param2) {
        TasksFragment fragment = new TasksFragment();
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

    FragmentTasksBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Tasks");

        ArrayList<Task> taskList = (ArrayList<Task>) getArguments().getSerializable("taskList");

        if (taskList == null || taskList.isEmpty()) {
            binding.cardViewTask.setVisibility(View.GONE);
            binding.buttonCreateTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.createTask();
                }
            });
        } else {
            displayTask(taskList, currentTaskIndex);

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Collections.sort(taskList, new Comparator<Task>() {
                @Override
                public int compare(Task t1, Task t2) {
                    try {
                        return dateFormat.parse(t2.getDate()).compareTo(dateFormat.parse(t1.getDate()));
                    } catch (Exception e) {
                        return 0;
                    }
                }
            });

            int taskCount = taskList.size();

            // Update task count display
            binding.textViewTasksCount.setText("You have " + taskCount + " tasks");

            displayTask(taskList, 0);

            binding.imageViewNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTaskIndex < taskList.size() - 1) {
                        currentTaskIndex++;
                    } else {
                        currentTaskIndex = 0;
                    }
                    displayTask(taskList, currentTaskIndex);
                }
            });

            binding.imageViewPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentTaskIndex > 0) {
                        currentTaskIndex--;
                    } else {
                        currentTaskIndex = taskList.size() - 1;
                    }
                    displayTask(taskList, currentTaskIndex);
                }
            });

            binding.imageViewDelete.setOnClickListener(v -> {
                taskList.remove(currentTaskIndex);
                if (taskList.isEmpty()) {
                    binding.textViewTasksCount.setText("You have 0 tasks");
                    binding.cardViewTask.setVisibility(View.GONE);
                } else {
                    if (currentTaskIndex == taskList.size()) {
                        currentTaskIndex = taskList.size() - 1;
                    }
                    displayTask(taskList, currentTaskIndex);
                    binding.textViewTasksCount.setText("You have " + taskList.size() + " tasks");
                }
            });

            binding.buttonCreateTask.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.createTask();
                }
            });
        }
    }

    TasksListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (TasksListener) context;
    }

    public static TasksFragment newInstance(ArrayList<Task> taskList) {
        TasksFragment fragment = new TasksFragment();
        Bundle args = new Bundle();
        args.putSerializable("taskList", taskList);
        fragment.setArguments(args);
        return fragment;
    }

    private int currentTaskIndex = 0;

    private void displayTask(ArrayList<Task> taskList, int index) {
        Task currentTask = taskList.get(index);
        binding.textViewTaskName.setText(currentTask.getName());
        binding.textViewTaskDate.setText(currentTask.getDate());
        binding.textViewTaskPriority.setText(currentTask.getPriority());

        binding.textViewTaskOutOf.setText("Task " + (index + 1) + " out of " + taskList.size());
    }

    public interface TasksListener {
        void createTask();
    }
}