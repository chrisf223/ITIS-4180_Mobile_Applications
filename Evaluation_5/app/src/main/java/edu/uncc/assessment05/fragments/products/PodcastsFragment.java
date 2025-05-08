package edu.uncc.assessment05.fragments.products;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.uncc.assessment05.Podcast;
import edu.uncc.assessment05.R;
import edu.uncc.assessment05.databinding.FragmentPodcastsBinding;
import edu.uncc.assessment05.databinding.ProductListItemBinding;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class PodcastsFragment extends Fragment {

    public PodcastsFragment() {
        // Required empty public constructor
    }

    FragmentPodcastsBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<Podcast> podcasts = new ArrayList<>();
    PodcastsAdapter adapter;
    ArrayList<String> favorties = new ArrayList<>();



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPodcastsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Podcasts");
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new PodcastsAdapter();
        binding.recyclerView.setAdapter(adapter);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.goto_favorites_action){
                    mListener.gotoFavorites();
                    return true;
                } else if(menuItem.getItemId() == R.id.logout_action){
                    mListener.logout();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        getPodcasts();
    }

    void getPodcasts() {
        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/design.json")
                .build();
        Log.d("demo", "Called getPodcasts()");

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("demo", "Failure: getPodcasts()");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String body = response.body().string();
                    Log.d("demo", "Got podcasts: " + body);

                    try {
                        JSONObject rootJson = new JSONObject(body);
                        JSONArray podcastsJsonArray = rootJson.getJSONArray("results");
                        podcasts.clear();

                        for (int i = 0; i < podcastsJsonArray.length(); i++) {
                            JSONObject podcastsJsonObject = podcastsJsonArray.getJSONObject(i);
                            Podcast podcast = new Podcast(podcastsJsonObject);
                            podcasts.add(podcast);
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

                } else {
                    Log.d("demo", "Failed to get podcasts");
                }
            }
        });
    }

    class PodcastsAdapter extends RecyclerView.Adapter<PodcastsAdapter.PodcastViewHolder> {
        @NonNull
        @Override
        public PodcastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new PodcastViewHolder(ProductListItemBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull PodcastViewHolder holder, int position) {
            Podcast podcast = podcasts.get(position);
            try {
                holder.setupUI(podcast);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public int getItemCount() {
            return podcasts.size();
        }

        class PodcastViewHolder extends RecyclerView.ViewHolder{
            ProductListItemBinding mBinding;
            Podcast mPodcast;

            public PodcastViewHolder( ProductListItemBinding vhBinding) {
                super(vhBinding.getRoot());
                mBinding = vhBinding;

                mBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //To DO!!!!
                    }
                });
            }

            public void setupUI(Podcast podcast) throws JSONException {
                this.mPodcast = podcast;
                mBinding.textViewCollectionName.setText(podcast.collectionName);
                mBinding.textViewArtistName.setText(podcast.artistName);
                mBinding.textViewReleaseDate.setText(podcast.releaseDate);
                Picasso.get().load(podcast.icon).into(mBinding.imageViewIcon);
                String genres = "";
                for (int i =0; i < podcast.genres.length(); i++) {
                    genres = genres + podcast.genres.getString(i) + ",";
                }
                mBinding.textViewGenres.setText(genres);
                mBinding.imageViewAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        if (favorties.contains(podcast.id)) {
                            Log.d(TAG, "Id already in db");
                        }
                        else {
                            DocumentReference docRef = db.collection("favorites").document();
                            HashMap<String, Object> data = new HashMap<>();
                            data.put("id", podcast.id);
                            data.put("genres", podcast.genres.toString());
                            data.put("collectionName", podcast.collectionName);
                            data.put("artistName", podcast.artistName);
                            data.put("icon", podcast.icon);
                            data.put("releaseDate", podcast.releaseDate);
                            docRef.set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d(TAG, "Success Favorite: ");
                                        favorties.add(podcast.id);
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }
    }

    PodcastsListener mListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof PodcastsListener) {
            mListener = (PodcastsListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement PodcastsListener");
        }
    }

    public interface PodcastsListener {
        void gotoFavorites();
        void logout();
    }
}