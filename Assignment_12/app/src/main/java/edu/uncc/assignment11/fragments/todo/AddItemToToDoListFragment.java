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

import java.util.HashMap;

import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentAddItemToToDoListBinding;
import edu.uncc.assignment11.models.ToDoList;

public class AddItemToToDoListFragment extends Fragment {
    private static final String ARG_PARAM_TODO_LIST = "ARG_PARAM_TODO_LIST";
    private ToDoList mTodoList;

    public AddItemToToDoListFragment() {
        // Required empty public constructor
    }

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

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
        binding.buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onCancelAddItemToList(mTodoList);
            }
        });

        binding.buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = binding.editTextName.getText().toString().trim();
                if (itemName.isEmpty()) {
                    Toast.makeText(getContext(), "Item name cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    String priority = "Low";
                    int checkedId = binding.radioGroup.getCheckedRadioButtonId();
                    if(checkedId == R.id.radioButtonMedium){
                        priority = "Medium";
                    } else if(checkedId == R.id.radioButtonHigh){
                        priority = "High";
                    }
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    DocumentReference docRef = db.collection("ToDoLists").document(mTodoList.getToDoListID()).collection("Items").document();
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("name", itemName);
                    data.put("parentID", mTodoList.getToDoListID());
                    data.put("itemID", docRef.getId());
                    data.put("priority", priority);

                    docRef.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mListener.onSuccessAddItemToList();
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });
                }
            }
        });


    }

    AddItemToListListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddItemToListListener) {
            mListener = (AddItemToListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement AddItemToListListener");
        }
    }

    public interface AddItemToListListener{
        void onSuccessAddItemToList();
        void onCancelAddItemToList(ToDoList todoList);
    }
}