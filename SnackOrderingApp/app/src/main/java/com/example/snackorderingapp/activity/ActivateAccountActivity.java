// File: app/src/main/java/com/example/snackorderingapp/activity/ActivateAccountActivity.java

package com.example.snackorderingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.MyVolleySingletonUtil;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.helper.ApiLinksHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class ActivateAccountActivity extends AppCompatActivity {

    private EditText[] codeInputs;
    private Button getOtpButton;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_account);

        phone = getIntent().getStringExtra("PHONE");

        codeInputs = new EditText[6];
        codeInputs[0] = findViewById(R.id.code1);
        codeInputs[1] = findViewById(R.id.code2);
        codeInputs[2] = findViewById(R.id.code3);
        codeInputs[3] = findViewById(R.id.code4);
        codeInputs[4] = findViewById(R.id.code5);
        codeInputs[5] = findViewById(R.id.code6);

        getOtpButton = findViewById(R.id.getOtpButton);

        setupCodeInputs();
        getOtpButton.setOnClickListener(v -> generateOtp());
    }

    private void setupCodeInputs() {
        for (int i = 0; i < codeInputs.length; i++) {
            final int index = i;
            codeInputs[i].addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() == 1) {
                        if (index < codeInputs.length - 1) {
                            codeInputs[index + 1].requestFocus();
                        }
                        if (index == codeInputs.length - 1) {
                            verifyOtp();
                        }
                    }
                }
            });
        }
    }

    private void generateOtp() {
        JSONObject params = new JSONObject();
        try {
            params.put("phone", phone);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiLinksHelper.generateOtpUri(), params,
                response -> {
                    Toast.makeText(ActivateAccountActivity.this, "Mã kích hoạt đã được gửi", Toast.LENGTH_LONG).show();
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorBody = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorJson = new JSONObject(errorBody);
                            if (errorJson.getInt("status") == 400 && errorJson.getString("message").equals("Max send attempts reached")) {
                                Toast.makeText(ActivateAccountActivity.this, "Đã đạt đến số lần gửi tối đa. Vui lòng thử lại sau.", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ActivateAccountActivity.this, "Lỗi khi gửi mã kích hoạt: " + errorJson.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ActivateAccountActivity.this, "Lỗi khi gửi mã kích hoạt", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ActivateAccountActivity.this, "Lỗi khi gửi mã kích hoạt", Toast.LENGTH_LONG).show();
                    }
                });

        MyVolleySingletonUtil.getInstance(this).addToRequestQueue(request);
    }

    private void verifyOtp() {
        StringBuilder codeBuilder = new StringBuilder();
        for (EditText codeInput : codeInputs) {
            codeBuilder.append(codeInput.getText().toString());
        }
        String code = codeBuilder.toString();

        JSONObject params = new JSONObject();
        try {
            params.put("phone", phone);
            params.put("otp", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiLinksHelper.verifyOtpUri(), params,
                response -> {
                    try {
                        String status = response.getString("status");
                        if ("approved".equals(status)) {
                            Toast.makeText(ActivateAccountActivity.this, "Tài khoản đã được kích hoạt thành công", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ActivateAccountActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else if ("pending".equals(status)) {
                            Toast.makeText(ActivateAccountActivity.this, "Mã kích hoạt không đúng. Vui lòng thử lại.", Toast.LENGTH_LONG).show();
                            // Clear the input fields
                            for (EditText codeInput : codeInputs) {
                                codeInput.setText("");
                            }
                            codeInputs[0].requestFocus();
                        } else {
                            Toast.makeText(ActivateAccountActivity.this, "Có lỗi xảy ra. Vui lòng thử lại sau.", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(ActivateAccountActivity.this, "Có lỗi xảy ra khi xử lý phản hồi", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.data != null) {
                        try {
                            String errorBody = new String(error.networkResponse.data, "UTF-8");
                            JSONObject errorJson = new JSONObject(errorBody);
                            if (errorJson.getInt("status") == 429 && errorJson.getString("error").equals("Maximum verification attempts reached. Please request a new OTP.")) {
                                Toast.makeText(ActivateAccountActivity.this, "Đã đạt đến số lần xác minh tối đa. Vui lòng yêu cầu mã OTP mới.", Toast.LENGTH_LONG).show();
                                // Disable verify button and enable generate OTP button
                            } else {
                                Toast.makeText(ActivateAccountActivity.this, "Kích hoạt tài khoản thất bại: " + errorJson.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            Toast.makeText(ActivateAccountActivity.this, "Kích hoạt tài khoản thất bại: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(ActivateAccountActivity.this, "Kích hoạt tài khoản thất bại: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        MyVolleySingletonUtil.getInstance(this).addToRequestQueue(request);
    }

}