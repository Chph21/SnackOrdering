package com.example.snackorderingapp.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snackorderingapp.R;

public class OrderHistoryActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        RecyclerView orderHistoryRecyclerView = findViewById(R.id.orderHistoryRecyclerView);

        // Load and display order history
    }
}