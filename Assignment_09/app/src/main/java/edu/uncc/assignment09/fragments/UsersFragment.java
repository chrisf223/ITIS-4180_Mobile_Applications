package edu.uncc.assignment09.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import edu.uncc.assignment09.R;
import edu.uncc.assignment09.databinding.FragmentUsersBinding;
import edu.uncc.assignment09.databinding.RowItemUserBinding;
import edu.uncc.assignment09.models.CreditCategory;
import edu.uncc.assignment09.models.User;

public class UsersFragment extends Fragment {
    public UsersFragment() {
        // Required empty public constructor
    }

    FragmentUsersBinding binding;
    String selectedSort;
    String sortDirection = "ASC";
    CreditCategory selectedFilterCategory;
    ArrayList<User> mUsers = new ArrayList<>();
    ArrayList<User> mFilteredUsers = new ArrayList<>();
    UsersAdapter adapter = new UsersAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Users");

        mUsers = mListener.getAllUsers();
        filterAndSortUsers();

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
                sortDirection = "ASC";
                filterAndSortUsers();
                updateSortLabel();
            }
        });

        binding.imageViewSortDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sortDirection = "DESC";
                filterAndSortUsers();
                updateSortLabel();
            }
        });

        updateFilterLabel();
        updateSortLabel();

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.recyclerView.setAdapter(adapter);
    }

    private void filterAndSortUsers() {
        // Filter users based on selected category
        mFilteredUsers = new ArrayList<>();
        if (selectedFilterCategory == null) {
            mFilteredUsers.addAll(mUsers);
        } else {
            String categoryName = selectedFilterCategory.getName();
            for (User user : mUsers) {
                int creditScore = user.getCreditScore();

                if (categoryName.equals("Poor") && creditScore >= 300) {
                    mFilteredUsers.add(user);
                } else if (categoryName.equals("Fair") && creditScore >= 580) {
                    mFilteredUsers.add(user);
                } else if (categoryName.equals("Good") && creditScore >= 670) {
                    mFilteredUsers.add(user);
                } else if (categoryName.equals("Very Good") && creditScore >= 740) {
                    mFilteredUsers.add(user);
                } else if (categoryName.equals("Excellent") && creditScore >= 800) {
                    mFilteredUsers.add(user);
                }
            }
        }

        // Sort users based on selected criteria and direction
        if (selectedSort != null) {
            Comparator<User> comparator = null;
            switch (selectedSort) {
                case "Name":
                    comparator = Comparator.comparing(User::getName);
                    break;
                case "Age":
                    comparator = Comparator.comparingInt(User::getAge);
                    break;
                case "State":
                    comparator = Comparator.comparing(User::getState);
                    break;
                case "Credit Score":
                    comparator = Comparator.comparingInt(User::getCreditScore);
                    break;
            }

            if (comparator != null) {
                if (sortDirection.equals("DESC")) {
                    comparator = comparator.reversed();
                }
                Collections.sort(mFilteredUsers, comparator);
            }
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    private void updateFilterLabel() {
        if (selectedFilterCategory == null) {
            binding.textViewFilter.setText("N/A");
        } else {
            binding.textViewFilter.setText(selectedFilterCategory.getName() + " or Higher");
        }
    }

    private void updateSortLabel() {
        if (selectedSort == null) {
            binding.textViewSort.setText("N/A");
        } else {
            binding.textViewSort.setText(selectedSort + " (" + sortDirection + ")");
        }
    }

    class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
        @NonNull
        @Override
        public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            RowItemUserBinding itemBinding = RowItemUserBinding.inflate(getLayoutInflater(), parent, false);
            return new UserViewHolder(itemBinding);
        }

        @Override
        public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
            User user = mFilteredUsers.get(position);
            holder.bind(user);
        }

        @Override
        public int getItemCount() {
            return mFilteredUsers.size();
        }

        class UserViewHolder extends RecyclerView.ViewHolder {
            RowItemUserBinding itemBinding;
            User mUser;
            public UserViewHolder(RowItemUserBinding itemBinding) {
                super(itemBinding.getRoot());
                this.itemBinding = itemBinding;
            }

            public void bind(User user) {
                this.mUser = user;
                itemBinding.textViewName.setText(user.getName());
                itemBinding.textViewAge.setText(user.getAge() + " years old");
                itemBinding.textViewState.setText(user.getState());

                itemBinding.textViewCreditScore.setText(String.valueOf(user.getCreditScore()));
                if(user.getCreditScore() <= 579){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.poor);
                } else if(user.getCreditScore() <= 669){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.fair);
                } else if(user.getCreditScore() <= 739){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.good);
                } else if(user.getCreditScore() <= 799){
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.very_good);
                } else {
                    itemBinding.imageViewCreditScore.setImageResource(R.drawable.excellent);
                }

                itemBinding.imageViewDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mListener.deleteUser(mUser);
                        mUsers = mListener.getAllUsers();
                        filterAndSortUsers();
                    }
                });
            }
        }
    }

    UsersListener mListener;

    public void setSelectedSort(String selectedSort) {
        this.selectedSort = selectedSort;
        filterAndSortUsers();
        updateSortLabel();
    }

    public void setSelectedFilterCategory(CreditCategory selectedFilterCategory) {
        this.selectedFilterCategory = selectedFilterCategory;
        filterAndSortUsers();
        updateFilterLabel();
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

    public void refreshUsers() {
        mUsers = mListener.getAllUsers();
        filterAndSortUsers();
    }

    public interface UsersListener {
        void gotoAddUser();
        void gotoSelectFilter();
        void gotoSelectSort();
        ArrayList<User> getAllUsers();
        void deleteUser(User user);
    }
}