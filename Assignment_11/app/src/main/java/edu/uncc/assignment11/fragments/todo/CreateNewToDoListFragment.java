package edu.uncc.assignment11.fragments.todo;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uncc.assignment11.databinding.FragmentCreateNewToDoListBinding;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreateNewToDoListFragment extends Fragment {
    public CreateNewToDoListFragment() {
        // Required empty public constructor
    }

    FragmentCreateNewToDoListBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCreateNewToDoListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Create New List");
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelCreateNewToDoList();
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = binding.editTextName.getText().toString().trim();
                if (listName.isEmpty()) {
                    Toast.makeText(getContext(), "List name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
                    String token = prefs.getString("token", null);

                    OkHttpClient client = new OkHttpClient();

                    RequestBody requestBody = new FormBody.Builder()
                            .add("name", listName)
                            .build();

                    Request request = new Request.Builder()
                            .url("https://www.theappsdr.com/api/todolists/create")
                            .header("Authorization", "BEARER " + token)
                            .post(requestBody)
                            .build();

                    client.newCall(request).enqueue(new okhttp3.Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            e.printStackTrace();
                            if (getActivity() != null) {
                                getActivity().runOnUiThread(() ->
                                        Toast.makeText(getContext(), "Failed to create list", Toast.LENGTH_SHORT).show()
                                );
                            }
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                            if (response.isSuccessful()) {
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(() -> {
                                        mListener.onSuccessCreateNewToDoList();
                                    });
                                }
                            } else {
                                if (getActivity() != null) {
                                    getActivity().runOnUiThread(() ->
                                            Toast.makeText(getContext(), "Error: Could not create list", Toast.LENGTH_SHORT).show()
                                    );
                                }
                            }
                        }
                    });

                }
            }
        });
    }

    CreateNewToDoListListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof CreateNewToDoListListener) {
            mListener = (CreateNewToDoListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement CreateNewToDoListListener");
        }
    }

    public interface CreateNewToDoListListener {
        void onSuccessCreateNewToDoList();
        void onCancelCreateNewToDoList();
    }
}