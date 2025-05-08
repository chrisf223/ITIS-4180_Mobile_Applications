package edu.uncc.assessment05;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Podcast implements Serializable {
    public String icon;
    public String collectionName;
    public String artistName;
    public String releaseDate;
    public JSONArray genres = new JSONArray();
    public String id;

    public Podcast() {

    }

    public Podcast(JSONObject jsonObject) throws JSONException {
        this.icon = jsonObject.getString("artworkUrl600");
        this.collectionName = jsonObject.getString("collectionName");
        this.artistName = jsonObject.getString("artistName");
        this.releaseDate = jsonObject.getString("releaseDate");
        this.genres = jsonObject.getJSONArray("genres");
        this.id = jsonObject.getString("collectionId");
    }

    }
