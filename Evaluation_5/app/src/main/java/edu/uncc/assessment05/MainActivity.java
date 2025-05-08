package edu.uncc.assessment05;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;

import edu.uncc.assessment05.fragments.auth.LoginFragment;
import edu.uncc.assessment05.fragments.auth.SignUpFragment;
import edu.uncc.assessment05.fragments.products.PodcastsFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.LoginListener, SignUpFragment.SignUpListener, PodcastsFragment.PodcastsListener {

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

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new LoginFragment())
                    .commit();
        }
        else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new PodcastsFragment())
                    .commit();
        }
    }

    @Override
    public void gotoSignUpUser() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new SignUpFragment())
                .commit();
    }

    @Override
    public void onLoginSuccessful() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new PodcastsFragment())
                .commit();
    }

    @Override
    public void gotoLogin() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new LoginFragment())
                .commit();
    }

    @Override
    public void onSignUpSuccessful() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new PodcastsFragment())
                .commit();
    }

    @Override
    public void gotoFavorites() {

    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new LoginFragment())
                .commit();
    }
}