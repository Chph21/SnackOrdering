package com.example.snackorderingapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.snackorderingapp.R;
import com.example.snackorderingapp.activity.CartActivity;
import com.example.snackorderingapp.model.CartItem;
import com.example.snackorderingapp.utils.CartUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private Context context;

    public CartAdapter(Context context, List<CartItem> cartItems) {
        this.context = context;
        this.cartItems = cartItems;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        holder.snackName.setText(cartItem.getSnack().getFoodName());
        holder.snackPrice.setText(String.valueOf(cartItem.getSnack().getPrice()));
        holder.snackAmount.setText(String.valueOf(cartItem.getAmount()));
        Picasso.get().load(cartItem.getSnack().getImageURL()).into(holder.snackImage);

        holder.buttonPlus.setOnClickListener(v -> {
            cartItem.setAmount(cartItem.getAmount() + 1);
            holder.snackAmount.setText(String.valueOf(cartItem.getAmount()));
            CartUtils.saveCart(context, cartItems);
            updateTotalPrice();
        });

        holder.buttonMinus.setOnClickListener(v -> {
            if (cartItem.getAmount() > 1) {
                cartItem.setAmount(cartItem.getAmount() - 1);
                holder.snackAmount.setText(String.valueOf(cartItem.getAmount()));
                CartUtils.saveCart(context, cartItems);
                updateTotalPrice();
            } else {
                showRemoveItemDialog(cartItem, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    private void showRemoveItemDialog(CartItem cartItem, int position) {
        new AlertDialog.Builder(context)
                .setTitle("Remove Item")
                .setMessage("Do you want to remove this item from the cart?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    cartItems.remove(position);
                    notifyItemRemoved(position);
                    CartUtils.saveCart(context, cartItems);
                    updateTotalPrice();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    public void updateTotalPrice() {
//        double totalPrice = 0;
//        for (CartItem item : cartItems) {
//            totalPrice += item.getSnack().getPrice() * item.getAmount();
//        }
        // Assuming you have a method to update the total price in the activity
        if (context instanceof CartActivity) {
            ((CartActivity) context).updateTotalPrice();
        }
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView snackName, snackPrice, snackAmount;
        ImageView snackImage;
        Button buttonPlus, buttonMinus;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            snackName = itemView.findViewById(R.id.snackName);
            snackPrice = itemView.findViewById(R.id.snackPrice);
            snackAmount = itemView.findViewById(R.id.snackAmount);
            snackImage = itemView.findViewById(R.id.snackImage);
            buttonPlus = itemView.findViewById(R.id.buttonPlus);
            buttonMinus = itemView.findViewById(R.id.buttonMinus);
        }
    }
}