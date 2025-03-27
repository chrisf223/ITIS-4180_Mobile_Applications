package com.example.evaluation_2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.evaluation_2.databinding.FragmentCalorieCalculatorBinding;

public class MainActivity extends AppCompatActivity implements CalorieCalculatorFragment.calculatorListener, GenderFragment.genderListener, WeightFragment.weightListener, HeightFragment.heightListener, AgeFragment.ageListener, ActivityLevelFragment.activityLevelListener {

    Calorie CaloriesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new CalorieCalculatorFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToGender() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new GenderFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToWeight() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new WeightFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToHeight() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new HeightFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToAge() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new AgeFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void goToActivityLevel() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new ActivityLevelFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void calculate() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main, new CalorieDetailsFragment())
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancel() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void submitLightlyActive() {
        getSupportFragmentManager().popBackStack();

    }

    @Override
    public void submitModeratelyActive() {
        getSupportFragmentManager().popBackStack();

    }

    @Override
    public void submitVeryActive() {
        getSupportFragmentManager().popBackStack();

    }

    @Override
    public void submitSuperActive() {
        getSupportFragmentManager().popBackStack();

    }

    @Override
    public void submitSedentary() {
        getSupportFragmentManager().popBackStack();

    }

    @Override
    public void submitAge() {
        getSupportFragmentManager().popBackStack();

    }

    @Override
    public void submitHeight() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void submitWeight() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void submitGender(String gender) {
        getSupportFragmentManager().popBackStack();
        CaloriesData.setGender(gender);
    }
}