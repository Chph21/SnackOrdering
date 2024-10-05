package com.example.snackorderingapp;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.model.Snack;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ApiService {
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    private MyVolleySingletonUtil volleySingleton;
    private String accessToken;

    public ApiService(Context context, String accessToken) {
        this.volleySingleton = MyVolleySingletonUtil.getInstance(context);
        this.accessToken = accessToken;
    }

    public interface ApiCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    public void getSnacks(final ApiCallback<List<Snack>> callback) {
        String url = BASE_URL + "food";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray contentArray = response.getJSONArray("content");
                            List<Snack> snacks = new ArrayList<>();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault());
                            for (int i = 0; i < contentArray.length(); i++) {
                                JSONObject snackJson = contentArray.getJSONObject(i);
                                Snack snack = new Snack(
                                        snackJson.getInt("foodId"),
                                        snackJson.getString("foodName"),
                                        snackJson.getString("imageURL"),
                                        snackJson.getString("description"),
                                        snackJson.getString("ingredients"),
                                        snackJson.getDouble("price"),
                                        snackJson.getBoolean("available"),
                                        snackJson.getString("categoryId"),
                                        snackJson.getString("createdBy"),
                                        dateFormat.parse(snackJson.getString("createdDate")),
                                        snackJson.getString("updatedBy"),
                                        dateFormat.parse(snackJson.getString("updatedDate"))
                                );
                                snacks.add(snack);
                            }
                            callback.onSuccess(snacks);
                        } catch (JSONException | ParseException e) {
                            callback.onError("Failed to parse response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Authorization", "Bearer " + accessToken);
                return headers;
            }
        };
        volleySingleton.addToRequestQueue(request);
    }

    public void login(String phone, String password, final ApiCallback<JSONObject> callback) {
        String url = BASE_URL + "account/login";

        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", password);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String accessToken = response.getString("access_token");
                            String refreshToken = response.getString("refresh_token");
                            callback.onSuccess(response);
                        } catch (JSONException e) {
                            callback.onError("Failed to parse response: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error.toString());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        volleySingleton.addToRequestQueue(request);
    }

    // Other methods (getSnackDetails, placeOrder, getOrderHistory) remain the same
    // ...
}