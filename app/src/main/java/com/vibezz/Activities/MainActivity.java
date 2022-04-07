package com.vibezz.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.vibezz.databinding.ActivityMainBinding;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Hide the Action Bar / Name of the App
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Splash_Screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Switching from MainActivity / Splash_Screen to SignUpActivity
                Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                startActivity(intent);
                finish();
            }
        }, 2000);

    }
}