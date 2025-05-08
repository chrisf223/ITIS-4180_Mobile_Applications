package edu.uncc.assignment11.fragments.todo;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentToDoListsBinding;
import edu.uncc.assignment11.databinding.ListItemTodoListBinding;
import edu.uncc.assignment11.models.ToDoList;

public class ToDoListsFragment extends Fragment {
    public ToDoListsFragment() {
        // Required empty public constructor
    }

    FragmentToDoListsBinding binding;
    ArrayList<ToDoList> mToDoLists = new ArrayList<>();
    ToDoListAdapter adapter;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentToDoListsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("ToDo Lists");

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.todo_lists_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.add_new_todo_list_action){
                    mListener.gotoCreateNewToDoList();
                    return true;
                } else if(menuItem.getItemId() == R.id.logout_action){
                    mListener.logout();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
        adapter = new ToDoListAdapter();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
        getAllToDoListsForUser();
    }

    private void getAllToDoListsForUser() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ToDoLists").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mToDoLists.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ToDoList list = document.toObject(ToDoList.class);
                        if (mAuth.getCurrentUser().getUid().equals(list.getCreatedBy())) {
                            mToDoLists.add(list);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void deleteToDoList(ToDoList toDoList) {
        //TODO: delete the todo list using the api
        //TODO: reload the todo lists for the currently logged in user
    }


    class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ToDoListViewHolder>{

        @NonNull
        @Override
        public ToDoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            ListItemTodoListBinding itemBinding = ListItemTodoListBinding.inflate(getLayoutInflater(), parent, false);
            return new ToDoListViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull ToDoListViewHolder holder, int position) {
            ToDoList toDoList = mToDoLists.get(position);
            holder.bind(toDoList);
        }

        @Override
        public int getItemCount() {
            return mToDoLists.size();
        }

        class ToDoListViewHolder extends RecyclerView.ViewHolder{
            ListItemTodoListBinding itemBinding;
            ToDoList mToDoList;
            public ToDoListViewHolder(ListItemTodoListBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(ToDoList toDoList) {
                    mToDoList = toDoList;
                    itemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mListener.gotoToDoListDetails(toDoList);
                        }
                    });

                    itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            db.collection("ToDoLists").document(mToDoList.getToDoListID()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    getAllToDoListsForUser();
                                }
                            });
                        }
                    });

                    itemBinding.textViewName.setText(toDoList.getName());


            }
        }
    }

    ToDoListsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ToDoListsListener) {
            mListener = (ToDoListsListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ToDoListsListener");
        }
    }

    public interface ToDoListsListener {
        void gotoCreateNewToDoList();
        void gotoToDoListDetails(ToDoList toDoList);
        void logout();
    }
}