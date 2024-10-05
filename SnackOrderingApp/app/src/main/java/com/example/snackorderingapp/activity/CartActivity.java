package com.example.snackorderingapp.activity;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snackorderingapp.R;

public class CartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        RecyclerView cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView);
        Button checkoutButton = findViewById(R.id.checkoutButton);

        // Load and display cart items
        // Implement checkout functionality
    }
}