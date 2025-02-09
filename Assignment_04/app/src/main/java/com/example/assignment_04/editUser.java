package com.example.assignment_04;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class editUser extends AppCompatActivity {

    TextView name;
    TextView email;
    RadioGroup radioGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);

        name = findViewById(R.id.user_name);
        email = findViewById(R.id.user_email);
        radioGroup = findViewById(R.id.radioGroup);
        User user = (User) getIntent().getSerializableExtra("user");

        if (user != null) {
            // Set existing data into the EditText fields and RadioGroup
            name.setText(user.getName());
            email.setText(user.getEmail());

            // Set the role RadioButton based on the current role
            if ("Student".equals(user.getRole())) {
                ((RadioButton) findViewById(R.id.studentRadio)).setChecked(true);
            } else if ("Employee".equals(user.getRole())) {
                ((RadioButton) findViewById(R.id.employeeRadio)).setChecked(true);
            }
            else {
                ((RadioButton) findViewById(R.id.otherRadio)).setChecked(true);
            }
        }

        findViewById(R.id.submitButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().isEmpty()) {
                    Toast.makeText(editUser.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
                else if (email.getText().toString().isEmpty()) {
                    Toast.makeText(editUser.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                }
                else {
                    String userName = name.getText().toString();
                    String userEmail = email.getText().toString();

                    int selectedRoleId = radioGroup.getCheckedRadioButtonId();
                    RadioButton selectedRoleButton = findViewById(selectedRoleId);
                    String userRole = selectedRoleButton.getText().toString();

                    User user = new User(userName, userEmail, userRole);

                    Intent intent = new Intent(editUser.this, profileActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);

                    finish();
                }
            }
        });

        findViewById(R.id.nextButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(editUser.this, profileActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);
            }
        });
    }
}