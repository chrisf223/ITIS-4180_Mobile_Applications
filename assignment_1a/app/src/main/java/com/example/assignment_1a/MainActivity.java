package com.example.assignment_1a;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//Assignment 01A
//Assignment_1A
//DeCristo Franceschini

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView enterTemp;
    TextView convertedTemp;
    TextView tempType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.cToFButton).setOnClickListener(this);
        findViewById(R.id.fToCButton).setOnClickListener(this);
        findViewById(R.id.resetButton).setOnClickListener(this);

        enterTemp = findViewById(R.id.enterTemp);
        convertedTemp = findViewById(R.id.convertedTempValue);
        tempType = findViewById(R.id.tempType);

    }


    @Override
    public void onClick(View v) {
        Log.d(TAG, "Clicked Button");
        if (v.getId() == R.id.cToFButton) {
            if (enterTemp.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a number", Toast.LENGTH_LONG).show();
            }
            else {
                Double C = Double.parseDouble(enterTemp.getText().toString());
                Double F = (C * (9.0 / 5.0)) + 32;
                convertedTemp.setText(String.format("%.1f", F));
                tempType.setText("F");
            }
        }
        else if (v.getId() == R.id.fToCButton) {
            if (enterTemp.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a number", Toast.LENGTH_LONG).show();
            }
            else {
                Double F = Double.parseDouble(enterTemp.getText().toString());
                Double C = (F-32.0)*(5.0/9.0);
                convertedTemp.setText(String.format("%.2f", C));
                tempType.setText("C");
            }
        }
        else if (v.getId() == R.id.resetButton) {
            enterTemp.setText("");
            tempType.setText("");
            convertedTemp.setText("");
        }
    }
}