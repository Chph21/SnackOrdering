package com.example.snackorderingapp.activity.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.MyVolleySingletonUtil;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.activity.LoginActivity;
import com.example.snackorderingapp.helper.ApiLinksHelper;
import com.example.snackorderingapp.helper.StringResourceHelper;
import com.example.snackorderingapp.model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminProfileActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private RequestQueue mRequestQueue;
    private TextView txtPhone, txtEmail, txtFullname, txtAddress;
    private Button logoutBtn;
    private Button updateBtn;
    private Button cancelBtn;
    private boolean isEditing = false;
    private User currentUser;



    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_admin_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_manage:
                    startActivity(new Intent(getApplicationContext(), ManageActivity.class));
                    finish();
                    return true;
                case R.id.nav_admin_profile:
                    return true;
            }
            return false;
        });

        // Initialize views
        txtPhone = findViewById(R.id.txt_phone);
        txtEmail = findViewById(R.id.txt_email);
        txtFullname = findViewById(R.id.txt_fullname);
        txtAddress = findViewById(R.id.txt_address);
        logoutBtn = findViewById(R.id.logout_btn);
        updateBtn = findViewById(R.id.update_btn);
        cancelBtn = findViewById(R.id.cancel_btn);
        cancelBtn.setVisibility(View.GONE);

        setFieldsEditable(false);

        updateBtn.setOnClickListener(v -> {
            if (!isEditing) {
                updateBtn.setText("Xác nhận");
                cancelBtn.setVisibility(View.VISIBLE);
                setFieldsEditable(true);
                isEditing = true;
            } else {
                updateUserInfo();
            }
        });

        cancelBtn.setOnClickListener(v -> {
            updateBtn.setText("Cập nhật thông tin");
            cancelBtn.setVisibility(View.GONE);
            setFieldsEditable(false);
            isEditing = false;
            fetchUserInfo(preferences.getString("phone", ""), preferences.getString("access_token", ""));
        });

        preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);
        String accessToken = preferences.getString("access_token", "");
        String phone = preferences.getString("phone", "");

        mRequestQueue = MyVolleySingletonUtil.getInstance(AdminProfileActivity.this).getRequestQueue();
        fetchUserInfo(phone, accessToken);
        logoutBtn.setOnClickListener(v -> logout());
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
                        currentUser = new User();
                        currentUser.setAccountId(content.getLong("accountId"));
                        currentUser.setPhone(content.getString("phone"));
                        currentUser.setEmail(content.isNull("email") ? null : content.getString("email"));
                        currentUser.setFirstName(content.getString("firstName"));
                        currentUser.setLastName(content.getString("lastName"));

                        txtPhone.setText(content.getString("phone"));
                        txtEmail.setText(content.isNull("email") ? "Chưa cung cấp" : content.getString("email"));
                        txtFullname.setText(content.getString("firstName") + " " + content.getString("lastName"));
                        txtAddress.setText(content.isNull("address") ? "Chưa cung cấp" : content.getString("address"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(AdminProfileActivity.this, "Error parsing user info", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(AdminProfileActivity.this, "Error fetching user info: " + error.toString(), Toast.LENGTH_SHORT).show();
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

    private void logout() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(AdminProfileActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setFieldsEditable(boolean editable) {
        txtPhone.setEnabled(false);
        txtEmail.setEnabled(editable);
        txtFullname.setEnabled(editable);
        txtAddress.setEnabled(editable);
    }

    private void updateUserInfo() {
        String url = ApiLinksHelper.updateUserUri();
        JSONObject requestBody = new JSONObject();
        try {
            String[] fullName = txtFullname.getText().toString().split(" ", 2);
            requestBody.put("accountId", currentUser.getAccountId());
            requestBody.put("firstName", fullName[0]);
            requestBody.put("lastName", fullName.length > 1 ? fullName[1] : "");
            requestBody.put("email", txtEmail.getText().toString());
            requestBody.put("phone", currentUser.getPhone());
            requestBody.put("address", txtAddress.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, requestBody,
                response -> {
                    Toast.makeText(AdminProfileActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    updateBtn.setText("Cập nhật thông tin");
                    cancelBtn.setVisibility(View.GONE);
                    setFieldsEditable(false);
                    isEditing = false;
                },
                error -> {
                    String errorMessage = new String(error.networkResponse.data);
                    System.out.println("Error Response: " + errorMessage);
                    Toast.makeText(AdminProfileActivity.this, "Lỗi cập nhật thông tin: " + errorMessage, Toast.LENGTH_SHORT).show();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + preferences.getString("access_token", ""));
                return headers;
            }
        };

        mRequestQueue.add(request);
    }

}
