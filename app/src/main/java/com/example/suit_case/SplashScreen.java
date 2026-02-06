package com.example.suit_case;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.suit_case.databinding.ActivitySplashScreenBinding;

public class SplashScreen extends AppCompatActivity {

    ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Animate the SuitCase logo by translating it along the X-axis and set the animation duration and start delay
        binding.logo.animate().translationX(2000).setDuration(2000).setStartDelay(2200);

        // Animate the SuitCase slogan text by translating it along the Y-axis and set the animation duration and start delay
        binding.splashTxt.animate().translationY(-1200).setDuration(2200).setStartDelay(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        }, 3000); // Wait for 3000 milliseconds (3 seconds) before transitioning to the login screen
    }
}