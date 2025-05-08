package com.example.exercise_13;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng startLatLng = null;
    private LatLng endLatLng = null;
    private final OkHttpClient client = new OkHttpClient();

    private static final String ROUTES_API_URL = "https://routes.googleapis.com/directions/v2:computeRoutes";
    private static final String PLACES_API_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = getApiKeyFromManifest();

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }

        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        setupAutocompleteFragments(apiKey);
    }

    private void setupAutocompleteFragments(String apiKey) {
        AutocompleteSupportFragment startFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_start);
        AutocompleteSupportFragment endFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_end);

        if (startFragment != null && endFragment != null) {
            startFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
            endFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

            startFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    startLatLng = place.getLatLng();
                    checkAndFetchRoute(apiKey);
                }

                @Override
                public void onError(@NonNull com.google.android.gms.common.api.Status status) {
                    Toast.makeText(MainActivity.this, "Error selecting start location", Toast.LENGTH_SHORT).show();
                }
            });

            endFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(@NonNull Place place) {
                    endLatLng = place.getLatLng();
                    checkAndFetchRoute(apiKey);
                }

                @Override
                public void onError(@NonNull com.google.android.gms.common.api.Status status) {
                    Toast.makeText(MainActivity.this, "Error selecting end location", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void checkAndFetchRoute(String apiKey) {
        if (startLatLng != null && endLatLng != null) {
            fetchRouteFromGoogle(apiKey);
        }
    }

    private void fetchRouteFromGoogle(String apiKey) {
        try {
            JSONObject body = new JSONObject();
            JSONObject origin = new JSONObject().put("location",
                    new JSONObject().put("latLng",
                            new JSONObject()
                                    .put("latitude", startLatLng.latitude)
                                    .put("longitude", startLatLng.longitude)));

            JSONObject destination = new JSONObject().put("location",
                    new JSONObject().put("latLng",
                            new JSONObject()
                                    .put("latitude", endLatLng.latitude)
                                    .put("longitude", endLatLng.longitude)));

            body.put("origin", origin);
            body.put("destination", destination);
            body.put("travelMode", "DRIVE");

            Request request = new Request.Builder()
                    .url(ROUTES_API_URL)
                    .addHeader("Content-Type", "application/json")
                    .addHeader("X-Goog-Api-Key", apiKey)
                    .addHeader("X-Goog-FieldMask", "routes.polyline.encodedPolyline")
                    .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to get route", Toast.LENGTH_SHORT).show());
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful() && response.body() != null) {
                        try {
                            String responseStr = response.body().string();
                            JSONObject responseJson = new JSONObject(responseStr);
                            JSONArray routes = responseJson.getJSONArray("routes");
                            if (routes.length() > 0) {
                                JSONObject polyline = routes.getJSONObject(0).getJSONObject("polyline");
                                String encodedPolyline = polyline.getString("encodedPolyline");
                                List<LatLng> decodedPath = decodePolyline(encodedPolyline);

                                runOnUiThread(() -> {
                                    drawRoute(decodedPath);
                                    fetchGasStationsAlongRoute(encodedPolyline);
                                });
                            }
                        } catch (Exception e) {
                            runOnUiThread(() -> Toast.makeText(MainActivity.this, "Parsing error", Toast.LENGTH_SHORT).show());
                        }
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawRoute(List<LatLng> points) {
        if (mMap == null || points.isEmpty()) return;

        mMap.clear();

        mMap.addPolyline(new PolylineOptions().addAll(points).color(Color.RED));

        mMap.addMarker(new MarkerOptions().position(startLatLng).title("Start"));
        mMap.addMarker(new MarkerOptions().position(endLatLng).title("End"));

        // Update to show midpoint of the route
        LatLng midPoint = calculateMidPointAlongRoute(points);
        mMap.addMarker(new MarkerOptions().position(midPoint).title("Midpoint"));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng point : points) builder.include(point);
        builder.include(startLatLng);
        builder.include(endLatLng);
        builder.include(midPoint);

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
    }

    private LatLng calculateMidPointAlongRoute(List<LatLng> routePoints) {
        if (routePoints == null || routePoints.size() < 2) {
            return null;
        }

        double totalDistance = 0.0;
        double[] segmentDistances = new double[routePoints.size() - 1];

        // Step 1: Calculate total route distance and store segment distances
        for (int i = 0; i < routePoints.size() - 1; i++) {
            double distance = distanceBetween(routePoints.get(i), routePoints.get(i + 1));
            segmentDistances[i] = distance;
            totalDistance += distance;
        }

        // Step 2: Find halfway point
        double halfDistance = totalDistance / 2.0;
        double accumulated = 0.0;

        for (int i = 0; i < segmentDistances.length; i++) {
            if (accumulated + segmentDistances[i] >= halfDistance) {
                double remaining = halfDistance - accumulated;
                double ratio = remaining / segmentDistances[i];
                LatLng start = routePoints.get(i);
                LatLng end = routePoints.get(i + 1);
                return interpolate(start, end, ratio);
            }
            accumulated += segmentDistances[i];
        }

        // Fallback (shouldn't happen)
        return routePoints.get(routePoints.size() / 2);
    }


    private void fetchGasStationsAlongRoute(String encodedPolyline) {
        String apiKey = getApiKeyFromManifest();
        fetchGasStationsAtLocation(startLatLng, apiKey);
        fetchGasStationsAtLocation(endLatLng, apiKey);

        LatLng midPoint = calculateMidPointAlongRoute(decodePolyline(encodedPolyline));
        fetchGasStationsAtLocation(midPoint, apiKey);
    }

    private void fetchGasStationsAtLocation(LatLng location, String apiKey) {
        String url = PLACES_API_URL + "?location=" + location.latitude + "," + location.longitude
                + "&radius=1000&type=gas_station&key=" + apiKey;

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch gas stations", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String responseStr = response.body().string();
                        JSONObject responseJson = new JSONObject(responseStr);
                        JSONArray results = responseJson.getJSONArray("results");
                        runOnUiThread(() -> {
                            for (int i = 0; i < results.length(); i++) {
                                try {
                                    JSONObject place = results.getJSONObject(i);
                                    double lat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                                    double lng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                                    String name = place.getString("name");

                                    LatLng gasStationLocation = new LatLng(lat, lng);
                                    mMap.addMarker(new MarkerOptions()
                                                    .position(gasStationLocation)
                                                    .title(name))
                                            .setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (Exception e) {
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error parsing gas stations", Toast.LENGTH_SHORT).show());
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
    }

    private String getApiKeyFromManifest() {
        try {
            ApplicationInfo ai = getPackageManager()
                    .getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            return bundle.getString("com.google.android.geo.API_KEY");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private List<LatLng> decodePolyline(String encoded) {
        List<LatLng> poly = new java.util.ArrayList<>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : result >> 1);
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : result >> 1);
            lng += dlng;

            poly.add(new LatLng(lat / 1E5, lng / 1E5));
        }

        return poly;
    }

    private double distanceBetween(LatLng a, LatLng b) {
        double earthRadius = 6371000; // meters
        double dLat = Math.toRadians(b.latitude - a.latitude);
        double dLng = Math.toRadians(b.longitude - a.longitude);
        double lat1 = Math.toRadians(a.latitude);
        double lat2 = Math.toRadians(b.latitude);

        double aCalc = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.sin(dLng / 2) * Math.sin(dLng / 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.atan2(Math.sqrt(aCalc), Math.sqrt(1 - aCalc));
        return earthRadius * c;
    }

    private LatLng interpolate(LatLng a, LatLng b, double fraction) {
        double lat = (b.latitude - a.latitude) * fraction + a.latitude;
        double lng = (b.longitude - a.longitude) * fraction + a.longitude;
        return new LatLng(lat, lng);
    }

}






