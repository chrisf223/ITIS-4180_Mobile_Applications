package edu.uncc.assignment11.fragments.todo;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import edu.uncc.assignment11.R;
import edu.uncc.assignment11.databinding.FragmentListDetailsBinding;
import edu.uncc.assignment11.databinding.ListItemListItemBinding;
import edu.uncc.assignment11.models.ToDoList;
import edu.uncc.assignment11.models.ToDoListItem;

public class ToDoListDetailsFragment extends Fragment {
    private static final String ARG_PARAM_TODO_LIST= "ARG_PARAM_TODO_LIST";
    FragmentListDetailsBinding binding;
    private ToDoList mToDoList;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String mParentID;
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
        binding.textViewListName.setText(mToDoList.getName());
        loadToDoListItems();
    }

    void loadToDoListItems(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("ToDoLists").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    mToDoListItems.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        ToDoList list = document.toObject(ToDoList.class);
                        mParentID = list.getToDoListID();
                        if (mAuth.getCurrentUser().getUid().equals(list.getCreatedBy())) {
                            db.collection("ToDoLists")
                                    .document(mParentID)
                                    .collection("Items")
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> itemTask) {
                                            if (itemTask.isSuccessful()) {
                                                for (QueryDocumentSnapshot itemDoc : itemTask.getResult()) {
                                                    ToDoListItem item = itemDoc.toObject(ToDoListItem.class);
                                                    Log.d(TAG, String.valueOf(item));
                                                    if (mToDoList.getToDoListID().equals(item.getParentID())) {
                                                        mToDoListItems.add(item);
                                                    }
                                                }
                                                adapter.notifyDataSetChanged();
                                            } else {
                                                Log.d(TAG, "Error getting items from subcollection: ", itemTask.getException());
                                            }
                                        }
                                    });
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });
    }

    void deleteToDoListItem(ToDoListItem toDoListItem){
        //TODO: Delete the item using the api
        //TODO: Reload the items for the to do list using the api
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
                        db.collection("ToDoLists")
                                .document(toDoListItem.getParentID())
                                .collection("Items")
                                .document(toDoListItem.getItemID())
                                .delete()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        loadToDoListItems();
                                    }
                                });
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