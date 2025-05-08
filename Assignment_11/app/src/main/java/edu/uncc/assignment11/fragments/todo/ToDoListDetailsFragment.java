package edu.uncc.assignment11.fragments.todo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentListDetailsBinding;
import edu.uncc.assignment11.databinding.ListItemListItemBinding;
import edu.uncc.assignment11.models.ToDoList;
import edu.uncc.assignment11.models.ToDoListItem;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ToDoListDetailsFragment extends Fragment {
    private static final String ARG_PARAM_TODO_LIST= "ARG_PARAM_TODO_LIST";
    FragmentListDetailsBinding binding;
    private ToDoList mToDoList;

    public static ToDoListDetailsFragment newInstance(ToDoList toDoList) {
        ToDoListDetailsFragment fragment = new ToDoListDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_TODO_LIST, toDoList);
        fragment.setArguments(args);
        return fragment;
    }

    public ToDoListDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mToDoList = (ToDoList) getArguments().getSerializable(ARG_PARAM_TODO_LIST);
        }
    }

    ArrayList<ToDoListItem> mToDoListItems = new ArrayList<>();
    ToDoListItemAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentListDetailsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("ToDo Lists");

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.todo_list_details_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.add_new_list_item_action){
                    mListener.gotoAddListItem(mToDoList);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        binding.buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.goBackToToDoLists();
            }
        });

        adapter = new ToDoListItemAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        loadToDoListItems();
    }

    void loadToDoListItems() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = prefs.getString("token", null);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/api/todolists/" + mToDoList.getId())
                .header("Authorization", "BEARER " + token)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace(); // Handle error if needed
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.d("ToDoDetails", "Raw response: " + responseBody);

                    try {
                        JSONObject root = new JSONObject(responseBody);
                        JSONObject todolistObject = root.getJSONObject("todolist");
                        JSONArray itemsArray = todolistObject.getJSONArray("items");

                        mToDoListItems.clear();

                        for (int i = 0; i < itemsArray.length(); i++) {
                            JSONObject itemObj = itemsArray.getJSONObject(i);
                            int id = itemObj.getInt("todolist_item_id");
                            String name = itemObj.getString("name");
                            String priority = itemObj.getString("priority");

                            mToDoListItems.add(new ToDoListItem(id, name, priority));
                        }

                        requireActivity().runOnUiThread(() -> adapter.notifyDataSetChanged());
                    } catch (JSONException e) {
                        Log.e("ToDoDetails", "JSON Exception: " + e.getMessage());
                    }
                }
            }
        });
    }


    void deleteToDoListItem(ToDoListItem toDoListItem) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        String token = prefs.getString("token", null);

        new AlertDialog.Builder(getContext())
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("OK", (dialog, which) -> {
                    OkHttpClient client = new OkHttpClient();

                    // Make sure the URL is correct, using POST method
                    String deleteUrl = "https://www.theappsdr.com/api/todolist-items/delete";
                    Log.d("ToDoDetails", "Deleting from URL: " + deleteUrl);

                    // Create request body with correct parameters
                    FormBody formBody = new FormBody.Builder()
                            .add("todolist_id", String.valueOf(mToDoList.getId())) // Add the todo list ID
                            .add("todolist_item_id", String.valueOf(toDoListItem.getId())) // Add the item ID
                            .build();

                    // Create POST request with Authorization header
                    Request request = new Request.Builder()
                            .url(deleteUrl)
                            .header("Authorization", "BEARER " + token)
                            .post(formBody)
                            .build();

                    // Make the request
                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace(); // Handle error if needed
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                requireActivity().runOnUiThread(() -> {
                                    Toast.makeText(getContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                                    loadToDoListItems(); // Refresh list
                                });
                            } else {
                                Log.e("ToDoDetails", "Failed to delete. Code: " + response.code());
                            }
                        }
                    });
                })
                .setNegativeButton("Cancel", null)
                .show();
    }




    class ToDoListItemAdapter extends RecyclerView.Adapter<ToDoListItemAdapter.ToDoListItemViewHolder>{

        @NonNull
        @Override
        public ToDoListItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemListItemBinding itemBinding = ListItemListItemBinding.inflate(getLayoutInflater(), parent, false);
            return new ToDoListItemViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ToDoListItemViewHolder holder, int position) {
            ToDoListItem toDoListItem = mToDoListItems.get(position);
            holder.bind(toDoListItem);
        }

        @Override
        public int getItemCount() {
            return mToDoListItems.size();
        }

        class ToDoListItemViewHolder extends RecyclerView.ViewHolder{
            ListItemListItemBinding itemBinding;
            ToDoListItem mToDoListItem;
            public ToDoListItemViewHolder(ListItemListItemBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(ToDoListItem toDoListItem) {
                this.mToDoListItem = toDoListItem;

                itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteToDoListItem(mToDoListItem);
                    }
                });

                itemBinding.textViewName.setText(toDoListItem.getName());
                itemBinding.textViewPriority.setText(toDoListItem.getPriority());
            }
        }
    }

    ToDoListDetailsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ToDoListDetailsListener) {
            mListener = (ToDoListDetailsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ToDoListDetailsListener");
        }
    }

    public interface ToDoListDetailsListener {
        void gotoAddListItem(ToDoList toDoList);
        void goBackToToDoLists();
    }
}