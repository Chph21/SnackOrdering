package com.example.snackorderingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.activity.admin.ManageOrderDetailActivity;
import com.example.snackorderingapp.MyVolleySingletonUtil;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.helper.ApiLinksHelper;
import com.example.snackorderingapp.helper.StringResourceHelper;
import com.example.snackorderingapp.model.Order;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private List<Order> orders;
    private Context context;
    private String accessToken;

    public OrdersAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        SharedPreferences preferences = context.getSharedPreferences(StringResourceHelper.getAuthTokenPref(), Context.MODE_PRIVATE);
        this.accessToken = preferences.getString("access_token", "");
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_row, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        MaterialButton btnViewDetail = holder.itemView.findViewById(R.id.btnViewDetail);
        btnViewDetail.setOnClickListener(v -> {
            Intent intent = new Intent(context, ManageOrderDetailActivity.class);
            intent.putExtra("order", order);
            context.startActivity(intent);
        });

        JsonObjectRequest userRequest = new JsonObjectRequest(Request.Method.GET,
                ApiLinksHelper.getUserByIdUri(order.getAccountId().intValue()),
                null,
                response -> {
                    try {
                        JSONObject content = response.getJSONObject("content");
                        String firstName = content.getString("firstName");
                        String lastName = content.getString("lastName");
                        String fullName = firstName + " " + lastName;
                        holder.tvCustomerName.setText(fullName);
                    } catch (JSONException e) {
                        holder.tvCustomerName.setText("Unknown Customer");
                    }
                },
                error -> holder.tvCustomerName.setText("Unknown Customer")
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue = MyVolleySingletonUtil.getInstance(context).getRequestQueue();
        requestQueue.add(userRequest);

        holder.tvOrderId.setText("#" + order.getOrderId());
        holder.tvOrderStatus.setText(order.getStatus());
        String dateStr = order.getCreatedDate();
        Instant instant = Instant.parse(dateStr);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        String formattedDate = dateTime.format(formatter);
        holder.tvOrderDate.setText(formattedDate);
        holder.tvOrderTotal.setText(String.format("$%.2f", order.getTotalMoney()));

        switch (order.getStatus()) {
            case "Pending":
                holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.status_pending_text));
                holder.tvOrderStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.status_pending_bg));
                break;
            case "Completed":
                holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.status_completed_text));
                holder.tvOrderStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.status_completed_bg));
                break;
            case "Cancelled":
                holder.tvOrderStatus.setTextColor(ContextCompat.getColor(context, R.color.status_cancelled_text));
                holder.tvOrderStatus.setBackgroundColor(ContextCompat.getColor(context, R.color.status_cancelled_bg));
                break;
        }

        holder.tvOrderStatus.setOnClickListener(v -> {
            String[] statusOptions = {"Pending", "Completed", "Cancelled"};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Update Order Status")
                    .setItems(statusOptions, (dialog, which) -> {
                        String newStatus = statusOptions[which];
                        updateOrderStatus(order, newStatus);
                    });
            builder.create().show();
        });
    }

    private void updateOrderStatus(Order order, String newStatus) {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("orderId", order.getOrderId().intValue());
            requestBody.put("orderNumber", order.getOrderNumber());
            requestBody.put("totalMoney", order.getTotalMoney());
            requestBody.put("status", newStatus);
            requestBody.put("accountId", order.getAccountId());
            requestBody.put("branchId", order.getBranchId().intValue());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        JsonObjectRequest updateRequest = new JsonObjectRequest(Request.Method.PUT,
                ApiLinksHelper.updateOrderUri(),
                requestBody,
                response -> {
                    order.setStatus(newStatus);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Order status updated successfully", Toast.LENGTH_SHORT).show();
                },
                error -> {
                    String errorMessage = new String(error.networkResponse.data);
                    Log.e("Volley", "Error response: " + errorMessage);
                    Toast.makeText(context, "Failed to update order status", Toast.LENGTH_SHORT).show();
                }        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue requestQueue = MyVolleySingletonUtil.getInstance(context).getRequestQueue();
        requestQueue.add(updateRequest);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOrderStatus, tvOrderDate, tvOrderTotal, tvCustomerName, tvOrderId;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvOrderStatus = itemView.findViewById(R.id.chipStatus);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
            tvOrderTotal = itemView.findViewById(R.id.tvOrderTotal);
            tvCustomerName = itemView.findViewById(R.id.tvCustomerName);
        }
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        notifyDataSetChanged();
    }
}
