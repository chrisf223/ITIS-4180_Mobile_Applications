package com.example.assignment_05;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements Welcome_Fragment.WelcomeListener, User_Fragment.UserListener, Profile_Fragment.ProfileListener, Edit_Fragment.EditListener {

    User mainUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.containerView, new Welcome_Fragment())
                .commit();
    }

    @Override
    public void goToUser() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, new User_Fragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToProfile() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, Profile_Fragment.newInstance(mainUser),"profile-fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void returnToProfile(User user) {
        Profile_Fragment profileFragment = (Profile_Fragment) getSupportFragmentManager().findFragmentByTag("profile-fragment");
        if (profileFragment != null) {
            Profile_Fragment.setData(user);
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void sendUserData(User user) {
        mainUser = user;
    }

    @Override
    public void cancelEdit() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void goToEdit() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.containerView, Edit_Fragment.newInstance(mainUser))
                .addToBackStack(null)
                .commit();
    }
}