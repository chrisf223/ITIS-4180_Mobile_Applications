package com.example.assignment_10;

import android.content.Context;
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
import com.example.assignment_10.databinding.FragmentCitiesBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class CitiesFragment extends Fragment {

    public CitiesFragment() {
        // Required empty public constructor
    }

    FragmentCitiesBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<City> cities = new ArrayList<>();
    CitiesAdapter adapter;
    CitiesListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCitiesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Cities");
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CitiesAdapter();
        binding.recyclerView.setAdapter(adapter);
        Log.d("demo", "Call getCities()");
        getCities();

    }

    void getCities() {
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/api/cities")
                .build();

        Log.d("demo", "Called getCities()");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("demo", "Failure: getCities()");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Log.d("demo", "onResponse: " + body);

                    try {
                        JSONObject rootJson = new JSONObject(body);
                        JSONArray citiesJsonArray = rootJson.getJSONArray("cities");
                        cities.clear();

                        for (int i = 0; i < citiesJsonArray.length(); i++) {
                            JSONObject citiesJsonObject = citiesJsonArray.getJSONObject(i);
                            City city = new City(citiesJsonObject);
                            cities.add(city);
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

    class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder>{
        @NonNull
        @Override
        public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CityViewHolder(CityItemBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
            City city = cities.get(position);
            holder.setupUI(city);
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }

        class CityViewHolder extends RecyclerView.ViewHolder{
            CityItemBinding mBinding;
            City mCity;
            public CityViewHolder( CityItemBinding vhBinding) {
                super(vhBinding.getRoot());
                mBinding = vhBinding;

                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onCitySelected(mCity);
                    }
                });
            }

            public void setupUI(City city) {
                this.mCity = city;
                mBinding.textViewCity.setText(city.name + ", " + city.state);
            }
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mListener = (CitiesListener) context;
    }

    interface CitiesListener {
        void onCitySelected(City city);
    }
}