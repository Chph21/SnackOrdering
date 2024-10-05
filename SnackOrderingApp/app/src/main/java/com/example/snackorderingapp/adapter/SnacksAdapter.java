package com.example.snackorderingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.model.Snack;
import java.util.List;

public class SnacksAdapter extends RecyclerView.Adapter<SnacksAdapter.SnackViewHolder> {
    private List<Snack> snacks;
    private OnSnackClickListener onSnackClickListener;

    public interface OnSnackClickListener {
        void onSnackClick(Snack snack);
    }

    public SnacksAdapter(OnSnackClickListener onSnackClickListener) {
        this.onSnackClickListener = onSnackClickListener;
    }

    public void setSnacks(List<Snack> snacks) {
        this.snacks = snacks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SnackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_snack, parent, false);
        return new SnackViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SnackViewHolder holder, int position) {
        Snack snack = snacks.get(position);
        holder.snackNameTextView.setText(snack.getFoodName());
        holder.itemView.setOnClickListener(v -> onSnackClickListener.onSnackClick(snack));
    }

    @Override
    public int getItemCount() {
        return snacks != null ? snacks.size() : 0;
    }

    static class SnackViewHolder extends RecyclerView.ViewHolder {
        TextView snackNameTextView;

        SnackViewHolder(@NonNull View itemView) {
            super(itemView);
            snackNameTextView = itemView.findViewById(R.id.snackNameTextView);
        }
    }
}