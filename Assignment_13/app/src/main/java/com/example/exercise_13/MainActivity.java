package com.example.exercise_13;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private final OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        fetchTripData();
    }

    private void fetchTripData() {
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/map/route")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to load route", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String json = response.body().string();

                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        JSONArray pathArray = jsonObject.getJSONArray("path");
                        ArrayList<LatLng> points = new ArrayList<>();

                        for (int i = 0; i < pathArray.length(); i++) {
                            JSONObject point = pathArray.getJSONObject(i);
                            double lat = point.getDouble("latitude");
                            double lng = point.getDouble("longitude");
                            points.add(new LatLng(lat, lng));
                        }

                        runOnUiThread(() -> drawRoute(points));

                    } catch (Exception e) {
                        e.printStackTrace();
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Parse error", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });
    }

    private void drawRoute(List<LatLng> points) {
        if (points.isEmpty() || mMap == null) return;

        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(points);
        mMap.addPolyline(polylineOptions);

        mMap.addMarker(new MarkerOptions().position(points.get(0)).title("Start"));
        mMap.addMarker(new MarkerOptions().position(points.get(points.size() - 1)).title("End"));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) {
            builder.include(point);
        }
        LatLngBounds bounds = builder.build();

        int padding = 100;
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }
}
