package edu.uncc.assessment04;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import edu.uncc.assessment04.fragments.todo.AddItemToToDoListFragment;
import edu.uncc.assessment04.fragments.users.AddUserFragment;
import edu.uncc.assessment04.fragments.todo.CreateNewToDoListFragment;
import edu.uncc.assessment04.fragments.users.PassCodeFragment;
import edu.uncc.assessment04.fragments.todo.ToDoListDetailsFragment;
import edu.uncc.assessment04.fragments.todo.ToDoListsFragment;
import edu.uncc.assessment04.fragments.users.UsersFragment;
import edu.uncc.assessment04.models.ToDoList;
import edu.uncc.assessment04.models.ToDoListItem;
import edu.uncc.assessment04.models.User;

import androidx.room.Room;
import edu.uncc.assessment04.models.AppDatabase;
import edu.uncc.assessment04.models.UserDao;

public class MainActivity extends AppCompatActivity implements UsersFragment.UsersListener, AddUserFragment.AddUserListener,
        PassCodeFragment.PassCodeListener, ToDoListsFragment.ToDoListsListener, ToDoListDetailsFragment.ToDoListDetailsListener,
        CreateNewToDoListFragment.CreateNewToDoListListener, AddItemToToDoListFragment.AddItemToListListener {
    User currentUser;
    private static AppDatabase db;

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

        if(currentUser == null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new UsersFragment())
                    .commit();
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new ToDoListsFragment())
                    .commit();
        }

        db = Room.databaseBuilder(getApplicationContext(),
                        AppDatabase.class, "todo-db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public static AppDatabase getDbInstance() {
        return db;
    }

    //UsersFragment.UsersListener
    @Override
    public void gotoEnterPassCode(User user) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, PassCodeFragment.newInstance(user))
                .commit();
    }

    //UsersFragment.UsersListener
    @Override
    public void gotoAddUser() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new AddUserFragment())
                .commit();
    }

    //UsersFragment.UsersListener
    @Override
    public ArrayList<User> getAllUsers() {
        //TODO: Fetch all the users from the database

        return new ArrayList<>(db.userDao().getAllUsers());
    }

    //AddUserFragment.AddUserListener
    @Override
    public void saveNewUser(User user) {
        //TODO: save the new user to the database

        new Thread(() -> {
            db.userDao().insert(user);
            runOnUiThread(() -> {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main, new UsersFragment())
                        .commit();
            });
        }).start();
    }

    //AddUserFragment.AddUserListener
    @Override
    public void cancelAddUser() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new UsersFragment())
                .commit();
    }

    //PassCodeFragment.PassCodeListener
    @Override
    public void onPasscodeSuccessful(User user) {
        currentUser = user;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new ToDoListsFragment())
                .commit();
    }

    //PassCodeFragment.PassCodeListener
    @Override
    public void onPassCodeCancel() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new UsersFragment())
                .commit();
    }

    //ToDoListsFragment.ToDoListsListener
    @Override
    public User getCurrentUser() {
        return currentUser;
    }

    //ToDoListsFragment.ToDoListsListener
    @Override
    public void gotoAddNewToDoList() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new CreateNewToDoListFragment())
                .commit();
    }

    //ToDoListsFragment.ToDoListsListener
    @Override
    public void logout() {
        currentUser = null;
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new UsersFragment())
                .commit();
    }

    //ToDoListsFragment.ToDoListsListener
    @Override
    public void gotoListDetails(ToDoList toDoList) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, ToDoListDetailsFragment.newInstance(toDoList))
                .commit();
    }

    //ToDoListsFragment.ToDoListsListener
    @Override
    public ArrayList<ToDoList> getAllToDoListsForUser(User user) {
        //TODO: Fetch the ToDoLists from the database for the given user

        return new ArrayList<>(db.toDoListDao().getAllListsForUser(user.getId()));
    }

    //ToDoListsFragment.ToDoListsListener
    @Override
    public void deleteToDoList(ToDoList toDoList) {
        //TODO: Delete the ToDoList from the database
        db.toDoListDao().delete(toDoList);
    }

    //ToDoListDetailsFragment.ToDoListDetailsListener
    @Override
    public void gotoAddListItem(ToDoList toDoList) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, AddItemToToDoListFragment.newInstance(toDoList))
                .commit();
    }

    //ToDoListDetailsFragment.ToDoListDetailsListener
    @Override
    public ArrayList<ToDoListItem> getAllItemsForToDoList(ToDoList toDoList) {
        //TODO: Fetch the ToDoListItems from the database for the given ToDoList

        return new ArrayList<>();
    }

    //ToDoListDetailsFragment.ToDoListDetailsListener
    @Override
    public void goBackToToDoLists() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new ToDoListsFragment())
                .commit();
    }

    //ToDoListDetailsFragment.ToDoListDetailsListener
    @Override
    public void deleteToDoListItem(ToDoList toDoList, ToDoListItem toDoListItem) {
        //TODO: Delete the ToDoListItem from the database
    }

    //CreateNewToDoListFragment.CreateNewToDoListListener
    @Override
    public void onCreateNewToDoList(User user, String listName) {
        //TODO: Save the new ToDoList to the database
        int userId = user.getId();

        ToDoList newList = new ToDoList(listName, userId);

        db.toDoListDao().insert(newList);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new ToDoListsFragment())
                .commit();
    }

    //CreateNewToDoListFragment.CreateNewToDoListListener
    @Override
    public void onCancelCreateNewToDoList() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new ToDoListsFragment())
                .commit();
    }

    //AddItemToToDoListFragment.AddItemToListListener
    @Override
    public void onAddItemToList(User user, ToDoList todoList, String itemName, String priority) {
        //TODO: Save the new ToDoListItem to the database

        ToDoListItem newItem = new ToDoListItem(itemName,priority);
        db.toDoListItemDao().
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, ToDoListDetailsFragment.newInstance(todoList))
                .commit();
    }

    //AddItemToToDoListFragment.AddItemToListListener
    @Override
    public void onCancelAddItemToList(ToDoList todoList) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, ToDoListDetailsFragment.newInstance(todoList))
                .commit();
    }
}