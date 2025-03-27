package edu.uncc.assignment08;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.uncc.assignment08.models.Task;

public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TaskHolder> {
    private ArrayList<Task> tasks;
    private OnTaskDeleteListener mListener;

    public TasksRecyclerViewAdapter(ArrayList<Task> tasks, OnTaskDeleteListener listener) {
        this.tasks = tasks;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_task_details, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Task task = tasks.get(position);
        holder.bind(task, mListener);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public class TaskHolder extends RecyclerView.ViewHolder {
        private TextView taskName;
        private TextView taskPriority;
        private TextView taskDate;
        private ImageView trashImageView;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            taskName = itemView.findViewById(R.id.textViewTaskName);
            taskPriority = itemView.findViewById(R.id.textViewTaskPriority);
            taskDate = itemView.findViewById(R.id.textViewTaskDate);
            trashImageView = itemView.findViewById(R.id.trashImageView);
        }

        public void bind(Task task, OnTaskDeleteListener listener) {
            taskName.setText(task.getName());

            // Set priority as text
            if (task.getPriority() == 1) {
                taskPriority.setText("Low");
            } else if (task.getPriority() == 2) {
                taskPriority.setText("Medium");
            } else {
                taskPriority.setText("High");
            }

            String formattedDate = android.text.format.DateFormat.format("MM/dd/yyyy", task.getDate()).toString();
            taskDate.setText(formattedDate);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaskSelected(task);
                }
            });

            trashImageView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onTaskDelete(task, getAdapterPosition());
                }
            });
        }
    }

    public interface OnTaskDeleteListener {
        void onTaskDelete(Task task, int position);
        void onTaskSelected(Task task);
    }
}



