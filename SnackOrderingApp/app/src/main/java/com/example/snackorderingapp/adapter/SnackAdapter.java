package com.example.snackorderingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snackorderingapp.R;
import com.example.snackorderingapp.activity.SnackDetailActivity;
import com.example.snackorderingapp.model.Snack;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SnackAdapter extends RecyclerView.Adapter<SnackAdapter.SnackViewHolder> {

    private List<Snack> snackList;
    private Context context;

    public SnackAdapter(Context context, List<Snack> snackList) {
        this.context = context;
        this.snackList = snackList;
    }

    @NonNull
    @Override
    public SnackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_snack, parent, false);
        return new SnackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SnackViewHolder holder, int position) {
        Snack snack = snackList.get(position);
        holder.snackName.setText(snack.getFoodName());
        holder.snackPrice.setText(String.valueOf(snack.getPrice()));
        Picasso.get().load(snack.getImageURL()).into(holder.snackImage);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SnackDetailActivity.class);
            intent.putExtra("snack", snack);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return snackList.size();
    }

    public static class SnackViewHolder extends RecyclerView.ViewHolder {
        TextView snackName, snackPrice;
        ImageView snackImage;

        public SnackViewHolder(@NonNull View itemView) {
            super(itemView);
            snackName = itemView.findViewById(R.id.snackName);
            snackPrice = itemView.findViewById(R.id.snackPrice);
            snackImage = itemView.findViewById(R.id.snackImage);
        }
    }
}