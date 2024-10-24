package com.example.snackorderingapp.activity.admin;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snackorderingapp.R;
import com.example.snackorderingapp.model.Order;
import com.google.android.material.button.MaterialButton;

public class ManageOrderDetailActivity extends AppCompatActivity {
    private TextView orderIdText, orderNumberText, totalMoneyText, statusText;
    private TextView createdByText, createdDateText, updatedByText, updatedDateText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_order_detail);

        // Initialize views
        orderIdText = findViewById(R.id.orderIdText);
        orderNumberText = findViewById(R.id.orderNumberText);
        totalMoneyText = findViewById(R.id.totalMoneyText);
        statusText = findViewById(R.id.statusText);
        createdByText = findViewById(R.id.createdByText);
        createdDateText = findViewById(R.id.createdDateText);
        updatedByText = findViewById(R.id.updatedByText);
        updatedDateText = findViewById(R.id.updatedDateText);

        // Get order from intent
        Order order = (Order) getIntent().getSerializableExtra("order");

        // Display order details
        if (order != null) {
            orderIdText.setText("Order ID: " + order.getOrderId());
            orderNumberText.setText("Order Number: " + order.getOrderNumber());
            totalMoneyText.setText("Total: $" + order.getTotalMoney());
            statusText.setText("Status: " + order.getStatus());
            createdByText.setText("Created By: " + order.getCreatedBy());
            createdDateText.setText("Created Date: " + order.getCreatedDate());
            updatedByText.setText("Updated By: " + order.getUpdatedBy());
            updatedDateText.setText("Updated Date: " + order.getUpdatedDate());
        }

        MaterialButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
    }
}
