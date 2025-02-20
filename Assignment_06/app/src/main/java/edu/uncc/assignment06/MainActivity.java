package edu.uncc.assignment06;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

// Assignment 06
// Assignment06
// DeCristo Franceschini
public class MainActivity extends AppCompatActivity implements TasksFragment.TasksListener, CreateTaskFragment.CreateTasksListener, SelectTaskDateFragment.CalendarTasksListener {

    ArrayList<Task> taskList = new ArrayList<Task>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TasksFragment tasksFragment = TasksFragment.newInstance(taskList);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, tasksFragment)
                .commit();

    }

    @Override
    public void createTask() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new CreateTaskFragment(), "create-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToCalendar() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new SelectTaskDateFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void submitDate(String date) {
        CreateTaskFragment CreateTask = (CreateTaskFragment) getSupportFragmentManager().findFragmentByTag("create-fragment");
        if (CreateTask != null) {
            CreateTaskFragment.setDate(date);
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void cancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void submit(Task task) {
        getSupportFragmentManager().popBackStack();
        taskList.add(task);
    }
}