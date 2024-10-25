package com.example.snackorderingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.snackorderingapp.R;
import com.example.snackorderingapp.model.Snack;
import com.example.snackorderingapp.utils.CartUtils;
import com.squareup.picasso.Picasso;

public class SnackDetailActivity extends AppCompatActivity {

    private ImageView snackImage;
    private TextView snackName, snackDescription, snackPrice;
    private EditText snackAmount;
    private Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_detail);

        snackImage = findViewById(R.id.snackImage);
        snackName = findViewById(R.id.snackName);
        snackDescription = findViewById(R.id.snackDescription);
        snackPrice = findViewById(R.id.snackPrice);
        snackAmount = findViewById(R.id.snackAmount);
        addToCartButton = findViewById(R.id.addToCartButton);

        // Get the snack data from the intent
        Intent intent = getIntent();
        Snack snack = (Snack) intent.getSerializableExtra("snack");

        // Set the snack data to the views
        if (snack != null) {
            snackName.setText(String.format("Tên: %s", snack.getFoodName()));
            snackDescription.setText(String.format("Mô tả: %s", snack.getDescription()));
            snackPrice.setText(String.format("Giá: %s", snack.getPrice()));
            // Picasso.get().load(snack.getImageURL()).into(snackImage);
        }

        addToCartButton.setOnClickListener(v -> {
            // Add the snack to the cart
            if (snack != null) {
                String amountStr = snackAmount.getText().toString();
                if (!amountStr.isEmpty()) {
                    int amount = Integer.parseInt(amountStr);
                    CartUtils.addToCart(SnackDetailActivity.this, snack, amount);
                    Toast.makeText(SnackDetailActivity.this, snack.getFoodName() + " added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SnackDetailActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}