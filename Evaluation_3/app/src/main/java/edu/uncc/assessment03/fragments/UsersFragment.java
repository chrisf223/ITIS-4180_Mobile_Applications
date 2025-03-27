package edu.uncc.assessment03.fragments;

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

import java.util.ArrayList;

import edu.uncc.assessment03.R;
import edu.uncc.assessment03.UserAdapter;
import edu.uncc.assessment03.databinding.FragmentUsersBinding;
import edu.uncc.assessment03.models.CreditCategory;
import edu.uncc.assessment03.models.User;

public class UsersFragment extends Fragment {
    public UsersFragment() {
        // Required empty public constructor
    }

    FragmentUsersBinding binding;
    String selectedSort;
    String sortDirection = "ASC";
    CreditCategory selectedFilterCategory;
    private UserAdapter userAdapter;
    private ArrayList<User> userList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Users");

        userAdapter = new UserAdapter(userList, (UserAdapter.DeleteUserListener) getActivity());
        userList.clear();
        userList.addAll(mListener.getAllUsers());
        userAdapter.notifyDataSetChanged();
        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.add_new_user_action){
                    mListener.gotoAddUser();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        binding.imageViewSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSelectSort();
            }
        });

        binding.imageViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.gotoSelectFilter();
            }
        });

        binding.imageViewSortAsc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.imageViewSortDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        if(selectedFilterCategory == null){
            binding.textViewFilter.setText("N/A");
        } else {
            binding.textViewFilter.setText(selectedFilterCategory.getName() + " or Higher" );
        }

        if(selectedSort == null){
            selectedSort = "Name";
            sortDirection = "ASC";
        }
        binding.textViewSort.setText(selectedSort + " (" + sortDirection + ")");
    }

    UsersListener mListener;


    public void setSelectedSort(String selectedSort) {
        this.selectedSort = selectedSort;
    }

    public void setSelectedFilterCategory(CreditCategory selectedFilterCategory) {
        this.selectedFilterCategory = selectedFilterCategory;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UsersListener) {
            mListener = (UsersListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement UsersListener");
        }
    }

    private void updateSortAndFilterText() {
        if (selectedFilterCategory == null) {
            binding.textViewFilter.setText("N/A");
        } else {
            binding.textViewFilter.setText(selectedFilterCategory.getName() + " or Higher");
        }

        if (selectedSort == null) {
            selectedSort = "Name";
            sortDirection = "ASC";
        }
        binding.textViewSort.setText(selectedSort + " (" + sortDirection + ")");
    }

    public void updateUserList(ArrayList<User> updatedUsers) {
        this.userList.clear();
        this.userList.addAll(updatedUsers);
        userAdapter.notifyDataSetChanged();
    }



    public interface UsersListener {
        void gotoAddUser();
        void gotoSelectFilter();
        void gotoSelectSort();
        ArrayList<User> getAllUsers();
    }
}