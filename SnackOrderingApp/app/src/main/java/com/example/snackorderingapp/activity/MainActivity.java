package com.example.snackorderingapp.activity;

import static android.widget.Toast.makeText;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snackorderingapp.ApiService;
import com.example.snackorderingapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private ApiService apiService;
    public static String accessToken;
    private String refreshToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize RecyclerView for popular snacks
        RecyclerView popularSnacksRecyclerView = findViewById(R.id.popularSnacksRecyclerView);
        // Set up adapter and layout manager

        // Set up search button
        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // Set up cart button
        Button cartButton = findViewById(R.id.cartButton);
        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });

        // Set up all snacks button
        Button allSnacksButton = findViewById(R.id.allSnacksButton);
        allSnacksButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AllSnacksActivity.class);
            intent.putExtra("ACCESS_TOKEN", accessToken);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        login(null);
    }

    public void login(View v) {
        apiService = new ApiService(this, null); // Initialize without token for login
        apiService.login("+84123456789", "admin", new ApiService.ApiCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                // Handle successful login
                try {
                    accessToken = result.getString("access_token");
                    refreshToken = result.getString("refresh_token");
                    // Store tokens or update UI
                    makeText(MainActivity.this, "Successfully connected to backend!", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "Received access_token: " + accessToken);
                    Log.d("MainActivity", "Received refresh_token: " + refreshToken);
                    apiService = new ApiService(MainActivity.this, accessToken); // Reinitialize with token
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String error) {
                // Handle login error
                Log.d("MainActivity", "Error: " + error);
                makeText(MainActivity.this, "Failed to connect to backend", Toast.LENGTH_SHORT).show();
            }
        });
    }
}