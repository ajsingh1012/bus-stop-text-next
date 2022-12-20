package com.example.busstoptextnext.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.busstoptextnext.R;

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // hide support action bar
        this.getSupportActionBar().hide();

        // Get elements
        imageView = findViewById(R.id.imageView_splash);
        progressBar = findViewById(R.id.progressBar);

        new Handler().postDelayed(() -> {
            // This method will be executed once the timer is over
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }, 2000);
    }
}