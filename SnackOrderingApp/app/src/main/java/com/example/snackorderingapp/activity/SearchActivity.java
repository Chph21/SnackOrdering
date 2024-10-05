package com.example.snackorderingapp.activity;

import android.os.Bundle;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snackorderingapp.R;

public class SearchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        EditText searchEditText = findViewById(R.id.searchEditText);
        RecyclerView searchResultsRecyclerView = findViewById(R.id.searchResultsRecyclerView);

        // Implement search functionality
    }
}