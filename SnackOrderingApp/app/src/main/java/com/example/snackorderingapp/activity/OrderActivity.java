package com.example.snackorderingapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.MyVolleySingletonUtil;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.adapter.UserOrderAdapter;
import com.example.snackorderingapp.helper.ApiLinksHelper;
import com.example.snackorderingapp.helper.StringResourceHelper;
import com.example.snackorderingapp.model.Order;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    private RequestQueue mRequestQueue;
    private RecyclerView ordersRecyclerView;
    private UserOrderAdapter orderAdapter;
    private List<Order> orderList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_order);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    return true;
                case R.id.bottom_order:
                    return true;
                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    finish();
                    return true;
            }
            return false;
        });

        preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);
        mRequestQueue = MyVolleySingletonUtil.getInstance(OrderActivity.this).getRequestQueue();

        String accessToken = preferences.getString("access_token", "");
        String phone = preferences.getString("phone", "");

        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        orderAdapter = new UserOrderAdapter(orderList);
        ordersRecyclerView.setAdapter(orderAdapter);

        fetchUserInfo(phone, accessToken);
    }

    private void fetchUserInfo(String phone, String accessToken) {
        if (Objects.equals(phone, "")) {
            Toast.makeText(this, "Invalid user ID", Toast.LENGTH_SHORT).show();
            return;
        }
        System.out.println("fetching phone: " + phone);
        String url = ApiLinksHelper.getUserInfoUri(phone);

        @SuppressLint("SetTextI18n") JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    System.out.println(response.toString());
                    try {
                        JSONObject content = response.getJSONObject("content");
                        int userId = content.getInt("accountId");
                        fetchUserOrders(userId, accessToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(OrderActivity.this, "Error parsing user info", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(OrderActivity.this, "Error fetching user info: " + error.toString(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        mRequestQueue.add(request);
    }

    private void fetchUserOrders(int userId, String accessToken) {
        String url = ApiLinksHelper.getUserOrders(userId);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    System.out.println(response.toString());
                    try {
                        JSONArray ordersArray = response.getJSONArray("content");
                        for (int i = 0; i < ordersArray.length(); i++) {
                            JSONObject orderObject = ordersArray.getJSONObject(i);
                            Long orderId = orderObject.getLong("orderId");
                            String orderNumber = orderObject.getString("orderNumber");
                            double totalMoney = orderObject.getDouble("totalMoney");
                            String status = orderObject.getString("status");
                            Long accountId = orderObject.getLong("accountId");
                            Long branchId = orderObject.getLong("branchId");
                            String createdBy = orderObject.getString("createdBy");
                            String createdDate = orderObject.getString("createdDate");
                            String updatedBy = orderObject.getString("updatedBy");
                            String updatedDate = orderObject.getString("updatedDate");

                            Order order = new Order(orderId, orderNumber, totalMoney, status, accountId, branchId, createdBy, createdDate, updatedBy, updatedDate);
                            orderList.add(order);
                        }
                        orderAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(OrderActivity.this, "Error parsing orders", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(OrderActivity.this, "Error fetching orders: " + error.toString(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };

        mRequestQueue.add(request);
    }
}