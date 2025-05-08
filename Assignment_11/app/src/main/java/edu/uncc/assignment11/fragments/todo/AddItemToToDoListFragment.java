package edu.uncc.assignment11.fragments.todo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentAddItemToToDoListBinding;
import edu.uncc.assignment11.models.ToDoList;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Callback;
import okhttp3.Response;
import java.io.IOException;

public class AddItemToToDoListFragment extends Fragment {

    private static final String ARG_PARAM_TODO_LIST = "ARG_PARAM_TODO_LIST";
    private ToDoList mTodoList;

    public AddItemToToDoListFragment() {
        // Required empty public constructor
    }

    public static AddItemToToDoListFragment newInstance(ToDoList toDoList) {
        AddItemToToDoListFragment fragment = new AddItemToToDoListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TODO_LIST, toDoList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTodoList = (ToDoList) getArguments().getSerializable(ARG_PARAM_TODO_LIST);
        }
    }

    FragmentAddItemToToDoListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAddItemToToDoListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Add Item to List");
        binding.buttonCancel.setOnClickListener(v -> mListener.onCancelAddItemToList(mTodoList));

        binding.buttonSubmit.setOnClickListener(v -> {
            String itemName = binding.editTextName.getText().toString().trim();
            if (itemName.isEmpty()) {
                Toast.makeText(getContext(), "Item name cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                String priority = "Low"; // Default priority
                int checkedId = binding.radioGroup.getCheckedRadioButtonId();
                if (checkedId == R.id.radioButtonMedium) {
                    priority = "Medium";
                } else if (checkedId == R.id.radioButtonHigh) {
                    priority = "High";
                }
                // Call the API to create the new to-do list item
                createToDoListItem(itemName, priority);
            }
        });
    }

    // Method to create a new ToDo List Item
    private void createToDoListItem(String itemName, String priority) {
        // Get the token from SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = prefs.getString("token", null);

        if (token != null) {
            OkHttpClient client = new OkHttpClient();
            String url = "https://www.theappsdr.com/api/todolist-items/create";

            // Create the request body
            RequestBody formBody = new FormBody.Builder()
                    .add("todolist_id", String.valueOf(mTodoList.getId())) // Use the passed to-do list id
                    .add("name", itemName) // Item name
                    .add("priority", priority) // Priority of the item
                    .build();

            // Create the POST request with authorization header
            Request request = new Request.Builder()
                    .url(url)
                    .header("Authorization", "BEARER " + token)
                    .post(formBody)
                    .build();

            // Execute the request asynchronously
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                    e.printStackTrace(); // Handle error
                }

                @Override
                public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()) {
                        // If successful, pop the back stack
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                            mListener.onSuccessAddItemToList();
                        });
                    } else {
                        requireActivity().runOnUiThread(() -> {
                            Toast.makeText(getContext(), "Failed to add item", Toast.LENGTH_SHORT).show();
                        });
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
        }
    }

    // Listener for communicating with the host activity
    AddItemToListListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddItemToListListener) {
            mListener = (AddItemToListListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement AddItemToListListener");
        }
    }

    public interface AddItemToListListener {
        void onSuccessAddItemToList(); // Called on successful addition of the item
        void onCancelAddItemToList(ToDoList todoList); // Called on cancel
    }
}
