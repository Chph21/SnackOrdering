package com.example.snackorderingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
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
import com.example.snackorderingapp.activity.admin.ManageActivity;
import com.example.snackorderingapp.helper.ApiLinksHelper;
import com.example.snackorderingapp.helper.LoginFormValidationHelper;
import com.example.snackorderingapp.helper.StringResourceHelper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    //private static final String USER_DETAIL_PREF = "USER_INFO";
    private SharedPreferences preferences;
    private RequestQueue mRequestQueue;

    private LoginFormValidationHelper loginValidator;
    private TextView txtGoToSignIn;

    private TextInputEditText txt_phone, txt_password;
    private TextInputLayout txtPhoneLayout, txtPasswordLayout;

    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mRequestQueue = MyVolleySingletonUtil.getInstance(LoginActivity.this).getRequestQueue();


        // INITIATE / HOOK VIEW COMPONENTS:
        txt_phone        = findViewById(R.id.txt_phone);
        txt_password     = findViewById(R.id.txt_password);
        txtGoToSignIn   = findViewById(R.id.txt_go_to_sign_up);
        loginBtn        = findViewById(R.id.login_btn);
        // GET LAYOUTS:
        txtPhoneLayout      = findViewById(R.id.txt_phone_layout);
        txtPasswordLayout   = findViewById(R.id.txt_password_layout);

        // GET FORM VALIDATOR OBJECT:
        loginValidator = new LoginFormValidationHelper(txt_phone, txt_password, txtPhoneLayout, txtPasswordLayout, loginBtn);

        // ADD TEXT FIELD LISTENERS:
        txt_phone.addTextChangedListener(loginValidator);
        txt_password.addTextChangedListener(loginValidator);

        processLogin();
        redirectToRegister();

    }

    public void goToRegister(){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToMainIfAuthenticated(){
        Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
        startActivity(intent);
        // DISPLAY SUCCESS MESSAGE IF AUTHENTICATED:
        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
        finish();
    }

    public void authenticate(String phone, String password) {
        String formattedPhone = loginValidator.getFormattedPhoneNumber();

        // SET USER DATA MAP OBJECT:
        HashMap<String, String> params = new HashMap<>();
        params.put("phone", formattedPhone);
        params.put("password", password);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, ApiLinksHelper.autenticateUri(), new JSONObject(params),
                response -> {
                    preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    try {
                        String accessToken = response.getString("access_token");
                        String refreshToken = response.getString("refresh_token");
                        // Decode the access token and extract user ID and role
                        String[] decodedValues = decodeAccessToken(accessToken);
                        String userId = decodedValues[0];
                        String role = decodedValues[1];

                        // Save tokens and user ID to SharedPreferences
                        editor.putString("access_token", accessToken);
                        editor.putString("refresh_token", refreshToken);
                        editor.putString("phone", userId);
                        editor.putString("role", role); // Save the role to SharedPreferences
                        editor.putBoolean("authenticated", true);
                        editor.apply();
                        System.out.println(role);
                        if (role.equals("USER")) {
                            Intent intent = new Intent(LoginActivity.this, ProfileActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (role.equals("ADMIN")) {
                            Intent intent = new Intent(LoginActivity.this, ManageActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid role", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        // Handle exception
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
                                    if (errorMessage.contains("invalid phone number format")) {
                                        Toast.makeText(LoginActivity.this, "Sai định dạng số điện thoại", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                    break;
                                case 401:
                                    Toast.makeText(LoginActivity.this, "Sai số điện thoại hoặc mật khẩu", Toast.LENGTH_LONG).show();
                                    break;
                                case 428:
                                    if (errorMessage.equals("User is disabled")) {
                                        Toast.makeText(LoginActivity.this, "Tài khoản chưa được kích hoạt", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(LoginActivity.this, ActivateAccountActivity.class);
                                        intent.putExtra("PHONE", formattedPhone);
                                        startActivity(intent);
                                        return;
                                    }
                                default:
                                    Toast.makeText(LoginActivity.this, "Lỗi phát sinh: " + errorMessage, Toast.LENGTH_LONG).show();
                                    break;
                            }
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(LoginActivity.this, "Lỗi phát sinh", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi phát sinh", Toast.LENGTH_LONG).show();
                    }
                    System.out.println("Error: " + error.toString());
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // ADD TO REQUEST QUEUE:
        mRequestQueue.add(request);
    }

    public void processLogin(){
        loginBtn.setOnClickListener(v -> {
            authenticate(Objects.requireNonNull(txt_phone.getText()).toString(), Objects.requireNonNull(txt_password.getText()).toString());
            // Toast.makeText(LoginActivity.this, "Login Button Clicked!", Toast.LENGTH_SHORT).show();
        });
    }

    public void redirectToRegister(){
        // END OF ON CLICK METHOD.
        txtGoToSignIn.setOnClickListener(v -> goToRegister());
        // END OF GO TO REGISTER ON CLICK LISTENER METHOD.
    }

    private String[] decodeAccessToken(String accessToken) {
        String[] parts = accessToken.split("\\.");
        String payload = new String(Base64.decode(parts[1], Base64.DEFAULT));
        try {
            JSONObject jsonPayload = new JSONObject(payload);
            String userId = jsonPayload.getString("sub");
            String role = jsonPayload.getString("role");
            return new String[] {userId, role};
        } catch (JSONException e) {
            e.printStackTrace();
            return new String[] {"", ""}; // Return invalid values if decoding fails
        }
    }

}
