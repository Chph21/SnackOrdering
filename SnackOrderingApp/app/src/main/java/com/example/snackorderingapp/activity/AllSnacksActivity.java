package com.example.snackorderingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.snackorderingapp.ApiService;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.adapter.SnacksAdapter;
import com.example.snackorderingapp.model.Snack;
import java.util.List;

public class AllSnacksActivity extends AppCompatActivity {
    private RecyclerView snacksRecyclerView;
    private SnacksAdapter snacksAdapter;
    private ApiService apiService;
    private String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_snacks);

        Intent intent = getIntent();
        accessToken = intent.getStringExtra("ACCESS_TOKEN");
        apiService = new ApiService(this, accessToken);

        snacksRecyclerView = findViewById(R.id.snacksRecyclerView);
        snacksRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter and set it to RecyclerView
        snacksAdapter = new SnacksAdapter(snack -> {
            Intent detailIntent = new Intent(AllSnacksActivity.this, SnackDetailActivity.class);
            detailIntent.putExtra("SNACK_ID", snack.getFoodId());
            startActivity(detailIntent);
        });
        snacksRecyclerView.setAdapter(snacksAdapter);

        // Load snacks data
        loadSnacks();
    }

    private void loadSnacks() {
        apiService.getSnacks(new ApiService.ApiCallback<List<Snack>>() {
            @Override
            public void onSuccess(List<Snack> snacks) {
                snacksAdapter.setSnacks(snacks);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(AllSnacksActivity.this, "Failed to load snacks: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}