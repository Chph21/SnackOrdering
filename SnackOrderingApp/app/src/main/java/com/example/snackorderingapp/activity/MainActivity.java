package com.example.snackorderingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.MyVolleySingletonUtil;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.adapter.SnackAdapter;
import com.example.snackorderingapp.helper.ApiLinksHelper;
import com.example.snackorderingapp.helper.StringResourceHelper;
import com.example.snackorderingapp.model.Snack;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private RequestQueue requestQueue;
    private List<Snack> snacks;
    private RecyclerView recyclerViewSnacks;
    private SnackAdapter snackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = MyVolleySingletonUtil.getInstance(MainActivity.this).getRequestQueue();

        SharedPreferences preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);
        String accessToken = preferences.getString("access_token", null);
        String refreshToken = preferences.getString("refresh_token", null);

        if (accessToken != null) {
            // Use the access token as needed
            Log.d("MainActivity", "Access Token: " + accessToken);
        } else {
            // Handle the case where the token is not available
            Log.d("MainActivity", "No access token found");
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    return true;
                case R.id.bottom_order:
                    startActivity(new Intent(getApplicationContext(), OrderActivity.class));
                    finish();
                    return true;
                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    return true;
            }
            return false;
        });

        recyclerViewSnacks = findViewById(R.id.recyclerViewSnacks);
        recyclerViewSnacks.setLayoutManager(new LinearLayoutManager(this));

        snacks = new ArrayList<>();
        snackAdapter = new SnackAdapter(this, snacks);
        recyclerViewSnacks.setAdapter(snackAdapter);

        getAllFoods();

        // Set up the cart button to open CartActivity
        ImageButton buttonCart = findViewById(R.id.buttonCart);
        buttonCart.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    private void getAllFoods() {
        SharedPreferences preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);
        String accessToken = preferences.getString("access_token", null);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiLinksHelper.getAllSnacksUri(), null,
                response -> {
                    System.out.println(response.toString());
                    // Parse the response
                    try {
                        JSONArray contentArray = response.getJSONArray("content");
                        parseFoodResponse(contentArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error processing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorResponse = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorJson = new JSONObject(errorResponse);
                            String errorMessage = errorJson.optString("message", "Unknown error occurred");
                            int statusCode = errorJson.optInt("status", 0);

                            switch (statusCode) {
                                case 400:
                                    Toast.makeText(MainActivity.this, "Bad Request: " + errorMessage, Toast.LENGTH_LONG).show();
                                    break;
                                case 401:
                                    Toast.makeText(MainActivity.this, "Unauthorized: " + errorMessage, Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(MainActivity.this, "Error: " + errorMessage, Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Error processing response: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Network error occurred", Toast.LENGTH_LONG).show();
                    }
                    System.out.println("Error: " + error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                if (accessToken != null) {
                    headers.put("Authorization", "Bearer " + accessToken);
                }
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(request);
    }

    private void parseFoodResponse(JSONArray response) {
        snacks.clear();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject foodObject = response.getJSONObject(i);

                // Parse the date strings
                String createdDateString = foodObject.getString("createdDate");
                String updatedDateString = foodObject.getString("updatedDate");

                // Convert the date strings to Date objects
                Date createdDate = parseDate(createdDateString);
                Date updatedDate = parseDate(updatedDateString);

                Snack snack = new Snack(
                        foodObject.getInt("foodId"),
                        foodObject.getString("foodName"),
                        foodObject.getString("imageURL"),
                        foodObject.getString("description"),
                        foodObject.getString("ingredients"),
                        foodObject.getDouble("price"),
                        foodObject.getBoolean("available"),
                        foodObject.getString("categoryId"),
                        foodObject.getString("createdBy"),
                        createdDate,
                        foodObject.getString("updatedBy"),
                        updatedDate
                );

                snacks.add(snack);
            }

            // Notify the adapter that the data has changed
            snackAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Helper method to parse date strings
    private Date parseDate(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}