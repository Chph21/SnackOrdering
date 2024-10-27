package com.example.snackorderingapp.activity;

import androidx.appcompat.app.AppCompatActivity;

public class AllSnacksActivity extends AppCompatActivity {
//    private RecyclerView snacksRecyclerView;
//    private SnacksAdapter snacksAdapter;
//    private ApiService apiService;
//    private String accessToken;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_all_snacks);
//
//        Intent intent = getIntent();
//        accessToken = intent.getStringExtra("ACCESS_TOKEN");
//        apiService = new ApiService(this, accessToken);
//
//        snacksRecyclerView = findViewById(R.id.snacksRecyclerView);
//        snacksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        // Initialize adapter and set it to RecyclerView
//        snacksAdapter = new SnacksAdapter(snack -> {
//            Intent detailIntent = new Intent(AllSnacksActivity.this, SnackDetailActivity.class);
//            detailIntent.putExtra("SNACK_ID", snack.getFoodId());
//            startActivity(detailIntent);
//        });
//        snacksRecyclerView.setAdapter(snacksAdapter);
//
//        // Load snacks data
//        loadSnacks();
//    }
//
//    private void loadSnacks() {
//        apiService.getSnacks(new ApiService.ApiCallback<List<Snack>>() {
//            @Override
//            public void onSuccess(List<Snack> snacks) {
//                snacksAdapter.setSnacks(snacks);
//            }
//
//            @Override
//            public void onError(String error) {
//                Toast.makeText(AllSnacksActivity.this, "Failed to load snacks: " + error, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
}