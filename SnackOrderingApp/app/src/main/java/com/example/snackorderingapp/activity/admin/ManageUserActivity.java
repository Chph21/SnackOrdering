package com.example.snackorderingapp.activity.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.MyVolleySingletonUtil;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.activity.LoginActivity;
import com.example.snackorderingapp.adapter.UsersAdapter;
import com.example.snackorderingapp.helper.ApiLinksHelper;
import com.example.snackorderingapp.helper.StringResourceHelper;
import com.example.snackorderingapp.model.User;
import com.google.android.material.appbar.MaterialToolbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageUserActivity extends AppCompatActivity {

    private RecyclerView usersRecyclerView;
    private UsersAdapter usersAdapter;
    private List<User> users;
    private SwipeRefreshLayout swipeRefresh;
    private RequestQueue mRequestQueue;
    private SharedPreferences preferences;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);

        initializeViews();
        setupToolbar();
        setupRecyclerView();
        setupSwipeRefresh();
        loadUsers();
    }

    private void initializeViews() {
        toolbar = findViewById(R.id.toolbar);
        usersRecyclerView = findViewById(R.id.usersRecyclerView);
        swipeRefresh = findViewById(R.id.swipeRefresh);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Manage Users");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupRecyclerView() {
        users = new ArrayList<>();
        usersAdapter = new UsersAdapter(this, users);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usersRecyclerView.setAdapter(usersAdapter);
    }

    private void setupSwipeRefresh() {
        swipeRefresh.setOnRefreshListener(() -> {
            loadUsers();
            swipeRefresh.setRefreshing(false);
        });
    }

    private void loadUsers() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(true);
        }

        mRequestQueue = MyVolleySingletonUtil.getInstance(this).getRequestQueue();
        preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);
        String accessToken = preferences.getString("access_token", "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiLinksHelper.getUsersUri(), null,
                response -> {
                    try {
                        JSONArray usersArray = response.getJSONArray("content");
                        List<User> usersList = new ArrayList<>();

                        for (int i = 0; i < usersArray.length(); i++) {
                            JSONObject userObj = usersArray.getJSONObject(i);
                            User user = new User(
                                    userObj.getLong("accountId"),
                                    userObj.isNull("firstName") ? null : userObj.getString("firstName"),
                                    userObj.isNull("lastName") ? null : userObj.getString("lastName"),
                                    userObj.isNull("email") ? null : userObj.getString("email"),
                                    userObj.getString("phone"),
                                    userObj.isNull("address") ? null : userObj.getString("address"),
                                    userObj.isNull("branchId") ? null : userObj.getLong("branchId"),
                                    userObj.isNull("birthday") ? null : userObj.getString("birthday"),
                                    userObj.getString("createdBy"),
                                    userObj.getString("createdDate"),
                                    userObj.isNull("updatedBy") ? null : userObj.getString("updatedBy"),
                                    userObj.isNull("updatedDate") ? null : userObj.getString("updatedDate")
                            );
                            usersList.add(user);
                        }

                        users.clear();
                        users.addAll(usersList);
                        usersAdapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing users data", Toast.LENGTH_SHORT).show();
                    } finally {
                        if (swipeRefresh != null) {
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 403) {
                        refreshTokenAndRetry();
                    } else {
                        Toast.makeText(this, "Error loading users", Toast.LENGTH_SHORT).show();
                    }
                    if (swipeRefresh != null) {
                        swipeRefresh.setRefreshing(false);
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        mRequestQueue.add(request);
    }

    private void refreshTokenAndRetry() {
        String refreshToken = preferences.getString("refresh_token", "");
        JSONObject emptyBody = new JSONObject();

        JsonObjectRequest refreshRequest = new JsonObjectRequest(Request.Method.POST,
                ApiLinksHelper.refreshTokenUri(),
                emptyBody,
                response -> {
                    try {
                        String newAccessToken = response.getString("access_token");
                        String newRefreshToken = response.getString("refresh_token");

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("access_token", newAccessToken);
                        editor.putString("refresh_token", newRefreshToken);
                        editor.apply();

                        loadUsers();
                    } catch (JSONException e) {
                        redirectToLogin();
                    }
                },
                error -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    redirectToLogin();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + refreshToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        mRequestQueue.add(refreshRequest);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(ManageUserActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
