package com.example.assignment_04;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class profileActivity extends AppCompatActivity {
TextView name;
TextView email;
TextView role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        name = findViewById(R.id.profileName);
        email = findViewById(R.id.profileEmail);
        role = findViewById(R.id.profileRole);

        User user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            name.setText(user.getName());
            email.setText(user.getEmail());
            role.setText(user.getRole());
        }

        findViewById(R.id.updateButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profileActivity.this, editUser.class);
                intent.putExtra("user", user);  // Pass the current User object to EditUserActivity
                startActivity(intent);
            }
        });
    }
}