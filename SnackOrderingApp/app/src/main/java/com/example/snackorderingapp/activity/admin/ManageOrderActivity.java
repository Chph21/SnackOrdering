package com.example.snackorderingapp.activity.admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.MyVolleySingletonUtil;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.activity.LoginActivity;
import com.example.snackorderingapp.adapter.OrdersAdapter;
import com.example.snackorderingapp.helper.ApiLinksHelper;
import com.example.snackorderingapp.helper.StringResourceHelper;
import com.example.snackorderingapp.model.Order;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageOrderActivity extends AppCompatActivity {

    private RecyclerView ordersRecyclerView;
    private OrdersAdapter ordersAdapter;
    private List<Order> orders;
    private SwipeRefreshLayout swipeRefresh;  // Add this line
    private TabLayout tabLayout;
    private List<Order> allOrders; // Store all orders
    private List<Order> filteredOrders; // Store filtered orders
    private RequestQueue mRequestQueue;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order);

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize orders list and adapter
        orders = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(this, orders);
        ordersRecyclerView.setAdapter(ordersAdapter);
        swipeRefresh = findViewById(R.id.swipeRefresh);
        swipeRefresh.setOnRefreshListener(() -> {
            loadOrders();
            swipeRefresh.setRefreshing(false);
        });
        // Load orders data
        loadOrders();
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                filterOrders(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void filterOrders(int tabPosition) {
        if (allOrders == null) return;

        filteredOrders = new ArrayList<>();
        switch (tabPosition) {
            case 0: // All Orders
                filteredOrders.addAll(allOrders);
                break;
            case 1: // Pending
                for (Order order : allOrders) {
                    if (order.getStatus().equals("Pending")) {
                        filteredOrders.add(order);
                    }
                }
                break;
            case 2: // Cancelled
                for (Order order : allOrders) {
                    if (order.getStatus().equals("Cancelled")) {
                        filteredOrders.add(order);
                    }
                }
                break;
            case 3: // Completed
                for (Order order : allOrders) {
                    if (order.getStatus().equals("Completed")) {
                        filteredOrders.add(order);
                    }
                }
                break;
        }
        ordersAdapter.setOrders(filteredOrders);
    }

    private void loadOrders() {
        if (swipeRefresh != null) {
            swipeRefresh.setRefreshing(true);
        }

        mRequestQueue = MyVolleySingletonUtil.getInstance(this).getRequestQueue();
        preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);
        String accessToken = preferences.getString("access_token", "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiLinksHelper.getOrdersUri(), null,
                response -> {
                    try {
                        JSONArray ordersArray = response.getJSONArray("content");
                        List<Order> ordersList = new ArrayList<>();

                        for (int i = 0; i < ordersArray.length(); i++) {
                            JSONObject orderObj = ordersArray.getJSONObject(i);
                            Order order = new Order(
                                    orderObj.getLong("orderId"),
                                    orderObj.isNull("orderNumber") ? null : orderObj.getString("orderNumber"),
                                    orderObj.getDouble("totalMoney"),
                                    orderObj.getString("status"),
                                    orderObj.getLong("accountId"),
                                    orderObj.getLong("branchId"),
                                    orderObj.getString("createdBy"),
                                    orderObj.getString("createdDate"),
                                    orderObj.getString("updatedBy"),
                                    orderObj.getString("updatedDate")
                            );
                            ordersList.add(order);
                        }
                        // Inside the response success block:
                        allOrders = ordersList;
                        filterOrders(tabLayout.getSelectedTabPosition());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing orders data", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(this, "Error loading orders", Toast.LENGTH_SHORT).show();
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
        System.out.println(refreshToken);
        JSONObject emptyBody = new JSONObject(); // Create empty JSON object for body

        JsonObjectRequest refreshRequest = new JsonObjectRequest(Request.Method.POST,
                ApiLinksHelper.refreshTokenUri(),
                emptyBody, // Pass the empty body here
                response -> {
                    try {
                        String newAccessToken = response.getString("access_token");
                        String newRefreshToken = response.getString("refresh_token");

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("access_token", newAccessToken);
                        editor.putString("refresh_token", newRefreshToken);
                        editor.apply();

                        loadOrders();
                    } catch (JSONException e) {
                        Intent intent = new Intent(ManageOrderActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                error -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(ManageOrderActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + refreshToken);
                headers.put("Content-Type", "application/json");
                System.out.println(headers);
                return headers;
            }
        };

        mRequestQueue.add(refreshRequest);
    }


}