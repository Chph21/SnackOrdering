package com.example.snackorderingapp.activity.admin;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.ApiService;
import com.example.snackorderingapp.MyVolleySingletonUtil;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.activity.LoginActivity;
import com.example.snackorderingapp.adapter.CategoryAdapter;
import com.example.snackorderingapp.helper.ApiLinksHelper;
import com.example.snackorderingapp.helper.StringResourceHelper;
import com.example.snackorderingapp.model.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class ManageCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private ApiService apiService;
    private RequestQueue mRequestQueue;
    private SharedPreferences preferences;
    private List<Category> categoryList;
    private AlertDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_category);

        recyclerView = findViewById(R.id.recyclerViewCategories);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize ApiService with access token
        String accessToken = "your_access_token";  // Get it from login or saved session
        apiService = new ApiService(this, accessToken);

        // Fetch categories from the backend
        fetchCategories();

        // Set on-click listener for Add button
        Button btnAddCategory = findViewById(R.id.btnAddCategory);
        btnAddCategory.setOnClickListener(v -> showAddUpdateDialog(null));
    }

    private void fetchCategories() {
        mRequestQueue = MyVolleySingletonUtil.getInstance(this).getRequestQueue();
        preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);
        String accessToken = preferences.getString("access_token", "");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiLinksHelper.getCategoriesUri(), null,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray contentArray = response.getJSONArray("content");
                            System.out.println(response.toString());
                            List<Category> categories = new ArrayList<>();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
                            for (int i = 0; i < contentArray.length(); i++) {
                                JSONObject categoryJson = contentArray.getJSONObject(i);
                                Category category = new Category(
                                        categoryJson.getInt("categoryId"),
                                        categoryJson.getString("description"),
                                        new ArrayList<>(),
                                        categoryJson.getString("createdBy"),
                                        dateFormat.parse(categoryJson.getString("createdDate")),
                                        categoryJson.getString("updatedBy"),
                                        dateFormat.parse(categoryJson.getString("updatedDate"))
                                );
                                System.out.println(category.getDescription());
                                categories.add(category);
                            }
                            categoryList = categories;

                            // Initialize and set the adapter here
                            categoryAdapter = new CategoryAdapter(categoryList, ManageCategoryActivity.this);
                            recyclerView.setAdapter(categoryAdapter); // Set the adapter to RecyclerView
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, error -> {
            if (error.networkResponse != null && error.networkResponse.statusCode == 403) {
                refreshTokenAndRetry();
            } else {
                Toast.makeText(this, "Error loading categories", Toast.LENGTH_SHORT).show();
            }

        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        mRequestQueue.add(request);
    }

    public void showAddUpdateDialog(Category category) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_update_category, null);

        EditText etDescription = dialogView.findViewById(R.id.etCategoryName);
        Button btnSaveCategory = dialogView.findViewById(R.id.btnSaveCategory);
        Button btnDeleteCategory = dialogView.findViewById(R.id.btnDeleteCategory);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        dialog = builder.create(); // Use the class member variable
        dialog.show();

        if (category != null) {
            // Updating existing category
            etDescription.setText(category.getDescription());
            btnDeleteCategory.setVisibility(View.VISIBLE);
            btnDeleteCategory.setOnClickListener(view -> {
                // Confirm before deletion
                new AlertDialog.Builder(this)
                        .setTitle("Delete Category")
                        .setMessage("Are you sure you want to delete this category?")
                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                            deleteCategory(category.getCategoryId(), response -> {
                                // Handle successful deletion
                                Toast.makeText(this, "Category deleted", Toast.LENGTH_SHORT).show();
                                fetchCategories();  // Refresh categories list
                            }, error -> Toast.makeText(this, "Failed to delete category", Toast.LENGTH_SHORT).show());
                            dialog.dismiss();
                        })
                        .setNegativeButton("No", null)
                        .show();
            });
        } else {
            // Adding new category, hide delete button
            btnDeleteCategory.setVisibility(View.GONE);
        }

        btnSaveCategory.setOnClickListener(view -> {
            String description = etDescription.getText().toString();
            if (!description.isEmpty()) {
                if (category != null) {
                    // Update existing category
                    updateCategory(category.getCategoryId(), description);
                } else {
                    // Add new category
                    addCategory(description);
                }
                dialog.dismiss();
            } else {
                etDescription.setError("Description is required");
            }
        });
    }



    private void addCategory(String description) {
        String url = ApiLinksHelper.saveCategoryUri(); // The API endpoint for adding a category
        JSONObject categoryData = new JSONObject();

        try {
            categoryData.put("description", description);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest addRequest = new JsonObjectRequest(Request.Method.POST, url, categoryData,
                response -> {
                    Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show();
                    fetchCategories(); // Refresh the list after adding
                },
                error -> {
                    Toast.makeText(this, "Error adding category", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                String accessToken = preferences.getString("access_token", "");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        mRequestQueue.add(addRequest);
    }

    private void updateCategory(int categoryId, String newDescription) {
        String url = ApiLinksHelper.saveCategoryUri(); // The API endpoint for updating a category
        JSONObject categoryData = new JSONObject();

        try {
            categoryData.put("categoryId", categoryId);
            categoryData.put("description", newDescription);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.PUT, url, categoryData,
                response -> {
                    Toast.makeText(this, "Category updated successfully", Toast.LENGTH_SHORT).show();
                    fetchCategories(); // Refresh the list after updating
                },
                error -> {
                    Toast.makeText(this, "Error updating category", Toast.LENGTH_SHORT).show();
                    error.printStackTrace();
                }) {
            @Override
            public Map<String, String> getHeaders() {
                String accessToken = preferences.getString("access_token", "");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        mRequestQueue.add(updateRequest);
    }

    public void deleteCategory(int categoryId, Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {
        // Construct the DELETE URL using the category ID
        String deleteUrl = ApiLinksHelper.deleteCategoryUri() + "/" + categoryId;

        // Create a JsonObjectRequest for DELETE
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                successListener,
                errorListener) {
            @Override
            public Map<String, String> getHeaders() {
                String accessToken = preferences.getString("access_token", "");
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the queue
        mRequestQueue.add(request);
    }



    private void refreshTokenAndRetry() {
        String refreshToken = preferences.getString("refresh_token", "");
        System.out.println(refreshToken);
        JSONObject emptyBody = new JSONObject(); // Create empty JSON object for body

        JsonObjectRequest refreshRequest = new JsonObjectRequest(Request.Method.POST,
                ApiLinksHelper.refreshTokenUri(),
                emptyBody, // Pass the empty body here
                response -> {
                    try {
                        String newAccessToken = response.getString("access_token");
                        String newRefreshToken = response.getString("refresh_token");

                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("access_token", newAccessToken);
                        editor.putString("refresh_token", newRefreshToken);
                        editor.apply();

                        fetchCategories();
                    } catch (JSONException e) {
                        Intent intent = new Intent(ManageCategoryActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                error -> {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(ManageCategoryActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + refreshToken);
                headers.put("Content-Type", "application/json");
                System.out.println(headers);
                return headers;
            }
        };

        mRequestQueue.add(refreshRequest);
    }

}