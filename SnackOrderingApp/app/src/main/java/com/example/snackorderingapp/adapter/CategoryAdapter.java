package com.example.snackorderingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snackorderingapp.R;
import com.example.snackorderingapp.activity.admin.ManageCategoryActivity;
import com.example.snackorderingapp.model.Category;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    private List<Category> categories;
    private ManageCategoryActivity activity;

    public CategoryAdapter(List<Category> categories, ManageCategoryActivity activity) {
        this.categories = categories;
        this.activity = activity; // Reference to ManageCategoryActivity to access showAddUpdateDialog
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        Category category = categories.get(position);
        holder.tvCategoryName.setText(category.getDescription()); // Display category description

        // Set up update button click listener
        holder.btnUpdate.setOnClickListener(v -> {
            activity.showAddUpdateDialog(category); // Pass the selected category to dialog
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName;
        Button btnUpdate;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
        }
    }
}
