package edu.uncc.assignment08;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import edu.uncc.assignment08.models.Task;

public class taskSummaryFragment extends Fragment {

    private static final String ARG_TASK = "task";

    private Task task;

    public taskSummaryFragment() {
    }

    public static taskSummaryFragment newInstance(Task task) {
        taskSummaryFragment fragment = new taskSummaryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK, task);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            task = (Task) getArguments().getSerializable(ARG_TASK);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Task Summary");
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_summary, container, false);

        TextView taskName = view.findViewById(R.id.textViewName);
        TextView taskPriority = view.findViewById(R.id.textViewPriority);
        TextView taskDate = view.findViewById(R.id.textViewDate);

        if (task != null) {
            taskName.setText(task.getName());
            if (task.getPriority() == 1) {
                taskPriority.setText("Low");
            } else if (task.getPriority() == 2) {
                taskPriority.setText("Medium");
            } else {
                taskPriority.setText("High");
            }
            taskDate.setText(android.text.format.DateFormat.format("MM/dd/yyyy", task.getDate()));
        }

        Button buttonBack = view.findViewById(R.id.buttonBack);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);

        buttonBack.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).onBackPressed();
            }
        });

        buttonDelete.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).onTaskDeleted(task);
                ((MainActivity) getActivity()).onBackPressed();
            }
        });

        return view;
    }
}
