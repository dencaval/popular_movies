package com.dencaval.project01;

import com.dencaval.project01.model.MovieInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by denis on 06/09/2016.
 */
public class utils {
    final static public String TMDB_API_KEY = "91aac8f2720c38e2a19de85f21271430";

    public enum Criteria{
        popular,
        userRating;
    }

    private ArrayList<MovieInfo> movieList = null;

    public static ArrayList<MovieInfo> getMovieList(String response, int count) throws JSONException {
        ArrayList<MovieInfo> movieList = new ArrayList<MovieInfo>();

        JSONObject json_response = new JSONObject(response);
        JSONArray results = json_response.getJSONArray("results");

        for (int i = 0; i < results.length(); i++){
            JSONObject movie_json = results.getJSONObject(i);
            MovieInfo movieInfo = new MovieInfo();
            movieInfo.id = movie_json.getString("id");
            movieInfo.posterPath = movie_json.getString("poster_path");
            movieInfo.originalTitle = movie_json.getString("original_title");
            movieInfo.overview = movie_json.getString("overview");
            movieInfo.userRating = movie_json.getString("vote_average");
            movieInfo.releaseDate = movie_json.getString("release_date");
            movieList.add(movieInfo);
        }

        return movieList;
    }
}
