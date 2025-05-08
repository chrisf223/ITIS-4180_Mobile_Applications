package edu.uncc.assignment11.fragments.todo;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;

import edu.uncc.assignment11.databinding.FragmentCreateNewToDoListBinding;
import edu.uncc.assignment11.models.ToDoList;

public class CreateNewToDoListFragment extends Fragment {
    public CreateNewToDoListFragment() {
        // Required empty public constructor
    }

    FragmentCreateNewToDoListBinding binding;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
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
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("ToDoLists").document();
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("name", listName);
                    data.put("createdBy", mAuth.getCurrentUser().getUid());
                    data.put("toDoListID", docRef.getId());

                    docRef.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mListener.onSuccessCreateNewToDoList();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
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