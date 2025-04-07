package com.example.assignment_10;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.assignment_10.databinding.CityItemBinding;
import com.example.assignment_10.databinding.ForecastItemBinding;
import com.example.assignment_10.databinding.FragmentWeatherBinding;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class WeatherFragment extends Fragment {


    private static final String ARG_PARAM_CITY = "ARG_PARAM_CITY";

    private City mCity;

    ArrayList<Forecast> forecasts = new ArrayList<>();

    ForecastsAdapter adapter;



    public WeatherFragment() {
        // Required empty public constructor
    }


    public static WeatherFragment newInstance(City city) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM_CITY, (Serializable) city);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCity = (City) getArguments().getSerializable(ARG_PARAM_CITY);
        }
    }

    FragmentWeatherBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Current Weather");
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ForecastsAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.textViewTitle.setText(mCity.getName() + ", " + mCity.getState());
        Log.d("demo", " + calling weather");
        getWeather();
    }

    private final OkHttpClient client = new OkHttpClient();

    void getWeather() {
        HttpUrl url = HttpUrl.parse("https://api.weather.gov/").newBuilder()
                .addPathSegment("points")
                .addPathSegment(mCity.getLat() + "," + mCity.getLng())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("demo", "Weather failed");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Log.d("demo", "get weather onResponse: " + body);
                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        String forecastUrl = jsonObject.getJSONObject("properties").getString("forecast");
                        fetchForecast(forecastUrl);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private final OkHttpClient forecastClient = new OkHttpClient();

    void fetchForecast(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();

        forecastClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Log.d("demo", "fetch forecast onResponse: " + body);

                    try {
                        JSONObject jsonObject = new JSONObject(body);
                        JSONObject properties = jsonObject.getJSONObject("properties");
                        JSONArray forecastsJsonArray = properties.getJSONArray("periods");
                        forecasts.clear();

                        for (int i = 0; i < forecastsJsonArray.length(); i++) {
                            JSONObject forecastsJsonObject = forecastsJsonArray.getJSONObject(i);
                            Forecast forecast = new Forecast(forecastsJsonObject);
                            forecasts.add(forecast);
                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });

                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

    class ForecastsAdapter extends RecyclerView.Adapter<ForecastsAdapter.ForecastViewHolder>{
        @NonNull
        @Override
        public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ForecastViewHolder(ForecastItemBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
            Forecast forecast = forecasts.get(position);
            holder.setupUI(forecast);
        }

        @Override
        public int getItemCount() {
            return forecasts.size();
        }

        class ForecastViewHolder extends RecyclerView.ViewHolder{
            ForecastItemBinding mBinding;
            Forecast mForecast;
            public ForecastViewHolder( ForecastItemBinding vhBinding) {
                super(vhBinding.getRoot());
                mBinding = vhBinding;
            }

            public void setupUI(Forecast forecast) {
                this.mForecast = forecast;
                mBinding.textViewPrecipitation.setText("Precipitation: " + forecast.precipitation);
                mBinding.textViewShortForecast.setText(forecast.shortForecast);
                mBinding.textViewTemp.setText(forecast.temp + " " + forecast.tempUnit);
                mBinding.textViewTime.setText(forecast.time);
                mBinding.textViewWind.setText(forecast.wind);
                Picasso.get().load(forecast.icon).into(mBinding.imageView);
            }
        }
    }
}