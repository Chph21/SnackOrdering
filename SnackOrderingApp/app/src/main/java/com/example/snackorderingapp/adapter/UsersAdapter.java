package com.example.snackorderingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snackorderingapp.R;
import com.example.snackorderingapp.activity.admin.ManageUserDetailActivity;
import com.example.snackorderingapp.model.User;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {
    private Context context;
    private List<User> users;

    public UsersAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);

        // Set User ID
        holder.tvUserId.setText("User #" + user.getAccountId());

        // Set Full Name
        String fullName = (user.getFirstName() != null ? user.getFirstName() : "") + " " +
                (user.getLastName() != null ? user.getLastName() : "");
        fullName = fullName.trim();
        holder.tvFullName.setText(fullName.isEmpty() ? "No Name" : fullName);

        // Set Phone
        holder.tvPhone.setText(user.getPhone());

        MaterialButton btnViewUserDetail = holder.itemView.findViewById(R.id.btnViewUserDetail);
        btnViewUserDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, ManageUserDetailActivity.class);
            intent.putExtra("user", user);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users) {
        this.users = users;
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserId;
        TextView tvFullName;
        TextView tvPhone;
        MaterialButton btnViewUserDetail;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserId = itemView.findViewById(R.id.tvUserId);  // Add this line
            tvFullName = itemView.findViewById(R.id.tvFullName);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            btnViewUserDetail = itemView.findViewById(R.id.btnViewUserDetail);
        }
    }
}
