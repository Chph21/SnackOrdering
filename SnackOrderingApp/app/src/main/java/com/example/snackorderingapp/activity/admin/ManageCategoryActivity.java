package com.example.snackorderingapp.activity.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.snackorderingapp.R;

import java.util.Objects;

public class ManageCategoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_category);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get references to the buttons and TextView (if needed)
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        Button btnUpdate = findViewById(R.id.btnUpdate);
        LinearLayout tvCategoryId = findViewById(R.id.tvCategoryId); // Assuming you have a way to get the ID

        // Set click listener for the "Add" button
        btnAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageCategoryActivity.this, UpdateCategoryActivity.class);
                startActivity(intent);
            }
        });

        // Set click listener for the "Update" button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Assuming you get the category ID from a TextView or another source
                String categoryId = "123"; // Replace this with actual logic to get the ID
                Intent intent = new Intent(ManageCategoryActivity.this, UpdateCategoryActivity.class);
                intent.putExtra("CATEGORY_ID", categoryId); // Add the category ID to the intent
                startActivity(intent);
            }
        });
    }
}