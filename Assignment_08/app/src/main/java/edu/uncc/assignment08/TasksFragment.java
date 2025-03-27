package edu.uncc.assignment08;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

import edu.uncc.assignment08.databinding.FragmentTasksBinding;
import edu.uncc.assignment08.models.SortSelection;
import edu.uncc.assignment08.models.Task;

public class TasksFragment extends Fragment implements TasksRecyclerViewAdapter.OnTaskDeleteListener{
    public TasksFragment() {
        // Required empty public constructor
    }

    FragmentTasksBinding binding;
    SortSelection sortSelection;
    TasksRecyclerViewAdapter adapter;


    public void setSortSelection(SortSelection sortSelection) {
        this.sortSelection = sortSelection;
        sortTasks();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTasksBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    ArrayList<Task> mTasks = new ArrayList<>();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Tasks");

        mTasks = mListener.getTasks();

        adapter = new TasksRecyclerViewAdapter(mTasks, this);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new  LinearLayoutManager(getContext()));

        updateTaskCount();

        binding.buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.clearAllTasks();
                mTasks.clear();
                adapter.notifyDataSetChanged();
                updateTaskCount();
            }
        });

        binding.buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoCreateTask();
            }
        });

        binding.buttonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSelectSort();
            }
        });

    }

    private void updateTaskCount() {
        String taskCountText = "You have " + mTasks.size() + " tasks";
        binding.textViewTasksCount.setText(taskCountText);
    }


    TasksListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof TasksListener) {
            mListener = (TasksListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TasksListener");
        }
    }

    private void sortTasks() {
        if (sortSelection == null) return; // No sorting if sortSelection is null

        switch (sortSelection.getSortAttribute()) {
            case "date":
                if (sortSelection.getSortOrder().equals("ASC")) {
                    Collections.sort(mTasks, (task1, task2) -> task1.getDate().compareTo(task2.getDate()));
                } else {
                    Collections.sort(mTasks, (task1, task2) -> task2.getDate().compareTo(task1.getDate()));
                }
                break;

            case "name":
                if (sortSelection.getSortOrder().equals("ASC")) {
                    Collections.sort(mTasks, (task1, task2) -> task1.getName().compareTo(task2.getName()));
                } else {
                    Collections.sort(mTasks, (task1, task2) -> task2.getName().compareTo(task1.getName()));
                }
                break;

            case "priority":
                if (sortSelection.getSortOrder().equals("ASC")) {
                    Collections.sort(mTasks, (task1, task2) -> Integer.compare(task1.getPriority(), task2.getPriority()));
                } else {
                    Collections.sort(mTasks, (task1, task2) -> Integer.compare(task2.getPriority(), task1.getPriority()));
                }
                break;

            default:
                break;
        }

        adapter.notifyDataSetChanged();
    }


    public void updateTaskList(ArrayList<Task> tasks) {
        mTasks = tasks;
        sortTasks();  // Sort tasks whenever the list is updated
        adapter.notifyDataSetChanged();
        updateTaskCount();
    }

    public void onTaskDelete(Task task, int position) {
        // Remove the task from the list
        mTasks.remove(position);

        // Notify the adapter that the item was removed
        adapter.notifyItemRemoved(position);

        // Update the task count
        updateTaskCount();

        // Optionally, inform the MainActivity that the task is deleted
        mListener.onTaskDeleted(task);
    }

    @Override
    public void onTaskSelected(Task task) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).onTaskSelected(task);
        }
    }


    interface TasksListener{
        void gotoCreateTask();
        void gotoSelectSort();
        void clearAllTasks();
        void gotoTaskDetails(Task task);
        ArrayList<Task> getTasks();
        void onTaskDeleted(Task task);
        void onTaskSelected(Task task);
    }
}