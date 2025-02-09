package com.example.evaluation_1;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
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
// DeCristo Franceschini
//

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    TextView weight;
    TextView beerCount;
    TextView wineCount;
    TextView liquorCount;
    TextView maltCount;
    TextView bacLevel;
    RadioGroup radioGroup;
    TextView safetyText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViewById(R.id.beerMinus).setOnClickListener(this);
        findViewById(R.id.beerPlus).setOnClickListener(this);
        findViewById(R.id.wineMinus).setOnClickListener(this);
        findViewById(R.id.winePlus).setOnClickListener(this);
        findViewById(R.id.liquorMinus).setOnClickListener(this);
        findViewById(R.id.liquorPlus).setOnClickListener(this);
        findViewById(R.id.maltMinus).setOnClickListener(this);
        findViewById(R.id.maltPlus).setOnClickListener(this);
        findViewById(R.id.calculateButton).setOnClickListener(this);
        findViewById(R.id.resetButton).setOnClickListener(this);


        beerCount = findViewById(R.id.beerNumber);
        wineCount = findViewById(R.id.wineNumber);
        liquorCount = findViewById(R.id.liquorNumber);
        maltCount = findViewById(R.id.maltNumber);
        weight = findViewById(R.id.weightValue);
        bacLevel = findViewById(R.id.bacLevel);
        radioGroup = findViewById(R.id.radioGroup);
        safetyText = findViewById(R.id.safety);


    }

    public void onClick(View v) {
        Log.d(TAG, "Clicked Button");
        if (v.getId() == R.id.beerMinus) {
            int temp = (int) Double.parseDouble(beerCount.getText().toString());
            if (temp > 0) {
                temp = temp - 1;
                beerCount.setText(String.format(String.valueOf(temp)));
            }
        }
        else if (v.getId() == R.id.beerPlus) {
            int temp = (int) Double.parseDouble(beerCount.getText().toString());
            if (temp < 10) {
                temp = temp + 1;
                beerCount.setText(String.format(String.valueOf(temp)));
            }
        }
        else if (v.getId() == R.id.wineMinus) {
            int temp = (int) Double.parseDouble(wineCount.getText().toString());
            if (temp > 0) {
                temp = temp - 1;
                wineCount.setText(String.format(String.valueOf(temp)));
            }
        }
        else if (v.getId() == R.id.winePlus) {
            int temp = (int) Double.parseDouble(wineCount.getText().toString());
            if (temp < 10) {
                temp = temp + 1;
                wineCount.setText(String.format(String.valueOf(temp)));
            }
        }
        else if (v.getId() == R.id.liquorMinus) {
            int temp = (int) Double.parseDouble(liquorCount.getText().toString());
            if (temp > 0) {
                temp = temp - 1;
                liquorCount.setText(String.format(String.valueOf(temp)));
            }
        }
        else if (v.getId() == R.id.liquorPlus) {
            int temp = (int) Double.parseDouble(liquorCount.getText().toString());
            if (temp < 10) {
                temp = temp + 1;
                liquorCount.setText(String.format(String.valueOf(temp)));
            }
        }
        else if (v.getId() == R.id.maltMinus) {
            int temp = (int) Double.parseDouble(maltCount.getText().toString());
            if (temp > 0) {
                temp = temp - 1;
                maltCount.setText(String.format(String.valueOf(temp)));
            }
        }
        else if (v.getId() == R.id.maltPlus) {
            int temp = (int) Double.parseDouble(maltCount.getText().toString());
            if (temp < 10) {
                temp = temp + 1;
                maltCount.setText(String.format(String.valueOf(temp)));
            }
        }
        else if (v.getId() == R.id.calculateButton) {
            if (weight.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter a number", Toast.LENGTH_LONG).show();
            }
            else {
                double total;
                double r;
                double w = Double.parseDouble(weight.getText().toString());
                double a = (Double.parseDouble(beerCount.getText().toString()) * 12.0 * (5.0/100.0)) + (Double.parseDouble(wineCount.getText().toString()) * 5.0 * (12.0/100.0)) + (Double.parseDouble(liquorCount.getText().toString()) * 1.5 * (40.0/100.0)) + (Double.parseDouble(maltCount.getText().toString()) * 9.0 * (7.0/100.0));
                if (radioGroup.getCheckedRadioButtonId() == R.id.femaleButton) {
                    r = 0.66;
                }
                else  {
                    r = 0.73;
                }
                total = a * 5.14 / (w * r);
                bacLevel.setText(String.format("%.3f", total));
                if (total >= 0.0 && total <= 0.08) {
                    safetyText.setText("You're safe");
                    safetyText.setBackgroundColor(Color.rgb(76,175,80));
                }
                else if (total > 0.08 && total <= 0.2) {
                    safetyText.setText("Be Careful");
                    safetyText.setBackgroundColor(Color.rgb(255,193,7));
                }
                else if (total > 0.2) {
                    safetyText.setText("Over the limit!");
                    safetyText.setBackgroundColor(Color.rgb(244,67,54));
                }
            }
        }
        else if (v.getId() == R.id.resetButton) {
            safetyText.setText("You're safe");
            safetyText.setBackgroundColor(Color.rgb(76,175,80));
            radioGroup.check(R.id.femaleButton);
            weight.setText("");
            wineCount.setText("0");
            beerCount.setText("0");
            liquorCount.setText("0");
            maltCount.setText("0");
            bacLevel.setText("0.00");
        }



    }
}