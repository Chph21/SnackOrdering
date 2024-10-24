package com.example.snackorderingapp.activity.admin;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.model.User;
import com.google.android.material.button.MaterialButton;

public class ManageUserDetailActivity extends AppCompatActivity {
    private TextView userIdText, fullNameText, phoneText, emailText;
    private TextView createdDateText, updatedDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user_detail);

        // Initialize views
        userIdText = findViewById(R.id.userIdText);
        fullNameText = findViewById(R.id.fullNameText);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);
        createdDateText = findViewById(R.id.createdDateText);
        updatedDateText = findViewById(R.id.updatedDateText);

        // Get user from intent
        User user = (User) getIntent().getSerializableExtra("user");

        // Display user details
        if (user != null) {
            userIdText.setText("User ID: " + user.getAccountId());
            String fullName = (user.getFirstName() != null ? user.getFirstName() : "") + " " +
                    (user.getLastName() != null ? user.getLastName() : "");
            fullNameText.setText("Full Name: " + fullName.trim());
            phoneText.setText("Phone: " + user.getPhone());
            emailText.setText("Email: " + (user.getEmail() != null ? user.getEmail() : "No Email"));
            createdDateText.setText("Created Date: " + user.getCreatedDate());
            updatedDateText.setText("Updated Date: " + user.getUpdatedDate());
        }

        MaterialButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}
