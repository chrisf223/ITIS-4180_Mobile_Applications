    package edu.uncc.assessment03;

    import android.os.Bundle;

    import androidx.activity.EdgeToEdge;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.graphics.Insets;
    import androidx.core.view.ViewCompat;
    import androidx.core.view.WindowInsetsCompat;

    import java.util.ArrayList;

    import edu.uncc.assessment03.fragments.AddUserFragment;
    import edu.uncc.assessment03.fragments.FilterFragment;
    import edu.uncc.assessment03.fragments.SelectStateFragment;
    import edu.uncc.assessment03.fragments.SortFragment;
    import edu.uncc.assessment03.fragments.UsersFragment;
    import edu.uncc.assessment03.models.CreditCategory;
    import edu.uncc.assessment03.models.State;
    import edu.uncc.assessment03.models.User;

    public class MainActivity extends AppCompatActivity implements UsersFragment.UsersListener, AddUserFragment.AddUserListener,
            SelectStateFragment.SelectStateListener, SortFragment.SortListener, FilterFragment.FilterListener, UserAdapter.DeleteUserListener {
        ArrayList<User> mUsers = new ArrayList<>();
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

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new UsersFragment(), "users-fragment")
                    .commit();
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
            return mUsers;
        }

        @Override
        public void sendBackNewUser(User user) {
            mUsers.add(user);
            UsersFragment usersFragment = (UsersFragment) getSupportFragmentManager().findFragmentByTag("users-fragment");
            if (usersFragment != null) {
                usersFragment.updateUserList(mUsers);
            }
            getSupportFragmentManager().popBackStack();
        }

        @Override
        public void onStateSelected(State state) {
            AddUserFragment addUserFragment = (AddUserFragment) getSupportFragmentManager().findFragmentByTag("add-user-fragment");
            if (addUserFragment != null) {
                addUserFragment.setSelectedState(state);
            }
            getSupportFragmentManager().popBackStack();
        }

        @Override
        public void onSortSelected(String sort) {
            UsersFragment usersFragment = (UsersFragment) getSupportFragmentManager().findFragmentByTag("users-fragment");
            if(usersFragment != null){
                usersFragment.setSelectedSort(sort);
            }
            getSupportFragmentManager().popBackStack();
        }

        @Override
        public void onFilterSelected(CreditCategory creditCategory) {
            UsersFragment usersFragment = (UsersFragment) getSupportFragmentManager().findFragmentByTag("users-fragment");
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
        public void onUserDelete(User user) {
            mUsers.remove(user);
            UsersFragment usersFragment = (UsersFragment) getSupportFragmentManager().findFragmentByTag("users-fragment");
            if (usersFragment != null) {
                usersFragment.updateUserList(mUsers);
            }
        }
    }