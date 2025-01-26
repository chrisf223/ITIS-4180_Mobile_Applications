package com.example.assignment_1b;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//Assignment 01B
//assignment_1b
//DeCristo Franceschini
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    TextView enterTemp;
    TextView convertedTemp;
    TextView tempType;
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.calculateButton).setOnClickListener(this);
        findViewById(R.id.resetButton).setOnClickListener(this);
        radioGroup = findViewById(R.id.radioGroup);

        enterTemp = findViewById(R.id.enterTemp);
        convertedTemp = findViewById(R.id.convertedTempValue);
        tempType = findViewById(R.id.tempType);

    }


    @Override
    public void onClick(View v) {
        Log.d(TAG, "Clicked Button");
        if (v.getId() == R.id.calculateButton) {
            if (enterTemp.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a number", Toast.LENGTH_LONG).show();
            }
            else if (radioGroup.getCheckedRadioButtonId() == R.id.cToF){
                Double C = Double.parseDouble(enterTemp.getText().toString());
                Double F = (C * (9.0 / 5.0)) + 32;
                convertedTemp.setText(String.format("%.1f", F));
                tempType.setText("F");
            }

            else if (radioGroup.getCheckedRadioButtonId() == R.id.fToC) {
                Double F = Double.parseDouble(enterTemp.getText().toString());
                Double C = (F-32.0)*(5.0/9.0);
                convertedTemp.setText(String.format("%.2f", C));
                tempType.setText("C");
            }
            else {
                Toast.makeText(this, "Please select a radio button", Toast.LENGTH_LONG).show();
            }
        }
        else if (v.getId() == R.id.resetButton) {
            enterTemp.setText("");
            tempType.setText("");
            convertedTemp.setText("");
            radioGroup.clearCheck();
        }
    }
}