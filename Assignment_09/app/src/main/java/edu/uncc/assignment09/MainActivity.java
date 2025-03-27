package edu.uncc.assignment09;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.uncc.assignment09.fragments.AddUserFragment;
import edu.uncc.assignment09.fragments.FilterFragment;
import edu.uncc.assignment09.fragments.SelectStateFragment;
import edu.uncc.assignment09.fragments.SortFragment;
import edu.uncc.assignment09.fragments.UsersFragment;
import edu.uncc.assignment09.models.AppDatabase;
import edu.uncc.assignment09.models.CreditCategory;
import edu.uncc.assignment09.models.State;
import edu.uncc.assignment09.models.User;
import edu.uncc.assignment09.models.UserDao;

public class MainActivity extends AppCompatActivity implements UsersFragment.UsersListener,
        AddUserFragment.AddUserListener, SelectStateFragment.SelectStateListener,
        SortFragment.SortListener, FilterFragment.FilterListener {

    private AppDatabase db;
    private ExecutorService executorService;
    private ArrayList<User> mUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize database and executor
        db = AppDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();

        // Load initial data before showing fragment
        loadInitialData();
    }

    private void loadInitialData() {
        executorService.execute(() -> {
            List<User> users = db.userDao().getAllUsers();
            runOnUiThread(() -> {
                mUsers.clear();
                mUsers.addAll(users);
                // Now show the fragment after data is loaded
                showUsersFragment();
            });
        });
    }

    private void showUsersFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new UsersFragment(), "users-fragment")
                .commit();
    }

    private void loadUsersFromDatabase() {
        executorService.execute(() -> {
            List<User> users = db.userDao().getAllUsers();
            runOnUiThread(() -> {
                mUsers.clear();
                mUsers.addAll(users);

                // Notify the UsersFragment to refresh
                UsersFragment usersFragment = (UsersFragment) getSupportFragmentManager()
                        .findFragmentByTag("users-fragment");
                if (usersFragment != null) {
                    usersFragment.refreshUsers();
                }
            });
        });
    }

    @Override
    public void gotoAddUser() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new AddUserFragment(), "add-user-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoSelectFilter() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new FilterFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void gotoSelectSort() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new SortFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(mUsers); // Return a copy to prevent concurrent modification
    }

    @Override
    public void deleteUser(User user) {
        executorService.execute(() -> {
            db.userDao().delete(user);
            loadUsersFromDatabase();
        });
    }

    @Override
    public void sendBackNewUser(User user) {
        executorService.execute(() -> {
            db.userDao().insert(user);
            loadUsersFromDatabase();
        });
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onStateSelected(State state) {
        AddUserFragment addUserFragment = (AddUserFragment) getSupportFragmentManager()
                .findFragmentByTag("add-user-fragment");
        if(addUserFragment != null){
            addUserFragment.setSelectedState(state);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onSortSelected(String sort) {
        UsersFragment usersFragment = (UsersFragment) getSupportFragmentManager()
                .findFragmentByTag("users-fragment");
        if(usersFragment != null){
            usersFragment.setSelectedSort(sort);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onFilterSelected(CreditCategory creditCategory) {
        UsersFragment usersFragment = (UsersFragment) getSupportFragmentManager()
                .findFragmentByTag("users-fragment");
        if(usersFragment != null){
            usersFragment.setSelectedFilterCategory(creditCategory);
        }
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void cancelAddOrSelection() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void gotoSelectState() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new SelectStateFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}