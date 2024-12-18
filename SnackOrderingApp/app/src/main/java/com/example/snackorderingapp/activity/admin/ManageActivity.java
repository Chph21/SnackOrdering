package com.example.snackorderingapp.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.snackorderingapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_manage);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_manage:
                    return true;
                case R.id.nav_admin_profile:
                    startActivity(new Intent(getApplicationContext(), AdminProfileActivity.class));
                    finish();
                    return true;
            }
            return false;
        });

        // Find the buttons by their IDs
        Button btnBranch = findViewById(R.id.btnBranch);
        Button btnOrder = findViewById(R.id.btnOrder);
        Button btnSnack = findViewById(R.id.btnSnack);
        Button btnCategory = findViewById(R.id.btnCategory);

        btnBranch.setOnClickListener(v -> {
            // Navigate to BranchActivity
            Intent intent = new Intent(ManageActivity.this, ManageUserActivity.class);
            startActivity(intent);
        });

        btnOrder.setOnClickListener(v -> {
            // Navigate to OrderActivity
            Intent intent = new Intent(ManageActivity.this, ManageOrderActivity.class);
            startActivity(intent);
        });

        btnSnack.setOnClickListener(v -> {
            // Navigate to SnackActivity
            Intent intent = new Intent(ManageActivity.this, ManageSnackActivity.class);
            startActivity(intent);
        });

        btnCategory.setOnClickListener(v -> {
            // Navigate to CategoriesActivity
            Intent intent = new Intent(ManageActivity.this, ManageCategoryActivity.class);
            startActivity(intent);
        });
    }
}