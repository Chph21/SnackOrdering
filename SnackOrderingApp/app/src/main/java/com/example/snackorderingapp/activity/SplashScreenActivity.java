package com.example.snackorderingapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.example.snackorderingapp.R;
import com.example.snackorderingapp.helper.StringResourceHelper;

@SuppressLint("CustomSplashScreen")
public class SplashScreenActivity extends AppCompatActivity {
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash_screen);

        // GET PREFERENCES:
        preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);

        // HANDLER METHOD TO REDIRECT TO LOGIN OR MAIN:
        new Handler().postDelayed(() -> {
            // CHECK IF USER IS AUTHENTICATED IF BLOCK:
            if(preferences.getBoolean("authenticated", false)) {
                goToMainIfAuthenticated();
            }else {
                goToLoginIfNotAuthenticated();
            }
        }, 2000);
    }

    public void goToMainIfAuthenticated(){
        Intent goToMainIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(goToMainIntent);
        finish();
    }

    public void goToLoginIfNotAuthenticated(){
        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}