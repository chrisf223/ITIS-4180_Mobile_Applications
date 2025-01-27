package com.example.assignment_02;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//Assignment 2
//File: Assignment_02
//DeCristo Franceschini
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize SeekBars
        SeekBar redBar = findViewById(R.id.redBar);
        SeekBar greenBar = findViewById(R.id.greenBar);
        SeekBar blueBar = findViewById(R.id.blueBar);

        // Set initial progress values
        redBar.setProgress(64);
        greenBar.setProgress(128);
        blueBar.setProgress(0);

        // Initialize TextViews
        TextView redValue = findViewById(R.id.redvalue);
        TextView greenValue = findViewById(R.id.greenValue);
        TextView blueValue = findViewById(R.id.blueValue);
        TextView hexValue = findViewById(R.id.hexValue);
        TextView rgbValue = findViewById(R.id.rgbValue);

        // Display the initial color (RGB: 64, 128, 0)
        ImageView colorView = findViewById(R.id.imageView);
        colorView.setBackgroundColor(Color.rgb(64, 128, 0));


        Button resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> {
            // Reset SeekBars to initial values
            redBar.setProgress(64);
            greenBar.setProgress(128);
            blueBar.setProgress(0);

            // Reset TextViews to display initial values
            redValue.setText("64");
            greenValue.setText("128");
            blueValue.setText("0");

            // Update the color display
            colorView.setBackgroundColor(Color.rgb(64, 128, 0));

            // Update RGB and HEX values
            rgbValue.setText("RGB: (64, 128, 0)");
            hexValue.setText("HEX: #408000");
        });

        redBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the red value TextView
                redValue.setText(String.valueOf(progress));

                // Update the displayed color
                colorView.setBackgroundColor(Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));

                // Update the RGB and HEX value TextViews
                rgbValue.setText("RGB: (" + redBar.getProgress() + ", " + greenBar.getProgress() + ", " + blueBar.getProgress() + ")");
                hexValue.setText(String.format("HEX: #%02X%02X%02X", redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        greenBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the green value TextView
                greenValue.setText(String.valueOf(progress));

                // Update the displayed color
                colorView.setBackgroundColor(Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));

                // Update the RGB and HEX value TextViews
                rgbValue.setText("RGB: (" + redBar.getProgress() + ", " + greenBar.getProgress() + ", " + blueBar.getProgress() + ")");
                hexValue.setText(String.format("HEX: #%02X%02X%02X", redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        blueBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // Update the blue value TextView
                blueValue.setText(String.valueOf(progress));

                // Update the displayed color
                colorView.setBackgroundColor(Color.rgb(redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));

                // Update the RGB and HEX value TextViews
                rgbValue.setText("RGB: (" + redBar.getProgress() + ", " + greenBar.getProgress() + ", " + blueBar.getProgress() + ")");
                hexValue.setText(String.format("HEX: #%02X%02X%02X", redBar.getProgress(), greenBar.getProgress(), blueBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        Button whiteButton = findViewById(R.id.whiteButton);
        whiteButton.setOnClickListener(v -> {
            // Set SeekBars to white color values (255, 255, 255)
            redBar.setProgress(255);
            greenBar.setProgress(255);
            blueBar.setProgress(255);

            // Update TextViews
            redValue.setText("255");
            greenValue.setText("255");
            blueValue.setText("255");

            // Update the color display
            colorView.setBackgroundColor(Color.rgb(255, 255, 255));

            // Update RGB and HEX values
            rgbValue.setText("RGB: (255, 255, 255)");
            hexValue.setText("HEX: #FFFFFF");
        });

        Button blackButton = findViewById(R.id.blackButton);
        blackButton.setOnClickListener(v -> {
            // Set SeekBars to black color values (0, 0, 0)
            redBar.setProgress(0);
            greenBar.setProgress(0);
            blueBar.setProgress(0);

            // Update TextViews
            redValue.setText("0");
            greenValue.setText("0");
            blueValue.setText("0");

            // Update the color display
            colorView.setBackgroundColor(Color.rgb(0, 0, 0));

            // Update RGB and HEX values
            rgbValue.setText("RGB: (0, 0, 0)");
            hexValue.setText("HEX: #000000");
        });

        Button blueButton = findViewById(R.id.blueButton);
        blueButton.setOnClickListener(v -> {
            // Set SeekBars to blue color values (0, 0, 255)
            redBar.setProgress(0);
            greenBar.setProgress(0);
            blueBar.setProgress(255);

            // Update TextViews
            redValue.setText("0");
            greenValue.setText("0");
            blueValue.setText("255");

            // Update the color display
            colorView.setBackgroundColor(Color.rgb(0, 0, 255));

            // Update RGB and HEX values
            rgbValue.setText("RGB: (0, 0, 255)");
            hexValue.setText("HEX: #0000FF");
        });


    }

}