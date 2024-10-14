package com.example.snackorderingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.MyVolleySingletonUtil;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.helper.ApiLinksHelper;
import com.example.snackorderingapp.helper.RegistrationFormValidatorHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private RequestQueue mRequestQueue;
    private RegistrationFormValidatorHelper registrationValidator;
    private TextView txtGoToLogin;
    private TextInputEditText txt_phone, txt_password, txt_confirm_password;
    private TextInputLayout txtPhoneLayout, txtPasswordLayout, txtConfirmPasswordLayout;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mRequestQueue = MyVolleySingletonUtil.getInstance(RegisterActivity.this).getRequestQueue();

        // Initialize view components
        txt_phone = findViewById(R.id.txt_phone);
        txt_password = findViewById(R.id.txt_password);
        txt_confirm_password = findViewById(R.id.txt_confirm_password);
        txtGoToLogin = findViewById(R.id.txt_go_to_login);
        registerBtn = findViewById(R.id.register_btn);

        txtPhoneLayout = findViewById(R.id.txt_phone_layout);
        txtPasswordLayout = findViewById(R.id.txt_password_layout);
        txtConfirmPasswordLayout = findViewById(R.id.txt_confirm_password_layout);

        // Initialize form validator
        registrationValidator = new RegistrationFormValidatorHelper(
                txt_phone, txt_password, txt_confirm_password,
                txtPhoneLayout, txtPasswordLayout, txtConfirmPasswordLayout,
                registerBtn
        );

        // Add text change listeners
        txt_phone.addTextChangedListener(registrationValidator);
        txt_password.addTextChangedListener(registrationValidator);
        txt_confirm_password.addTextChangedListener(registrationValidator);

        processRegistration();
        redirectToLogin();
    }

    private void processRegistration() {
        registerBtn.setOnClickListener(v -> {
            String password = Objects.requireNonNull(txt_password.getText()).toString();
            String confirmPassword = Objects.requireNonNull(txt_confirm_password.getText()).toString();

            if (password.equals(confirmPassword)) {
                // Convert the phone number here
                String formattedPhone = registrationValidator.getFormattedPhoneNumber();
                register(formattedPhone, password);
            } else {
                Toast.makeText(RegisterActivity.this, "Mật khẩu không khớp", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void register(String phone, String password) {
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", password);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiLinksHelper.registerUri(), new JSONObject(params),
                response -> {
                    try {
                        int id = response.getInt("id");
                        String registeredPhone = response.getString("phone");
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công với số điện thoại: " + registeredPhone, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this, ActivateAccountActivity.class);
                        intent.putExtra("PHONE", registeredPhone);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(RegisterActivity.this, "Lỗi xử lý phản hồi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorResponse = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorJson = new JSONObject(errorResponse);
                            String errorMessage = errorJson.optString("message", "Unknown error occurred");
                            int statusCode = errorJson.optInt("status", 0);

                            if (statusCode == 400 && errorMessage.contains("invalid phone number format")) {
                                Toast.makeText(RegisterActivity.this, "Sai định dạng số điện thoại", Toast.LENGTH_LONG).show();
                            } else if (statusCode == 409 && errorMessage.contains("Duplicate entry")) {
                                Toast.makeText(RegisterActivity.this, "Số điện thoại đã được đăng ký", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_LONG).show();
                            }
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(RegisterActivity.this, "Lỗi phát sinh", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(RegisterActivity.this, "Lỗi phát sinh", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        mRequestQueue.add(request);
    }

    private void goToLogin() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void redirectToLogin() {
        txtGoToLogin.setOnClickListener(v -> goToLogin());
    }
}