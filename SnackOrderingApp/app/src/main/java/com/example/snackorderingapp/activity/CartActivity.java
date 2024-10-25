package com.example.snackorderingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.MyVolleySingletonUtil;
import com.example.snackorderingapp.R;
import com.example.snackorderingapp.adapter.CartAdapter;
import com.example.snackorderingapp.helper.StringResourceHelper;
import com.example.snackorderingapp.model.CartItem;
import com.example.snackorderingapp.utils.CartUtils;
import com.example.snackorderingapp.helper.ApiLinksHelper;
import com.example.snackorderingapp.zalopay.CreateOrder;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.listeners.PayOrderListener;

public class CartActivity extends AppCompatActivity {
    private static final String TAG = "CartActivity";
    private RecyclerView cartItemsRecyclerView;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.bottom_profile);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                    return true;
                case R.id.bottom_order:
                    startActivity(new Intent(getApplicationContext(), OrderActivity.class));
                    finish();
                    return true;
                case R.id.bottom_profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    return true;
            }
            return false;
        });

        requestQueue = MyVolleySingletonUtil.getInstance(CartActivity.this).getRequestQueue();
        // Initialize ZaloPay SDK
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ZaloPaySDK.init(2553, Environment.SANDBOX);

        cartItemsRecyclerView = findViewById(R.id.cartItemsRecyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        checkoutButton = findViewById(R.id.checkoutButton);

        cartItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load and display cart items
        cartItems = CartUtils.getCart(this);
        cartAdapter = new CartAdapter(this, cartItems);
        cartItemsRecyclerView.setAdapter(cartAdapter);

        // Calculate and display total price
        updateTotalPrice();

        // Implement checkout functionality
        checkoutButton.setOnClickListener(v -> initiatePayment());
    }

    public void updateTotalPrice() {
        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getSnack().getPrice() * item.getAmount();
        }
        totalPriceTextView.setText(String.format("Total: %.0f VND", totalPrice));
    }

    private void initiatePayment() {
        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getSnack().getPrice() * item.getAmount();
        }
        String totalString = String.format("%.0f", totalPrice);

        CreateOrder orderApi = new CreateOrder();
        try {
            JSONObject data = orderApi.createOrder(totalString);
            Log.d(TAG, "Order Data: " + data.toString()); // Log the order data
            if (data.has("return_code")) {
                String code = data.getString("return_code");
                if (code.equals("1")) {
                    String token = data.getString("zp_trans_token");
                    Log.d(TAG, "Initiating payment with token: " + token); // Log before initiating payment
                    ZaloPaySDK.getInstance().payOrder(CartActivity.this, token, "demozpdk://app", new PayOrderListener() {
                        @Override
                        public void onPaymentSucceeded(String s, String s1, String s2) {
                            createOrderInBackend(totalString);
                            Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                            intent.putExtra("result", "Thanh toán thành công");
                            startActivity(intent);
                        }

                        @Override
                        public void onPaymentCanceled(String s, String s1) {
                            Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                            intent.putExtra("result", "Hủy thanh toán");
                            startActivity(intent);
                        }

                        @Override
                        public void onPaymentError(ZaloPayError zaloPayError, String s, String s1) {
                            Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
                            intent.putExtra("result", "Lỗi thanh toán");
                            startActivity(intent);
                        }
                    });
                } else {
                    // Handle other return codes
                    Toast.makeText(CartActivity.this, "Error: Unexpected return code " + code, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Error: Unexpected return code " + code);
                }
            } else {
                // Handle missing return_code
                Toast.makeText(CartActivity.this, "Error: Missing return_code in response", Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Error: Missing return_code in response");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(CartActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Error: " + e.getMessage(), e);
        }
    }

    private void createOrderInBackend(String totalString) {
        // Get the access token from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);
        String accessToken = preferences.getString("access_token", null);

        // Create JSON object for the request body
        JSONObject orderData = new JSONObject();
        try {
            orderData.put("totalMoney", Double.parseDouble(totalString));
            orderData.put("status", "Complete"); // Assuming the status is "PAID"
            orderData.put("accountId", 5); // Replace with actual account ID
            orderData.put("branchId", 1); // Replace with actual branch ID
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Error creating JSON request body: " + e.getMessage(), e);
            return;
        }

        Log.d(TAG, "Request Data: " + orderData.toString()); // Log the request data

        // Create JsonObjectRequest
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                ApiLinksHelper.getOrdersUri(), // Replace with your backend API URL
                orderData,
                response -> {
                    Log.d(TAG, "Backend Order Response: " + response.toString());
                    // Extract order ID from response
                    int orderId = response.optJSONObject("content").optInt("orderId");
                    // Create order details for each cart item
                    createOrderDetails(orderId);
                },
                error -> {
                    Log.e(TAG, "Backend Order Error: " + error.toString());
                    Toast.makeText(CartActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + accessToken);
                headers.put("Content-Type", "application/json");
                Log.d(TAG, "Request Headers: " + headers.toString()); // Log the request headers
                return headers;
            }
        };

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest);
    }

    private void createOrderDetails(int orderId) {
        // Get the access token from SharedPreferences
        SharedPreferences preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);
        String accessToken = preferences.getString("access_token", null);

        // Iterate over cart items and create order details
        for (CartItem item : cartItems) {
            JSONObject orderDetailData = new JSONObject();
            try {
                orderDetailData.put("orderId", orderId);
                orderDetailData.put("branchFoodId", item.getSnack().getFoodId()); // Assuming getId() returns the branchFoodId
                orderDetailData.put("quantity", item.getAmount());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "Error creating JSON request body for order detail: " + e.getMessage(), e);
                continue;
            }

            Log.d(TAG, "Order Detail Request Data: " + orderDetailData.toString()); // Log the request data

            // Create JsonObjectRequest for order detail
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.POST,
                    ApiLinksHelper.getOrderDetailsUri(), // Replace with your backend API URL for order details
                    orderDetailData,
                    response -> {
                        Log.d(TAG, "Backend Order Detail Response: " + response.toString());
                    },
                    error -> {
                        Log.e(TAG, "Backend Order Detail Error: " + error.toString());
                        Toast.makeText(CartActivity.this, "Error: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
            ) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + accessToken);
                    headers.put("Content-Type", "application/json");
                    Log.d(TAG, "Order Detail Request Headers: " + headers.toString()); // Log the request headers
                    return headers;
                }
            };

            // Add the request to the RequestQueue
            requestQueue.add(jsonObjectRequest);
        }
    }

    private void redirectToPaymentNotification(String message) {
        Intent intent = new Intent(CartActivity.this, PaymentNotification.class);
        intent.putExtra("result", message);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
        // Log the intent data received from ZaloPay
        Log.d(TAG, "onNewIntent called with intent: " + intent.toString());
        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Log.d(TAG, "Intent extra: " + key + " = " + intent.getExtras().get(key));
            }
        }
        // Handle the result from ZaloPay and redirect to the main screen if necessary
        String result = intent.getStringExtra("result");
        if (result != null) {
            redirectToPaymentNotification(result);
        }
    }
}