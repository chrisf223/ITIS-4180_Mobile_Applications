package com.example.assignment_03;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// Assignment 3
// Assignment_03
// DeCristo Franceschini
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView priceInput;
    TextView discount;
    TextView customValue;
    TextView finalDiscount;
    SeekBar customBar;
    RadioGroup radioGroup;
    int seekBarvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViewById(R.id.reset_button).setOnClickListener(this);
        findViewById(R.id.calculate_button).setOnClickListener(this);

        priceInput = findViewById(R.id.price_input);
        discount = findViewById(R.id.discount_number);
        finalDiscount = findViewById(R.id.final_value);
        customValue = findViewById(R.id.seekbar_value);
        radioGroup = findViewById(R.id.radioGroup);
        customBar = findViewById(R.id.seekBar);
        seekBarvalue = 25;

        customBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the red value TextView
                customValue.setText(String.valueOf(progress)+"%");
                seekBarvalue = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG, "Clicked Button");
        if (v.getId() == R.id.reset_button) {
            customBar.setProgress(25);
            priceInput.setText("");
            discount.setText("0.00");
            finalDiscount.setText("0.00");
            radioGroup.check(R.id.ten_button);
        }

        else if (v.getId() == R.id.calculate_button) {
            if (priceInput.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a number", Toast.LENGTH_LONG).show();
            }
            else if (radioGroup.getCheckedRadioButtonId() == R.id.ten_button){
                double temp = Double.parseDouble(priceInput.getText().toString());
                double discountTotal = temp * .1;
                double newTotal = temp * .9;
                discount.setText(String.format("%.2f", discountTotal));
                finalDiscount.setText(String.format("%.2f", newTotal));
            }
            else if (radioGroup.getCheckedRadioButtonId() == R.id.fifteen_button){
                double temp = Double.parseDouble(priceInput.getText().toString());
                double discountTotal = temp * .15;
                double newTotal = temp * .85;
                discount.setText(String.format("%.2f", discountTotal));
                finalDiscount.setText(String.format("%.2f", newTotal));
            }
            else if (radioGroup.getCheckedRadioButtonId() == R.id.eighteen_button){
                double temp = Double.parseDouble(priceInput.getText().toString());
                double discountTotal = temp * .18;
                double newTotal = temp * .82;
                discount.setText(String.format("%.2f", discountTotal));
                finalDiscount.setText(String.format("%.2f", newTotal));
            }
            else if (radioGroup.getCheckedRadioButtonId() == R.id.custom_button){
                double temp = Double.parseDouble(priceInput.getText().toString());
                double discountTotal = temp * ((double) seekBarvalue /100);
                double newTotal = temp * ((100-(double) seekBarvalue) /100);
                discount.setText(String.format("%.2f", discountTotal));
                finalDiscount.setText(String.format("%.2f", newTotal));
            }
        }
    }
}