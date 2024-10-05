package com.example.snackorderingapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.model.Snack;
//import com.squareup.picasso.Picasso;

import java.util.Date;

public class SnackDetailActivity extends AppCompatActivity {
    private ImageView snackImageView;
    private TextView snackNameTextView;
    private TextView snackDescriptionTextView;
    private TextView snackPriceTextView;
    private Button addToCartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snack_detail);

        // Initialize UI elements
        snackImageView = findViewById(R.id.snackImageView);
        snackNameTextView = findViewById(R.id.snackNameTextView);
        snackDescriptionTextView = findViewById(R.id.snackDescriptionTextView);
        snackPriceTextView = findViewById(R.id.snackPriceTextView);
        addToCartButton = findViewById(R.id.addToCartButton);

        // Get snack details from intent
        int snackId = getIntent().getIntExtra("SNACK_ID", -1);

        // Load and display snack details
        loadSnackDetails(snackId);
    }

    private void loadSnackDetails(int snackId) {
        // For demonstration, let's assume we have a method to get a snack by its ID
        // In a real application, this would likely involve a network request
        Snack snack = getSnackById(snackId);

        if (snack != null) {
            // Load image using Picasso or any other image loading library
//            Picasso.get().load(snack.getImageURL()).into(snackImageView);

            // Set text views with snack details
            snackNameTextView.setText(snack.getFoodName());
            snackDescriptionTextView.setText(snack.getDescription());
            snackPriceTextView.setText(String.format("$%.2f", snack.getPrice()));
        }
    }

    private Snack getSnackById(int snackId) {
        // This is a placeholder method. Replace it with actual data fetching logic.
        // For now, returning a dummy snack object.
        return new Snack(
                snackId,
                "Sample Snack",
                "https://example.com/image.jpg",
                "This is a sample description.",
                "Sample ingredients",
                9.99,
                true,
                "Sample category",
                "Sample creator",
                new Date(),
                "Sample updater",
                new Date()
        );
    }
}