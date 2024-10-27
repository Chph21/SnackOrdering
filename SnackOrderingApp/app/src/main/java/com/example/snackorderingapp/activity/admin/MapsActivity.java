package com.example.snackorderingapp.activity.admin;

import androidx.fragment.app.FragmentActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.snackorderingapp.ApiService;
import com.example.snackorderingapp.MyVolleySingletonUtil;
import com.example.snackorderingapp.helper.ApiLinksHelper;
import com.example.snackorderingapp.helper.StringResourceHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.snackorderingapp.databinding.ActivityMapsBinding;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private ApiService apiService;
    private RequestQueue mRequestQueue;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(com.example.snackorderingapp.R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mRequestQueue = MyVolleySingletonUtil.getInstance(this).getRequestQueue();
        preferences = getSharedPreferences(StringResourceHelper.getAuthTokenPref(), MODE_PRIVATE);
        String accessToken = preferences.getString("access_token", "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, ApiLinksHelper.getLocationUri(), null,
                response -> {
                    try {
                        // Parse the latitude and longitude from the JSON object
                        double latitude = response.getJSONObject("content").getDouble("latitude");
                        double longitude = response.getJSONObject("content").getDouble("longitude");

                        // Check if mMap is not null before adding a marker
                        if (mMap != null) {
                            LatLng location = new LatLng(latitude, longitude);
                            mMap.addMarker(new MarkerOptions().position(location).title("Fetched Location"));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
                        } else {
                            Log.e("MapsActivity", "GoogleMap instance is null.");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Error parsing location data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    if (error.networkResponse != null && error.networkResponse.statusCode == 403) {
                        Toast.makeText(this, "Error 403 loading location", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error loading location", Toast.LENGTH_SHORT).show();
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

        // Add the request to the request queue
        mRequestQueue.add(request);
    }



}