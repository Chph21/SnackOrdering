package com.example.snackorderingapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.snackorderingapp.model.CartItem;
import com.example.snackorderingapp.model.Snack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartUtils {

    private static final String CART_PREFS = "cart_prefs";
    private static final String CART_KEY = "cart_key";

    public static void addToCart(Context context, Snack snack, int amount) {
        List<CartItem> cart = getCart(context);
        boolean snackExists = false;
        for (CartItem item : cart) {
            if (item.getSnack().getFoodId() == snack.getFoodId()) {
                item.setAmount(item.getAmount() + amount);
                snackExists = true;
                break;
            }
        }
        if (!snackExists) {
            cart.add(new CartItem(snack, amount));
        }
        saveCart(context, cart);
    }

    public static List<CartItem> getCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE);
        String cartJson = prefs.getString(CART_KEY, null);
        if (cartJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<CartItem>>() {}.getType();
            return gson.fromJson(cartJson, type);
        } else {
            return new ArrayList<>();
        }
    }

    public static void saveCart(Context context, List<CartItem> cart) {
        SharedPreferences prefs = context.getSharedPreferences(CART_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String cartJson = gson.toJson(cart);
        editor.putString(CART_KEY, cartJson);
        editor.apply();
    }
}